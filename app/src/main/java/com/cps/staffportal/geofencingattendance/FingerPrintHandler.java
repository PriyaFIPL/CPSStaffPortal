package com.cps.staffportal.geofencingattendance;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.hardware.fingerprint.FingerprintManager;
//import android.net.wifi.WifiManager;
//import android.os.Build;
//import android.os.CancellationSignal;
//import android.provider.Settings;
//import android.text.format.Formatter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import webservice.WebService;
//
//@RequiresApi(api = Build.VERSION_CODES.M)
//class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
//
//    private Context context;
//    private TextView tvResult;
//    private ImageView ivScanResult;
//
//    // Constructor
//    public FingerprintHandler(Context mContext) {
//        context = mContext;
//    }
//
//    // Fingerprint authentication starts here..
//    public void Authentication(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
//        CancellationSignal cancellationSignal = new CancellationSignal();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        tvResult = (TextView) ((Activity)context).findViewById(R.id.tvResult);
//        ivScanResult = (ImageView) ((Activity)context).findViewById(R.id.ivScanResult);
//        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
//    }
//    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        tvResult.setText("ERROR");
//        ivScanResult.setImageResource(R.drawable.biometric_failed);
//        super.onAuthenticationError(errorCode, errString);
//    }
//
//    @Override
//    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        tvResult.setText("HELP");
//        ivScanResult.setImageResource(R.drawable.biometric_resultdisplay);
//        super.onAuthenticationHelp(helpCode, helpString);
//    }
//
//    // On authentication failed
//    @Override
//    public void onAuthenticationFailed() {
//        tvResult.setText("FAILED");
//        ivScanResult.setImageResource(R.drawable.biometric_failed);
////        this.update("Authentication Failed!!!", false);
//        super.onAuthenticationFailed();
//    }
//
//    // On successful authentication
//    @Override
//    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//        tvResult.setText("SUCCESS");
//        ivScanResult.setImageResource(R.drawable.biometric_success);
////        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
////        strIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
////        strimeiNumber = Settings.Secure.getString(getContentResolver(),
////                Settings.Secure.ANDROID_ID);
////        strParameters = new String[]{"String", "employeeid", String.valueOf(lngEmployeeId),
////                "String", "ipaddress",strIpAddress, "String", "gpscoordinates",strGpsCoordinates,
////                "String", "deviceUID",strimeiNumber};
////        new Thread(new Runnable() {
////            public void run() {
////                WebService.strParameters = strParameters;
////                WebService.METHOD_NAME = "punchDetails";
////                MainActivity.AsyncCallSaveWS task = new MainActivity.AsyncCallSaveWS();
////                task.execute();
////            }
////        }).start();
//
//        super.onAuthenticationSucceeded(result);
//    }
//}
