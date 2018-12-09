package com.example.user.medical6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSelectionActivity extends AppCompatActivity {

    private Button ProfileBtn,NewRecordBtn,HistoryBtn,QuestionnaireBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selection);

        ProfileBtn=(Button)findViewById(R.id.ProfileBtn);


        Button btn1=(Button)findViewById(R.id.ProfileBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainSelectionActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        NewRecordBtn=(Button)findViewById(R.id.NewRecordBtn);


        Button btn2=(Button)findViewById(R.id.NewRecordBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainSelectionActivity.this,RecordActivity.class);
                startActivity(intent);
            }
        });
        //-----------------------------
        HistoryBtn=(Button)findViewById(R.id.HistoryBtn);


        Button btn3=(Button)findViewById(R.id.HistoryBtn);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainSelectionActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });
        //---------
        QuestionnaireBtn=(Button)findViewById(R.id.QuestionnaireBtn);


        Button btn4=(Button)findViewById(R.id.QuestionnaireBtn);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainSelectionActivity.this,QuestionnaireActivity.class);
                startActivity(intent);
            }
        });
    }
}
