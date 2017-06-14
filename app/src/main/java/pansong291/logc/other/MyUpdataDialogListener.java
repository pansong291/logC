package pansong291.logc.other;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import pansong291.crash.ActivityControl;
import pansong291.logc.ui.Zactivity;

public class MyUpdataDialogListener implements DialogInterface.OnClickListener
{
 Activity act;
 MyUpdata upd;
 SharedPreferences sp=null;
 
 public MyUpdataDialogListener(Activity a)
 {
  act=a;
 }
 
 public void setUpDa(MyUpdata u)
 {
  upd=u;
 }
 
 @Override
 public void onClick(DialogInterface p1,int p2)
 {
  switch(p2)
  {
   case -1:
//    act.toast("下载地址为"+upd.getDownloadUrl());
    act.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(upd.getDownloadUrl())));
   case -2:
    if(sp==null)sp=act.getSharedPreferences(act.getPackageName()+"_preferences",0);
	if(sp.getBoolean(Zactivity.QZGX,false))
	{
	 ActivityControl.getActivityControl().finishProgrom();
	}
	break;
//   case -3:
//	sp.edit().putBoolean("checkBox3",false).commit();
//	break;
  }
 }
}
