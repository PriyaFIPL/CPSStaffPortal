package webservice;

/**
 * Created by Firstline Infotech on 03-05-2019.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.StringTokenizer;

public class SqlliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public SqlliteController(Context applicationcontext){
        super(applicationcontext, "androidsqlitestudentinformation.db", null, 1);
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
            deleteQuery = "DELETE FROM userwisemenuaccessrights";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertLoginStaffDetails(long lngEmployeeId,String strStaffName,String strDepartment,String strDesignation,String strMenuIds,String strNetId,String strPwd){
        Log.e("RADHA TEST","LOG ERROR 3");
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS stafflogindetails (employeeid INTEGER," +
                "employeename VARCHAR(75)," +
                "department VARCHAR(30)," +
                "designation VARCHAR(100)," +
                "netid VARCHAR(100)," +
                "password  VARCHAR(100),"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        //query = "DROP TABLE userwisemenuaccessrights";
        //database.execSQL(query);
//        query= "CREATE TABLE IF NOT EXISTS userwisemenuaccessrights (employeeid INTEGER," +
//                "menuid INTEGER," +
//                "menuname VARCHAR(50)," +
//                "menusortnumber INTEGER," +
//                "iconname VARCHAR(100)," +
//                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
//        database.execSQL(query);
        Log.e("RADHA TEST","LOG ERROR 4");
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeename",strStaffName);
        values.put("department",strDepartment);
        values.put("designation",strDesignation);
        values.put("netid",strNetId);
        values.put("password",strPwd);

        database.insert("stafflogindetails", null, values);
        Log.e("RADHA TEST","LOG ERROR 5");
        Log.e("RADHA TEST","LOG ERROR 5 "+ strMenuIds);
        values = new ContentValues();
//        StringTokenizer strMenuId = new StringTokenizer(strMenuIds,",");
//        long lngMenuId=0;
//        while (strMenuId.hasMoreTokens()){
//            StringTokenizer strMenuIdInner = new StringTokenizer(strMenuId.nextToken().trim(),"##");
//            values.put("employeeid",lngEmployeeId);
//            lngMenuId=Long.parseLong(strMenuIdInner.nextToken().trim());
//            values.put("menuid",lngMenuId);
//            values.put("menuname",strMenuIdInner.nextToken().trim());
//            if (lngMenuId == 1){
//                values.put("iconname", "R.drawable.icon_profile");
//            }else if (lngMenuId == 2){
//                values.put("iconname", "R.drawable.icon_timetable");
//            } else if (lngMenuId == 3){
//                values.put("iconname", "R.drawable.icon_leavestatus");
//            }else if (lngMenuId == 4){
//                values.put("iconname", "R.drawable.icon_leaveentry");
//            }else if (lngMenuId == 5){
//                values.put("iconname", "R.drawable.icon_leaveapproval");
//            }else if (lngMenuId == 6){
//                values.put("iconname", "R.drawable.icon_studentattendance");
//            }else if (lngMenuId == 7){
//                values.put("iconname", "R.drawable.icon_markentry");
//            }else if (lngMenuId == 8){
//                values.put("iconname", "R.drawable.icon_biometriclog");
//            }else if (lngMenuId == 9){
//                values.put("iconname", "R.drawable.icon_payslip");
//            }else if (lngMenuId == 10){
//                values.put("iconname", "R.drawable.icon_notification");
//            }else if (lngMenuId == 50){
//                values.put("iconname", "R.drawable.icon_logout");
//            }
//            values.put("menusortnumber",lngMenuId);
//            database.insert("userwisemenuaccessrights", null, values);
//            Log.e("RADHA TEST","LOG ERROR 6");
//        }
        database.close();
    }

    public void deletePaySlipPayPeriod(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM payslippayperiod WHERE employeeid="+lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);

            deleteQuery = "DROP TABLE payslippayperiod";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertPaySlipPayPeriod(long lngEmployeeId,int intOfficeId, int intPayStructureId,
                                       int intPayperiodId,String strPayPeriod,String strPayPeriodPDFFilename){
        SQLiteDatabase database = this.getWritableDatabase();
//        String query = "DROP TABLE payslippayperiod";
//        database.execSQL(query);
        String query= "CREATE TABLE IF NOT EXISTS payslippayperiod (employeeid INTEGER," +
                "officeid integer," +
                "paystructureid integer," +
                "payperiodid integer," +
                "payperioddesc VARCHAR(50)," +
                "payslipfilename VARCHAR(150),"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime'))," +
                "lastloggedin DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("officeid",intOfficeId);
        values.put("paystructureid",intPayStructureId);
        values.put("payperiodid",intPayperiodId);
        values.put("payperioddesc",strPayPeriod);
        values.put("payslipfilename",strPayPeriodPDFFilename);
        database.insert("payslippayperiod", null, values);
        database.close();
    }

    public void insertProfileDetails(long lngEmployeeId,
                 String strEmployeeName,String strDivision,
                 String strDesignation,String strDob,String strDoj,
                 String strMobile,String strEmail,String strQualification,
                 String strAddress){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String dropQuery = "DROP TABLE profiledetails";
            Log.d("query", dropQuery);
            database.execSQL(dropQuery);
        }catch (Exception e){}
        String query= "CREATE TABLE IF NOT EXISTS profiledetails (employeeid INTEGER PRIMARY KEY, " +
                "employeename VARCHAR(100)," +
                "division VARCHAR(100)," +
                "designation VARCHAR(100)," +
                "dob VARCHAR(20)," +
                "doj VARCHAR(20)," +
                "mobile VARCHAR(10)," +
                "email VARCHAR(120)," +
                "address TEXT," +
                "qualification VARCHAR(100)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        Log.d("query", query);
        database.execSQL(query);
        try {
            String deleteQuery = "DELETE FROM profiledetails where employeeid=" + lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch (Exception e){}
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeename",strEmployeeName);
        values.put("division",strDivision);
        values.put("designation",strDesignation);
        values.put("qualification",strQualification);
        values.put("dob",strDob);
        values.put("doj",strDoj);
        values.put("mobile",strMobile);
        values.put("email",strEmail);
        values.put("address",strAddress);
        database.insert("profiledetails", null, values);
        database.close();
    }

    public void deleteLeaveStatus(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DROP TABLE leavestatus";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void cancelLeaveApplication(long lngLeaveApplnId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE * FROM leavestatus WHERE leaveapplicationid = " + lngLeaveApplnId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }
    public void insertLeaveStatus(long lngEmployeeId,long lngLeaveApplnId,String strApplndate,String strLeaveType,
                                  String strFromDate,String strToDate,String strSession,
                                  String strReason,String strNoofdays,String strLeaveStatus){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS leavestatus (employeeid INTEGER, " +
                "leaveapplicationid BIGINT," +
                "applicationdate VARCHAR(30)," +
                "leavetype VARCHAR(30),"+
                "fromdate VARCHAR(30),"+
                "todate VARCHAR(30)," +
                "session VARCHAR(15)," +
                "reason VARCHAR(200),"+
                "noofdays REAL,"+
                "leavestatus VARCHAR(15),"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("leaveapplicationid",lngLeaveApplnId);
        values.put("applicationdate",strApplndate);
        values.put("leavetype",strLeaveType);
        values.put("fromdate",strFromDate);
        values.put("todate",strToDate);
        values.put("session",strSession);
        values.put("reason",strReason);
        values.put("noofdays",strNoofdays);
        values.put("leavestatus",strLeaveStatus);
        Log.d("insert query",String.valueOf(values));
        database.insert("leavestatus", null, values);
        database.close();
    }

    public void insertStaffTimeTable(long lngEmployeeId,int intDayorderTemplateid,int intDayorderid,
                                        int intHourid,String strDayOrderDescc, String strFromTime, String strToTime,String strSubjectCode,String strSubjectDesc){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS stafftimetable (employeeid INTEGER," +
                "dayordertemplateid INTEGER," +
                "dayorderid INTEGER," +
                "hourid INTEGER," +
                "dayorderdesc VARCHAR(50)," +
                "fromtime DATETIME DEFAULT (datetime('now','localtime'))," +
                "totime DATETIME DEFAULT (datetime('now','localtime'))," +
                "subjectcode VARCHAR(15)," +
                "subjectdesc VARCHAR(1000)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("dayordertemplateid",intDayorderTemplateid);
        values.put("dayorderid",intDayorderid);
        values.put("hourid",intHourid);
        values.put("dayorderdesc",strDayOrderDescc);
        values.put("fromtime",strFromTime);
        values.put("totime",strToTime);
        values.put("subjectcode",strSubjectCode);
        values.put("subjectdesc",strSubjectDesc);
        database.insert("stafftimetable", null, values);
        database.close();
    }

    public void deleteStaffTimeTable(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM stafftimetable where employeeid = " + lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

//    public void insertStudentPhoto(long lngEmployeeId,byte[] photo){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS employeephoto (employeeid INTEGER PRIMARY KEY, " +
//                "employeephoto blob,"+
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        try {
//            String deleteQuery = "DELETE FROM employeephoto where employeeid=" + lngEmployeeId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch (Exception e){}
//        ContentValues values = new ContentValues();
//        values.put("employeeid",lngEmployeeId);
//        values.put("employeephoto",photo);
//        database.insert("employeephoto", null, values);
//        database.close();
//    }


    public void deleteStaffSubjects(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM staffsubjetcs where employeeid = " + lngEmployeeId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertStaffSubjects(long lngEmployeeId,String strProgramSection,String strSubjectCode,
                                    String strSubjectDesc,long lngProgSecId,long lngSubjectId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS staffsubjetcs (employeeid INTEGER, " +
                "programsection TEXT," +
                "subjectcode VARCHAR(30)," +
                "subjectdesc TEXT," +
                "programsectionid INTEGER," +
                "subjectid INTEGER," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("programsection",strProgramSection);
        values.put("subjectcode",strSubjectCode);
        values.put("subjectdesc",strSubjectDesc);
        values.put("programsectionid",lngProgSecId);
        values.put("subjectid",lngSubjectId);
        Log.d("insert query",String.valueOf(values));
        database.insert("staffsubjetcs", null, values);
        database.close();
    }

    public void insertStaffList(int intEmpCategoryId,String strEmpCategoryDesc,String strEmployeeCode,String strEmployeeName,long lngEmployeeId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS stafflist (employeecategoryid INTEGER, " +
                "employeecategory VARCHAR(50)," +
                "employeecode VARCHAR(10),"+
                "employeename VARCHAR(75),"+
                "employeeid INTEGER,"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("employeecategoryid",intEmpCategoryId);
        values.put("employeecategory",strEmpCategoryDesc);
        values.put("employeecode",strEmployeeCode);
        values.put("employeename",strEmployeeName);
       // Log.d("insert query",String.valueOf(values));
        database.insert("stafflist", null, values);
        database.close();
    }

    public void deleteStaffList(){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM stafflist";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
            deleteQuery = "DROP TABLE stafflist";
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void deleteNotificationDetails(long lngEmployeeId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            String deleteQuery = "DELETE FROM notificationdetails where employeeid = " + lngEmployeeId;
            Log.d("query", deleteQuery);
            String query= "DROP TABLE notificationdetails ";
            database.execSQL(query);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertNotificationDetails(long lngEmployeeId,String strNotificationDate,String strNotificationTime,String strNotificationTitle,
                                          String strNotificationMessage){
        SQLiteDatabase database = this.getWritableDatabase();
        //String query= "DROP TABLE notificationdetails ";
        //database.execSQL(query);
        String query= "CREATE TABLE IF NOT EXISTS notificationdetails (employeeid INTEGER, " +
                "notificationtitle VARCHAR(50)," +
                "notificationmessage TEXT," +
                "notificationdate DATE," +
                "notificationtime TIME," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        ContentValues values = new ContentValues();
        values.put("employeeid",lngEmployeeId);
        values.put("notificationtitle",strNotificationTitle);
        values.put("notificationmessage",strNotificationMessage);
        values.put("notificationdate",strNotificationDate);
        values.put("notificationtime",strNotificationTime);
        Log.d("insert query",String.valueOf(values));
        database.insert("notificationdetails", null, values);
        database.close();
    }

    public void insertStaffPhoto(long lngStaffId,byte[] photo){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS staffphoto (staffid INTEGER PRIMARY KEY, staffphoto blob,"+
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        try {
            String deleteQuery = "DELETE FROM staffphoto where staffid = " + lngStaffId;
            Log.d("query", deleteQuery);
            database.execSQL(deleteQuery);
        }catch (Exception e){}
        ContentValues values = new ContentValues();
        values.put("staffid",lngStaffId);
        values.put("staffphoto",photo);
        database.insert("staffphoto", null, values);
        database.close();
    }


//    public void deleteFinanceDetails(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM financedetails where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertFinanceDetails(long lngStudentId,String strTerm,String strDueName,String strDueamount,String strDueDate,String strReceiptDate,
//                                     String strAmtCollected,String strMode,String strReceiptNum){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS financedetails (studentid INTEGER, " +
//                "term VARCHAR(20)," +
//                "duename VARCHAR(75)," +
//                "dueamount REAL," +
//                "duedate VARCHAR(30)," +
//                "receiptdate VARCHAR(30)," +
//                "amountcollected REAL," +
//                "mode VARCHAR(50)," +
//                "receiptnum VARCHAR(30)," +
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("term",strTerm);
//        values.put("duename",strDueName);
//        values.put("dueamount",strDueamount);
//        values.put("duedate",strDueDate);
//        values.put("receiptdate",strReceiptDate);
//        values.put("amountcollected",strAmtCollected);
//        values.put("mode",strMode);
//        values.put("receiptnum",strReceiptNum);
//
//        Log.d("insert query",String.valueOf(values));
//        database.insert("financedetails", null, values);
//        database.close();
//    }
//
//    public void deleteHostelDetails(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM hosteldetails where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertHostelDetails(long lngStudentId,String strAcaYear,String strAllotedDate,String strHostelName,String strRoomName,String strRoomType){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS hosteldetails (studentid INTEGER, " +
//                "academicyear VARCHAR(20)," +
//                "alloteddate VARCHAR(30)," +
//                "hostelname VARCHAR(150)," +
//                "roomname VARCHAR(100)," +
//                "roomtype VARCHAR(75)," +
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("academicyear",strAcaYear);
//        values.put("alloteddate",strAllotedDate);
//        values.put("hostelname",strHostelName);
//        values.put("roomname",strRoomName);
//        values.put("roomtype",strRoomType);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("hosteldetails", null, values);
//        database.close();
//    }
//
//    public void deleteStudentSubjects(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM studentsubjects where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertStudentSubjects(long lngStudentId,String strSemester,String strSubCode,String strSubjectDesc,String strCredit){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS studentsubjects (studentid INTEGER, " +
//                "semester VARCHAR(20)," +
//                "subjectcode VARCHAR(10)," +
//                "subjectdesc VARCHAR(75)," +
//                "credit VARCHAR(20)," +
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("semester",strSemester);
//        values.put("subjectcode",strSubCode);
//        values.put("subjectdesc",strSubjectDesc);
//        values.put("credit",strCredit);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("studentsubjects", null, values);
//        database.close();
//    }
//
//    public void deleteSubjectAttendance(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM subjectattendance where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertSubjectAttendance(long lngStudentId,String strSubjectCode,String strSubjectDesc,String strPresent,String strAbsent,String strTotal,String strPresentPercentage){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS subjectattendance (studentid INTEGER, " +
//                "subjectcode VARCHAR(10)," +
//                "subjectdesc VARCHAR(75)," +
//                "presenthrs REAL,"+
//                "absenthrs REAL,"+
//                "totalhrs REAL,"+
//                "presentpercentage REAL,"+
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("subjectcode",strSubjectCode);
//        values.put("subjectdesc",strSubjectDesc);
//        values.put("presenthrs",strPresent);
//        values.put("absenthrs",strAbsent);
//        values.put("totalhrs",strTotal);
//        values.put("presentpercentage",strPresentPercentage);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("subjectattendance", null, values);
//        database.close();
//    }
//
//    public void deleteCummulativeAttendance(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM cummulativeattendance where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertCummulativeAttendance(long lngStudentId,String strAttendancemonthyear,String strPresent,String strAbsent,String strODPresent,String strODAbsent,
//                                            String strMedical){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS cummulativeattendance (studentid INTEGER, " +
//                "attendancemonthyear VARCHAR(10)," +
//                "presenthrs REAL,"+
//                "absenthrs REAL,"+
//                "odpresent REAL,"+
//                "odabsent REAL,"+
//                "medical REAL,"+
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("attendancemonthyear",strAttendancemonthyear);
//        values.put("presenthrs",strPresent);
//        values.put("absenthrs",strAbsent);
//        values.put("odpresent",strODPresent);
//        values.put("odabsent",strODAbsent);
//        values.put("medical",strMedical);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("cummulativeattendance", null, values);
//        database.close();
//    }
//
//    public void deleteHourwiseAttendance(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM hourwiseattendance  where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
////            deleteQuery = "DROP TABLE hourwiseattendance ";
////            Log.d("query", deleteQuery);
////            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertHourwiseAttendance(long lngStudentId,String strAttendancedate,String strh1,String strh2,String strh3,String strh4,
//                                            String strh5, String strh6, String strh7,String strh8){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS hourwiseattendance (studentid INTEGER, " +
//                "attendancedate VARCHAR(30)," +
//                "h1 INTEGER,"+
//                "h2 INTEGER,"+
//                "h3 INTEGER,"+
//                "h4 INTEGER,"+
//                "h5 INTEGER,"+
//                "h6 INTEGER,"+
//                "h7 INTEGER,"+
//                "h8 INTEGER,"+
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("attendancedate",strAttendancedate);
//        values.put("h1",strh1);
//        values.put("h2",strh2);
//        values.put("h3",strh3);
//        values.put("h4",strh4);
//        values.put("h5",strh5);
//        values.put("h6",strh6);
//        values.put("h7",strh7);
//        values.put("h8",strh8);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("hourwiseattendance", null, values);
//        database.close();
//    }
//
//    public void deleteInternalMarkDetails(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM internalmarkdetails where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertInternalMarkDetails(long lngStudentId,String strSubCode,String strSubjectDesc,String strMarkObtained,String strMaxMarks){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS internalmarkdetails (studentid INTEGER, " +
//                "subjectcode VARCHAR(10)," +
//                "subjectdesc VARCHAR(75)," +
//                "markobtained REAL," +
//                "maxmarks REAL," +
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("subjectcode",strSubCode);
//        values.put("subjectdesc",strSubjectDesc);
//        values.put("markobtained",strMarkObtained);
//        values.put("maxmarks",strMaxMarks);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("internalmarkdetails", null, values);
//        database.close();
//    }
//
//    public void deleteExamDetails(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try {
//            String deleteQuery = "DELETE FROM examdetails where studentid = " + lngStudentId;
//            Log.d("query", deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertExamDetails(long lngStudentId,String strSemester,String strMonthYear,
//                          String strSubCode,String strSubjectDesc,String strMarkObtained,String strInternal,
//                                  String strExternal,String strCredit,String strGrade,String strResult,String strattempts){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS examdetails (studentid INTEGER, " +
//                "semester VARCHAR(5)," +
//                "monthyear VARCHAR(20)," +
//                "subjectcode VARCHAR(10)," +
//                "subjectdesc VARCHAR(75)," +
//                "markobtained REAL," +
//                "internal REAL," +
//                "external REAL," +
//                "credit INTEGER," +
//                "grade VARCHAR(3)," +
//                "result VARCHAR(15)," +
//                "attempts INTEGER,"+
//                "lastupdatedate DATETIME DEFAULT CURRENT_TIMESTAMP)";
//        database.execSQL(query);
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("semester",strSemester);
//        values.put("monthyear",strMonthYear);
//        values.put("subjectcode",strSubCode);
//        values.put("subjectdesc",strSubjectDesc);
//        values.put("markobtained",strMarkObtained);
//        values.put("internal",strInternal);
//        values.put("external",strExternal);
//        values.put("credit",strCredit);
//        values.put("grade",strGrade);
//        values.put("result",strResult);
//        values.put("attempts",strattempts);
//        Log.d("insert query",String.valueOf(values));
//        database.insert("examdetails", null, values);
//        database.close();
//    }


    public void deleteMemberDetails(long lngStudentId){
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        try{
            String deleteQuery = "DELETE FROM memberdetails where studentid = " + lngStudentId;
            Log.d("query",deleteQuery);
            database.execSQL(deleteQuery);
        }catch(Exception e){

        }
    }

    public void insertMemberDetails(long lngStudentId,String strMemberCode,String strMemberType,String strMemberName,String strPolicyName,String strStatus){
        SQLiteDatabase database = this.getWritableDatabase();
        String query= "CREATE TABLE IF NOT EXISTS memberdetails (studentid INTEGER, " +
                "membercode VARCHAR(15)," +
                "membertype VARCHAR(100)," +
                "membername VARCHAR(125)," +
                "policyname VARCHAR(100)," +
                "status VARCHAR(10)," +
                "lastupdatedate DATETIME DEFAULT (datetime('now','localtime')))";
        database.execSQL(query);
        try {
            ContentValues values = new ContentValues();
            values.put("studentid", lngStudentId);
            values.put("membercode", strMemberCode);
            values.put("membertype", strMemberType);
            values.put("membername", strMemberName);
            values.put("policyname", strPolicyName);
            values.put("status", strStatus);
            Log.d("insert query", String.valueOf(values));
            database.insert("memberdetails", null, values);
            database.close();
        }catch (Exception e){
            Log.e("Problem", e + " ");
        }
    }
//
//    public void deleteSubjectContent(long lngStudentId){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try{
//            String deleteQuery = "DELETE FROM subjectcontent where studentid = " + lngStudentId;
//            Log.d("query",deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void deleteBroadcast(long lngStudentId) {
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try{
//            String deleteQuery = "DELETE FROM broadcast where studentid = " + lngStudentId;
//            Log.d("query",deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void deleteFeePaid(long lngStudentId) {
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try{
//            String deleteQuery = "DELETE FROM studentfeepaid where studentid = " + lngStudentId;
//            Log.d("query",deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void insertTemplateClassTimeTable(long lngTemplateId,long lngProgSecId,String strDayOrderDesc,String strHourId,String strShiftTime,String strSubjects){
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("templateid",lngTemplateId);
//        values.put("programsectionid",lngProgSecId);
//        values.put("dayorderdesc",strDayOrderDesc);
//        values.put("hourid",strHourId);
//        values.put("shifttime",strShiftTime);
//        values.put("subjects",strSubjects);
//        database.insert("templateclasstimetable", null, values);
//        database.close();
//    }
//
//    public void insertStudentFeePaid(long lngStudentId,String strDueDetails,String strReceiptDetails){
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query= "CREATE TABLE IF NOT EXISTS studentfeepaid (studentid INTEGER,duedetails TEXT,receiptdetails TEXT)";
//        database.execSQL(query);
//
//        ContentValues values = new ContentValues();
//        values.put("studentid",lngStudentId);
//        values.put("duedetails",strDueDetails);
//        values.put("receiptdetails",strReceiptDetails);
//        database.insert("studentfeepaid", null, values);
//        database.close();
//    }

//    public void insertStudent(HashMap<String, String> queryValues) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("StudentName", queryValues.get("StudentName"));
//        database.insert("Students", null, values);
//        database.close();
//    }
//
//    public int updateStudent(HashMap<String, String> queryValues) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("StudentName", queryValues.get("StudentName"));
//        return database.update("Students", values, "StudentId" + " = ?", new String[] { queryValues.get("StudentId") });
//        //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
//        //Log.d(LOGCAT,updateQuery);
//        //database.rawQuery(updateQuery, null);
//        //return database.update("words", values, "txtWord  = ?", new String[] { word });
//    }
//
//    public void deleteShiftTime(long id){
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try{
//            String deleteQuery = "DELETE FROM templateshifttimings where templateid=" + id;
//            Log.d("query",deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }
//
//    public void deleteClassTimeTable(long templateid,long secid) {
//        Log.d(LOGCAT,"delete");
//        SQLiteDatabase database = this.getWritableDatabase();
//        try{
//            String deleteQuery = "DELETE FROM templateclasstimetable where templateid="+ templateid + " AND programsectionid=" + secid;
//            Log.d("query",deleteQuery);
//            database.execSQL(deleteQuery);
//        }catch(Exception e){
//
//        }
//    }

//    public ArrayList<HashMap<String, String>> getTimeTable(long lngTemplateId) {
//        ArrayList<HashMap<String, String>> shifttimingList;
//        shifttimingList = new ArrayList<HashMap<String, String>>();
//        String selectQuery = "SELECT dayorderdesc,subjects FROM templateclasstimetable WHWERE templateid="+lngTemplateId;
//        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()){
//            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("DayOrder", cursor.getString(0));
//                map.put("ShiftTime", cursor.getString(1));
//                shifttimingList.add(map);
//            } while (cursor.moveToNext());
//        }
//        return shifttimingList;
//    }

//    public HashMap<String, String> getStudentInfo(String id) {
//        HashMap<String, String> wordList = new HashMap<String, String>();
//        SQLiteDatabase database = this.getReadableDatabase();
//        String selectQuery = "SELECT * FROM Students where StudentId='"+id+"'";
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                //HashMap<String, String> map = new HashMap<String, String>();
//                wordList.put("StudentName", cursor.getString(1));
//                //wordList.add(map);
//            } while (cursor.moveToNext());
//        }
//        return wordList;
//    }
}



