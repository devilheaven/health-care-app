package com.example.user.medical6;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Spinner SelectMonth = (Spinner)findViewById(R.id.spinBIMonth);
        ArrayAdapter<CharSequence> Month = ArrayAdapter.createFromResource(HistoryActivity.this,
                R.array.month,
                android.R.layout.simple_spinner_dropdown_item);
        SelectMonth.setAdapter(Month);

        Spinner SelectState = (Spinner)findViewById(R.id.spinBIState);
        ArrayAdapter<CharSequence> State = ArrayAdapter.createFromResource(HistoryActivity.this,
                R.array.BIState,
                android.R.layout.simple_spinner_dropdown_item);
        SelectState.setAdapter(State);
    }
}