package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import static com.example.user.medical6.dataBase.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,AdapterView.OnItemSelectedListener {
    public dataBase DH=null;
    private Calendar  calendar = Calendar.getInstance();
    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result="questionnaire";
    static final  String result2="questionnaire2";
    // data base 變數宣告
    SQLiteDatabase db;

    appFunction function = new appFunction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        final EditText hight=(EditText)findViewById(R.id.hight);
        final EditText wight=(EditText)findViewById(R.id.wight);
        final EditText bmi=(EditText)findViewById(R.id.bmi);

        final EditText dataEdit1 = (EditText) findViewById(R.id.birth);
        dataEdit1.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit1);
            }
        });
        final EditText dataEdit2 = (EditText) findViewById(R.id.cancer1);
        dataEdit2.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit2);
            }
        });
        final EditText dataEdit3 = (EditText) findViewById(R.id.cancer2);
        dataEdit3.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit3);
            }
        });
        final EditText dataEdit4 = (EditText) findViewById(R.id.cancer3);
        dataEdit4.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit4);
            }
        });
        final EditText dataEdit5 = (EditText) findViewById(R.id.cancer4);
        dataEdit5.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit5);
            }
        });
        final EditText dataEdit6 = (EditText) findViewById(R.id.cancer5);
        dataEdit6.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit6);
            }
        });
        final EditText dataEdit7 = (EditText) findViewById(R.id.cancer6);
        dataEdit7.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit7);
            }
        });
        final EditText dataEdit8 = (EditText) findViewById(R.id.cancer7);
        dataEdit8.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit8);
            }
        });
        final EditText dataEdit9 = (EditText) findViewById(R.id.cancer8);
        dataEdit9.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit9);
            }
        });
        final EditText dataEdit10 = (EditText) findViewById(R.id.cancer9);
        dataEdit10.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                function.getDate(QuestionnaireActivity.this, dataEdit10);
            }
        });

        final Button btngo = (Button) findViewById(R.id.btngo);
        btngo.setOnClickListener(this);

        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();

        Cursor cur1 =db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);
        if (cur1.getCount()>0){
            cur1.moveToFirst();
            hight.setText(cur1.getString(4));
        }else{
            hight.setText("0");
        }
        cur1 =db.rawQuery(" SELECT *  FROM  examine ORDER BY _id DESC " ,null);
        if (cur1.getCount()>0){
            cur1.moveToFirst();
            wight.setText(cur1.getString(2));
        }else{
            wight.setText("0");
        }
        Float BMI = Float.valueOf(0);
        if(Float.parseFloat(wight.getText().toString()) != 0 && Float.parseFloat(hight.getText().toString()) != 0){
            BMI = Float.parseFloat(wight.getText().toString())/Float.parseFloat(hight.getText().toString())/Float.parseFloat(hight.getText().toString())*10000;
        }else{
            BMI = Float.valueOf(0);
        }

        bmi.setText(BMI.toString());
    }

    public  void  json(){
        //宣告物件 將欄位轉成json
        final EditText age=(EditText)findViewById(R.id.age);
        final EditText hight=(EditText)findViewById(R.id.hight);
        final EditText wight=(EditText)findViewById(R.id.wight);
        final EditText bmi=(EditText)findViewById(R.id.bmi);
        final EditText birth = (EditText) findViewById(R.id.birth);
        final EditText cancer1 = (EditText) findViewById(R.id.cancer1);
        final EditText cancer2=(EditText)findViewById(R.id.cancer2);
        final EditText cancer3=(EditText)findViewById(R.id.cancer3);
        final EditText cancer4 = (EditText) findViewById(R.id.cancer4);
        final EditText cancer5=(EditText)findViewById(R.id.cancer5);
        final EditText cancer6=(EditText)findViewById(R.id.cancer6);
        final EditText cancer7 = (EditText) findViewById(R.id.cancer7);
        final EditText cancer8=(EditText)findViewById(R.id.cancer8);
        final EditText cancer9 = (EditText) findViewById(R.id.cancer9);

        final Spinner lev = (Spinner) findViewById(R.id.level);
        final Spinner salary = (Spinner) findViewById(R.id.salary);
        final Spinner smoke = (Spinner) findViewById(R.id.smoke);
        final Spinner drink = (Spinner) findViewById(R.id.drink);
        final Spinner hur = (Spinner) findViewById(R.id.hur);
        final Spinner overnight = (Spinner) findViewById(R.id.overnight);
        final Spinner sleep = (Spinner) findViewById(R.id.sleep);
        final Spinner exerice = (Spinner) findViewById(R.id.exerice);
        final Spinner candy = (Spinner) findViewById(R.id.candy);
        final Spinner heart = (Spinner) findViewById(R.id.heart);
        final Spinner blood = (Spinner) findViewById(R.id.blood);
        final Spinner sick = (Spinner) findViewById(R.id.sick);
        final Spinner stomache = (Spinner) findViewById(R.id.stomache);
        final Spinner sick3 = (Spinner) findViewById(R.id.sick3);
        final Spinner stomachecan = (Spinner) findViewById(R.id.stomachecan);
        final Spinner sick4 = (Spinner) findViewById(R.id.sick4);
        final Spinner overstomach = (Spinner) findViewById(R.id.overstomach);
        final Spinner sick24 = (Spinner) findViewById(R.id.sick24);
        final Spinner sick25 = (Spinner) findViewById(R.id.sick25);
        final Spinner sick26 = (Spinner) findViewById(R.id.sick26);
        final Spinner sick27 = (Spinner) findViewById(R.id.sick27);
        final Spinner sick28 = (Spinner) findViewById(R.id.sick28);
        final Spinner sick29 = (Spinner) findViewById(R.id.sick29);
        final Spinner sick30 = (Spinner) findViewById(R.id.sick30);
        final Spinner sick31 = (Spinner) findViewById(R.id.sick31);
        final Spinner sick32 = (Spinner) findViewById(R.id.sick32);
        final Spinner sick33 = (Spinner) findViewById(R.id.sick33);
        final Spinner sick34 = (Spinner) findViewById(R.id.sick34);
        final Spinner sick35 = (Spinner) findViewById(R.id.sick35);
        final Spinner sick36 = (Spinner) findViewById(R.id.sick36);
        final Spinner sick37 = (Spinner) findViewById(R.id.sick37);
        final Spinner sick38 = (Spinner) findViewById(R.id.sick38);
        final Spinner sick39 = (Spinner) findViewById(R.id.sick39);
        final Spinner sick40 = (Spinner) findViewById(R.id.sick40);
        final Spinner sick41 = (Spinner) findViewById(R.id.sick41);
        final Spinner sick42 = (Spinner) findViewById(R.id.sick42);
        final Spinner sick43 = (Spinner) findViewById(R.id.sick43);
        final Spinner sick44 = (Spinner) findViewById(R.id.sick44);
        final Spinner sick45 = (Spinner) findViewById(R.id.sick45);
        final Spinner sick46 = (Spinner) findViewById(R.id.sick46);

        final RadioGroup rdgsex = (RadioGroup) findViewById(R.id.rdgsex);
        final RadioButton rbMale = (RadioButton) findViewById(R.id.rbMale);
        final RadioButton rbFmale = (RadioButton) findViewById(R.id.rbFmale);
        lev.setOnItemSelectedListener(this);
        salary.setOnItemSelectedListener(this);
        smoke.setOnItemSelectedListener(this);
        drink.setOnItemSelectedListener(this);
        hur.setOnItemSelectedListener(this);
        overnight.setOnItemSelectedListener(this);
        sleep.setOnItemSelectedListener(this);
        exerice.setOnItemSelectedListener(this);
        candy.setOnItemSelectedListener(this);
        heart.setOnItemSelectedListener(this);
        blood.setOnItemSelectedListener(this);
        sick.setOnItemSelectedListener(this);
        stomache.setOnItemSelectedListener(this);
        sick3.setOnItemSelectedListener(this);
        stomachecan.setOnItemSelectedListener(this);
        sick4.setOnItemSelectedListener(this);
        overstomach.setOnItemSelectedListener(this);
        sick24.setOnItemSelectedListener(this);
        sick25.setOnItemSelectedListener(this);
        sick26.setOnItemSelectedListener(this);
        sick27.setOnItemSelectedListener(this);
        sick28.setOnItemSelectedListener(this);
        sick29.setOnItemSelectedListener(this);
        sick30.setOnItemSelectedListener(this);
        sick31.setOnItemSelectedListener(this);
        sick32.setOnItemSelectedListener(this);
        sick33.setOnItemSelectedListener(this);
        sick34.setOnItemSelectedListener(this);
        sick35.setOnItemSelectedListener(this);
        sick36.setOnItemSelectedListener(this);
        sick37.setOnItemSelectedListener(this);
        sick38.setOnItemSelectedListener(this);
        sick39.setOnItemSelectedListener(this);
        sick40.setOnItemSelectedListener(this);
        sick41.setOnItemSelectedListener(this);
        sick42.setOnItemSelectedListener(this);
        sick43.setOnItemSelectedListener(this);
        sick44.setOnItemSelectedListener(this);
        sick45.setOnItemSelectedListener(this);
        sick46.setOnItemSelectedListener(this);

        rdgsex.setOnCheckedChangeListener(this);

        //將欄位傳換成json
        JSONObject postData=new JSONObject();
        JSONObject postData2=new JSONObject();
      try{
          if (rdgsex.getCheckedRadioButtonId()==R.id.rbMale){
              //男性
              postData.put("gender",function.getValue("gender","男"));
          }
          else {
              //女性
              postData.put("gender",function.getValue("gender","女"));
          }
          postData.put("age",age.getText().toString());
          postData.put("birthday",birth.getText());
          postData.put("height",hight.getText().toString());
          postData.put("weight",wight.getText().toString());
          postData.put("Body_mass_index",bmi.getText().toString());
          postData.put("TSD_diagnosis_date",cancer1.getText());
          postData.put("heart_disease_diagnosis_date",cancer2.getText());
          postData.put("hypertension_diagnosis_date",cancer3.getText());
          postData.put("stroke_diagnosis_date",cancer4.getText());
          postData.put("gastric_ulcer_diagnosis_date",cancer5.getText());
          postData.put("duodenal_ulcer_diagnosis_date",cancer6.getText());
          postData.put("gastric_cancer_diagnosis_date",cancer7.getText());
          postData.put("colon_cancer_diagnosis_date",cancer8.getText());
          postData.put("gastroesophageal_reflux_diagnosis_date",cancer9.getText());

          postData.put("educatin_level",function.getValue("educatin_level",lev.getSelectedItem().toString()));
          postData.put("income",function.getValue("income",salary.getSelectedItem().toString()));
          postData.put("smoking_behavior",function.getValue("smoking_behavior",smoke.getSelectedItem().toString()));
          postData.put("alcoholism",function.getValue("alcoholism",drink.getSelectedItem().toString()));
          postData.put("sleep_hours",function.getValue("sleep_hours",hur.getSelectedItem().toString()));
          postData.put("stay_overnight",function.getValue("stay_overnight",overnight.getSelectedItem().toString()));
          postData.put("taking_nap",function.getValue("taking_nap",sleep.getSelectedItem().toString()));
          postData.put("exercise_frequency",function.getValue("exercise_frequency",exerice.getSelectedItem().toString()));
          postData.put("T2D",function.getValue("yesNo",candy.getSelectedItem().toString()));
          postData.put("heart_disease",function.getValue("yesNo",heart.getSelectedItem().toString()));
          postData.put("hypertention",function.getValue("yesNo",blood.getSelectedItem().toString()));
          postData.put("stroke",function.getValue("yesNo",sick.getSelectedItem().toString()));
          postData.put("gastric_ulcer",function.getValue("yesNo",stomache.getSelectedItem().toString()));
          //duodenal_ulcer sick4.getSelectedItem()有問題
          postData.put("duodenal_ulcer",function.getValue("yesNo",sick3.getSelectedItem().toString()));
          postData.put("gastric_cancer",function.getValue("yesNo",stomachecan.getSelectedItem().toString()));
          postData.put("colon_cancer",function.getValue("yesNo",sick4.getSelectedItem().toString()));
          postData.put("gastroesophageal_reflux",function.getValue("yesNo",overstomach.getSelectedItem().toString()));
          postData.put("using_antibiotics",function.getValue("yesNo",sick24.getSelectedItem().toString()));
          postData.put("Birth_location",function.getValue("Birth_location",sick25.getSelectedItem().toString()));
          postData.put("family_stomach_ulcer",function.getValue("yesNoUnknow",sick26.getSelectedItem().toString()));
          postData.put("family_duodenal_ulcer",function.getValue("yesNoUnknow",sick27.getSelectedItem().toString()));
          //yesNoUnknow  food
          postData.put("family_gastric_ulcer",function.getValue("yesNoUnknow",sick28.getSelectedItem().toString()));
          postData.put("family_colorectal_ulcer",function.getValue("yesNoUnknow",sick29.getSelectedItem().toString()));
          postData.put("family_gastroesophageal_reflux",function.getValue("yesNoUnknow",sick30.getSelectedItem().toString()));
          postData.put("other_gastrointestinal_disease",function.getValue("yesNoUnknow",sick31.getSelectedItem().toString()));

          postData2.put("Fq_grains",function.getValue("food",sick32.getSelectedItem().toString()));
          postData2.put("Fq_rice",function.getValue("food",sick33.getSelectedItem().toString()));
          postData2.put("Fq_noodle",function.getValue("food",sick34.getSelectedItem().toString()));
          postData2.put("Fq_milk",function.getValue("food",sick35.getSelectedItem().toString()));
          postData2.put("Fq_vegetables",function.getValue("food",sick36.getSelectedItem().toString()));
          postData2.put("Fq_nuts",function.getValue("food",sick37.getSelectedItem().toString()));
          postData2.put("Fq_fruits",function.getValue("food",sick38.getSelectedItem().toString()));
          postData2.put("Fq_seafood",function.getValue("food",sick39.getSelectedItem().toString()));
          postData2.put("Fq_meat",function.getValue("food",sick40.getSelectedItem().toString()));
          postData2.put("Fq_viscera",function.getValue("food",sick41.getSelectedItem().toString()));
          postData2.put("Fq_drinks",function.getValue("food",sick42.getSelectedItem().toString()));
          postData2.put("Fq_coffee",function.getValue("food",sick43.getSelectedItem().toString()));
          postData2.put("Fq_tea",function.getValue("food",sick44.getSelectedItem().toString()));
          postData2.put("Fq_dessert",function.getValue("food",sick45.getSelectedItem().toString()));
          postData2.put("Fq_alcohol",function.getValue("food",sick46.getSelectedItem().toString()));

      }catch (Exception e){
           e.getMessage();
      }

        SharedPreferences SharedPreferences = getSharedPreferences("question",MODE_PRIVATE);
        SharedPreferences SharedPreferences2 = getSharedPreferences("question2",MODE_PRIVATE);
        //編輯文件
        SharedPreferences.Editor editor = SharedPreferences.edit();
        SharedPreferences.Editor editor2 = SharedPreferences2.edit();
        //將json檔案存入字串
        String a = postData.toString();
        String b = postData2.toString();
        //存入sharepreference
        editor.putString(result,a);
        editor2.putString(result2,b);

        editor.commit();
        editor2.commit();
        Toast.makeText(this, "送出成功 !", Toast.LENGTH_SHORT).show();
        new question1().execute();
        new question2().execute();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClick(View v){
        json();
    }

    //api 傳值
    public class question1 extends AsyncTask<String,Void,String> {
        SharedPreferences SharedPreferences1 = getSharedPreferences("question",MODE_PRIVATE);

        String Json1 =  SharedPreferences1.getString(result,"");

        JSONObject jsonString = new JSONObject();

        protected String responsetitle= "";
        protected String responsestring= "";

        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //新增一個Calendar,並且指定時間
        Calendar calendar = Calendar.getInstance();

        Date tdt = calendar.getTime();//取得加減過後的Date

        //依照設定格式取得字串
        String time = sdf.format(tdt);

        // SQL lite query
        Cursor cur1 = db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            if (cur1.getCount()>0){
                cur1.moveToFirst();
                try {
                    JSONObject tempJsonArray =new JSONObject(Json1);
                    jsonString.put("protocolId",1000);
                    jsonString.put("subjectId",cur1.getString(1));
                    jsonString.put("formId",1000);
                    jsonString.put("visit",time);
                    jsonString.put("datarecord",tempJsonArray)  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SSLsetting();
            try {
                URL url = new URL("https://tlbinfo.cims.tw:8443/csis/createFormRecord.do?token=Fg7oI5I814N9G0N9omcGeboBj3kB");
//                URL url = new URL("https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-type","application/json");
                System.out.println("Now json: "+jsonString);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString.toString());
                writer.flush();
                writer.close();
                InputStream is;
                try
                {
                    is = connection.getInputStream();
                }
                catch(IOException exception)
                {
                    is = connection.getErrorStream();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Log.v("Header",connection.getHeaderFields().toString());
                responsetitle = String.valueOf(connection.getResponseCode())+ " " + connection.getResponseMessage();
                responsestring = response.toString();

                in.close();
                os.close();
                is.close();

                Log.v("Status", responsetitle);
                Log.v("Content",responsestring);

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("IOException","ERROR");
            }

            return responsestring;
        }
        @Override
        protected void onPostExecute(String s) {
            String errorCode = "";
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(s);
                errorCode = jsonObject.getString("Error Code");
                message = jsonObject.getString("Message");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            if ("SUCCESS".equals(errorCode)){
                Log.e("questionnaire1/Back",s);
            }else if("005".equals(errorCode)){
                Log.e("questionnaire1/Back","JSON error./t"+s);
            }else if("007".equals(errorCode)){
                Log.e("questionnaire1/Back","Invalid protocol./t"+s);
            }else if("010".equals(errorCode)){
                Log.e("questionnaire1/Back","Invalid form./t"+s);
            }else if("011".equals(errorCode)){
                Log.e("questionnaire1/Back","Invalid date format./t"+s);
            }else if("012".equals(errorCode)){
                Log.e("questionnaire1/Back","Patient not found./t"+s);
            }else{
                Log.e("questionnaire1/Back",s);
            }
            Log.v("Back",s);
        }
    }

    public class question2 extends AsyncTask<String,Void,String> {
        SharedPreferences SharedPreferences2 = getSharedPreferences("question2",MODE_PRIVATE);

        String Json2 =  SharedPreferences2.getString(result2,"");

        JSONObject jsonString = new JSONObject();

        protected String responsetitle= "";
        protected String responsestring= "";

        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //新增一個Calendar,並且指定時間
        Calendar calendar = Calendar.getInstance();

        Date tdt=calendar.getTime();//取得加減過後的Date

        //依照設定格式取得字串
        String time=sdf.format(tdt);

        // SQL lite query
        Cursor cur1 =db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);

        @Override
        protected String doInBackground(String... strings) {
            if (cur1.getCount()>0){
                cur1.moveToFirst();
                try {
                    JSONObject tempJsonArray =new JSONObject(Json2);
                    jsonString.put("protocolId",1000);
                    jsonString.put("subjectId",cur1.getString(1));
                    jsonString.put("formId",1001);
                    jsonString.put("visit",time);
                    jsonString.put("datarecord",tempJsonArray)  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SSLsetting();
            try {
                URL url = new URL("https://tlbinfo.cims.tw:8443/csis/createFormRecord.do?token=Fg7oI5I814N9G0N9omcGeboBj3kB");
//                URL url = new URL("https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-type","application/json");
                System.out.println("Now json: "+jsonString);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonString.toString());
                writer.flush();
                writer.close();
                InputStream is;
                try
                {
                    is = connection.getInputStream();
                }
                catch(IOException exception)
                {
                    is = connection.getErrorStream();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Log.v("Header",connection.getHeaderFields().toString());
                responsetitle = String.valueOf(connection.getResponseCode())+ " " + connection.getResponseMessage();
                responsestring = response.toString();

                in.close();
                os.close();
                is.close();

                Log.v("Status", responsetitle);
                Log.v("Content",responsestring);

            } catch (IOException e) {
                e.printStackTrace();
                Log.v("IOException","ERROR");
            }

            return responsestring;
        }
        @Override
        protected void onPostExecute(String s) {
            String errorCode = "";
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(s);
                errorCode = jsonObject.getString("Error Code");
                message = jsonObject.getString("Message");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            if ("SUCCESS".equals(errorCode)){
                Log.e("questionnaire2/Back",s);
            }else if("005".equals(errorCode)){
                Log.e("questionnaire2/Back","JSON error./t"+s);
            }else if("007".equals(errorCode)){
                Log.e("questionnaire2/Back","Invalid protocol./t"+s);
            }else if("010".equals(errorCode)){
                Log.e("questionnaire2/Back","Invalid form./t"+s);
            }else if("011".equals(errorCode)){
                Log.e("questionnaire2/Back","Invalid date format./t"+s);
            }else if("012".equals(errorCode)){
                Log.e("questionnaire2/Back","Patient not found./t"+s);
            }else{
                Log.e("questionnaire2/Back",s);
            }
            Log.v("Back",s);
        }
    }
    //SSL 設定 (忽略所有的認證)
    private void SSLsetting() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}
