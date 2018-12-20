package com.example.user.medical6;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class HistoryActivity extends SQLiteOpenHelper {
    public static final String TABLE_c = "customer";  //資料表名稱
    public static final String TABLE_e = "examine";  //資料表名稱
    //客戶資料表
    public static final String id = "id";
    public static final String height = "身高";
    public static final String sex = "性別";
    //檢驗資料表
    public static final String num = "檢驗編號";
    public static final String time = "時間";
    public static final String weight = "體重";
    public static final String sbp = "收縮壓";
    public static final String pbp = "舒張壓";
    public static final String hr= "心律";
    public static final String idnum= "客戶代碼";
    private final static String DATABASE_NAME = "sql.db";  //資料庫名稱

    private final static int DATABASE_VERSION = 1;  //資料庫版本

    public HistoryActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String customer = "CREATE TABLE " + TABLE_c + " (" +id + " INTEGER PRIMARY KEY , " + height + " VARCHAR(32), " + sex + " VARCHAR(5));";
        final String examine = "CREATE TABLE " + TABLE_e  + " (" +num + " INTEGER PRIMARY KEY , " + time + " VARCHAR(32), " + weight + " VARCHAR(32), "+ sbp + " VARCHAR(32), "+ pbp + " VARCHAR(32), "+ hr + " VARCHAR(32), " + idnum + " VARCHAR(32));";

        db.execSQL(customer);
        db.execSQL(examine);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_c;
        final String DROP_TABLE1 = "DROP TABLE IF EXISTS " + TABLE_e;

        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE1);
        onCreate(db);
    }
}