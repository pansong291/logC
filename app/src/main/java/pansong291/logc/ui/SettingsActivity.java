package pansong291.logc.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import pansong291.logc.R;
import pansong291.logc.other.MultiClick;
import pansong291.crash.ActivityControl;

public class SettingsActivity extends PreferenceActivity
implements OnPreferenceClickListener,
OnPreferenceChangeListener
{
 PreferenceScreen pmain;
 Preference prefc1;
 CheckBoxPreference cbp1,cbp2;
 EditTextPreference edtp;
 Preference btn1,btn2,btn3;
 MultiClick multic;
 private boolean firstStart=true;//每次刚开始启动this Activity
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  addPreferencesFromResource(R.xml.settings);
  ActivityControl.getActivityControl().addActivity(this);
  
  pmain=(PreferenceScreen)findPreference("set_main");
  prefc1=findPreference("set_yc");
  cbp1=(CheckBoxPreference)findPreference(Zactivity.SINGLE_LINE);
  cbp2=(CheckBoxPreference)findPreference("set_checkBox2");
  edtp=(EditTextPreference)findPreference(Zactivity.LIST_LEN);
  btn1=findPreference("set_about");
  btn2=findPreference("set_help");
  btn3=findPreference("set_checknew");
  btn3.setTitle("版本: "+MainActivity.VERSION_NAME);
  multic=new MultiClick(4,350)
  {
   protected void onClick(Object[]obj)
   {
	if(firstStart&&!MainActivity.setyc1)
	{
	 MainActivity.setyc2=false;
	}
	MainActivity.setyc1=firstStart=false;
   }
  };

  if(MainActivity.setyc1||MainActivity.setyc2)
  {
   pmain.removePreference(prefc1);
  }else
  {
   MainActivity.setyc1=
   MainActivity.setyc2=true;
  }
  
  btn1.setOnPreferenceClickListener(this);
  btn2.setOnPreferenceClickListener(this);
  btn3.setOnPreferenceClickListener(this);
  edtp.setOnPreferenceChangeListener(this);
  cbp2.setOnPreferenceChangeListener(this);

 }
 
 @Override
 public boolean onPreferenceClick(Preference p1)
 {
  switch(p1.getKey())
  {
   case "set_help":
    new AlertDialog.Builder(this)
     .setTitle(R.string.dialog_title_help)
     .setMessage(R.string.dialog_message_help)
     .setPositiveButton(R.string.dialog_button_positive,null)
     .show();
	break;
   case "set_about":
	if(!MainActivity.setyc2)
	{
	 MainActivity.setyc1=true;
	 MainActivity.setyc2=true;
	}
	multic.onMultiClick(this);
	break;
   case "set_checknew":
	p1.setSummary("正在检测更新中，请稍候...");
	break;
  }
  return true;
 }
 public boolean onPreferenceChange(Preference p1,Object p2)
 {
  switch(p1.getKey())
  {
   case Zactivity.LIST_LEN:
    int sptext=0;
    try{
     sptext=Integer.parseInt(p2.toString());
    }catch(Exception e){}
    if(sptext<20||sptext>5000)
    {
     return false;
    }
    break;
   case "set_checkBox2":
	if(!(boolean)p2)
	 p1.getEditor().remove(Zactivity.TIME_L).commit();
	else p1.getEditor().putLong(Zactivity.TIME_L,1424959652436L).commit();
	break;
  }
  return true;
 }
 
 @Override
 public void onBackPressed()
 {
  super.onBackPressed();
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ActivityControl.getActivityControl().removeActivity(this);
 }
 
 
 
}
