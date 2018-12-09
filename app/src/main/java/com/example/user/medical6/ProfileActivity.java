package com.example.user.medical6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    private Button LogoutBtn,SubscribeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button btn1=(Button)findViewById(R.id.LogoutBtn);
        LogoutBtn=(Button)findViewById(R.id.LogoutBtn);

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
}
