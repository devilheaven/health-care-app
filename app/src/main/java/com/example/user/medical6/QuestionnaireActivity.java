package com.example.user.medical6;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.*;

import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,AdapterView.OnItemSelectedListener {
    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result = "result";
    // data base 變數宣告
    dataBase DH = null;
    SQLiteDatabase db;

    HashMap<Integer,String> objectMap = new HashMap<Integer, String>();

    appFunction function = new appFunction();
    autoCreateForm createForm = new autoCreateForm();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        objectMap.put(1000,createForm.createRadioButton(this, (LinearLayout) findViewById(R.id.form), "1000", "gender", "性別：","0-男,1-女" , ","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","birthday","生日：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","age","年齡：","22","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","height","身高：","170(cm)","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","weight","身高：","70(kg)","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","Body_mass_index","BMI：","20","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","educatin_level","教育程度：","未受教育,小學,國(初)中,高中(職),大學(專科),研究所以上",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","income","家庭收入：","困苦,尚可,小康,富裕",","));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","smoking_behavior","您有抽菸習慣嗎?：","從未抽菸,已戒菸,目前有抽菸習慣",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","alcoholism","您有飲酒習慣嗎?：","從未飲酒,已戒酒,偶爾,目前有飲酒習慣",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","sleep_hours","您平均每天睡幾個小時? ：","不到 6 小時,6~6.9 小時,7~7.9 小時,8 小時以上",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","stay_overnight","您是否常熬夜?：","很少(每週≤1次),偶爾(每週2~4次),經常(每週≥5次)",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","taking_nap","您是否有午睡的習慣?：","很少(每週≤1次),偶爾(每週2~4次),經常(每週≥5次)",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","exercise_frequency","您是否有運動的習慣?：","每月≤1次,每月2~3次,每週1~2次,每週3~4次,每週≥5次",","));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","T2D","第二型糖尿病：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","TSD_diagnosis_date","第二型糖尿病確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","heart_disease","心臟病：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","heart_disease_diagnosis_date","心臟病確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","hypertention","高血壓：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","hypertension_diagnosis_date","高血壓確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","stroke","中風：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","stroke_diagnosis_date","中風確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","gastric_ulcer","胃潰瘍：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","gastric_ulcer_diagnosis_date","胃潰瘍確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","duodenal_ulcer","十二指腸潰瘍：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","duodenal_ulcer_diagnosis_date","十二指腸潰瘍確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","gastric_cancer","胃癌：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","gastric_cancer_diagnosis_date","胃癌確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","colon_cancer","大腸癌：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","colon_cancer_diagnosis_date","大腸癌確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","gastroesophageal_reflux","胃食道逆流：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.form),"1000","gastroesophageal_reflux_diagnosis_date","胃食道逆流確診日期：","01/31/1999(MM/DD/YY)","date"));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","Birth_location","本籍：","臺灣閩南人,臺灣客家人,臺灣原住民,中國各省份,其他",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","family_stomach_ulcer","父、母、兄弟、姊妹、兒子、女兒有無胃潰瘍：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","family_duodenal_ulcer","父、母、兄弟、姊妹、兒子、女兒有無十二指腸潰瘍：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","family_gastric_ulcer","父、母、兄弟、姊妹、兒子、女兒有無胃癌：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","family_colorectal_ulcer","父、母、兄弟、姊妹、兒子、女兒有無大腸癌：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","family_gastroesophageal_reflux","父、母、兄弟、姊妹、兒子、女兒有無胃食道逆流：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.form),"1000","other_gastrointestinal_disease","父、母、兄弟、姊妹、兒子、女兒罹患其他消化道相關疾病：","有,無,不詳",","));

        Log.e("question/hash", String.valueOf(objectMap));

//        final EditText hight=(EditText)findViewById(R.id.hight);
//        final EditText wight=(EditText)findViewById(R.id.wight);
//        final EditText bmi=(EditText)findViewById(R.id.bmi);
//
//        final EditText dataEdit1 = (EditText) findViewById(R.id.birth);
//        dataEdit1.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit1);
//            }
//        });
//        final EditText dataEdit2 = (EditText) findViewById(R.id.cancer1);
//        dataEdit2.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit2);
//            }
//        });
//        final EditText dataEdit3 = (EditText) findViewById(R.id.cancer2);
//        dataEdit3.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit3);
//            }
//        });
//        final EditText dataEdit4 = (EditText) findViewById(R.id.cancer3);
//        dataEdit4.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit4);
//            }
//        });
//        final EditText dataEdit5 = (EditText) findViewById(R.id.cancer4);
//        dataEdit5.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit5);
//            }
//        });
//        final EditText dataEdit6 = (EditText) findViewById(R.id.cancer5);
//        dataEdit6.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit6);
//            }
//        });
//        final EditText dataEdit7 = (EditText) findViewById(R.id.cancer6);
//        dataEdit7.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit7);
//            }
//        });
//        final EditText dataEdit8 = (EditText) findViewById(R.id.cancer7);
//        dataEdit8.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit8);
//            }
//        });
//        final EditText dataEdit9 = (EditText) findViewById(R.id.cancer8);
//        dataEdit9.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit9);
//            }
//        });
//        final EditText dataEdit10 = (EditText) findViewById(R.id.cancer9);
//        dataEdit10.setOnClickListener(new View.OnClickListener() {
//            //            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View v) {
//                function.getDate(QuestionnaireActivity.this, dataEdit10);
//            }
//        });
//
        final Button btngo = (Button) findViewById(R.id.btngo);
        btngo.setOnClickListener(this);
//
//        //讀取資料
//        DH=new dataBase(this);
//        DH.close();
//        db =DH.getReadableDatabase();
//
//        Cursor cur1 =db.rawQuery(" SELECT *  FROM  customer ORDER BY id DESC " ,null);
//        if (cur1.getCount()>0){
//            cur1.moveToFirst();
//            hight.setText(cur1.getString(4));
//        }else{
//            hight.setText("0");
//        }
//        cur1 =db.rawQuery(" SELECT *  FROM  examine ORDER BY _id DESC " ,null);
//        if (cur1.getCount()>0){
//            cur1.moveToFirst();
//            wight.setText(cur1.getString(2));
//        }else{
//            wight.setText("0");
//        }
//        Float BMI = Float.valueOf(0);
//        if(Float.parseFloat(wight.getText().toString()) != 0 && Float.parseFloat(hight.getText().toString()) != 0){
//            BMI = Float.parseFloat(wight.getText().toString())/Float.parseFloat(hight.getText().toString())/Float.parseFloat(hight.getText().toString())*10000;
//        }else{
//            BMI = Float.valueOf(0);
//        }
//
//        bmi.setText(BMI.toString());
    }
//
    public  void  json(){
//        //宣告物件 將欄位轉成json
//        final EditText age = (EditText)findViewById(R.id.age);
//        final EditText hight = (EditText)findViewById(R.id.hight);
//        final EditText wight = (EditText)findViewById(R.id.wight);
//        final EditText bmi = (EditText)findViewById(R.id.bmi);
//        final EditText birth = (EditText) findViewById(R.id.birth);
//        final EditText cancer1 = (EditText) findViewById(R.id.cancer1);
//        final EditText cancer2 = (EditText)findViewById(R.id.cancer2);
//        final EditText cancer3 = (EditText)findViewById(R.id.cancer3);
//        final EditText cancer4 = (EditText) findViewById(R.id.cancer4);
//        final EditText cancer5 = (EditText)findViewById(R.id.cancer5);
//        final EditText cancer6 = (EditText)findViewById(R.id.cancer6);
//        final EditText cancer7 = (EditText) findViewById(R.id.cancer7);
//        final EditText cancer8 = (EditText)findViewById(R.id.cancer8);
//        final EditText cancer9 = (EditText) findViewById(R.id.cancer9);
//
//        final Spinner lev = (Spinner) findViewById(R.id.level);
//        final Spinner salary = (Spinner) findViewById(R.id.salary);
//        final Spinner smoke = (Spinner) findViewById(R.id.smoke);
//        final Spinner drink = (Spinner) findViewById(R.id.drink);
//        final Spinner hur = (Spinner) findViewById(R.id.hur);
//        final Spinner overnight = (Spinner) findViewById(R.id.overnight);
//        final Spinner sleep = (Spinner) findViewById(R.id.sleep);
//        final Spinner exerice = (Spinner) findViewById(R.id.exerice);
//        final Spinner candy = (Spinner) findViewById(R.id.candy);
//        final Spinner heart = (Spinner) findViewById(R.id.heart);
//        final Spinner blood = (Spinner) findViewById(R.id.blood);
//        final Spinner sick = (Spinner) findViewById(R.id.sick);
//        final Spinner stomache = (Spinner) findViewById(R.id.stomache);
//        final Spinner sick3 = (Spinner) findViewById(R.id.sick3);
//        final Spinner stomachecan = (Spinner) findViewById(R.id.stomachecan);
//        final Spinner sick4 = (Spinner) findViewById(R.id.sick4);
//        final Spinner overstomach = (Spinner) findViewById(R.id.overstomach);
//        final Spinner sick24 = (Spinner) findViewById(R.id.sick24);
//        final Spinner sick25 = (Spinner) findViewById(R.id.sick25);
//        final Spinner sick26 = (Spinner) findViewById(R.id.sick26);
//        final Spinner sick27 = (Spinner) findViewById(R.id.sick27);
//        final Spinner sick28 = (Spinner) findViewById(R.id.sick28);
//        final Spinner sick29 = (Spinner) findViewById(R.id.sick29);
//        final Spinner sick30 = (Spinner) findViewById(R.id.sick30);
//        final Spinner sick31 = (Spinner) findViewById(R.id.sick31);
//        final Spinner sick32 = (Spinner) findViewById(R.id.sick32);
//        final Spinner sick33 = (Spinner) findViewById(R.id.sick33);
//        final Spinner sick34 = (Spinner) findViewById(R.id.sick34);
//        final Spinner sick35 = (Spinner) findViewById(R.id.sick35);
//        final Spinner sick36 = (Spinner) findViewById(R.id.sick36);
//        final Spinner sick37 = (Spinner) findViewById(R.id.sick37);
//        final Spinner sick38 = (Spinner) findViewById(R.id.sick38);
//        final Spinner sick39 = (Spinner) findViewById(R.id.sick39);
//        final Spinner sick40 = (Spinner) findViewById(R.id.sick40);
//        final Spinner sick41 = (Spinner) findViewById(R.id.sick41);
//        final Spinner sick42 = (Spinner) findViewById(R.id.sick42);
//        final Spinner sick43 = (Spinner) findViewById(R.id.sick43);
//        final Spinner sick44 = (Spinner) findViewById(R.id.sick44);
//        final Spinner sick45 = (Spinner) findViewById(R.id.sick45);
//        final Spinner sick46 = (Spinner) findViewById(R.id.sick46);
//
//        final RadioGroup rdgsex = (RadioGroup) findViewById(R.id.rdgsex);
//        final RadioButton rbMale = (RadioButton) findViewById(R.id.rbMale);
//        final RadioButton rbFmale = (RadioButton) findViewById(R.id.rbFmale);
//        lev.setOnItemSelectedListener(this);
//        salary.setOnItemSelectedListener(this);
//        smoke.setOnItemSelectedListener(this);
//        drink.setOnItemSelectedListener(this);
//        hur.setOnItemSelectedListener(this);
//        overnight.setOnItemSelectedListener(this);
//        sleep.setOnItemSelectedListener(this);
//        exerice.setOnItemSelectedListener(this);
//        candy.setOnItemSelectedListener(this);
//        heart.setOnItemSelectedListener(this);
//        blood.setOnItemSelectedListener(this);
//        sick.setOnItemSelectedListener(this);
//        stomache.setOnItemSelectedListener(this);
//        sick3.setOnItemSelectedListener(this);
//        stomachecan.setOnItemSelectedListener(this);
//        sick4.setOnItemSelectedListener(this);
//        overstomach.setOnItemSelectedListener(this);
//        sick24.setOnItemSelectedListener(this);
//        sick25.setOnItemSelectedListener(this);
//        sick26.setOnItemSelectedListener(this);
//        sick27.setOnItemSelectedListener(this);
//        sick28.setOnItemSelectedListener(this);
//        sick29.setOnItemSelectedListener(this);
//        sick30.setOnItemSelectedListener(this);
//        sick31.setOnItemSelectedListener(this);
//        sick32.setOnItemSelectedListener(this);
//        sick33.setOnItemSelectedListener(this);
//        sick34.setOnItemSelectedListener(this);
//        sick35.setOnItemSelectedListener(this);
//        sick36.setOnItemSelectedListener(this);
//        sick37.setOnItemSelectedListener(this);
//        sick38.setOnItemSelectedListener(this);
//        sick39.setOnItemSelectedListener(this);
//        sick40.setOnItemSelectedListener(this);
//        sick41.setOnItemSelectedListener(this);
//        sick42.setOnItemSelectedListener(this);
//        sick43.setOnItemSelectedListener(this);
//        sick44.setOnItemSelectedListener(this);
//        sick45.setOnItemSelectedListener(this);
//        sick46.setOnItemSelectedListener(this);
//
//        rdgsex.setOnCheckedChangeListener(this);
//
        //將欄位傳換成json
        JSONObject postData = function.getObjectValue(this,objectMap.get(1000),",");
        JSONObject postData2=new JSONObject();
//      try{
//          if (rdgsex.getCheckedRadioButtonId()==R.id.rbMale){
//              //男性
//              postData.put("gender",function.getValue("gender","男"));
//          }
//          else {
//              //女性
//              postData.put("gender",function.getValue("gender","女"));
//          }
//          postData.put("age",age.getText().toString());
//          postData.put("birthday",birth.getText());
//          postData.put("height",hight.getText().toString());
//          postData.put("weight",wight.getText().toString());
//          postData.put("Body_mass_index",bmi.getText().toString());
//          postData.put("TSD_diagnosis_date",cancer1.getText());
//          postData.put("heart_disease_diagnosis_date",cancer2.getText());
//          postData.put("hypertension_diagnosis_date",cancer3.getText());
//          postData.put("stroke_diagnosis_date",cancer4.getText());
//          postData.put("gastric_ulcer_diagnosis_date",cancer5.getText());
//          postData.put("duodenal_ulcer_diagnosis_date",cancer6.getText());
//          postData.put("gastric_cancer_diagnosis_date",cancer7.getText());
//          postData.put("colon_cancer_diagnosis_date",cancer8.getText());
//          postData.put("gastroesophageal_reflux_diagnosis_date",cancer9.getText());
//
//          postData.put("educatin_level",function.getValue("educatin_level",lev.getSelectedItem().toString()));
//          postData.put("income",function.getValue("income",salary.getSelectedItem().toString()));
//          postData.put("smoking_behavior",function.getValue("smoking_behavior",smoke.getSelectedItem().toString()));
//          postData.put("alcoholism",function.getValue("alcoholism",drink.getSelectedItem().toString()));
//          postData.put("sleep_hours",function.getValue("sleep_hours",hur.getSelectedItem().toString()));
//          postData.put("stay_overnight",function.getValue("stay_overnight",overnight.getSelectedItem().toString()));
//          postData.put("taking_nap",function.getValue("taking_nap",sleep.getSelectedItem().toString()));
//          postData.put("exercise_frequency",function.getValue("exercise_frequency",exerice.getSelectedItem().toString()));
//          postData.put("T2D",function.getValue("yesNo",candy.getSelectedItem().toString()));
//          postData.put("heart_disease",function.getValue("yesNo",heart.getSelectedItem().toString()));
//          postData.put("hypertention",function.getValue("yesNo",blood.getSelectedItem().toString()));
//          postData.put("stroke",function.getValue("yesNo",sick.getSelectedItem().toString()));
//          postData.put("gastric_ulcer",function.getValue("yesNo",stomache.getSelectedItem().toString()));
//          postData.put("duodenal_ulcer",function.getValue("yesNo",sick3.getSelectedItem().toString()));
//          postData.put("gastric_cancer",function.getValue("yesNo",stomachecan.getSelectedItem().toString()));
//          postData.put("colon_cancer",function.getValue("yesNo",sick4.getSelectedItem().toString()));
//          postData.put("gastroesophageal_reflux",function.getValue("yesNo",overstomach.getSelectedItem().toString()));
//          postData.put("using_antibiotics",function.getValue("yesNo",sick24.getSelectedItem().toString()));
//          postData.put("Birth_location",function.getValue("Birth_location",sick25.getSelectedItem().toString()));
//          postData.put("family_stomach_ulcer",function.getValue("yesNoUnknow",sick26.getSelectedItem().toString()));
//          postData.put("family_duodenal_ulcer",function.getValue("yesNoUnknow",sick27.getSelectedItem().toString()));
//          //yesNoUnknow  food
//          postData.put("family_gastric_ulcer",function.getValue("yesNoUnknow",sick28.getSelectedItem().toString()));
//          postData.put("family_colorectal_ulcer",function.getValue("yesNoUnknow",sick29.getSelectedItem().toString()));
//          postData.put("family_gastroesophageal_reflux",function.getValue("yesNoUnknow",sick30.getSelectedItem().toString()));
//          postData.put("other_gastrointestinal_disease",function.getValue("yesNoUnknow",sick31.getSelectedItem().toString()));
//
//          postData2.put("Fq_grains",function.getValue("food",sick32.getSelectedItem().toString()));
//          postData2.put("Fq_rice",function.getValue("food",sick33.getSelectedItem().toString()));
//          postData2.put("Fq_noodle",function.getValue("food",sick34.getSelectedItem().toString()));
//          postData2.put("Fq_milk",function.getValue("food",sick35.getSelectedItem().toString()));
//          postData2.put("Fq_vegetables",function.getValue("food",sick36.getSelectedItem().toString()));
//          postData2.put("Fq_nuts",function.getValue("food",sick37.getSelectedItem().toString()));
//          postData2.put("Fq_fruits",function.getValue("food",sick38.getSelectedItem().toString()));
//          postData2.put("Fq_seafood",function.getValue("food",sick39.getSelectedItem().toString()));
//          postData2.put("Fq_meat",function.getValue("food",sick40.getSelectedItem().toString()));
//          postData2.put("Fq_viscera",function.getValue("food",sick41.getSelectedItem().toString()));
//          postData2.put("Fq_drinks",function.getValue("food",sick42.getSelectedItem().toString()));
//          postData2.put("Fq_coffee",function.getValue("food",sick43.getSelectedItem().toString()));
//          postData2.put("Fq_tea",function.getValue("food",sick44.getSelectedItem().toString()));
//          postData2.put("Fq_dessert",function.getValue("food",sick45.getSelectedItem().toString()));
//          postData2.put("Fq_alcohol",function.getValue("food",sick46.getSelectedItem().toString()));
//
//      }catch (Exception e){
//           e.getMessage();
//      }

        SharedPreferences SharedPreferences = getSharedPreferences("question1",MODE_PRIVATE);
        SharedPreferences SharedPreferences2 = getSharedPreferences("question2",MODE_PRIVATE);
        //編輯文件
        SharedPreferences.Editor editor = SharedPreferences.edit();
        SharedPreferences.Editor editor2 = SharedPreferences2.edit();
        //將json檔案存入字串
        String a = postData.toString();
        String b = postData2.toString();
        //存入sharepreference
        editor.putString(result,a);
        editor2.putString(result,b);

        editor.commit();
        editor2.commit();
        Toast.makeText(this, "送出成功 !", Toast.LENGTH_SHORT).show();

        APIFunction API = new APIFunction();
        String urlAddress = "https://tlbinfo.cims.tw:8443/csis/createFormRecord.do?token=Fg7oI5I814N9G0N9omcGeboBj3kB";
//        String urlAddress = "https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI";

//        API.questionAPIconnect(this, urlAddress, getSharedPreferences("question1",MODE_PRIVATE),1000, 1000);
//        API.questionAPIconnect(this, urlAddress, getSharedPreferences("question2",MODE_PRIVATE),1000, 1001);
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
}
