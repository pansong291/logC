package pansong291.logc.other;

import java.io.DataOutputStream;

public class Utils
{
 public static boolean checkRootPermission()
 {
  int result=-1;

  Process process=null;
  String command="echo root";

  DataOutputStream os=null;
  try
  {
   process=Runtime.getRuntime().exec("su");
   os=new DataOutputStream(process.getOutputStream());

   // donnot use os.writeBytes(commmand), avoid chinese charset error
   os.write(command.getBytes());
   os.writeBytes("\nexit\n");
   os.flush();

   result=process.waitFor();

   if(process!=null)
   {
    process.destroy();
   }
  }catch(Exception e)
  {
  }finally
  {
   try{
   if(os!=null)
    os.close();
   }catch(Exception e){}
  }
  return result==0;
 }
}
