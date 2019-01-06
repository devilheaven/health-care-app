package com.example.user.medical6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import static com.example.user.medical6.dataBase.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class HistoryActivity extends AppCompatActivity  {
    public dataBase DH=null;
    Cursor cur;
    static final String[] FROM=new String[]{time,weight,sbp,dbp,hr,record_status};
    SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    ListView lvListall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();

        cur=db.rawQuery(" SELECT * FROM  examine ORDER BY "+ time+" LIMIT  20 " ,null);
        adapter = new SimpleCursorAdapter(this, R.layout.item, cur, FROM,new int[] { R.id.editTextDate, R.id.editTextWeight, R.id.editTextSdp, R.id.editTextDbp, R.id.editTextHr,R.id.DoEat},0);
        lvListall=(ListView)findViewById(R.id.lvListall);
        lvListall.setAdapter(adapter);
        requery();

        Button btnUpdate = (Button) findViewById(R.id.btnBIRefresh);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur=db.rawQuery(" SELECT * FROM  examine ORDER BY "+ time ,null);
                adapter = new SimpleCursorAdapter(HistoryActivity.this, R.layout.item, cur, FROM,new int[] { R.id.editTextDate, R.id.editTextWeight, R.id.editTextSdp, R.id.editTextDbp, R.id.editTextHr,R.id.DoEat},0);
                lvListall=(ListView)findViewById(R.id.lvListall);
                lvListall.setAdapter(adapter);
                Toast tos = Toast.makeText(HistoryActivity.this, "更新成功!", Toast.LENGTH_SHORT);
                tos.show();
            }
        });
    }

    public void requery() {

    }

}