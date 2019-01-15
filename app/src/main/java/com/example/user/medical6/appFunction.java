package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.user.medical6.dataBase.*;

public class appFunction extends AppCompatActivity {
    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

    // data base 變數宣告
    Cursor cur;
    ContentValues values = new ContentValues();
    SQLiteDatabase db;

    private int mYear;
    private int mMonth;
    private int mDay;

    void searchtime(TextView targetItem) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        calendar = Calendar.getInstance();
        Date tdt = calendar.getTime();
        String time = sdf.format(tdt);
        targetItem.setText(time);
    }

    void searchtime(EditText targetItem) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        calendar = Calendar.getInstance();
        Date tdt = calendar.getTime();
        String time = sdf.format(tdt);
        targetItem.setText(time);
    }

    void getDate(Context context, final EditText dataEdit){
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context,
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

    void searchheight(dataBase DH, EditText targetItem){
        db = DH.getReadableDatabase();
        cur = db.rawQuery(" SELECT " + height + "  FROM  customer " ,null);
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

        HashMap<String,Integer> gender = new HashMap<>();
        gender.put("男",0);
        gender.put("女",1);

        HashMap<String,Integer> yesNo = new HashMap<>();
        yesNo.put("無",0);
        yesNo.put("有",1);

        HashMap<String,Integer> yesNoUnknow = new HashMap<>();
        yesNoUnknow.put("有",0);
        yesNoUnknow.put("無",1);
        yesNoUnknow.put("不詳",2);

        HashMap<String,Integer> educatinlevel = new HashMap<>();
        educatinlevel.put("未受教育",0);
        educatinlevel.put("小學",1);
        educatinlevel.put("國(初)中",2);
        educatinlevel.put("高中(職)",3);
        educatinlevel.put("大學(專科)",4);
        educatinlevel.put("研究所以上",5);

        HashMap<String,Integer> income = new HashMap<>();
        income.put("困苦",0);
        income.put("尚可",1);
        income.put("小康",2);
        income.put("富裕",3);

        HashMap<String,Integer> smoking_behavior = new HashMap<>();
        smoking_behavior.put("從未抽菸",0);
        smoking_behavior.put("已戒菸",1);
        smoking_behavior.put("目前有抽菸習慣",2);

        HashMap<String,Integer> alcoholism = new HashMap<>();
        alcoholism.put("從未飲酒",0);
        alcoholism.put("已戒酒",1);
        alcoholism.put("偶爾",2);
        alcoholism.put("目前有飲酒習慣",3);

        HashMap<String,Integer> sleep_hours = new HashMap<>();
        sleep_hours.put("不到 6 小時",0);
        sleep_hours.put("6~6.9 小時",1);
        sleep_hours.put("7~7.9 小時",2);
        sleep_hours.put("8小時以上",3);

        HashMap<String,Integer> stay_overnight = new HashMap<>();
        stay_overnight.put("很少(每週≤1次)",0);
        stay_overnight.put("偶爾(每週2~4次)",1);
        stay_overnight.put("經常(每週≥5次)",2);

        HashMap<String,Integer> taking_nap = new HashMap<>();
        taking_nap.put("很少(每週≤1次)",0);
        taking_nap.put("偶爾(每週2~4次)",1);
        taking_nap.put("經常(每週≥5次)",2);

        HashMap<String,Integer> exercise_frequency = new HashMap<>();
        exercise_frequency.put("每月≤1次",0);
        exercise_frequency.put("每月2~3次",1);
        exercise_frequency.put("每週1~2次",2);
        exercise_frequency.put("每週3~4次",3);
        exercise_frequency.put("每週≥5次",4);

        HashMap<String,Integer> food = new HashMap<>();
        food.put("都沒有吃此類食物",0);
        food.put("每週十次中有一至二次",1);
        food.put("每週十次中有三至五次",2);
        food.put("每週十次中有六至八次",3);
        food.put("每週十次中有八次以上",4);

        HashMap<String,Integer> Birth_location = new HashMap<>();
        Birth_location.put("臺灣閩南人",0);
        Birth_location.put("臺灣客家人",1);
        Birth_location.put("臺灣原住民",2);
        Birth_location.put("中國各省份",3);
        Birth_location.put("其他",4);

        String value = "";
        switch (model){
            case "record_status":
                value = record_status  .get(key).toString();
                break;
            case "gender":
                value = gender.get(key).toString();
                break;
            case "educatin_level":
                value = educatinlevel.get(key).toString();
                break;
            case "income":
                value = income.get(key).toString();
                break;
            case "smoking_behavior":
                value = smoking_behavior.get(key).toString();
                break;
            case "alcoholism":
                value = alcoholism.get(key).toString();
                break;
            case "sleep_hours":
                value = sleep_hours.get(key).toString();
                break;
            case "stay_overnight":
                value = stay_overnight.get(key).toString();
                break;
            case "taking_nap":
                value = taking_nap.get(key).toString();
                break;
            case "exercise_frequency":
                value = exercise_frequency.get(key).toString();
                break;
            case "yesNo":
                value = yesNo.get(key).toString();
                break;
            case "food":
                value = food.get(key).toString();
                break;
            case "Birth_location":
                value = Birth_location.get(key).toString();
                break;
            case "yesNoUnknow":
                value = yesNoUnknow.get(key).toString();
                break;
        }
        return value;
    }
}
