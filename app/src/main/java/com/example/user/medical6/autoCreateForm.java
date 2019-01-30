package com.example.user.medical6;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

class autoCreateForm {
    static int tempID = 5000;
    appFunction function = new appFunction();
    String createSpinner(Context context, LinearLayout targetLocation, String formId, String dataPointName, String labelName, String optionName, String spiltSymbol){
        LinearLayout groupLayout = new LinearLayout(context);
        Spinner SpTemp = new Spinner(context);
        TextView label = new TextView(context);
        HashMap<Integer,String> optionsMap = new HashMap<Integer, String>();


        String[] tempOptionName = optionName.split(spiltSymbol);

        for (int i = 0; i < tempOptionName.length; i++)
        {
            optionsMap.put(i,tempOptionName[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_template, tempOptionName);
        adapter.setDropDownViewResource(R.layout.spinner_template);

        SpTemp.setAdapter(adapter);

        groupLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        label.setText(labelName);
        SpTemp.setLayoutParams(params);
        SpTemp.setId(tempID);
        tempID++;


        groupLayout.addView(label);
        groupLayout.addView(SpTemp);

        targetLocation.addView(groupLayout);

        return formId + "-" + dataPointName + "-" + "spinner" + "-" + tempID;
//        String name = SpTemp.getSelectedItem().toString();
//        String id = optionsMap.get(SpTemp.getSelectedItemPosition());
    }

    String createRadioButton(Context context, LinearLayout targetLocation, String formId, String dataPointName, String labelName,String optionName, String spiltSymbol){
        LinearLayout groupLayout = new LinearLayout(context);
        LinearLayout tempLayout = new LinearLayout(context);
        TextView label = new TextView(context);
        RadioGroup btnGroup = new RadioGroup(context);

        String[] tempOptionName = optionName.split(spiltSymbol);

        groupLayout.setOrientation(LinearLayout.VERTICAL);
        tempLayout.setOrientation(LinearLayout.VERTICAL);
        btnGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0 ; i < tempOptionName.length ; i++){
            RadioButton btnTemp = new RadioButton(context);
            btnTemp.setText(tempOptionName[i]);
            btnGroup.addView(btnTemp);
        }

        label.setText(labelName);
        btnGroup.setId(tempID);
        tempID++;

        tempLayout.addView(btnGroup);
        groupLayout.addView(label);
        groupLayout.addView(tempLayout);

        targetLocation.addView(groupLayout);

        return formId + "-" + dataPointName + "-" + "radioGroup" + "-" + tempID;
    }

    String createEditText(final Context context, LinearLayout targetLocation, String formId, String dataPointName, String labelName, String hint,String Model){
        LinearLayout groupLayout = new LinearLayout(context);
        final EditText etTemp =new EditText(context);
        TextView label = new TextView(context);

        groupLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        label.setText(labelName);
        etTemp.setLayoutParams(params);
        etTemp.setHint(hint);
        etTemp.setId(tempID);
        tempID++;


        switch (Model){
            case "date":
                etTemp.setOnClickListener(new View.OnClickListener() {
                    //            @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        function.getDate(context, etTemp);
                    }
                });
                break;
            case "text":
                break;
        }

        groupLayout.addView(label);
        groupLayout.addView(etTemp);

        targetLocation.addView(groupLayout);
        return formId + "-" + dataPointName + "-" + "editText" + "-" + tempID;
    }
}

