//package com.cps.staffportal;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import webservice.SqlliteController;
//
//public class HomeScreenCategory extends AppCompatActivity {
//    RecyclerView mRecyclerView;                           // Declaring RecyclerView
////    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
//    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
//    private TextView tvPageTitle;
//    private int intCategoryId = 0;
//    private long lngEmployeeId=0;
//    private String strMenuId = "";
//    SQLiteDatabase db;
//    SqlliteController controllerdb = new SqlliteController(this);
//    String[] MENULIST= new String[1];
//    int[] ICONS =new int[1];
//    private int intFlag = 0;
//
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.homescreencategory);
//        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
//        tvPageTitle = (TextView) findViewById(R.id.pageTitle);
//        intFlag=getIntent().getIntExtra("Flag",1);
//        Button btnBack=(Button) findViewById(R.id.button_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//            Intent intent = new Intent(HomeScreenCategory.this, HomePageGridViewLayout.class);
//            startActivity(intent);
//            }
//        });
//        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
//        lngEmployeeId = loginsession.getLong("userid", 1);
//        intCategoryId = loginsession.getInt("categoryid", 0);
////        intCategoryId = getIntent().getIntExtra("CategoryId", 0);
//        if (intCategoryId == 2){// Leave Management
//            tvPageTitle.setText(getResources().getString(R.string.hattendance));
//        }else if (intCategoryId == 3){// Leave Management
//            tvPageTitle.setText(getResources().getString(R.string.mLeaveManagement));
//        }else if (intCategoryId == 4){//Workforce
//            tvPageTitle.setText(getResources().getString(R.string.hWorkForce));
//        }else if (intCategoryId == 5){//Academic
//            tvPageTitle.setText(getResources().getString(R.string.hAcademic));
//        }else if (intCategoryId == 6){//Communication
//            tvPageTitle.setText(getResources().getString(R.string.hNotification));
//        }else if (intCategoryId == 7){//Dashboard
//            tvPageTitle.setText(getResources().getString(R.string.hDashboard));
//        }else if (intCategoryId == 8){//Canteen
//            tvPageTitle.setText(getResources().getString(R.string.hCanteen));
//        }else if (intCategoryId == 9){//Others
//            tvPageTitle.setText(getResources().getString(R.string.hOthers));
//        }
//        getMenuValues();
//    }
//
//    @Override
//    public void onBackPressed(){
//        super.onBackPressed();
//        Intent intent;
////        if (intFlag == 1){
////            intent = new Intent(HomeScreenCategory.this, HomeScreenCategory.class);
////            startActivity(intent);
////        }
////        if (intFlag == 2){
//            intent = new Intent(HomeScreenCategory.this, HomePageGridViewLayout.class);
//            startActivity(intent);
////        }
//        this.finish();
//    }
//
//    private void getMenuValues() {
//        db = controllerdb.getReadableDatabase();
//        try {
//            if (intCategoryId == 1){ // Profile
//                strMenuId = "1";
//            }else  if (intCategoryId == 2){ // Profile
//                strMenuId = "23,24";
//            }else if (intCategoryId == 3){// Leave Management
//                strMenuId = "3,4,5,20";
//            }else if (intCategoryId == 4){//Workforce
//                strMenuId = "8,9";
//            }else if (intCategoryId == 5){//Academic
//                strMenuId = "2,6,7,19";
//            }else if (intCategoryId == 6){//Communication
//                strMenuId = "10,11,13,16";  //,12 atpresent Notificationtoall hided
//            }else if (intCategoryId == 7){//Dashboard
//                strMenuId = "17,18";
//            }else if (intCategoryId == 8){//Canteen
//                strMenuId = "14";
//            }else if (intCategoryId == 9){//Others
//                strMenuId = "15";
//            }else if (intCategoryId == 10){//Logout
//                strMenuId = "50";
//            }
//            Cursor cursor = db.rawQuery("SELECT * FROM userwisemenuaccessrights WHERE employeeid =" + lngEmployeeId + " AND menuid in ("+strMenuId +")  ORDER BY  menusortnumber", null );
//            MENULIST= new String[cursor.getCount()];
//            ICONS= new int[cursor.getCount()];
//            cursor.moveToFirst();
//            if (cursor.moveToFirst()){
//                int i = 0;
//                do {
//                    long lngMenuId=cursor.getLong(cursor.getColumnIndex("menuid"));
//                    String strMenuname = cursor.getString(cursor.getColumnIndex("menuname"))+ "##" +lngMenuId;
//
//                    MENULIST[i] = strMenuname;
//                    if (lngMenuId == 1){
//                        ICONS[i] = R.drawable.icon_profile;
//                    }else if (lngMenuId == 2){
//                        ICONS[i] = R.drawable.icon_timetable;
//                    } else if (lngMenuId == 3){
//                        ICONS[i] = R.drawable.icon_leavestatus;
//                    }else if (lngMenuId == 4){
//                        ICONS[i] = R.drawable.icon_leaveentry;
//                    }else if (lngMenuId == 5){
//                        ICONS[i] = R.drawable.icon_leaveapproval;
//                    }else if (lngMenuId == 6){
//                        ICONS[i] = R.drawable.icon_studentattendance;
//                    }else if (lngMenuId == 7){
//                        ICONS[i] = R.drawable.icon_markentry;
//                    }else if (lngMenuId == 8){
//                        ICONS[i] = R.drawable.icon_biometriclog;
//                    }else if (lngMenuId == 9){
//                        ICONS[i] = R.drawable.icon_payslip;
//                    }else if (lngMenuId == 10){
//                        ICONS[i] = R.drawable.icon_notification;
//                    }else if (lngMenuId == 11){
//                        ICONS[i] = R.drawable.icon_notificationtoemployee;
//                    }else if (lngMenuId == 12){
//                        ICONS[i] = R.drawable.icon_notificationtoall;
//                    }else if (lngMenuId == 13) {
//                        ICONS[i] = R.drawable.icon_notificationview;
//                    }else if (lngMenuId == 14){
//                        ICONS[i] = R.drawable.icon_canteen; //canteen
//                    }else if (lngMenuId == 15){
//                        ICONS[i] = R.drawable.icon_venubooking; //Venue Booking
//                    }else if (lngMenuId == 16){
//                        ICONS[i] = R.drawable.icon_enotification; //ENotice View
//                    }else if (lngMenuId == 17){
//                        ICONS[i] = R.drawable.icon_managementdashboard; //Management Dashboard
//                    }else if (lngMenuId == 18){
//                        ICONS[i] = R.drawable.icon_facultydashboard; //Faculty Dashboard
//                    }else if (lngMenuId == 19){
//                        ICONS[i] = R.drawable.icon_lms; //LMS Index
//                    }else if (lngMenuId == 20){
//                        ICONS[i] = R.drawable.icon_permissionentry; //Permission Entry
//                    }else if (lngMenuId == 23){
//                        ICONS[i] = R.drawable.icon_attendancepunch; //Punch Entry
//                    }else if (lngMenuId == 24){
//                        ICONS[i] = R.drawable.icon_punchreport; //Punch Entry
//                    }else if (lngMenuId == 50){
//                        ICONS[i] = R.drawable.icon_logout;
//                    }
//                    i++;
//                }while (cursor.moveToNext());
//            }
//            cursor.close();
//
//            if (MENULIST.length == 0) {
//                Toast.makeText(HomeScreenCategory.this, getResources().getString(R.string.noFeature), Toast.LENGTH_LONG).show();
//            } else {
//                mRecyclerView = (RecyclerView) findViewById(R.id.rvMenuContentCategory); // Assigning the RecyclerView Object to the xml View
//                mRecyclerView.setHasFixedSize(true);
//                // Letting the system know that the list objects are of fixed size
//                HomeScreenAdapter TVA = new HomeScreenAdapter(MENULIST, ICONS);
//                mRecyclerView.setAdapter(TVA);
//                mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
//                mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
//            }
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//}
