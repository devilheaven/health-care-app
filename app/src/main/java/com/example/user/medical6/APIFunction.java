package com.example.user.medical6;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.user.medical6.dataBase.*;

public class APIFunction {
    // SharedPreferences SharedPreferences;
    String Json;
    URL url ;

    // data base 變數宣告
    public dataBase DH=null;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();

    int tempProtocolId, tempFormId;

    appFunction function = new appFunction();
    TextView tvInfo, tvLoginType;

    void memberAPIconnect(Context context, String urlAddress, TextView Info, TextView LogInType){
        //讀取資料
        DH = new dataBase(context);
        DH.close();
        db = DH.getReadableDatabase();

        tvInfo = Info;
        tvLoginType = LogInType;
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new numberCheck().execute();
    }

    void questionAPIconnect(Context context, String urlAddress, SharedPreferences SharedPreferences, int protocolId, int formId){
        //讀取資料
        DH = new dataBase(context);
        DH.close();
        db = DH.getReadableDatabase();

        tempProtocolId = protocolId;
        tempFormId = formId;

        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Json = SharedPreferences.getString("result","");
        new questionnaries().execute();
    }

    //api 傳值
    public class numberCheck extends AsyncTask<String,Void,String> {
        protected String responsetitle= "";
        protected String responsestring= "";

        // SQL lite query
        Cursor cur1 = db.rawQuery(" SELECT "+ subjectId+ "," + lastName+ ","+ idnum1 +"  FROM " + TABLE_c + " ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonString = new JSONObject();
            if (cur1.getCount()>0){
                cur1.moveToFirst();
                jsonString = function.memberJson(1000,cur1.getString(0),cur1.getString(1),cur1.getString(2));
//                jsonString = function.memberJson(1032,cur1.getString(0),cur1.getString(1),cur1.getString(2));
//                jsonString = function.memberJson(1032,"A001","劉","PSEUDO-LQKZRBQ2");
            }

            SSLsetting();
            try {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-type","application/json");


                System.out.println("Now json: "+jsonString);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString.toString());
                writer.flush();
                writer.close();
                InputStream is;
                try
                {
                    is = connection.getInputStream();
                }
                catch(IOException exception)
                {
                    is = connection.getErrorStream();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Log.v("Header",connection.getHeaderFields().toString());
                responsetitle = String.valueOf(connection.getResponseCode())+ " " + connection.getResponseMessage();
                responsestring = response.toString();

                in.close();
                os.close();
                is.close();

                Log.v("Status", responsetitle);
                Log.v("Content",responsestring);

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("IOException","ERROR");
            }

            return responsestring;
        }
        @Override
        protected void onPostExecute(String s) {
            String errorCode = "";
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(s);
                errorCode = jsonObject.getString("Error Code");
                message = jsonObject.getString("Message");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            if ("009".equals(errorCode)){
                if (cur1.getCount()>0){
                    cur1.moveToFirst();
                    tvInfo.setText(cur1.getString(0)+"\t"+cur1.getString(1)+"先生/小姐");
                    tvLoginType.setText("member");
                }
            }else{
                tvInfo.setText("登入失敗");
                tvLoginType.setText("guest");
                if (cur1.getCount()>0){
                    db.execSQL("delete from customer");
                }

            }

            Log.v("Back",s);
        }
    }

    private class questionnaries extends AsyncTask<String,Void,String> {
        JSONObject jsonString = new JSONObject();

        protected String responsetitle= "";
        protected String responsestring= "";

        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //新增一個Calendar,並且指定時間
        Calendar calendar = Calendar.getInstance();

        Date tdt=calendar.getTime();//取得加減過後的Date

        //依照設定格式取得字串
        String time=sdf.format(tdt);

        // SQL lite query
        Cursor cur1 = db.rawQuery(" SELECT " + subjectId + "  FROM  customer ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            if (cur1.getCount()>0){
                cur1.moveToFirst();
                try {
                    JSONObject tempJsonArray =new JSONObject(Json);
                    jsonString.put("protocolId",tempProtocolId);
                    jsonString.put("subjectId",cur1.getString(0));
                    jsonString.put("formId",tempFormId);
                    jsonString.put("visit",time);
                    jsonString.put("datarecord",tempJsonArray)  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SSLsetting();
            try {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-type","application/json");
                System.out.println("Now json: "+jsonString);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString.toString());
                writer.flush();
                writer.close();
                InputStream is;
                try
                {
                    is = connection.getInputStream();
                }
                catch(IOException exception)
                {
                    is = connection.getErrorStream();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Log.v("Header",connection.getHeaderFields().toString());
                responsetitle = String.valueOf(connection.getResponseCode())+ " " + connection.getResponseMessage();
                responsestring = response.toString();

                in.close();
                os.close();
                is.close();

                Log.v("Status", responsetitle);
                Log.v("Content",responsestring);

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("IOException","ERROR");
            }

            return responsestring;
        }
        @Override
        protected void onPostExecute(String s) {
            String errorCode = "";
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(s);
                errorCode = jsonObject.getString("Error Code");
                message = jsonObject.getString("Message");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            if ("SUCCESS".equals(errorCode)){
                Log.e("bodyInformation/Back",s);
            }else if("005".equals(errorCode)){
                Log.e("bodyInformation/Back","JSON error./t"+s);
            }else if("007".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid protocol./t"+s);
            }else if("010".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid form./t"+s);
            }else if("011".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid date format./t"+s);
            }else if("012".equals(errorCode)){
                Log.e("bodyInformation/Back","Patient not found./t"+s);
            }else{
                Log.e("bodyInformation/Back",s);
            }
            Log.v("Back",s);
        }
    }

    //SSL 設定 (忽略所有的認證)
    private void SSLsetting() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}
