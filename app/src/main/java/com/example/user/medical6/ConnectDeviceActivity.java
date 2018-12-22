package com.example.user.medical6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConnectDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);
        Spinner SelectDevice = (Spinner)findViewById(R.id.deviceSelection);
        ArrayAdapter<CharSequence> Device = ArrayAdapter.createFromResource(ConnectDeviceActivity.this,
                R.array.device,
                android.R.layout.simple_spinner_dropdown_item);
        SelectDevice.setAdapter(Device);
    }
}
