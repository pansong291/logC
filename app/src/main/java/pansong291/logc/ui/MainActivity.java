package pansong291.logc.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import pansong291.logc.R;
import pansong291.logc.other.LogReader;
import pansong291.logc.other.MyAdapter;
import pansong291.logc.other.MyAdapter.LogItemValue;
import pansong291.logc.ui.Zactivity;
import pansong291.logc.other.Utils;

public class MainActivity extends Zactivity
implements AdapterView.OnItemClickListener,
AdapterView.OnItemLongClickListener,
DialogInterface.OnClickListener
{
 static int VERSION_CODE;
 static String VERSION_NAME;
 
 View mll1,mll2,mll3,mbackground;
 EditText medtxt,//输入包名的edit
 medsrh;//搜索内容
 MenuItem mitem;//暂停开始按钮
 LogReader logre;
 Thread mthread;
 ListView listview;
 AlertDialog copyDialog1,copyDialog2;
 int listLength,//list显示的最多条数
 findlogPosition=-1;//当前查找内容的位置
 String logPackag;//记录要查看log的包名
 public static int fontsize=14;//字体大小
 public static String choosefont="sans";//选择字体
 public static boolean singlel,//是否单行显示
 hangHisLog,//行号意义是否是log数
 blackBackground,//暗背景
 bigFont,//字体加粗
 monitoredMe=false,//监控本程序
 checkmodel=false,//多选模式
 lianmodel=false,//连选模式
 setyc1=true,setyc2=true;//设置隐藏某选项
 private boolean firstStart=true,//每次刚开始启动this Activity
 check2=false;//选择第二项
 private int lianx1;//连选第一项
// lianx2;//连选第二项
 MyAdapter adapter;
 long lineH=0;//记录读取了多少行log
 Handler handler=new Handler()
 {
  public void handleMessage(Message msg)
  {
   switch(msg.what)
   {
	case -83:
	 if(adapter.getCount()>=listLength)
	  adapter.removeLogInfo(0);
	 adapter.addLogInfo((++lineH)+".",msg.obj.toString());
	 adapter.notifyDataSetChanged();
	 break;
   }
  }
 };
 /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
   super.onCreate(savedInstanceState);
//   setTheme(android.R.style.Theme_Holo);
   setContentView(R.layout.activity_main);
   
   try{
    PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(),0);
    VERSION_CODE=pi.versionCode;
    VERSION_NAME=pi.versionName;
   }catch(Exception e)
   {
    Log.e("VersionInfo","Exception",e);    
   }
   
//   singlel=msp.getBoolean("singleLine",true);
   logPackag=sp.getString(LOG_PACKAGE,"");
   
   mll1=findViewById(R.id.logcatLinearLayout2);
   mll2=findViewById(R.id.logcatLinearLayout3);
   mll3=findViewById(R.id.logcatLinearLayout4);
   mbackground=findViewById(R.id.logcatLinearLayout1);
   medtxt=(EditText)findViewById(R.id.logcatEditText1);
   medsrh=(EditText)findViewById(R.id.logcatEditText2);
   listview=(ListView)findViewById(R.id.logcatListView1);
   adapter=new MyAdapter(this);
   
   listview.setAdapter(adapter);
   listview.setOnItemClickListener(this);
   listview.setOnItemLongClickListener(this);
   
   copyDialog1=new AlertDialog.Builder(this)
   .setMessage(R.string.dialog_message_copy2)
   .setPositiveButton(R.string.dialog_button_add,this)
   .setNeutralButton(R.string.dialog_button_choose,this)
   .setNegativeButton(R.string.dialog_button_copy,this)
   .create();
   copyDialog2=new AlertDialog.Builder(this)
   .setMessage(R.string.dialog_message_copy2)
   .setPositiveButton(R.string.dialog_button_add,this)
   .setNegativeButton(R.string.dialog_button_copy,this)
   .create();
   
   if(Utils.checkRootPermission())
   {
    toast("已获取root权限");
   }else
   {
    new AlertDialog.Builder(this)
     .setTitle(R.string.dialog_title_no_root)
     .setMessage(R.string.dialog_message_no_root)
     .setPositiveButton(R.string.dialog_button_positive,null)
     .show();
   }
   
   if(sp.getBoolean(APP_FIRST_RUN,true))
   {
    new AlertDialog.Builder(this)
     .setTitle(R.string.dialog_title_help)
     .setMessage(R.string.dialog_message_help)
     .setPositiveButton(R.string.dialog_button_positive,null)
     .show();
	sp.edit().putBoolean(APP_FIRST_RUN,false).commit();
   }
   
   if(sp.getBoolean(SET_CB3,true)||sp.getBoolean(QZGX,false))
   {
    
   }
  }
  protected void onResume()
  {
   super.onResume();
   //需要刷新的设置数据
   boolean b1=singlel;
   singlel=sp.getBoolean(SINGLE_LINE,true);
   adapter.reSetMyInt(b1!=singlel);
//   int i1=listLength;
   listLength=Integer.parseInt(sp.getString(LIST_LEN,"1000"));
//   if(i1!=listLength)
   monitoredMe=(sp.getLong(TIME_L,0L)==1424959652436L);
   choosefont=sp.getString(FONT_TYPE,"sans");
   fontsize=Integer.parseInt(sp.getString(FONT_SIZE,"14"));
   bigFont=sp.getBoolean(BIG_FONT,false);
   blackBackground=sp.getBoolean(BLACK_BACK,true);
   hangHisLog=sp.getBoolean(LINE_CODE_IS_LOG,false);
   mbackground.setBackgroundColor(MyAdapter.getTxtC(true,null));
   adapter.setDataMaxLine(listLength);
   adapter.notifyDataSetChanged();
   if(firstStart&&logPackag.length()>1)
   {
	medtxt.setText(logPackag);
	onLogStart(medtxt);
   }
  }

  private void startLog(String m)
  {
   logre=new LogReader(m,handler);
   mthread=new Thread(logre);
   mthread.start();
   setNowTime();
   Log.v("logC","Start to read logs: "+m);
  }
  
  private void setNowTime()
  {
   Time tt=new Time();
   tt.setToNow();
   LogReader.hour=tt.hour;
   LogReader.minute=tt.minute;
  }
  
  @Override
  public void onItemClick(AdapterView<?> p1,View p2,int p3,long p4)
  {
   setyc1=true;
   if(lianmodel)
   {
	CheckBox cb1=(CheckBox)p2.findViewById(R.id.logitemCB1);
	if(check2)
	{
	 adapter.setLianChecked(lianx1,p3);
	 shuaXinCheck(-1);
	 check2=false;
	 lianmodel=false;
	}else
	{
	 toast(R.string.toast_choose_last);
	 lianx1=p3;
	 cb1.setChecked(lianmodel);
	 adapter.intItem.get(p3).checked=lianmodel;
	 check2=true;
	}
   }else if(checkmodel)
   {
	CheckBox cb1=(CheckBox)p2.findViewById(R.id.logitemCB1);
	boolean b1=!cb1.isChecked();
    adapter.intItem.get(p3).checked=b1;
	cb1.setChecked(b1);
   }else
   {
    TextView tv1=(TextView)p2.findViewById(R.id.logitemTV2),
     tv2=(TextView)p2.findViewById(R.id.logitemTV3);
	int i1=tv2.getVisibility();
    adapter.intItem.get(p3).index=i1;
	tv1.setVisibility(i1);
	tv2.setVisibility(8-i1);
//    if(tv1.getVisibility()==0)
//    {
//	 tv1.setVisibility(8);
//	 tv2.setVisibility(0);
//    }else
//    {
//	 tv1.setVisibility(0);
//	 tv2.setVisibility(8);
//    }
   }
  }
  
  String logline;
  public boolean onItemLongClick(AdapterView<?> p1,View p2,int p3,long p4)
  {
   setyc1=true;
   if(!checkmodel)
   {
	logline=((LogItemValue)p1.getItemAtPosition(p3)).lo;
    copyDialog1.show();
   }else
   {
	logline=adapter.getMutilLog();
	copyDialog2.show();
   }
   return true;
  }
  
  public void onClick(DialogInterface p1,int p2)
  {
   switch(p2)
   {
	case -1:
	 mcb.setText(mcb.getText()+"\n\n"+logline);
	 toast(R.string.toast_copy_add);
	 break;
	case -2:
	 toast(R.string.toast_copy_reSet);
	 mcb.setText(logline);
	 break;
	case -3:
	 // 进入多选模式，在这里刷新listView
	 checkmodel=true;
	 mll3.setVisibility(0);
	 shuaXinCheck(0);
	 break;
   }
  }
  
  public void onLogStart(View v)
  {
   setyc1=true;
   String str=medtxt.getText().toString();
   if(str.length()<1)
   {
	toast(R.string.toast_packageName_not_input);return;
   }else if(str.indexOf(".")<1)
   {
	toast(R.string.toast_packageName_not_right);return;
   }else if(str.equals(getPackageName())&&!monitoredMe)
   {
	toast(R.string.toast_packageName_not_monitore);return;
   }else if((!firstStart)&&sp.getString(LOG_PACKAGE,"").equals(str))
   {
	mll1.setVisibility(8);return;
   }
   if(mthread!=null&&!mthread.interrupted())
	mthread.interrupt();
   adapter.clearLogInfo();
   lineH=0;
//   adapter.notifyDataSetChanged();
   mll1.setVisibility(8);
   sp.edit().putString(LOG_PACKAGE,str).commit();
   if(mitem!=null)mitem.setTitle(R.string.menu_title_suspend)
   .setIcon(R.drawable.ic_media_pause);
   startLog(str);
   firstStart=false;
  }

  public void lianCheck(View v)
  {
   setyc1=true;
   lianmodel=true;
   toast(R.string.toast_choose_first);
//   throw new NullPointerException("This is a NullPointerException.No why.");
  }
  
  public void selectAll(View v)
  {
   setyc1=true;
   adapter.setChecked(checkmodel);
   shuaXinCheck(-1);
  }
  
  public void exitCheckModel(View v)
  {
   setyc1=true;
   checkmodel=false;
   mll3.setVisibility(8);
   adapter.setChecked(checkmodel);
   shuaXinCheck(8);
  }
  
  public void shuaXinCheck(int visible)
  {
   int seeFirst=listview.getFirstVisiblePosition();
   for(int i=0;i<listview.getChildCount();i++)
   {
    CheckBox cb1=(CheckBox)listview.getChildAt(i)
	 .findViewById(R.id.logitemCB1);
	cb1.setChecked(adapter.intItem.get(seeFirst+i).checked);
    if(visible!=-1)cb1.setVisibility(visible);
   }
  }
  
  public void findClick(View v)
  {
   setyc1=true;
   int pn=0;
   String stxt=medsrh.getText().toString();
   if(stxt.length()<1)
   {
	toast(R.string.toast_searchText_not_input);
	return;
   }else if(((TextView)v).getText().equals("⇩"))
   {
	pn=adapter.findLog(findlogPosition+1,true,stxt);
   }else
   {
	pn=adapter.findLog(findlogPosition-1,false,stxt);
   }
   if(pn>=0&&pn<adapter.getCount())
   {
	View lCI;
	if(findlogPosition>=0&&findlogPosition<adapter.getCount())
	{
	 adapter.intItem.get(findlogPosition).searchOn=false;
	 lCI=listview.getChildAt(findlogPosition-listview.getFirstVisiblePosition());
	 if(lCI!=null)lCI.findViewById(R.id.logitemLinearL1)
	  .setBackgroundResource(R.color.alpha);
	}
	adapter.intItem.get(pn).searchOn=true;
	lCI=listview.getChildAt(pn-listview.getFirstVisiblePosition());
	if(lCI!=null)lCI.findViewById(R.id.logitemLinearL1).setBackgroundResource(R.color.color_searched);
	listview.setSelection(pn);
	findlogPosition=pn;
   }
  }
  
  public void closeFind(View v)
  {
   setyc1=true;
   View lCI=null;
   if(findlogPosition>=0)
   {
	adapter.intItem.get(findlogPosition).searchOn=false;
	lCI=listview.getChildAt(findlogPosition-listview.getFirstVisiblePosition());
   }
   if(lCI!=null)lCI.findViewById(R.id.logitemLinearL1)
	.setBackgroundResource(R.color.alpha);
   mll2.setVisibility(8);
   findlogPosition=-1;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
   getMenuInflater().inflate(R.xml.menu,menu);
   mitem=menu.findItem(R.id.menu_read);
   if(logre!=null&&logre.reading)
   {
	mitem.setTitle(R.string.menu_title_suspend)
	.setIcon(R.drawable.ic_media_pause);
   }
   return true;
  }
  
//  public boolean onPrepareOptionsMenu(Menu menu)
//  {
//   menu.findItem(R.id.menu_more);
//   return true;
//  }
  
  public boolean onOptionsItemSelected(MenuItem item)
  {
   switch(item.getItemId())
   {
	case R.id.menu_read:
	 setyc1=true;
	 if(logre==null)
	 {
	  Log.e("logc","the class LogReader is null.");
	 }else if(logre.reading)
	 {
	  logre.reading=false;
	  item.setTitle(R.string.menu_title_start)
	  .setIcon(R.drawable.ic_media_play);
	 }else
	 {
	  logre.reading=true;
	  item.setTitle(R.string.menu_title_suspend)
	  .setIcon(R.drawable.ic_media_pause);
	 }
	 break;
	case R.id.menu_clear:
	 setyc1=true;
	 adapter.clearLogInfo();
	 lineH=0;
	 adapter.notifyDataSetChanged();
	 break;
	case R.id.menu_search:
	 setyc1=true;
	 mll2.setVisibility(0);
	 break;
	case R.id.menu_newstart:
	 setyc1=true;
	 mll1.setVisibility(0);
	 break;
	case R.id.menu_setting:
	 startActivity(new Intent(this,SettingsActivity.class));
	 break;
   }
   return true;
  }
  
  public void onBackPressed()
  {
   super.onBackPressed();
   if(logre!=null)logre.closeReader();
   if(mthread!=null&&!mthread.interrupted())
	mthread.interrupt();
   System.exit(0);
  }
  
  
}
