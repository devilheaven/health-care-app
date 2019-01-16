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

import java.util.ArrayList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


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

        setLineChart();
        Button btnUpdate = (Button) findViewById(R.id.btnBIRefresh);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur=db.rawQuery(" SELECT * FROM  examine ORDER BY "+ time ,null);
                adapter = new SimpleCursorAdapter(HistoryActivity.this, R.layout.item, cur, FROM,new int[] { R.id.editTextDate, R.id.editTextWeight, R.id.editTextSdp, R.id.editTextDbp, R.id.editTextHr,R.id.DoEat},0);
                lvListall=(ListView)findViewById(R.id.lvListall);
                lvListall.setAdapter(adapter);
                setLineChart();
                Toast tos = Toast.makeText(HistoryActivity.this, "更新成功!", Toast.LENGTH_SHORT);
                tos.show();
            }
        });
    }

    //LineChart
    private void setLineChart(){
        LineChart lineChart=(LineChart)findViewById(R.id.chart);
        //line1
        Cursor c = db.rawQuery(" SELECT * FROM  examine ORDER BY "+ time +" LIMIT  10 " ,null);
        ArrayList<Entry> entries1=new ArrayList<>(); //建立資料串列
        c.moveToFirst();
        for (int i = 0; i<=(c.getCount()-1);i++){
            entries1.add( new Entry(Float.valueOf( c.getString( c.getColumnIndex("hr") ) ),i) );
            c.moveToNext();
        };

        LineDataSet dataSet1=new LineDataSet(entries1,"HeartRate");//建立對應資料集
        dataSet1.setDrawCubic(true);//設定資料集屬性 (設定為平滑曲線)
        // dataSet1.setDrawFilled(true);// 設定下方曲線填滿
        dataSet1.setColors(ColorTemplate.COLORFUL_COLORS); //設定曲線顏色

        ArrayList<String>labels=new ArrayList<>(); //設定橫坐標千串列
        for (int i=0; i<=(c.getCount()-1);i++){
            labels.add(String.valueOf(i));
        }

        ArrayList<LineDataSet>dataSets=new ArrayList<>();
        dataSets.add(dataSet1);
        LineData data=new LineData(labels,dataSets); //建立對應資料
        lineChart.setData(data);//設定對應資料給圖表控制項
        lineChart.setDescription("HeartRate");//設定圖表控制項屬性
        lineChart.animateY(5000);//設定圖表由下而上動態顯示
        lineChart.invalidate();
    }

    public void requery() {

    }

}