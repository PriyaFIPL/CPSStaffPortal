package com.cps.staffportal.geofencingattendance;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class LOCStatusColor {

  public static void SetStatusColor(Window window, int colorCode){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(colorCode);
    }
  }
}