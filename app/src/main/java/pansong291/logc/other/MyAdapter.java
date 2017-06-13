package pansong291.logc.other;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import pansong291.logc.R;
import pansong291.logc.ui.MainActivity;

public class MyAdapter extends BaseAdapter
{
 private Context context;
 private LayoutInflater vinflate;
 private ArrayList<LogItemValue>dataList=new ArrayList<LogItemValue>();
 private int tcolor,cansee;
 private Typeface typeface;
 public ArrayList<MyInt>intItem=new ArrayList<MyInt>();
 public MyAdapter(Context c)//,int p)
 {
  context=c;
  vinflate=LayoutInflater.from(context);
//  cansee=MainActivity.singlel?0:8;
//  for(int i=0;i<p;i++)
//  {
//   intItem.add(new MyInt(cansee,false));
//  }
 }

 public void clearLogInfo()
 {
  dataList.clear();
 }
 
 public void removeLogInfo(int i)
 {
  dataList.remove(i);
 }
 
 public void addLogInfo(String a,String b)
 {
  dataList.add(new LogItemValue(a,b));
 }
 
 public int findLog(int po,boolean ne,String fs)
 {
  int i=po;
  if(ne)
  {
   while(i>=0&&i<dataList.size())
   {
	if(dataList.get(i).lo.indexOf(fs)>-1)
	 return i;
	i++;
   }
  }else
  {
   while(i>=0&&i<dataList.size())
   {
	if(dataList.get(i).lo.indexOf(fs)>-1)
	 return i;
	i--;
   }
  }
  return -1;
 }
 
 public void setDataMaxLine(int l)
 {
  cansee=MainActivity.singlel?0:8;
  int i1=dataList.size()-l;
  while(i1>0)
  {
   dataList.remove(l-1+(i1--));
  }
  int i2=intItem.size()-l;
  while(i2>0)
  {
   intItem.remove(l-1+(i2--));
  }
  while(i2<0)
  {
   intItem.add(new MyInt(cansee,false));
   i2++;
  }
 }
 
 public void reSetMyInt(boolean k)
 {
  cansee=MainActivity.singlel?0:8;
  for(int i=0;k&&i<intItem.size();i++)
  {
   intItem.get(i).index=cansee;
  }
 }
 
 public String getMutilLog()
 {
  String s="";
  for(int i=0;i<dataList.size();i++)
  {
   if(intItem.get(i).checked)
    s+=dataList.get(i).lo+"\n";
  }
  return s;
 }
 
 public void setLianChecked(int a,int b)
 {
  boolean b1=a>b;
  int c=b1?a-b:b-a,d=b1?a:b;
  for(int i=0;i<c+1;i++)
  {
   intItem.get(d-i).checked=true;
  }
 }
 
 public void setChecked(boolean b)
 {
  for(int i=0;i<dataList.size();i++)
  {
   intItem.get(i).checked=b;
  }
 }
 
 public static int getTxtC(boolean b,String g)
 {
  int c=-8355712;
  if(g==null)g="none";
  if(b)c=MainActivity.blackBackground?
	-16777216:-1;
  else if(g.indexOf(" D ")>-1)c=-14649152;
  else if(g.indexOf(" I ")>-1)c=-16742400;
  else if(g.indexOf(" W ")>-1)c=-4026624;
  else if(g.indexOf(" E ")>-1)c=-3526400;
  return c;
 }
 
 @Override
 public int getCount()
 {
  return dataList.size();
 }
 public Object getItem(int p1)
 {
  return dataList.get(p1);
 }
 public long getItemId(int p1)
 {
  return p1;
 }
 public View getView(int i,View v,ViewGroup p)
 {
  LogItemView liv;
  if(v==null)
  {
   v=vinflate.inflate(R.layout.list_logitem,null);
   liv=new LogItemView();
   //这句要注释掉，否则item不能点击
   //v.setClickable(true);
   liv.backv=v.findViewById(R.id.logitemLinearL1);
   liv.hH=(TextView)v.findViewById(R.id.logitemTV1);
   liv.lo1=(TextView)v.findViewById(R.id.logitemTV2);
   liv.lo2=(TextView)v.findViewById(R.id.logitemTV3);
   liv.cb=(CheckBox)v.findViewById(R.id.logitemCB1);
   v.setTag(liv);
  }else
  {
   liv=(LogItemView)v.getTag();
  }
  LogItemValue logUnit=dataList.get(i);
  typeface=Typeface.create(MainActivity.choosefont,MainActivity.bigFont?1:0);
  liv.lo1.setTypeface(typeface);
  liv.lo2.setTypeface(typeface);
  liv.hH.setTypeface(typeface);
  liv.lo1.setTextSize(MainActivity.fontsize);
  liv.lo2.setTextSize(MainActivity.fontsize);
  liv.hH.setTextSize(MainActivity.fontsize);
  if(MainActivity.checkmodel)
   liv.cb.setVisibility(0);
  else liv.cb.setVisibility(8);
  liv.cb.setChecked(intItem.get(i).checked);
  if(!MainActivity.hangHisLog)
   liv.hH.setText(i+1+".");
  else if(logUnit.Hh!=null)
   liv.hH.setText(logUnit.Hh);
  if(logUnit!=null)
  {
   liv.lo1.setText(logUnit.lo);
   liv.lo2.setText(logUnit.lo);
   liv.lo1.setTextColor(tcolor=getTxtC(false,logUnit.lo));
   liv.lo2.setTextColor(tcolor);
  }
  liv.lo1.setVisibility(intItem.get(i).index);
  liv.lo2.setVisibility(8-intItem.get(i).index);
  if(intItem.get(i).searchOn)
   liv.backv.setBackgroundResource(R.color.color_searched);
  else
   liv.backv.setBackgroundResource(R.color.alpha);
  return v;
 }
 
 private class LogItemView
 {
  TextView hH,lo1,lo2;
  CheckBox cb;
  public View backv;
 }
 
 public class LogItemValue
 {
  public String Hh,lo;
  public LogItemValue(String a,String b)
  {
   Hh=a;lo=b;
  }
 }
 
 public class MyInt
 {
  public int index;
  public boolean searchOn,checked;
  public MyInt(int c,boolean d)
  {
   index=c;checked=searchOn=d;
  }
 }
 
}
