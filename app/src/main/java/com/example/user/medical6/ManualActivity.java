package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//呼叫dataBase類別定義的常數
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.user.medical6.dataBase.*;

public class ManualActivity extends AppCompatActivity {
    private EditText editTextWeight,editTextHr,editTextDbp,editTextSbp,editTextHeight,dataEdit;

    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

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
        //cur = db.rawQuery(" SELECT weight   FROM examine WHERE sbp=  " + editTextSbp.getText().toString() , null);



        searchheight();
    }

    public void add(){

        SQLiteDatabase db=DH.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(editTextWeight.getText().toString().matches("") || editTextHr.getText().toString().matches("")|| editTextDbp.getText().toString().matches("")|| editTextSbp.getText().toString().matches("")) {
            Toast toast = Toast.makeText(ManualActivity.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            values.put(weight,editTextWeight.getText().toString() );
            values.put(sbp, editTextSbp.getText().toString());
            values.put(dbp, editTextDbp.getText().toString());
            values.put(hr, editTextHr.getText().toString());
            values.put(time, dataEdit.getText().toString());
            values.put(record_status, spinnerDoEat.getSelectedItem().toString());
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
}
