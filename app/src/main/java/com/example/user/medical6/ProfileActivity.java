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
    TextView txvInfo,tvLoginType;
    // data base 變數宣告
    public dataBase DH=null;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();
    Cursor cur1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //讀取資料
        DH = new dataBase(this);
        DH.close();
        db = DH.getReadableDatabase();

        txvInfo = (TextView) findViewById(R.id.txvInfo);
        tvLoginType = (TextView) findViewById(R.id.LoginType);

        Button btnMyID = (Button)findViewById(R.id.MyIDBtn);
        btnMyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScanQRCode(); //掃描QR Code的函數
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

        cur1 = db.rawQuery(" SELECT *  FROM  customer " ,null);
        if (cur1.getCount()>0){
            cur1.moveToFirst();
            txvInfo.setText(cur1.getString(1)+"\t"+cur1.getString(3)+"先生/小姐");
            tvLoginType.setText("member");
        }else{
            txvInfo.setText("請掃描 QR code");
            tvLoginType.setText("guest");
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //取得intent回傳結果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null) {
            if (result.getContents() == null){
                txvInfo.setText("取消QR Code讀取作業");
                tvLoginType.setText("guest");
            }else{
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

                    APIFunction API = new APIFunction();
                    API.memberAPIconnect(this,"https://tlbinfo.cims.tw:8443/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI",txvInfo,tvLoginType);
//                    API.memberAPIconnect(this,"https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI",txvInfo,tvLoginType);
                }else{
                    String mag = "請掃描正確的 QR code";
                    AlertDialog.Builder bdr = new AlertDialog.Builder(this);
                    bdr.setMessage(mag);
                    bdr.setCancelable(true);
                    bdr.show();
                    tvLoginType.setText("guest");
                    txvInfo.setText("請掃描正確的 QR code");
//                    txvInfo.setText("請掃描正確的 QR code\n information:"+resultStr);
                }
            }
        }
        else
            super.onActivityResult(resultCode,resultCode,data); //預設父親類別執行指令
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
