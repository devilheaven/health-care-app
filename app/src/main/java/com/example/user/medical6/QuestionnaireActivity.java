package com.example.user.medical6;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.json.*;

import java.lang.reflect.Array;
import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity{
    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result = "result";

    HashMap<Integer,String> objectMap = new HashMap<Integer, String>();

    appFunction function = new appFunction();
    autoCreateForm createForm = new autoCreateForm();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Log.e("question/Back",createForm.readFromFile(this,"form_1000.txt"));

        objectMap.put(1000,createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList), "1000", "gender", "性別：","男,女" , ","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","birthday","生日：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","age","年齡：","22","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","height","身高：","170(cm)","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","weight","身高：","70(kg)","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","Body_mass_index","BMI：","20","text"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","educatin_level","教育程度：","未受教育,小學,國(初)中,高中(職),大學(專科),研究所以上",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","income","家庭收入：","困苦,尚可,小康,富裕",","));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","smoking_behavior","您有抽菸習慣嗎?：","從未抽菸,已戒菸,目前有抽菸習慣",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","alcoholism","您有飲酒習慣嗎?：","從未飲酒,已戒酒,偶爾,目前有飲酒習慣",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","sleep_hours","您平均每天睡幾個小時? ：","不到 6 小時,6~6.9 小時,7~7.9 小時,8 小時以上",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","stay_overnight","您是否常熬夜?：","很少(每週≤1次),偶爾(每週2~4次),經常(每週≥5次)",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","taking_nap","您是否有午睡的習慣?：","很少(每週≤1次),偶爾(每週2~4次),經常(每週≥5次)",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","exercise_frequency","您是否有運動的習慣?：","每月≤1次,每月2~3次,每週1~2次,每週3~4次,每週≥5次",","));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","T2D","第二型糖尿病：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","TSD_diagnosis_date","第二型糖尿病確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","heart_disease","心臟病：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","heart_disease_diagnosis_date","心臟病確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","hypertention","高血壓：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","hypertension_diagnosis_date","高血壓確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","stroke","中風：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","stroke_diagnosis_date","中風確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","gastric_ulcer","胃潰瘍：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","gastric_ulcer_diagnosis_date","胃潰瘍確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","duodenal_ulcer","十二指腸潰瘍：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","duodenal_ulcer_diagnosis_date","十二指腸潰瘍確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","gastric_cancer","胃癌：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","gastric_cancer_diagnosis_date","胃癌確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","colon_cancer","大腸癌：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","colon_cancer_diagnosis_date","大腸癌確診日期：","01/31/1999(MM/DD/YY)","date"));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","gastroesophageal_reflux","胃食道逆流：","無,有",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createEditText(this,(LinearLayout) findViewById(R.id.formList),"1000","gastroesophageal_reflux_diagnosis_date","胃食道逆流確診日期：","01/31/1999(MM/DD/YY)","date"));

        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","Birth_location","本籍：","臺灣閩南人,臺灣客家人,臺灣原住民,中國各省份,其他",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","family_stomach_ulcer","父、母、兄弟、姊妹、兒子、女兒有無胃潰瘍：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","family_duodenal_ulcer","父、母、兄弟、姊妹、兒子、女兒有無十二指腸潰瘍：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","family_gastric_ulcer","父、母、兄弟、姊妹、兒子、女兒有無胃癌：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","family_colorectal_ulcer","父、母、兄弟、姊妹、兒子、女兒有無大腸癌：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","family_gastroesophageal_reflux","父、母、兄弟、姊妹、兒子、女兒有無胃食道逆流：","有,無,不詳",","));
        objectMap.put(1000,objectMap.get(1000) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1000","other_gastrointestinal_disease","父、母、兄弟、姊妹、兒子、女兒罹患其他消化道相關疾病：","有,無,不詳",","));

        objectMap.put(1001,createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList), "1001", "Fq_grains", "請問您吃乾式米飯類（如：糙米飯、紫米，五穀雜糧等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上" , ","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_rice","請問您吃米飯類（如：白飯、炒飯、壽司等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_noodle","請問您吃麵類（如：麵條、米粉、冬粉等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));

        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_milk","請問您吃奶類（如：鮮奶、優酪乳、起司、豆漿等）的頻率為?","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_vegetables","請問您吃蔬菜類（如：淺/深色蔬菜、海產植物類、醃漬蔬菜類）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_nuts","請問您吃堅果製品類（如：花生、腰果、核桃等）的頻率為？ ","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_fruits","請問您吃新鮮水果類的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_seafood","請問您吃海鮮類（含所有魚類、貝類）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_meat","請問您吃肉類（如：雞、猪、牛、絞肉等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_viscera","請問您吃內臟類（如：豬肝）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_drinks","請問您喝飲料類（如：碳酸飲料、運動飲料、奶等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_coffee","請問您喝咖啡類（如：即溶/自製咖啡、市售咖啡飲料、拿鐵等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_tea","請問您喝含茶葉成分飲料（如：紅/綠/烏龍茶、客家擂茶等）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_dessert","請問您吃甜食（如：冰淇淋、餅乾、蛋糕）的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));
        objectMap.put(1001,objectMap.get(1001) + "," + createForm.createSpinner(this,(LinearLayout) findViewById(R.id.formList),"1001","Fq_alcohol","請問您平常喝酒的頻率為？","都沒有吃此類食物,每週十次中有一至二次,每週十次中有三至五次,每週十次中有六至八次,每週十次中有八次以上",","));

//        Log.e("question/hash", String.valueOf(objectMap));
        final Button btngo = (Button) findViewById(R.id.btngo);
        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json();
            }
        });
    }
    public  void  json(){
        //將欄位傳換成json
        JSONObject postData = function.getObjectValue((LinearLayout) findViewById(R.id.formList),objectMap.get(1000),",");
        JSONObject postData2 = function.getObjectValue((LinearLayout) findViewById(R.id.formList),objectMap.get(1001),",");

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

        API.questionAPIconnect(this, urlAddress, getSharedPreferences("question1",MODE_PRIVATE),1000, 1000);
        API.questionAPIconnect(this, urlAddress, getSharedPreferences("question2",MODE_PRIVATE),1000, 1001);
    }
}
