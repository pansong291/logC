package pansong291.logc.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.Toast;
import pansong291.crash.ActivityControl;

public class Zactivity extends Activity
{
 public static final String LOG_PACKAGE="logPackage",APP_FIRST_RUN="app_first_run",
 SET_CB3="set_checkBox3",SINGLE_LINE="singleLine",LIST_LEN="listLength",
 TIME_L="timeL",BIG_FONT="bigFont",FONT_SIZE="fontSize",
 FONT_TYPE="fontType",BLACK_BACK="blackBack",LINE_CODE_IS_LOG="lineCodeisLog";
 public final String V_CODE="v_code",QZGX="qzGX";
 
 public SharedPreferences sp;
 public ClipboardManager mcb;
 
 public boolean spPutString(String s1,String s2)
 {
  return sp.edit().putString(s1,s2).commit();
 }
 
 @Override
 protected void onResume()
 {
  super.onResume();
  
 }
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ActivityControl.getActivityControl().addActivity(this);
  
  sp=getSharedPreferences(getPackageName()+"_preferences",0);
  mcb=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ActivityControl.getActivityControl().removeActivity(this);
 }
 
 public void toast(int i)
 {
  toast(getString(i));
 }
 
 public void toast(String s)
 {
  toast(s,0);
 }
 
 public void toast(String s,int i)
 {
  Toast.makeText(this,s,i).show();
 }
 
}
