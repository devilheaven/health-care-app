package com.example.user.medical6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecordActivity extends AppCompatActivity {

    private Button ConnectDeviceBtn,ManualBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Button btn1=(Button)findViewById(R.id.ConnectDeviceBtn);
        ConnectDeviceBtn=(Button)findViewById(R.id.ConnectDeviceBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RecordActivity.this,ConnectDeviceActivity.class);
                startActivity(intent);
            }
        });
        //---------------------------
        Button btn2=(Button)findViewById(R.id.ManualBtn);
        ConnectDeviceBtn=(Button)findViewById(R.id.ManualBtn);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RecordActivity.this,ManualActivity.class);
                startActivity(intent);
            }
        });
    }
}
