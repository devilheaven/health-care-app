package com.example.user.medical6;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//呼叫dataBase類別定義的常數
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import static com.example.user.medical6.dataBase.*;

public class ManualActivity extends AppCompatActivity {
    private EditText editTextWeight,editTextHr,editTextDbp,editTextSbp,editTextHeight,dataEdit;

    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result = "result";

    public dataBase DH=null;
    SQLiteDatabase db;
    Cursor cur;

    private appFunction function = new appFunction();

    private Spinner spinnerDoEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_manual);

        try {
            dataEdit = (EditText) findViewById(R.id.editTextDate);
            function.searchtime(dataEdit);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DH = new dataBase(this);
        DH.close();

        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextHr = (EditText) findViewById(R.id.editTextHr);
        editTextDbp = (EditText) findViewById(R.id.editTextDbp);
        editTextSbp = (EditText) findViewById(R.id.editTextSdp);
        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        spinnerDoEat= findViewById(R.id.DoEat);

        //SQLiteDatabase db =DH.getReadableDatabase();
        function.searchheight(DH,(EditText) findViewById(R.id.editTextHeight));
    }

    public void add(){
        db = DH.getWritableDatabase();

        if(editTextWeight.getText().toString().matches("") || editTextHeight.getText().toString().matches("")|| editTextHr.getText().toString().matches("")|| editTextDbp.getText().toString().matches("")|| editTextSbp.getText().toString().matches("")) {
            Toast toast = Toast.makeText(ManualActivity.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            JSONObject postData=new JSONObject();
            ContentValues values = new ContentValues();
            ContentValues values2 = new ContentValues();

            cur=db.rawQuery(" SELECT " + id + "  FROM  customer " ,null);

            values.put(weight,editTextWeight.getText().toString() );
            values.put(sbp, editTextSbp.getText().toString());
            values.put(dbp, editTextDbp.getText().toString());
            values.put(hr, editTextHr.getText().toString());
            values.put(time, dataEdit.getText().toString());
            values.put(record_status, spinnerDoEat.getSelectedItem().toString());

            values2.put(height, editTextHeight.getText().toString());
//            postData
            String[] arrayData = dataEdit.getText().toString().split("-");
            try {
                postData.put("weight_kg",editTextWeight.getText().toString() );
                postData.put("heart_rate", editTextHr.getText().toString());
                postData.put("diastolic_bp", editTextDbp.getText().toString());
                postData.put("systolic_bp", editTextSbp.getText().toString());
                postData.put("height_cm", editTextHeight.getText().toString());
                postData.put("record_status", function.getValue("record_status",spinnerDoEat.getSelectedItem().toString()));
                postData.put("record_date", arrayData[0]);
                postData.put("record_time", arrayData[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences SharedPreferences = getSharedPreferences("bodyInformation",MODE_PRIVATE);

            //編輯文件
            SharedPreferences.Editor editor = SharedPreferences.edit();

            //將json檔案存入字串
            String a = postData.toString();

            //存入sharepreference
            editor.putString(result,a);

            editor.commit();

            APIFunction API = new APIFunction();
            String urlAddress = "https://tlbinfo.cims.tw:8443/csis/createFormRecord.do?token=Fg7oI5I814N9G0N9omcGeboBj3kB";
//               String urlAddress = "https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI";
            API.questionAPIconnect(ManualActivity.this, urlAddress, getSharedPreferences("bodyInformation",MODE_PRIVATE),1000, 1020);

            if(cur.getCount()>0){
                cur.moveToLast();
                db.update(TABLE_c,values2,id+"="+cur.getString(0),null);
            }else{
                db.insert(TABLE_c, null, values2);
            }
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
