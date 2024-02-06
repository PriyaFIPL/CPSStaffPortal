package com.cps.staffportal.geofencingattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.cps.staffportal.CheckNetwork;
//import com.cps.staffportal.LoginsrmActivity;
import com.cps.staffportal.HomePageGridViewLayout;
import com.cps.staffportal.R;

import webservice.LOCSqlliteController;

public class LOCLandingPage extends AppCompatActivity {
    private TextView tvEmployee, tvInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        setContentView(R.layout.landingpage_loc);
        LOCStatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorPrimary));

        tvInformation = findViewById(R.id.tvInformation);
        try {
//            ReturnMessage = getIntent().getStringExtra("ReturnMessage","");
            tvInformation.setText(getIntent().getStringExtra("ReturnMessage"));
        } catch (Exception e) {
        }
        // finding the elements by their id's alloted.
        tvEmployee = findViewById(R.id.txtEmployeeName);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridViewmenu);
        setSingleEvent(gridLayout);
        if (!CheckNetwork.isInternetAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "You dont have Internet connection", Toast.LENGTH_LONG).show();
            tvInformation.setText("You dont have Internet connection");
            return;
        }
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
//        lngEmployeeId = loginsession.getLong("userid", 1);
        tvEmployee.setText("Welcome " + loginsession.getString("employeename", ""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_CANCELED) {
                tvInformation.setText(data.getData().toString());
            }
        }
    }

    private void setSingleEvent(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Send intent to SingleViewActivity
                    if (!CheckNetwork.isInternetAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "You dont have Internet connection", Toast.LENGTH_LONG).show();
                        tvInformation.setText("You dont have Internet connection");
                        return;
                    } else {
                        tvInformation.setText("");
                    }
                    if (finalI == 0) {
                        Intent intent = new Intent(LOCLandingPage.this, LOCMainActivity.class);
                        //TODO Radha
                        //    Intent intent = new Intent(LandingPage.this, MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        startActivityForResult(intent, 2);// Activity is started with requestCode 2
                        finish();
                    }
                    if (finalI == 1) {
                        Intent intent = new Intent(LOCLandingPage.this, LOCPunchDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    if (finalI == 2) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LOCLandingPage.this);
                        alertDialogBuilder.setTitle("Exit Attendance?");
                        alertDialogBuilder
                                .setMessage("Click yes to exit!")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("SessionLogin", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = myPrefs.edit();
                                                editor.clear();
                                                editor.commit();
                                                LOCSqlliteController sc = new LOCSqlliteController(getApplicationContext());
                                                sc.deleteLoginStaffDetails();
                                                Intent intent = new Intent(LOCLandingPage.this, HomePageGridViewLayout.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
//                                    moveTaskToBack(true);
//                                    android.os.Process.killProcess(android.os.Process.myPid());
//                                    System.exit(1);
                                            }
                                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });
        }
    }
}