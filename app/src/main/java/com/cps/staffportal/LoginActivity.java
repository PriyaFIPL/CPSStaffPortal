package com.cps.staffportal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cps.staffportal.geofencingattendance.LOCLandingPage;
import com.cps.staffportal.geofencingattendance.LOCPunchDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.cps.staffportal.geofencingattendance.LOCStatusColor;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import webservice.EncryptDecrypt;
import webservice.SqlliteController;
import webservice.WebService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button butLogin;
    ImageButton btnInfo;
    TextInputEditText etUsername, etPassword;
    TextInputLayout passwordInputLayout,usernameInputLayout;
    String editTextUsername, editTextPassword;
    ProgressDialog dialog;
    private static String strParameters[];
    private static String ResultString = "";
    SQLiteDatabase db;
    private String imeiNumber="";
    private String strDeviceModel = "";
    private String strIpAddress = "";
    private String strDeviceBrand = "";
    private String strGpsCoordinates = "";
    private String token="";
    TelephonyManager telephonyManager;
    SqlliteController controllerdb = new SqlliteController(this);
    EncryptDecrypt crypt = new EncryptDecrypt();

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        LOCStatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorPrimary));

        dialog = new ProgressDialog(LoginActivity.this);

//        FirebaseMessaging.getInstance().subscribeToTopic("evarsity");
//
//        FirebaseInstanceId.getInstance().getInstanceId()
//        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (!task.isSuccessful()) {
//                    //Log.w(TAG, "getInstanceId failed", task.getException());
//                    return;
//                }
//                token = task.getResult().getToken();
//               //  Log.e("Token Test : ", token);
//               }
//        });
        /*
        if(getIntent().getExtras()!= null) {
            String notificationTitle = getIntent().getExtras().toString();
            String notificationTitle1 = getIntent().getExtras().getString("notification"," Title Empty");
            String notificationBody = getIntent().getExtras().getString("body","Body Empty");
            String notificationMessage = getIntent().getExtras().getString("title","title Empty");
            Log.i("TEST: " ,notificationTitle+" : "+notificationBody +" : "+ notificationMessage);
            Toast.makeText(this,notificationTitle+" : "+notificationBody+" : "+ notificationMessage,Toast.LENGTH_LONG).show();
        }*/
        deviceId();

        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        db = controllerdb.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM stafflogindetails ", null);
            if (cursor.moveToFirst()) {
                SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
                SharedPreferences.Editor ed = loginsession.edit();
                do {
                    ed.putLong("userid", cursor.getLong(cursor.getColumnIndex("employeeid")));
                    ed.putString("employeename", cursor.getString(cursor.getColumnIndex("employeename")));
                    ed.putString("department", cursor.getString(cursor.getColumnIndex("department")));
                    ed.putString("designation", cursor.getString(cursor.getColumnIndex("designation")));
                    ed.commit();
                } while (cursor.moveToNext());
               // Intent intent = new Intent(MainActivity.this, HomePageGridViewLayout.class);
//                Intent intent = new Intent(LoginActivity.this, HomePageGridViewLayout.class);
                Intent intent = new Intent(LoginActivity.this, HomePageGridViewLayout.class);
                startActivity(intent);
                finish();
            }
            cursor.close();
        }catch (Exception e){

        }
        if(CheckNetwork.isInternetAvailable(LoginActivity.this)) {
            butLogin = (Button) findViewById(R.id.loginButton);
            butLogin.setOnClickListener(this);
            btnInfo = (ImageButton) findViewById(R.id.btnInfo);
            btnInfo.setOnClickListener(this);
            hideKeyboard();
        }
        else {
            Toast.makeText(LoginActivity.this,getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
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
        etUsername = (TextInputEditText) findViewById(R.id.usernameInput);
        etPassword = (TextInputEditText) findViewById(R.id.passwordInput);
        passwordInputLayout =  (TextInputLayout) findViewById(R.id.passwordInputLayout);
        usernameInputLayout =  (TextInputLayout) findViewById(R.id.usernameInputLayout);
        editTextUsername = etUsername.getText().toString().trim();
        editTextPassword = etPassword.getText().toString().trim();
        strParameters = new String[] { "String","userid","", "String","password", ""};
        SharedPreferences myPrefs = v.getContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
        SqlliteController sc = new SqlliteController(v.getContext());
        sc.deleteLoginStaffDetails();

        if (v.getId() == R.id.loginButton){
            if (!CheckNetwork.isInternetAvailable(LoginActivity.this)){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                return;
            } else {
                if (!Utility.isNotNull(editTextUsername)) {
                    usernameInputLayout.setError("username is required!");
                }
                if (!Utility.isNotNull(editTextPassword)) {
                    // etPassword.setError("password is required!");
                    passwordInputLayout.setError("password is required!");
                }
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                String strIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                if (strIpAddress.equals("")) {
                    strIpAddress = getMobileIPAddress(true);
                }
                strDeviceModel = Build.MODEL;
                strDeviceBrand = Build.BRAND;
                deviceId();
                token = "dF8s0YDG3gU:APA91bGnf_G_CNOeAfAUPQgD8IJUZYbsX8JoxkrOOJk1DT9KO6G8brEcTNoh2CxZPLAeS1ejoaRmtoMdNP2bZDo7jrzGII4DNDONeUuoUU1XJJETRaGgX8eOlZ98mC9AY-9F7qxXmT-H";
                // public static String URL = "https://firstlineinfotech.com/srmistEmployeeAndroid/EmployeeAndroid?wsdl";
                if (Utility.isNotNull(editTextPassword) && Utility.isNotNull(editTextUsername)) {
                    Log.i("TEST RADHA TEST", imeiNumber);
                    strParameters = new String[]{"String", "userid", editTextUsername, "String", "password", editTextPassword,
                            "String", "mobilemodel", strDeviceModel + " " + strDeviceBrand,
                            "String", "ipaddress", strIpAddress, "String", "gpscoordinates", strGpsCoordinates,
                            "String", "deviceUID", imeiNumber, "String", "acesstoken", token};
                       /* strParameters = new String[]{"String", "userid", editTextUsername, "String", "password", editTextPassword,
                                "String", "deviceid", imeiNumber, "String", "acesstoken", token};
                        strParameters = new String[]{"String", "netid", editTextUsername, "String", "password", editTextPassword,
                                "String","mobilemodel",strDeviceModel + " " + strDeviceBrand,
                                "String","ipaddress",strIpAddress,"String","gpscoordinates",strGpsCoordinates,
                                "String","deviceUID",imeiNumber};
                        */
                    new Thread(new Runnable() {
                        public void run() {
                            WebService.strParameters = strParameters;
                            WebService.METHOD_NAME = "authenticateLoginUserJson";  //"authenticateLoginUserJsonEncrypted";
                            AsyncCallWS task = new AsyncCallWS();
                            task.execute();
                        }
                    }).start();
                    // dialog.setMessage("Loading......");
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.show();
                    hideKeyboard();
                    butLogin.setEnabled(false);
                }
            }
        } else if (v.getId() == R.id.btnInfo) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginCredentials), Toast.LENGTH_LONG).show();
        }else {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void deviceId(){
        try {
            telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
             imeiNumber = telephonyManager.getDeviceId();
        }catch(SecurityException e){
            imeiNumber = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                        return;
                    }
                    imeiNumber = telephonyManager.getDeviceId();
                    Log.d("TAG", imeiNumber);
//                    Toast.makeText(MainActivity.this,imeiNumber,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.loginPermission),Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... params) {
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
               // String strResultString = crypt.getDecryptedData(ResultString.toString());
                Log.i("TEST : ",ResultString.toString());
                JSONObject object = new JSONObject(ResultString.toString());
                if (!object.isNull("employeeid")){
                    ed.putLong("userid", object.getLong("employeeid"));
                    ed.putLong("officeid", object.getLong("officeid"));
//                    ed.putString("registerno", object.getString("registerno"));
                    ed.putString("employeename", object.getString("employeename"));
                    ed.putString("department", object.getString("department"));
                    ed.putString("designation", object.getString("designation"));
                    ed.putString("netid",editTextUsername);
                    ed.putString("pwd",editTextPassword);
//                    ed.putString("school", object.getString("officename"));
//                    ed.putLong("courseid", object.getLong("courseid"));
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginSuccess), Toast.LENGTH_LONG).show();
                    ed.commit();
                    SqlliteController sc = new SqlliteController(LoginActivity.this);
                    sc.deleteLoginStaffDetails();
                    Log.e("RADHA TEST","LOG ERROR 1");
               sc.insertLoginStaffDetails(object.getLong("employeeid"),object.getString("employeename"),
                            object.getString("department"),object.getString("designation"),
                            "",editTextUsername,editTextPassword);
                    Log.e("RADHA TEST","LOG ERROR 2");
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent intent = new Intent(LoginActivity.this, HomePageGridViewLayout.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    butLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e){
                System.out.println("Error in Login Activity:"+e.getMessage());
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        finishAffinity();
    }
}