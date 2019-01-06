package com.example.user.medical6;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//呼叫dataBase類別定義的常數
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.user.medical6.dataBase.*;

public class ManualActivity extends AppCompatActivity {
    private EditText editTextWeight,editTextHr,editTextDbp,editTextSbp,editTextHeight,dataEdit;

    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result = "bodyInformation";

    public dataBase DH=null;
    SQLiteDatabase db;
    Cursor cur;

    private Spinner spinnerDoEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_manual);

        try {
            searchtime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DH=new dataBase(this);
        DH.close();

        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextHr = (EditText) findViewById(R.id.editTextHr);
        editTextDbp = (EditText) findViewById(R.id.editTextDbp);
        editTextSbp = (EditText) findViewById(R.id.editTextSdp);
        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        spinnerDoEat= findViewById(R.id.DoEat);

        //SQLiteDatabase db =DH.getReadableDatabase();
        searchheight();
    }

    public void add(){

        SQLiteDatabase db=DH.getWritableDatabase();
        ContentValues values = new ContentValues();

        JSONObject postData=new JSONObject();

        if(editTextWeight.getText().toString().matches("") || editTextHeight.getText().toString().matches("")|| editTextHr.getText().toString().matches("")|| editTextDbp.getText().toString().matches("")|| editTextSbp.getText().toString().matches("")) {
            Toast toast = Toast.makeText(ManualActivity.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            ContentValues values2 = new ContentValues();
            cur=db.rawQuery(" SELECT " + id + "  FROM  customer " ,null);

            values.put(weight,editTextWeight.getText().toString() );
            values.put(sbp, editTextSbp.getText().toString());
            values.put(dbp, editTextDbp.getText().toString());
            values.put(hr, editTextHr.getText().toString());
            values.put(time, dataEdit.getText().toString());
            values2.put(height, editTextHeight.getText().toString());
            values.put(record_status, spinnerDoEat.getSelectedItem().toString());

//            postData
            String[] arrayData = dataEdit.getText().toString().split("-");
            try {
                postData.put("weight_kg",editTextWeight.getText().toString() );
                postData.put("heart_rate", editTextHr.getText().toString());
                postData.put("diastolic_bp", editTextDbp.getText().toString());
                postData.put("systolic_bp", editTextSbp.getText().toString());
                postData.put("height_cm", editTextHeight.getText().toString());
                postData.put("record_status", getValue("record_status",spinnerDoEat.getSelectedItem().toString()));
                postData.put("record_date", arrayData[0]);
                postData.put("record_time", arrayData[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences SharedPreferences = getSharedPreferences("bodyInformation",MODE_PRIVATE);

            //編輯文件
            SharedPreferences.Editor editor = SharedPreferences.edit();

            //將json檔案存入字串
            String a = postData.toString();

            //存入sharepreference
            editor.putString(result,a);

            editor.commit();
            new bodyInformation().execute();

            if(cur.getCount()>0){
                cur.moveToLast();
                db.update(TABLE_c,values2,id+"="+cur.getString(0),null);
            }else{
                db.insert(TABLE_c, null, values2);
            }
            db.insert(TABLE_e, null, values);
            Toast.makeText(this, "新增成功!!", Toast.LENGTH_SHORT).show();
            delete();
        }

    }
    public  void delete(){
        editTextWeight.setText("");
        editTextSbp.setText("");
        editTextDbp.setText("");
        editTextHr.setText("");
        editTextHeight.setText("");
    }
    public void uponclick(View view){
        add();
        delete();
    }

    public void deonclick(View view){
        delete();
    }

    private void fillTextView (int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    private void searchtime() throws ParseException {
        dataEdit = (EditText) findViewById(R.id.editTextDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        calendar = Calendar.getInstance();
        Date tdt = calendar.getTime();
        String time = sdf.format(tdt);
        dataEdit.setText(time);
    }

    public void searchheight(){
        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();

        cur=db.rawQuery(" SELECT " + height + "  FROM  customer " ,null);
        EditText HeightText = (EditText) findViewById(R.id.editTextHeight);
        if(cur.getCount()>0){
            cur.moveToLast();
            HeightText.setText(cur.getString(0));
        }else{
            HeightText.setHint("請輸入身高");
        }
    }

    //get value
    public String getValue(String model,String key){
        HashMap<String,Integer> record_status = new HashMap<>();
        record_status.put("飯前",1);
        record_status.put("飯後",2);

        String value = "";
        switch (model){
            case "record_status":
                value = record_status  .get(key).toString();
                break;
        }
        return value;
    }

    private class bodyInformation extends AsyncTask<String,Void,String> {
        SharedPreferences SharedPreferences2 = getSharedPreferences("bodyInformation",MODE_PRIVATE);

        String Json2 =  SharedPreferences2.getString(result,"");

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
        Cursor cur1 =db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            if (cur1.getCount()>0){
                cur1.moveToFirst();
                try {
                    JSONObject tempJsonArray =new JSONObject(Json2);
                    jsonString.put("protocolId",1000);
                    jsonString.put("subjectId",cur1.getString(1));
                    jsonString.put("formId",1020);
                    jsonString.put("visit",time);
                    jsonString.put("datarecord",tempJsonArray)  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SSLsetting();
            try {
                URL url = new URL("https://tlbinfo.cims.tw:8443/csis/createFormRecord.do?token=Fg7oI5I814N9G0N9omcGeboBj3kB");
//                URL url = new URL("https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
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
