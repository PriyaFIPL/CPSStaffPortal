package webservice;

/**
 * Created by Firstline Infotech on 03-05-2019.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LOCSqlliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public LOCSqlliteController(Context applicationcontext){
        super(applicationcontext, "staffattendance.db", null, 1);
        Log.d(LOGCAT,"Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database){
//        String query;
//        query = "DROP TABLE IF EXISTS templateshifttimings";
//        database.execSQL(query);
//        query= "CREATE TABLE IF NOT EXISTS templateshifttimings (templateid INTEGER PRIMARY KEY, templateshifttime TEXT)";
//        database.execSQL(query);
//
//        query = "DROP TABLE IF EXISTS templateclasstimetable";
//        database.execSQL(query);
//        query= "CREATE TABLE IF NOT EXISTS templateclasstimetable (templateid INTEGER,programsectionid INTEGER,dayorderdesc VARCHAR(20),hourid VARCHAR(30),shifttime varchar(40),subjects TEXT)";
//        database.execSQL(query);
//        Log.d(LOGCAT,"templateshifttimings Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
//        String query;
//        query = "DROP TABLE IF EXISTS Students";
//        database.execSQL(query);
//        onCreate(database);
    }


    public void deleteLoginStaffDetails(){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM stafflogindetails";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
//            deleteQuery = "DELETE FROM userwisemenuaccessrights";
            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertLoginStaffDetails(long lngEmployeeId, String strStaffName, String strDepartment, String strDesignation,
                                        String strNetId, String strPwd){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "";
        try {
            query = "DROP TABLE stafflogindetails";
            database.execSQL(query);
        }catch (Exception e){}
        query= "CREATE TABLE IF NOT EXISTS stafflogindetails (" +
                "employeeid INTEGER," +
                "employeename VARCHAR(75)," +
                "department VARCHAR(30)," +
                "designation VARCHAR(100)," +
                "netid VARCHAR(100)," +
                "password  VARCHAR(100),"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeename",strStaffName);
        values.put("department",strDepartment);
        values.put("designation",strDesignation);
        values.put("netid",strNetId);
        values.put("password",strPwd);
        database.insert("stafflogindetails", null, values);
        database.close();
    }
}