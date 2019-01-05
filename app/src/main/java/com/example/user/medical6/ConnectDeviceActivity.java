package com.example.user.medical6;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.ParseException;


public class ConnectDeviceActivity extends AppCompatActivity {
    private final static String TAG = ConnectDeviceActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button btnWeight;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private BluetoothLeService mBluetoothLeService;
    private xServiceConnection mServiceConnection;
    private Handler mHandler;
    private String mBleScanTagId = "";

    private byte[] mPreScanRecord = {0};
    XenonDecoder mXbDecoder;
    private boolean mScanning;
    private String mDeviceAddress;
    private boolean mEnableScan = true;
    private static long mBleScanInterval = 20000;//20000
    private static long mBleScanTimeout = 15000;//15000
    private TextView tvTextWeight;

    private EditText dataEdit;
    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;

    // data base 變數宣告
    public dataBase DH=null;
    Cursor cur;
    SQLiteDatabase db;

    private class xServiceConnection implements ServiceConnection {

        public xServiceConnection() {
            super();

        }

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ConnectDeviceActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);
//        Spinner SelectDevice = (Spinner)findViewById(R.id.deviceSelection);
//        ArrayAdapter<CharSequence> Device = ArrayAdapter.createFromResource(ConnectDeviceActivity.this,
//                R.array.device,
//                android.R.layout.simple_spinner_dropdown_item);
//        SelectDevice.setAdapter(Device);

        checkLocationPermission();
        btnWeight = (Button) findViewById(R.id.btnCWeight);
        btnWeight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Check if BLE is supported on the device.
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Toast.makeText(ConnectDeviceActivity.this, "BLE is not supported in this device!", Toast.LENGTH_SHORT).show();
                    //finish();
                }

                // Initializes Bluetooth adapter.
                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();

                // Ensures Bluetooth is available on the device and it is enabled. If not,
                // displays a dialog requesting user permission to enable Bluetooth.
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                mBleScanTagId = XenonHelper.XENON_ADVERTISING_NAME;

                // BLE Setting
                mBluetoothAdapter = bluetoothManager.getAdapter();

                if (mBluetoothAdapter.isEnabled()) {
                    mHandler = new Handler();
                    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                    if (mBleScanInterval != 0) {
                        mHandler.post(doTask);
                    }

                    tvTextWeight = (TextView) findViewById(R.id.Weight);
                    scanLeDevice(true);
                }
                  }
        });

        try {
            searchtime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        searchheight();

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            mDeviceAddress = device.getAddress();
            String adName = "";
            String msg = device.getAddress() + "---payload = ";
            for (byte b : scanRecord)
                msg += String.format("%02x ", b);
            Log.i(TAG, msg);
            // parse ScanRecord data
            List<AdRecord> records = AdRecord
                    .parseScanRecord(scanRecord);
            XenonData xbData = null;
            // get advertise Name
            for (AdRecord packet : records) {
                Log.i(TAG, "BG Service getType:" + packet.getType());
                // Find the device name record
                if (packet.getType() == AdRecord.TYPE_NAME) {
                    adName = AdRecord.getName(packet);
                    Log.i(TAG, "BG ADV Name:" + adName);
                    Log.i(TAG, "BG DEVICE ADDRESS:" + device.getAddress());
                } else if (packet.getType() == AdRecord.TYPE_MANUFACTURER_SPEC_DATA) {
                    //  xbData = packet.getXenonData();

                }
            }

            if (mBluetoothLeService == null) {
                Intent gattServiceIntent = new Intent(
                        ConnectDeviceActivity.this,
                        BluetoothLeService.class);
                mServiceConnection = new xServiceConnection();
                bindService(gattServiceIntent, mServiceConnection,
                        BIND_AUTO_CREATE);
            } else {

                if (!adName.equals("")) {
                    if (adName.toLowerCase().equals(mBleScanTagId.toLowerCase())) {

                        mBluetoothLeService.connect(mDeviceAddress, false, false, null);
                    }
                }
            }
            mPreScanRecord = scanRecord;

        }
    };


    private Runnable doTask = new Runnable() {
        @Override
        public void run() {
            try {
                if (!mScanning) {
                    if (mEnableScan) {
                        scanLeDevice(true);
                    }
                    //   mHandler.postDelayed(doTask, mBleScanInterval);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    };


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                }
            }, mBleScanTimeout);
            mScanning = true;

            mBluetoothAdapter.startLeScan(mLeScanCallback);

        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        return intentFilter;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            String address;
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    Bundle bundle = intent.getBundleExtra(BluetoothLeService.EXTRA_DATA);
                    address = bundle.getString("address");
                    byte value[] = bundle.getByteArray("value");
                    AdRecord adRecord = new AdRecord(value.length + 1, 0xFF, value);
                    XenonData xbData = adRecord.getXenonData();
                    String uid = String.format("%02x ", xbData.productId) + String.format("%02x ", xbData.deviceId);
                    String valueString="";
                    try {
                        byte[] weightValue = new byte[5];
                        weightValue[0] = xbData.values[3];
                        weightValue[1] = xbData.values[4];
                        weightValue[2] = xbData.values[5];
                        weightValue[3] = xbData.values[6];
                        weightValue[4] = xbData.values[7];
                        valueString = new String(weightValue, "UTF-8");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    tvTextWeight.setText(valueString);
                    Log.e("values", valueString);
                    //   mXbDecoder.putData(uid, xbData.values);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void searchtime() throws ParseException {

        TextView datetext = (TextView) findViewById(R.id.CurrentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        calendar = Calendar.getInstance();
        Date tdt = calendar.getTime();
        String time = sdf.format(tdt);
        datetext.setText(time);

    }

    public void searchheight(){

        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();

        cur=db.rawQuery(" SELECT  height  FROM  customer " ,null);

        EditText HeightText = (EditText) findViewById(R.id.editTextHeight);
        if(cur.getCount()>0){
            HeightText.setText(cur.getString(0));
        }else{
            HeightText.setHint("請輸入身高");
        }
    }
}

