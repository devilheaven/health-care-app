package com.example.user.medical6;

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

    EditText etxtContent;
    TextView txvInfo;
    public dataBase DH=null;
    Cursor cur;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnLogout = (Button)findViewById(R.id.LogoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnSubscribe = (Button)findViewById(R.id.SubscribeBtn);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,SubscribeActivity.class);
                startActivity(intent);
            }
        });

        etxtContent = (EditText)findViewById(R.id.etxtContent);
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
                String resultStr=result.getContents();
                String UrlLocation = "https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI"; //API位置
                String[] arrayData = resultStr.split(",");
                values.put(subjectId, arrayData[0]);
                values.put(idnum1, arrayData[2]);
                values.put(lastName, arrayData[1]);
                values.put(height, "178");
                values.put(sex, "male");
                db.insert(TABLE_c, null, values);

                Cursor cur1=db.rawQuery(" SELECT *  FROM  customer " ,null);
                String jsonString = null;

                if (cur1.getCount()>0){
                    cur1.moveToLast();
                    jsonString ="{\"protocolId\":1032,\"subjectId\":\""+cur1.getString(1)+"\",\"lastName\":\""+cur1.getString(3)+"\",\"guid\":\""+cur1.getString(2)+"\"}";
                }

                String returnData = jsonString;

//                String PostData = "{\"protocolId\" : 1032 ,\"subjectId\" : \"A" +arrayData[0]+"\" ,\"lastName\" : \""+arrayData[1]+"\" , \"guid\" : \""+arrayData[2]+"\"}";

//                etxtContent.setText(PostData);
//                new PatientsCreate().execute();
//                String returnData = httpConnectionPost(UrlLocation,PostData);
                new PatientsCreate().execute();
                txvInfo.setText((CharSequence) returnData);
                //txvInfo.setText("QR Code讀取完成!");
                }
        }
        else
            super.onActivityResult(resultCode,resultCode,data); //預設父親類別執行指令
    }

    public class PatientsCreate extends AsyncTask<String,Void,String> {
        protected String responsetitle= "";
        protected String responsestring= "";

        @Override
        protected String doInBackground(String... strings) {
            Cursor cur1=db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);
            String jsonString = "";

            if (cur1.getCount()>0){
                cur1.moveToFirst();
                jsonString ="{\"protocolId\":1032,\"subjectId\":\""+cur1.getString(1)+"\",\"lastName\":\""+cur1.getString(3)+"\",\"guid\":\""+cur1.getString(2)+"\"}";
            }

            SSLsetting();
            try {
                URL url = new URL("https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
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
                writer.write(jsonString);
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v("Back",s);
        }
    }

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
