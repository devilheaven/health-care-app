package com.example.user.medical6;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.user.medical6.dataBase.*;
import static com.example.user.medical6.dataBase.subjectId;

public class appFunction extends AppCompatActivity {
    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

    // data base 變數宣告
    Cursor cur;
    ContentValues values = new ContentValues();
    SQLiteDatabase db = null;

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

        String value = "";
        switch (model){
            case "record_status":
                value = record_status  .get(key).toString();
                break;
        }
        return value;
    }

    JSONObject memberJson (int protocolId, String subjectId, String lastName, String guid){
        JSONObject jsonString = new JSONObject();
        try {
            jsonString.put("protocolId",protocolId);
            jsonString.put("subjectId",subjectId);
            jsonString.put("lastName",lastName);
            jsonString.put("guid",guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    JSONObject questionnariesJson (dataBase DH, int protocolId, Integer formId, String answer){
        db = DH.getReadableDatabase();
        //定義好時間字串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //新增一個Calendar,並且指定時間
        Calendar calendar = Calendar.getInstance();

        Date tdt = calendar.getTime();//取得加減過後的Date

        //依照設定格式取得字串
        String time = sdf.format(tdt);

        // SQL lite query
        Cursor cur1 = db.rawQuery(" SELECT " + subjectId + "  FROM  customer ORDER BY id DESC " ,null);

        // SQL lite query
        JSONObject jsonString = new JSONObject();
        if (cur1.getCount()>0){
            cur1.moveToFirst();
            try {
                JSONObject tempJsonArray =new JSONObject(answer);
                jsonString.put("protocolId",protocolId);
                jsonString.put("subjectId",cur1.getString(0));
                jsonString.put("formId",formId);
                jsonString.put("visit",time);
                jsonString.put("datarecord",tempJsonArray)  ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }

    JSONObject getObjectValue(LinearLayout targetLayout, String objectMap, String splitSymbol){
        JSONObject jsonValue = new JSONObject();
        String[] tempObjectArray = objectMap.split(splitSymbol);
        for (int i = 0; i < tempObjectArray.length; i++){
            String[] tempOptionName = tempObjectArray[i].split("-");
            switch (tempOptionName[2]){
                case "spinner":
                    Spinner tempSpinnerObject;
                    tempSpinnerObject = (Spinner) targetLayout.findViewWithTag(tempObjectArray[i]);
                    Integer id = tempSpinnerObject.getSelectedItemPosition();
                    try {
                        jsonValue.put(tempOptionName[1],id);
//                        Log.e("question/" + tempObjectArray[i] + "/catch value:", String.valueOf(id));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "radioGroup":
                    RadioGroup tempRadioGroupObject;
                    tempRadioGroupObject = (RadioGroup)targetLayout.findViewWithTag(tempObjectArray[i]);
                    RadioButton tempRadioButtonObject =findViewById(tempRadioGroupObject.getCheckedRadioButtonId());
                    String[] tempRadioSelected = tempRadioButtonObject.getText().toString().split("-");
                    try {
                        jsonValue.put(tempOptionName[1],tempRadioSelected[0]);
//                        Log.e("question/" + tempObjectArray[i] + "/catch value:", tempRadioSelected[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "editText":
                    EditText tempEditTextObject;
                    tempEditTextObject = (EditText)targetLayout.findViewWithTag(tempObjectArray[i]);
                    String value = tempEditTextObject.getText().toString();
                    try {
                        jsonValue.put(tempOptionName[1],value);
//                        Log.e("question/" + tempObjectArray[i] + "/catch value:", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return jsonValue;
    }
}
