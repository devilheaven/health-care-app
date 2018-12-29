package com.example.user.medical6;

import android.content.ContentValues;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import static com.example.user.medical6.dataBase.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class HistoryActivity extends AppCompatActivity  {
    public dataBase DH=null;
    Cursor cur;
    static final String[] FROM=new String[]{time,weight,sbp,dbp,hr};
    SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    ListView lvListall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        Spinner SelectState = (Spinner)findViewById(R.id.spinBIState);
        ArrayAdapter<CharSequence> State = ArrayAdapter.createFromResource(HistoryActivity.this,
                R.array.BIState,
                android.R.layout.simple_spinner_dropdown_item);
        SelectState.setAdapter(State);



        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();


        cur=db.rawQuery(" SELECT  * FROM  examine " ,null);
//        adapter=new SimpleCursorAdapter(this, R.layout.activity_manual, cur, FROM,new int[] { R.id.txvtime, R.id.txvkg, R.id.txvsbp, R.id.txvdbp, R.id.txvhr},0);
        adapter=new SimpleCursorAdapter(this, R.layout.activity_manual, cur, FROM,new int[] { R.id.editTextDate, R.id.editTextWeight, R.id.editTextSdp, R.id.editTextDbp, R.id.editTextHr},0);
        lvListall=(ListView)findViewById(R.id.lvListall);
        lvListall.setAdapter(adapter);
        requery();


    }

    public void requery() {




    }



}