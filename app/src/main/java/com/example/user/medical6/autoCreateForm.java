package com.example.user.medical6;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

class autoCreateForm {
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
        SpTemp.setTag(formId + "-" + dataPointName + "-" + "spinner");
        SpTemp.setLayoutParams(params);

        groupLayout.addView(label);
        groupLayout.addView(SpTemp);

        targetLocation.addView(groupLayout);

        return formId + "-" + dataPointName + "-" + "spinner";
    }

    String createEditText(final Context context, LinearLayout targetLocation, String formId, String dataPointName, String labelName, String hint,String Model){
        LinearLayout groupLayout = new LinearLayout(context);
        final EditText etTemp =new EditText(context);
        TextView label = new TextView(context);

        groupLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        label.setText(labelName);
        etTemp.setTag(formId + "-" + dataPointName + "-" + "editText");
        etTemp.setLayoutParams(params);
        etTemp.setHint(hint);

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
        return formId + "-" + dataPointName + "-" + "editText";
    }

    String readFromFile(Context context,String FileName) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(FileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}

