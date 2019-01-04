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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;


import com.github.mikephil.charting.components.YAxis;




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

        setLineChart(); //長條圖方法



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
    //LineChart
    private void setLineChart(){
        LineChart lineChart=(LineChart)findViewById(R.id.chart);
        /*
        //创建Entry保存你的数据
        List<Entry> entry1 = new ArrayList<Entry>() ; //折线一的数据源
        List<Entry> entry2 = new ArrayList<Entry>() ; //折线二的数据源
        //向折线一添加数据
        Entry x1 = new Entry(0f , 10000f) ;
        entry1.add(x1) ;
        Entry x2 = new Entry(1f , 14000f) ;
        entry1.add(x2) ;
        Entry x3 = new Entry(2f , 5000f) ;
        entry1.add(x3) ;
        Entry x4 = new Entry(3f , 12000f) ;
        entry1.add(x4) ;
        //向折线二添加数据
        Entry y1 = new Entry(0f , 13000f) ;
        entry2.add(y1) ;
        Entry y2 = new Entry(1f , 15500f) ;
        entry2.add(y2) ;
        Entry y3 = new Entry(3f , 5500f) ;
        entry2.add(y3) ;
        Entry y4 = new Entry(4f , 10000f) ;
        entry2.add(y4) ;
        //将数据传递给LineDataSet对象
        LineDataSet set1 = new LineDataSet(entry1 , "折线一") ;
        //调用setAxisDependency（）指定DataSets绘制相应的折线
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //折线一 折线的颜色
        set1.setColor(getResources().getColor(R.color.colorAccent));
        LineDataSet set2 = new LineDataSet(entry2 , "折线二") ;
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        //折线二 折线的颜色
        set2.setColor(getResources().getColor(R.color.colorPrimary));
        //使用接口ILineDataSet
        List<LineDataSet> dataSets = new ArrayList<LineDataSet>() ;
        //添加数据
        dataSets.add(set1) ;
        dataSets.add(set2) ;
        LineData data = new LineData(dataSets) ;
        //设置图表
        lineChart.setData(data);
        //刷新
        lineChart.invalidate();

        */
        //---------------------------------------------------------------------------------
        ArrayList<Entry> entries1=new ArrayList<>(); //建立資料串列
        entries1.add(new Entry(4f,0));
        entries1.add(new Entry(8f,1));
        entries1.add(new Entry(6f,2));
        entries1.add(new Entry(12f,3));
        entries1.add(new Entry(18f,4));
        entries1.add(new Entry(9f,5));

        LineDataSet dataSet1=new LineDataSet(entries1,"舒張壓");//建立對應資料集
        dataSet1.setDrawCubic(true);//設定資料集屬性 (設定為平滑曲線)
       // dataSet1.setDrawFilled(true);// 設定下方曲線填滿
        dataSet1.setColors(ColorTemplate.COLORFUL_COLORS); //設定曲線顏色
        //
        ArrayList<Entry> entries2=new ArrayList<>(); //建立資料串列
        entries2.add(new Entry(8f,0));
        entries2.add(new Entry(8f,1));
        entries2.add(new Entry(8f,2));
        entries2.add(new Entry(8f,3));
        entries2.add(new Entry(8f,4));
        entries2.add(new Entry(8f,5));

        LineDataSet dataSet2=new LineDataSet(entries2,"舒張壓");//建立對應資料集
        dataSet2.setDrawCubic(true);//設定資料集屬性 (設定為平滑曲線)
        // dataSet2.setDrawFilled(true);// 設定下方曲線填滿
        dataSet2.setColors(ColorTemplate.COLORFUL_COLORS); //設定曲線顏色
        //
        ArrayList<String>labels=new ArrayList<>(); //設定橫坐標千串列
        labels.add("一月");
        labels.add("二月");
        labels.add("三月");
        labels.add("四月");
        labels.add("五月");
        labels.add("六月");

        ArrayList<LineDataSet>dataSets=new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        LineData data=new LineData(labels,dataSets); //建立對應資料
        lineChart.setData(data);//設定對應資料給圖表控制項
        lineChart.setDescription("血壓");//設定圖表控制項屬性
        lineChart.animateY(5000);//設定圖表由下而上動態顯示
        lineChart.invalidate();

        //------------------------------------------------------------
    }


}