package com.cps.staffportal.geofencingattendance;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.cps.staffportal.HomeScreenCategory;
import com.cps.staffportal.HomePageGridViewLayout;
import com.cps.staffportal.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import webservice.WebService;

public class LOCPunchDetails extends AppCompatActivity { //implements View.OnClickListener{
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private TextView tvPageTitle, tvLastUpdated;
    private static String strParameters[];
    private String strResultMessage="";
    private static String ResultString = "";
    private long lngEmployeeId=0;
    private ArrayList<String> punch_list = new ArrayList<String>(200);
//    ImageButton butLogout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.punchreport_loc);
        Button btnBack=(Button) findViewById(R.id.button_back);
        Button btnRefresh=(Button) findViewById(R.id.button_refresh);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
        btnRefresh.setVisibility(View.GONE);
        TextView tvPageTitle = (TextView) findViewById(R.id.pageTitle);
        tvPageTitle.setText("Attendance Punch Report");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//
//        // using toolbar as ActionBar
//        setSupportActionBar(toolbar);

//        butLogout = (ImageButton) findViewById(R.id.btnlogout);
//        butLogout.setOnClickListener(this);

        LOCStatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorPrimary));
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
        displayPunchDetails();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, LOCLandingPage.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnlogout:
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                alertDialogBuilder.setTitle("Exit Application?");
//                alertDialogBuilder
//                        .setMessage("Click yes to exit!")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = myPrefs.edit();
//                                editor.clear();
//                                editor.commit();
//                                SqlliteController sc = new SqlliteController(getApplicationContext());
//                                moveTaskToBack(true);
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//        }
//    }

    public void displayPunchDetails(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strFromDate = formatter.format(date)+ " 00:01";
        String strToDate = formatter.format(date)+ " 23:59";
         strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId),
                            "String","fromdate",strFromDate,
                            "String","todate",strToDate};
        WebService.strParameters = strParameters;
        WebService.METHOD_NAME = "getPunchReport";
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(LOCPunchDetails.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading......");
            //show dialog
            dialog.show();
//            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try{
                punch_list.clear();
                JSONObject jsonObject = new JSONObject(ResultString.toString());
                if (jsonObject.getString("Status").equals("Error"))
                    strResultMessage = jsonObject.getString("Message");

                if (jsonObject.getString("Status").equals("Success")){
                    String strPunchDetails = jsonObject.getString("Data");
                    String[] strColumns = strPunchDetails.split(",");
                    for (int i = 0; i <= strColumns.length - 1; i++) {
                        punch_list.add(strColumns[i]);
                    }
                }
                else{
                    Toast.makeText(LOCPunchDetails.this, "Response: "+strResultMessage , Toast.LENGTH_LONG).show();
                    punch_list.add(strResultMessage);
                }
                if (punch_list.size() == 0) {
                    Toast.makeText(LOCPunchDetails.this, "Response: No Data Found", Toast.LENGTH_LONG).show();
                } else {
                    mRecyclerView = (RecyclerView) findViewById(R.id.rvPunchList); // Assigning the RecyclerView Object to the xml View
                    mRecyclerView.setHasFixedSize(true);
                    // Letting the system know that the list objects are of fixed size
                    LOCPunchDetailsLVAdapter TVA = new LOCPunchDetailsLVAdapter(punch_list, R.layout.viewpunchlistitem_loc);
                    mRecyclerView.setAdapter(TVA);
                    mLayoutManager = new LinearLayoutManager(LOCPunchDetails.this);                 // Creating a layout Manager
                    mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(LOCPunchDetails.this, "Response: "+strResultMessage + " , " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}