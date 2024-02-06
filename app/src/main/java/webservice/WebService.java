package webservice;

import android.os.Build;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

import static org.apache.http.conn.ssl.SSLSocketFactory.SSL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by fipl on 09-12-2016.
 */

public class WebService {

    //Namespace of the Webservice - can be found in WSDL
    public static String NAMESPACE = "http://ws.fipl.com/";
    //Webservice URL - WSDL File location
    //For SRMIST live server database
   // public static String URL = "https://firstlineinfotech.com/srmistEmployeeAndroid/EmployeeAndroid?wsdl";
//    private static final String URL = "https://uatserver.srmist.edu.in/evarsitywebservice/EmployeeAndroid?wsdl";//Make sure you changed IP address
    private static final String URL = "https://erpchennaipublicschool.com/EmployeeAndroidCPS/EmployeeAndroid?wsdl";


    //For HARIYANA live server database
    //public static String URL = "https://firstlineinfotech.com/srmistEmployeeAndroid/EmployeeAndroid?wsdl";

    //SOAP Action URI again Namespace + Web method name
    public static String SOAP_ACTION = "http://ws.fipl.com/";
    public static String ResultString = "";

    private static byte[] image;
    public static String METHOD_NAME = "";
    public static String strParameters[];

    public static String invokeWS(){
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String strBody="";
        ResultString ="";
        // Log.e("RADHA 1 Method: ", METHOD_NAME);
        if(strParameters != null) {
            for (int i = 0; i <= strParameters.length - 1; i = i + 3) {
                strBody += "<" + strParameters[i + 1] + ">" + strParameters[i + 2] + "</" + strParameters[i + 1] + ">";
                //  Log.e("RADHA 2 param: ", strBody);
            }
        }
        EncryptDecrypt ED = new EncryptDecrypt();
        String strEncryptedData =  ED.getEncryptedData(strBody);
        PropertyInfo piInfo = new PropertyInfo();
        piInfo.setType(String.class);
        piInfo.setName("EncryptedData");
        piInfo.setValue(strEncryptedData);
        request.addProperty(piInfo);

        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = false;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,100000); //,100000
            System.setProperty("http.keepAlive", "false");
            androidHttpTransport.call("\""+SOAP_ACTION+METHOD_NAME+"\"", envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                //this is the actual part that will call the webservice
                String str= ((SoapFault) envelope.bodyIn).faultstring;
            }else {
                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject) envelope.bodyIn;
                ResultString = result.getProperty(0).toString();
                ResultString = ED.getDecryptedData(ResultString);
            }
            // Get the SoapResult from the envelope body.
        } catch (Exception e) {
            //      ResultString = e.getMessage();
            e.printStackTrace();
        }
        //Log.e("RADHA 3 ResultString: ", ResultString);

        return ResultString;
    }


//    public static String invokeWS() {
//        Log.e("Method Name : " , METHOD_NAME);
//        Log.e("Method Name : " , METHOD_NAME);
        // No change in Server side. we will use same https URL from Mobile to server request.

// Create a trust manager that does not validate certificate chains
//        TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//
//                    public void checkClientTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//
//                    public void checkServerTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//                }
//        };
//        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//
//            @Override
//            public boolean verify(String s, SSLSession sslSession) {
//                return true;
//            }
//        });
// Install the all-trusting trust manager
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            String strBody = "";
//            if (strParameters != null) {
//
//                for (int i = 0; i <= strParameters.length - 1; i = i + 3) {
//
//                    strBody += "<" + strParameters[i + 1] + ">" + strParameters[i + 2] + "</" + strParameters[i + 1] + ">";
//               Log.e("Method Params: ","<" + strParameters[i + 1] + ">" + strParameters[i + 2] + "</" + strParameters[i + 1] + ">");
//                }
//            }
//            EncryptDecrypt ED = new EncryptDecrypt();
//            String strEncryptedData = ED.getEncryptedData(strBody);
//            PropertyInfo piInfo = new PropertyInfo();
//            piInfo.setType(String.class);
//            piInfo.setName("EncryptedData");
//            piInfo.setValue(strEncryptedData);
//            request.addProperty(piInfo);
//
//
//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
//                    SoapEnvelope.VER10);
//            soapEnvelope.dotNet = false;
//            soapEnvelope.setOutputSoapObject(request);
//            HttpTransportSE transport = new HttpTransportSE(URL, 100000);
//            transport.debug = true;
//            System.setProperty("http.keepAlive", "false");
//
//            transport.call("\"" + SOAP_ACTION + METHOD_NAME + "\"", soapEnvelope);
//            if (soapEnvelope.bodyIn instanceof SoapFault) {
//                //this is the actual part that will call the webservice
//                String str = ((SoapFault) soapEnvelope.bodyIn).faultstring;
//            } else {
//                // Get the SoapResult from the envelope body.
//                SoapObject result = (SoapObject) soapEnvelope.bodyIn;
//                ResultString = result.getProperty(0).toString();
//                ResultString = ED.getDecryptedData(ResultString);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("RESULT METHOD:",METHOD_NAME);
//        Log.e("RESULT STRING:",ResultString);
//        return ResultString;
//    }

    public static String invokeWSTest() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String strBody = "";
        if (strParameters != null) {

            for (int i = 0; i <= strParameters.length - 1; i = i + 3) {

                strBody += "<" + strParameters[i + 1] + ">" + strParameters[i + 2] + "</" + strParameters[i + 1] + ">";
            }
        }
        Log.d("TEST : ", strBody);
        EncryptDecrypt ED = new EncryptDecrypt();
        String strEncryptedData = ED.getEncryptedData(strBody);
        PropertyInfo piInfo = new PropertyInfo();
        piInfo.setType(String.class);
        piInfo.setName("EncryptedData");
        piInfo.setValue(strEncryptedData);
        request.addProperty(piInfo);

        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        envelope.dotNet = false;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 100000); //,100000
            System.setProperty("http.keepAlive", "false");
            androidHttpTransport.call("\"" + SOAP_ACTION + METHOD_NAME + "\"", envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                //this is the actual part that will call the webservice
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                Log.i("TEST", str);
            } else {
                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject) envelope.bodyIn;
                ResultString = result.getProperty(0).toString();
                ResultString = ED.getDecryptedData(ResultString);
            }
            // Get the SoapResult from the envelope body.
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return ResultString;
    }

//    public static byte[] invokeWSforImage(){
//        //Object result;
//        //Initialize soap request + add parameters
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//        //Use this to add parameters
//        for (int i=0; i<=strParameters.length-1; i=i+3){
//            PropertyInfo piInfo = new PropertyInfo();
//            if (strParameters[i]=="int"){
//                piInfo.setType(int.class);
//                piInfo.setName(strParameters[i + 1]);
//                piInfo.setValue(strParameters[i + 2]);
//            } else if (strParameters[i]=="Long"){
//                piInfo.setType(Long.class);
//                piInfo.setName(strParameters[i + 1]);
//                piInfo.setValue(Long.parseLong(strParameters[i + 2]));
//            }
//            request.addProperty(piInfo);
//        }
//        //Declare the version of the SOAP request
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.setOutputSoapObject(request);
//        envelope.dotNet = false;
//        try {
//            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,100000); //,100000
//            System.setProperty("http.keepAlive", "false");
//            //this is the actual part that will call the webservice
//            //androidHttpTransport.setXmlVersionTag("?xml version=\"1.0\" encoding=\"utf-8\"?>");
//            System.out.println("Method Name:"+METHOD_NAME);
//            androidHttpTransport.call("\""+SOAP_ACTION+METHOD_NAME+"\"", envelope);
//            if (envelope.bodyIn instanceof SoapFault) {
//                //this is the actual part that will call the webservice
//                String str= ((SoapFault) envelope.bodyIn).faultstring;
//                Log.i("", str);
//            }else {
//                // Get the SoapResult from the envelope body.
//                SoapObject result = (SoapObject) envelope.bodyIn;
//                //ResultString = result.getProperty(0).toString();
//                byte[] b = result.getProperty(0).getBytes();
////                byte[] b = result.getBytes();
//            }
//            // Get the SoapResult from the envelope body.
//        } catch (Exception e) {
//            Log.e(TAG, "Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return ResultString;
//    }

    public static ArrayList invokeWSArray() {
        //Object result;
        //Initialize soap request + add parameters
        ArrayList<String> arrlist = new ArrayList<String>();
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Use this to add parameters
        for (int i = 0; i <= strParameters.length - 1; i = i + 3) {
            PropertyInfo piInfo = new PropertyInfo();
            if (strParameters[i] == "String") {
                piInfo.setType(String.class);
                piInfo.setName(strParameters[i + 1]);
                piInfo.setValue(strParameters[i + 2]);
            } else {
                piInfo.setType(Long.class);
                piInfo.setName(strParameters[i + 1]);
                piInfo.setValue(Long.parseLong(strParameters[i + 2]));
            }
            request.addProperty(piInfo);
        }
        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = false;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 100000); //,100000
            System.out.println("Method Name:" + METHOD_NAME);

            androidHttpTransport.call("\"" + SOAP_ACTION + METHOD_NAME + "\"", envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                //this is the actual part that will call the webservice
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                Log.i("", str);
            } else {
                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject) envelope.bodyIn;
                ResultString = result.getProperty(0).toString();
                java.util.StringTokenizer st = new java.util.StringTokenizer(ResultString, ",");
                while (st.hasMoreTokens()) {
                    arrlist.add(st.nextToken());
                }
            }
            // Get the SoapResult from the envelope body.
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return arrlist;
    }

    public static ArrayList invokeWSArrayInner() {
        //Object result;
        //Initialize soap request + add parameters
        ArrayList<String> arrlist = new ArrayList<String>();
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Use this to add parameters
        for (int i = 0; i <= strParameters.length - 1; i = i + 3) {
            PropertyInfo piInfo = new PropertyInfo();
            if (strParameters[i] == "String") {
                piInfo.setType(String.class);
                piInfo.setName(strParameters[i + 1]);
                piInfo.setValue(strParameters[i + 2]);
            } else {
                piInfo.setType(Long.class);
                piInfo.setName(strParameters[i + 1]);
                piInfo.setValue(Long.parseLong(strParameters[i + 2]));
            }
            request.addProperty(piInfo);
        }
        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = false;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 100000); //,100000
            System.out.println("Method Name:" + METHOD_NAME);

            androidHttpTransport.call("\"" + SOAP_ACTION + METHOD_NAME + "\"", envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                //this is the actual part that will call the webservice
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                Log.i("", str);
            } else {
                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject) envelope.bodyIn;
                ResultString = result.getProperty(0).toString();
                java.util.StringTokenizer st = new java.util.StringTokenizer(ResultString, "#");
                while (st.hasMoreTokens()) {
                    arrlist.add(st.nextToken());
                }
            }
            // Get the SoapResult from the envelope body.
        } catch (Exception e) {
//            System.out.println(e);
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return arrlist;
    }
}

