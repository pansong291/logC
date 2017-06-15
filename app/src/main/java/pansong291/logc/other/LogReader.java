package pansong291.logc.other;

import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class LogReader implements Runnable
{
 private int hour,minute,second;
 public boolean reading=true;
 private boolean running=true;
 private String packageN;
 private Handler handler;
 private BufferedReader reader;
 
 public void run()
 {
  setNowTime();
  readLog();
 }
 
 private void readLog()
 {
  try
  {
   String commands[]=new String[]{"logcat -v threadtime"};
   Process process=Runtime.getRuntime().exec("su");
   DataOutputStream os=new DataOutputStream(process.getOutputStream());
   for(String command:commands)
   {
    if(command==null)
    {
     continue;
    }

    // donnot use os.writeBytes(commmand), avoid chinese charset error
    os.write(command.getBytes());
    os.writeBytes("\n");
    os.flush();
   }
   os.writeBytes("exit\n");
   os.flush();
   //以上为通过root得到process
   reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
   String line,pid=null;
   while(running)
   {
    if((line=reader.readLine())==null)continue;
	if(reading&&filteHistory(line))
	{
	 if(line.indexOf(packageN)>-1)
	 {
	  sendTxt(line);
	  if(pid==null&&line.indexOf("Start proc "+packageN)>-1&&line.indexOf("uid=")>-1)
	  {
	   pid=line.substring(line.indexOf("Start proc "+packageN),line.indexOf("uid=")).split("=")[1];
	  }else if(pid==null&&line.indexOf("Start proc ")>-1&&line.indexOf(":"+packageN)>-1)
      {
       pid=line.substring(line.indexOf("Start proc "),line.indexOf(":"+packageN)).split("c ")[1];
      }else if(pid==null&&line.indexOf("Start proc")>-1)
      {
       
      }
	 }else if(pid!=null&&line.indexOf(pid)>-1)
	 {
	  sendTxt(line);
	 }
	}
   }
  }catch(Exception e)
  {}
 }
 
 public LogReader(String s,Handler h)
 {
  packageN=s;handler=h;
 }
 
 private void setNowTime()
 {
  Time tt=new Time();
  tt.setToNow();
  hour=tt.hour;
  minute=tt.minute;
  second=tt.second;
 }
 
 private boolean filteHistory(String l)
 {
  int k,m,n,s;
  k=l.indexOf(":");
  if(k<2)
   return true;
  m=Integer.parseInt(l.substring(k-2,k).trim());
  n=Integer.parseInt(l.substring(k+1,k+3).trim());
  s=Integer.parseInt(l.substring(k+4,k+6).trim());
  return(m>=hour&&n>=minute&&s>=second);
 }
 
 public void sendTxt(String g)
 {
  Message msg=handler.obtainMessage(-83,g);
  handler.sendMessage(msg);
 }
 
// public String getlog()
// {
//  return log.toString();
// }
 
 public boolean closeReader()
 {
  try
  {
   running=false;
   if(reader!=null)reader.close();
  }catch(Exception e)
  {
   return false;
  }
  return true;
 }
 
}
