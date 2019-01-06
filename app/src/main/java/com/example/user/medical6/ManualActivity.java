package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//呼叫dataBase類別定義的常數
import java.util.Calendar;

import static com.example.user.medical6.dataBase.*;

public class ManualActivity extends AppCompatActivity {
    private int mYear;
    private int mMonth;
    private int mDay;

    public dataBase DH=null;
    SQLiteDatabase db;
    Cursor cur;

    private Calendar calendar;
    private EditText editTextWeight,editTextHr,editTextDbp,editTextSbp,editTextHeight,dataEdit;
    private Spinner spinnerDoEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_manual);
        dataEdit = (EditText) findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();
        dataEdit.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManualActivity.this,
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
        });

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

}
