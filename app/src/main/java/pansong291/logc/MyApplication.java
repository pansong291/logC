package pansong291.logc;

import pansong291.logc.ui.CrashActivity;
import pansong291.crash.CrashApplication;

public class MyApplication extends CrashApplication
{
 @Override
 public Class<?> getPackageActivity()
 {
  setShouldShowDeviceInfo(false);
  return CrashActivity.class;
 }

}
