package com.cps.staffportal.geofencingattendance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.cps.staffportal.CheckNetwork;
import com.cps.staffportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import webservice.LOCSqlliteController;
import webservice.WebService;

public class LOCLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LOCGpsTracker LOCGpsTracker;
    Button butLogin;
    TextInputEditText etUsername, etPassword;;
    String editTextUsername, editTextPassword;
    private static String strParameters[];
    private static String ResultString = "";
    SQLiteDatabase db;
    private String imeiNumber="";
    private String token="";
    private String strGpsCoordinates = "";
    TelephonyManager telephonyManager;
    LOCSqlliteController controllerdb = new LOCSqlliteController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.activity_login_loc);

        etUsername = (TextInputEditText) findViewById(R.id.etNetId);
        etPassword = (TextInputEditText) findViewById(R.id.mypass);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        LOCStatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorPrimary));
        db = controllerdb.getReadableDatabase();
//        Intent intent = new Intent(LoginActivity.this, LandingPage.class);
//        startActivity(intent);

        try {
            Cursor cursor = db.rawQuery("SELECT employeeid,employeename,department,designation," +
                    "netid,password FROM stafflogindetails ", null);
            if (cursor.moveToFirst()){
                SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
                SharedPreferences.Editor ed = loginsession.edit();
                do {
                    editTextUsername = cursor.getString(cursor.getColumnIndexOrThrow("netid"));
                    editTextPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                    ed.putString("netid",cursor.getString(cursor.getColumnIndexOrThrow("netid")));
                    ed.putString("pwd",cursor.getString(cursor.getColumnIndexOrThrow("password")));
                    ed.commit();
                } while (cursor.moveToNext());
                Intent intent = new Intent(LOCLoginActivity.this, LOCLandingPage.class);
                startActivity(intent);
//                if (Utility.isNotNull(editTextPassword) && Utility.isNotNull(editTextUsername)) {
//                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//                    String strIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
//                    telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
//                    String strDeviceModel = Build.MODEL;
//                    String strDeviceBrand = Build.BRAND;
//                    imeiNumber = Settings.Secure.getString(getContentResolver(),
//                            Settings.Secure.ANDROID_ID);
//
//                    strParameters = new String[]{"String", "netid", editTextUsername, "String", "password", editTextPassword,
//                            "String","mobilemodel",strDeviceModel + " " + strDeviceBrand,
//                            "String","ipaddress",strIpAddress,"String","gpscoordinates",strGpsCoordinates,
//                            "String","deviceUID",imeiNumber};
//                    new Thread(new Runnable() {
//                        public void run() {
//                            WebService.strParameters = strParameters;
//                            WebService.METHOD_NAME = "AuthenticateUser";
//                            AsyncCallWS task = new AsyncCallWS();
//                            task.execute();
//                        }
//                    }).start();
//                }
            } else {

            }
            cursor.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(CheckNetwork.isInternetAvailable(LOCLoginActivity.this)) {
            butLogin = (Button) findViewById(R.id.btnlogin);
            butLogin.setOnClickListener(this);
            hideKeyboard();
        }
        else {
            Toast.makeText(LOCLoginActivity.this,"You dont have Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

                            boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    // drop ip6 port suffix
                                    int delim = sAddr.indexOf('%');
                                    return delim < 0 ? sAddr : sAddr.substring(0, delim);
                                }
                            }


                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    @Override
    public void onClick(View v){
        editTextUsername = etUsername.getText().toString().trim();
        editTextPassword = etPassword.getText().toString().trim();
        strParameters = new String[] { "String","netid",""};
        SharedPreferences myPrefs = v.getContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
        LOCSqlliteController sc = new LOCSqlliteController(v.getContext());
        sc.deleteLoginStaffDetails();

        if (v.getId() == R.id.btnlogin){
//            case R.id.btnlogin:
            if (! LOCUtility.isNotNull(editTextUsername)){
                etUsername.setError("username is required!");
            }
            if (! LOCUtility.isNotNull(editTextPassword)){
                etPassword.setError("password is required!");
            }
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String strIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            if (strIpAddress.equals("")){
                strIpAddress = getMobileIPAddress(true);
            }
            telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
            String strDeviceModel = Build.MODEL;
            String strDeviceBrand = Build.BRAND;
            imeiNumber = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            if (LOCUtility.isNotNull(editTextPassword) && LOCUtility.isNotNull(editTextUsername)) {
                strParameters = new String[]{"String", "netid", editTextUsername, "String", "password", editTextPassword,
                        "String","mobilemodel",strDeviceModel + " " + strDeviceBrand,
                        "String","ipaddress",strIpAddress,"String","gpscoordinates",strGpsCoordinates,
                        "String","deviceUID",imeiNumber};
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                    else{
                        LOCGpsTracker = new LOCGpsTracker(LOCLoginActivity.this);
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
                new Thread(new Runnable() {
                    public void run() {
                        WebService.strParameters = strParameters;
                        WebService.METHOD_NAME = "AuthenticateUser";
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }
                }).start();
            }
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
//            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Log.i(TAG, "doInBackground");
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
            SharedPreferences.Editor ed = loginsession.edit();
            try{
                JSONObject object = new JSONObject(ResultString.toString());
                if (object.getString("Status").equals("Success")) {
                    JSONArray data = (JSONArray) object.get("Data");
                    JSONObject datainner = data.getJSONObject(0);
                    ed.putLong("userid", datainner.getLong("employeeid"));
                    ed.putString("employeename", datainner.getString("employeename"));
                    ed.putString("department", datainner.getString("divisionname"));
                    ed.putString("designation", datainner.getString("designation"));
                    ed.putString("gpscoordinate", datainner.getString("gpscoordinate"));
                    ed.putString("radius", datainner.getString("radius"));
                    ed.putString("alternategpscoordinate", datainner.getString("alternategpscoordinate"));
                    ed.putString("alternateradius", datainner.getString("alternateradius"));

                    ed.putString("netid",editTextUsername);
                    ed.putString("pwd",editTextPassword);
                    ed.commit();
                    LOCSqlliteController sc = new LOCSqlliteController(LOCLoginActivity.this);
                    sc.insertLoginStaffDetails(datainner.getLong("employeeid"),datainner.getString("employeename"),
                            datainner.getString("divisionname"),datainner.getString("designation"),
                            editTextUsername,editTextPassword);


//                    Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LOCLoginActivity.this, LOCLandingPage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LOCLoginActivity.this, object.getString("Message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(LOCLoginActivity.this, "Error in NETID Verification" + ex.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error in NETID Verification: " + ex.getMessage());
            }
        }
    }
}