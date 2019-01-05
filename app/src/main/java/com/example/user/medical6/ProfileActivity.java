package com.example.user.medical6;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.user.medical6.dataBase.*;

public class ProfileActivity extends AppCompatActivity {
    TextView txvInfo;
    // data base 變數宣告
    public dataBase DH=null;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();
    Cursor cur1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnSubscribe = (Button)findViewById(R.id.SubscribeBtn);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,SubscribeActivity.class);
                startActivity(intent);
            }
        });

        txvInfo = (TextView) findViewById(R.id.txvInfo);

        Button btnMyID = (Button)findViewById(R.id.MyIDBtn);
        btnMyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScanQRCode(); //掃描QR Code的函數
            }
        });

        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //取得intent回傳結果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents() == null)
                txvInfo.setText("取消QR Code讀取作業");
            else{
                cur1=db.rawQuery(" SELECT *  FROM  customer " ,null);
                //清除舊資料
                if (cur1.getCount()>0){
                    db.execSQL("delete from customer");
                }

                // QR code result string
                String resultStr=result.getContents();
                String[] arrayData = resultStr.split(",");
                //新增資料
                if (arrayData.length == 3){
                    values.put(subjectId, arrayData[0]);
                    values.put(idnum1, arrayData[2]);
                    values.put(lastName, arrayData[1]);
                    values.put(height, "178");
                    values.put(sex, "male");
                    db.insert(TABLE_c, null, values);
//                //查詢資料
//                cur1=db.rawQuery(" SELECT *  FROM  customer " ,null);
//                String jsonString = null;
//                if (cur1.getCount()>0){
//                    cur1.moveToLast();
//                    jsonString ="{\"protocolId\":1032,\"subjectId\":\""+cur1.getString(1)+"\",\"lastName\":\""+cur1.getString(3)+"\",\"guid\":\""+cur1.getString(2)+"\"}";
//                }
//                String returnData = jsonString;
//                txvInfo.setText((CharSequence) returnData);
                    new PatientsCreate().execute();
                }else{
                    String mag = "請掃描正確的 QR code";
                    AlertDialog.Builder bdr = new AlertDialog.Builder(this);
                    bdr.setMessage(mag);
                    bdr.setCancelable(true);
                    bdr.show();
                    txvInfo.setText("請掃描正確的 QR code");
//                    txvInfo.setText("請掃描正確的 QR code\n information:"+resultStr);
                }
            }
        }
        else
            super.onActivityResult(resultCode,resultCode,data); //預設父親類別執行指令
    }

    //api 傳值
    public class PatientsCreate extends AsyncTask<String,Void,String> {
        protected String responsetitle= "";
        protected String responsestring= "";
        // SQL lite query
        Cursor cur1 =db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonString = new JSONObject();
            if (cur1.getCount()>0){
                cur1.moveToFirst();
//                jsonString ="{\"protocolId\":1000,\"subjectId\":\""+cur1.getString(1)+"\",\"lastName\":\""+cur1.getString(3)+"\",\"guid\":\""+cur1.getString(2)+"\"}";
                try {
                    jsonString.put("protocolId",1000);
                    jsonString.put("subjectId",cur1.getString(1));
                    jsonString.put("lastName",cur1.getString(3));
                    jsonString.put("guid",cur1.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                jsonString ="{\"protocolId\":1032,\"subjectId\":\""+cur1.getString(1)+"\",\"lastName\":\""+cur1.getString(3)+"\",\"guid\":\""+cur1.getString(2)+"\"}";
            }

            SSLsetting();
            try {
                URL url = new URL("https://tlbinfo.cims.tw:8443/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
//                URL url = new URL("https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-type","application/json");

//                jsonString ="{\"protocolId\":1032,\"subjectId\":\"A001\",\"lastName\":\"劉\",\"guid\":\"PSEUDO-LQKZRBQ2\"}";
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
                txvInfo.setText(cur1.getString(1)+"\t"+cur1.getString(3)+"先生/小姐");
            }else{
                txvInfo.setText("登入失敗");
                if (cur1.getCount()>0){
                    db.execSQL("delete from customer");
                }

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

    //QR code scanner function
    private void doScanQRCode() //產生QR Code的函數
    {
        IntentIntegrator integrator = new IntentIntegrator(this); //產生intent整合物件
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //設定條碼格式
        integrator.setPrompt("掃描QR Code"); //設定掃描時提示文字
        integrator.setCameraId(0);//設定使用後相機讀取QR Code
        integrator.setBeepEnabled(true);//提示音
        integrator.setBarcodeImageEnabled(false);//設定掃描圖檔不要存檔，掃完即丟掉
        integrator.setOrientationLocked(false);
        integrator.initiateScan();//起到掃描功能
    }
}
