package com.cps.staffportal.geofencingattendance;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.cps.staffportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import webservice.WebService;

public class LOCMapsActivity extends AppCompatActivity {

    private LOCGpsTracker LOCGpsTracker;
    private LatLng latLng;
    private TextView tvResult;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.landingpage_loc);
        tvResult = findViewById(R.id.tvInformation);
        tvResult.setText("Wait authenticate process going on ...........");
//        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
//
//        try {
//            strNetId = loginsession.getString("netid", "");
//            strPassword = loginsession.getString("pwd", "");
//            strGPSCoordinates = loginsession.getString("gpscoordinate", "");
//            strAltGPSCoordinates = loginsession.getString("alternategpscoordinate", "");
//            String[] strColumns = strGPSCoordinates.split(",");
//            ldblGPSCoordinates1 = Double.parseDouble(strColumns[0].toString());
//            ldblGPSCoordinates2 = Double.parseDouble(strColumns[1].toString());
//
//            strColumns = strAltGPSCoordinates.split(",");
//            ldblAltGPSCoordinates1 = Double.parseDouble(strColumns[0].toString());
//            ldblAltGPSCoordinates2 = Double.parseDouble(strColumns[1].toString());
//            ldblRadius = Double.parseDouble(loginsession.getString("radius", "1.00"));
//            ldblAltRadius = Double.parseDouble(loginsession.getString("alternateradius", "1.00"));
//        }catch(Exception e){
//            authenticateUser();
//        }

        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            else{
                getLocation();
            }
        } catch (Exception e){
            e.printStackTrace();
       }
    }

    public static String getMobileIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress().toUpperCase();
                           /* boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    // drop ip6 port suffix
                                    int delim = sAddr.indexOf('%');
                                    return delim < 0 ? sAddr : sAddr.substring(0, delim);
                                }
                            }*/
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public void authenticateUser(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String strIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        if (strIpAddress.equals("")){
            strIpAddress = getMobileIPAddress(true);
        }
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        String strDeviceModel = Build.MODEL;
        String strDeviceBrand = Build.BRAND;
        String imeiNumber = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            else{
                LOCGpsTracker = new LOCGpsTracker(LOCMapsActivity.this);
                if (LOCGpsTracker.canGetLocation()) {
                    double latitude = LOCGpsTracker.getLatitude();
                    double longitude = LOCGpsTracker.getLongitude();
                    strGpsCoordinates = latitude + "," + longitude;
                }
                LOCGpsTracker.stopUsingGPS();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        strParameters = new String[]{"String", "netid", strNetId, "String", "password", strPassword,
                "String","mobilemodel",strDeviceModel + " " + strDeviceBrand,
                "String","ipaddress",strIpAddress,"String","gpscoordinates",strGpsCoordinates,
                "String","deviceUID",imeiNumber};
        new Thread(new Runnable() {
            public void run() {
                WebService.strParameters = strParameters;
                WebService.METHOD_NAME = "AuthenticateUser";
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LOCLandingPage.class);
        startActivity(intent);
        // finish the activity picture
        this.finish();
    }

    public void getLocation(){  //View view
        LOCGpsTracker = new LOCGpsTracker(LOCMapsActivity.this);
        if (LOCGpsTracker.canGetLocation()) {
            double latitude = LOCGpsTracker.getLatitude();
            double longitude = LOCGpsTracker.getLongitude();
            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        }else{
            LOCGpsTracker.showSettingsAlert();
        }
    }
}
