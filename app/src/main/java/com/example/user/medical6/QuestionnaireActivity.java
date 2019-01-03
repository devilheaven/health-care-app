package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;



public class QuestionnaireActivity extends AppCompatActivity {

    //new for 1 line
    private Calendar  calendar= Calendar.getInstance();
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        final EditText dataEdit1 = (EditText) findViewById(R.id.birth);
        dataEdit1.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit1, calendar);
            }
        });
        final EditText dataEdit2 = (EditText) findViewById(R.id.cancer1);
        dataEdit2.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit2, calendar);
            }
        });
        final EditText dataEdit3 = (EditText) findViewById(R.id.cancer2);
        dataEdit3.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit3, calendar);
            }
        });
        final EditText dataEdit4 = (EditText) findViewById(R.id.cancer3);
        dataEdit4.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit4, calendar);
            }
        });
        final EditText dataEdit5 = (EditText) findViewById(R.id.cancer4);
        dataEdit5.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit5, calendar);
            }
        });
        final EditText dataEdit6 = (EditText) findViewById(R.id.cancer5);
        dataEdit6.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit6,  calendar);
            }
        });
        final EditText dataEdit7 = (EditText) findViewById(R.id.cancer6);
        dataEdit7.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit7,  calendar);
            }
        });
        final EditText dataEdit8 = (EditText) findViewById(R.id.cancer7);
        dataEdit8.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit8,  calendar);
            }
        });
        final EditText dataEdit9 = (EditText) findViewById(R.id.cancer8);
        dataEdit9.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit9,  calendar);
            }
        });
        final EditText dataEdit10 = (EditText) findViewById(R.id.cancer9);
        dataEdit10.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                getDate(dataEdit10,  calendar);
            }
        });

    }

    private void getDate(final EditText dataEdit,final Calendar calendar){
        new DatePickerDialog(QuestionnaireActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        dataEdit.setText(new StringBuilder()
                                .append((mMonth + 1) < 10 ? "0"
                                        + (mMonth + 1) : (mMonth + 1))
                                .append("/")
                                .append((mDay < 10 ? "0" + mDay : mDay))
                                .append("/")
                                .append(mYear));
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
