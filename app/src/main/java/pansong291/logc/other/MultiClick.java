package pansong291.logc.other;

public abstract class MultiClick
{
 private int c,j,t;
 private long tn;
 public MultiClick()
 {
  reSet(2,260);
 }
 public MultiClick(int count)
 {
  reSet(count,260);
 }
 public MultiClick(int count,int time)
 {
  reSet(count,time);
 }
 private void reSet()
 {
  j=0;tn=0;
 }
 private void reSet(int count,int time)
 {
  c=count;t=time;reSet();
 }
 public boolean onMultiClick(Object...obj)
 {
  if(tn!=0&&System.currentTimeMillis()-tn>t)
   reSet();//重置
  tn=System.currentTimeMillis();
  j++;
  if(j==c)
  {
   onClick(obj);reSet();
   //响应并重置
   return true;
  }
  return false;
 }
 protected abstract void onClick(Object[]o);
}
