package com.example.user.medical6;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.user.medical6.dataBase.*;

public class appFunction {
    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

    // data base 變數宣告
    private dataBase DH=null;
    private Cursor cur;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;

    void searchtime(TextView targetItem) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        calendar = Calendar.getInstance();
        Date tdt = calendar.getTime();
        String time = sdf.format(tdt);
        targetItem.setText(time);
    }

    void searchheight(EditText targetItem){
        cur=db.rawQuery(" SELECT " + height + "  FROM  customer " ,null);
        if(cur.getCount()>0){
            cur.moveToLast();
            targetItem.setText(cur.getString(0));
        }else{
            targetItem.setHint("請輸入身高");
        }
    }

    //get value
    String getValue(String model,String key){
        HashMap<String,Integer> record_status = new HashMap<>();
        record_status.put("飯前",1);
        record_status.put("飯後",2);

        String value = "";
        switch (model){
            case "record_status":
                value = record_status  .get(key).toString();
                break;
        }
        return value;
    }

}
