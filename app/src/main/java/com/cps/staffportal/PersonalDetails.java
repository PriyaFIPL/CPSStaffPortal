package com.cps.staffportal;

import static webservice.WebService.strParameters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import webservice.SqlliteController;
import webservice.WebService;

/**
 * Created by Firstline Infotech on 30-04-2019.
 */

public class PersonalDetails extends AppCompatActivity {
    private static String ResultString = "";
    private long lngEmployeeId = 0;
    ImageView imageView;
    TextView tvPageTitle, tvLastUpdated, txtLastUpdatedProfile, tvAddress;
    TextView tvEmployee, tvDivision, tvDesignation;
    TextInputEditText tvDob, tvQualification, tvDoj, tvMobile, tvEmail;
    SQLiteDatabase db;
    SqlliteController controllerdb = new SqlliteController(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldetails);
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.colorblue));
        tvPageTitle = findViewById(R.id.pageTitle);
        tvPageTitle.setText("Profile");
        tvLastUpdated = findViewById(R.id.txtLastUpdated);
        txtLastUpdatedProfile = findViewById(R.id.txtLastUpdatedProfile);
        Button btnBack = findViewById(R.id.button_back);
        Button btnRefresh = findViewById(R.id.button_refresh);
//        Button butEdit = findViewById(R.id.btnEdit);
        imageView = findViewById(R.id.imgStudentPhoto);
//        ImageButton editPhoto= findViewById(R.id.editPhoto);
//        editPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

//        butEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                //check Internet
//                if (!CheckNetwork.isInternetAvailable(PersonalDetails.this)) {
//                    Toast.makeText(PersonalDetails.this,getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
//                    return;
//                }else {
////                    Intent intent = new Intent(PersonalDetails.this, ProfileEdit.class);
////                    startActivityForResult(intent, 2);
//                    finish();
//                }
////            startActivity(intent);
//            }
//        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckNetwork.isInternetAvailable(PersonalDetails.this)) {
                    Toast.makeText(PersonalDetails.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
                    WebService.strParameters = strParameters;
                    WebService.METHOD_NAME = "employeePersonalDetailsJson";
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });
        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);
//        strSchool = loginsession.getString("school", "");
        //displayProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
        WebService.strParameters = strParameters;
        WebService.METHOD_NAME = "employeePersonalDetailsJson";
        AsyncCallWS task = new AsyncCallWS();
        task.execute();*/
        displayProfile();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomePageGridViewLayout.class);
        startActivity(intent);
        this.finish();
    }

    public void setCirularImage(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(10);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    @SuppressLint("Range")
    private void displayProfile(){
        db = controllerdb.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT strftime('%d-%m-%Y %H:%M:%S', lastupdatedate) as lastupdated,* FROM profiledetails pf WHERE employeeid=" + lngEmployeeId, null);
            if (cursor.moveToFirst()) {
                do {
                    txtLastUpdatedProfile.setText("Last Updated: " + cursor.getString(cursor.getColumnIndex("lastupdated")));
                    tvLastUpdated.setText("Last Updated: " + cursor.getString(cursor.getColumnIndex("lastupdated")));
                    tvEmployee = findViewById(R.id.txtEmployeeName);
                    tvEmployee.setText(cursor.getString(cursor.getColumnIndex("employeename")));

                    tvQualification = findViewById(R.id.txtQualification);
                    tvQualification.setText(cursor.getString(cursor.getColumnIndex("qualification")));

                    tvDivision = findViewById(R.id.txtDivision);
                    tvDivision.setText(cursor.getString(cursor.getColumnIndex("division")));

                    tvDesignation = findViewById(R.id.txtDesignation);
                    tvDesignation.setText(cursor.getString(cursor.getColumnIndex("designation")));

                    tvDob = findViewById(R.id.txtDob);
                    tvDob.setText(cursor.getString(cursor.getColumnIndex("dob")));

                    tvDoj = findViewById(R.id.txtDoj);
                    tvDoj.setText(cursor.getString(cursor.getColumnIndex("doj")));

                    tvMobile = findViewById(R.id.txtMobile);
                    tvMobile.setText(cursor.getString(cursor.getColumnIndex("mobile")));

                    tvEmail = findViewById(R.id.txtEmmail);
                    tvEmail.setText(cursor.getString(cursor.getColumnIndex("email")));

                    tvAddress = findViewById(R.id.txtAddress);
                    tvAddress.setText(cursor.getString(cursor.getColumnIndex("address")));
                } while (cursor.moveToNext());
                try {
                    cursor = db.rawQuery("SELECT * FROM staffphoto pf WHERE staffid=" + lngEmployeeId, null);
                    if (cursor.moveToFirst()) {
                        do {
                            byte[] byteArrPhoto = cursor.getBlob(1); //Base64.decode(base64,Base64.DEFAULT);
                            setCirularImage(byteArrPhoto);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                }
            } else {
                if (!CheckNetwork.isInternetAvailable(PersonalDetails.this)) {
                    Toast.makeText(PersonalDetails.this, getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
                    strParameters = strParameters;
                    WebService.METHOD_NAME = "employeePersonalDetailsJson";
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
            strParameters = strParameters;
            WebService.METHOD_NAME = "employeePersonalDetailsJson";
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }
    }

//    public static byte[] getBytes(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        return stream.toByteArray();
//    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(PersonalDetails.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (Debug.isDebuggerConnected())
                Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
            SharedPreferences.Editor ed = loginsession.edit();
            try {
                SqlliteController sc = new SqlliteController(PersonalDetails.this);
                JSONObject object = new JSONObject(ResultString);
                if (!object.isNull("Name")) {
                    sc.insertProfileDetails(lngEmployeeId, object.getString("Name"),
                            object.getString("Division"), object.getString("Designation"),
                            object.getString("DOB"), object.getString("DOJ"),
                            object.getString("Mobile"), object.getString("Email"),
                            object.getString("Qualification"), object.getString("Address"));
                    if (object.has("staffphoto")) {
                        String base64 = object.getString("staffphoto");
                        byte[] byteArrPhoto = Base64.decode(base64, Base64.DEFAULT);
                        sc.insertStaffPhoto(lngEmployeeId, byteArrPhoto);
                        setCirularImage(byteArrPhoto);
                        loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
                        SharedPreferences.Editor editP = loginsession.edit();
                        editP.putString("profileImage", base64);
                        editP.commit();
                    }
                    displayProfile();
                } else {
                    Toast.makeText(PersonalDetails.this, "No Personal Details Found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 3);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 4);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                //Update List
                strParameters = new String[]{"Long", "employeeid", String.valueOf(lngEmployeeId)};
                WebService.strParameters = strParameters;
                WebService.METHOD_NAME = "employeePersonalDetailsJson";
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            } else if (requestCode == 3) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    imageView.setImageBitmap(bitmap);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 4) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image ", picturePath + "");
                imageView.setImageBitmap(thumbnail);
            }
        }
    }
}