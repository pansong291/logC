package pansong291.logc.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import pansong291.logc.R;

public class UpDataActivity extends Zactivity
{
 String urli;
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  // TODO: Implement this method
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_updata);
  urli=getIntent().getStringExtra("downU");
  gotoupdata(null);
 }
 
 public void gotoupdata(View v)
 {
  Uri uri=Uri.parse(urli); 
  Intent it=new Intent(Intent.ACTION_VIEW,uri);
  startActivity(it);
 }

 @Override
 public void onBackPressed()
 {
  // TODO: Implement this method
  super.onBackPressed();
  System.exit(0);
 }
 
}
