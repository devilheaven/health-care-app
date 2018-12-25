package com.example.user.medical6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private Button LogoutBtn,SubscribeBtn;
    EditText etxtContent;
    TextView txvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button btn1=(Button)findViewById(R.id.LogoutBtn);
        LogoutBtn=(Button)findViewById(R.id.LogoutBtn);
        etxtContent=(EditText)findViewById(R.id.etxtContent);
        txvInfo=(TextView) findViewById(R.id.txvInfo);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //-------------------------------------
        Button btn2=(Button)findViewById(R.id.SubscribeBtn);
        SubscribeBtn=(Button)findViewById(R.id.SubscribeBtn);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,SubscribeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //取得intent回傳結果
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
                txvInfo.setText("取消QR Code讀取作業");
            else{
                String resultStr=result.getContents();
                etxtContent.setText(resultStr);
                txvInfo.setText("QR Code讀取完成!");
                }
        }
        else
            super.onActivityResult(resultCode,resultCode,data); //預設父親類別執行指令
    }
    public void btnOnClick(View view)
    {
        int id=view.getId();
        switch(id)
        {

            case R.id.MyIDBtn:
                doScanQRCode(); //掃描QR Code的函數
                break;
        }
    }
    private void doScanQRCode() //產生QR Code的函數
    {
        IntentIntegrator integrator=new IntentIntegrator(this); //產生intent整合物件
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //設定條碼格式
        integrator.setPrompt("掃描QR Code"); //設定掃描時提示文字
        integrator.setCameraId(0);//設定使用後相機讀取QR Code
        integrator.setBeepEnabled(false);//關閉提示音
        integrator.setBarcodeImageEnabled(false);//設定掃描圖檔不要存檔，掃完即丟掉
        integrator.initiateScan();//起到掃描功能
    }

}
