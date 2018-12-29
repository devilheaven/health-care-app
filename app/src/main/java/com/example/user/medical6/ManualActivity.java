package com.example.user.medical6;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//呼叫dataBase類別定義的常數
import static com.example.user.medical6.dataBase.*;

public class ManualActivity extends AppCompatActivity {

    public dataBase DH=null;
    SQLiteDatabase db;
    Cursor cur;
    public EditText editTextWeight,editTextHr,editTextDbp,editTextSbp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        DH=new dataBase(this);
        DH.close();

        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextHr = (EditText) findViewById(R.id.editTextHr);
        editTextDbp = (EditText) findViewById(R.id.editTextDbp);
        editTextSbp = (EditText) findViewById(R.id.editTextSdp);

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
            //values.put(num, );
            //values.put(time,getdate());
            values.put(weight,editTextWeight.getText().toString() );
            values.put(sbp, editTextSbp.getText().toString());
            values.put(dbp, editTextDbp.getText().toString());
            values.put(hr, editTextHr.getText().toString());
            //values.put(idnum, "david@yahoo.com");
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

    }
    public void uponclick(View view){

        add();


    }

    public void deonclick(View view){

        delete();
        //editTextWeight.setText("");
        SQLiteDatabase db = DH.getReadableDatabase();
        //cur=db.rawQuery(" SELECT  * FROM examine WHERE _id= 2 " ,null);

    }

}
