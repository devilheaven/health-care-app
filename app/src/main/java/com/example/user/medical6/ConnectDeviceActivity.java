package com.example.user.medical6;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.List;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.user.medical6.dataBase.*;

public class ConnectDeviceActivity extends AppCompatActivity {
    private final static String TAG = ConnectDeviceActivity.class.getSimpleName();
    private String mBleScanTagId = "";
    private String mDeviceAddress;
    private boolean mScanning;
    private boolean mEnableScan = true;
    private static long mBleScanInterval = 20000;//20000
    private static long mBleScanTimeout = 15000;//15000
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private TextView tvTextWeight;
    private Spinner spinnerDoEat;
    private Button btnWeight ,btnSave;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;

    private xServiceConnection mServiceConnection;
    private Handler mHandler;

    //宣告sharepreference的儲存名稱 之後會用來存入sharepreference儲存空間
    static final  String result = "bodyInformation";

    private byte[] mPreScanRecord = {0};
    XenonDecoder mXbDecoder;

    //定義顯示時間套件
    private Calendar calendar; //通過 Calendar 獲取系統時間

    // data base 變數宣告
    dataBase DH=null;
    Cursor cur;
    ContentValues values = new ContentValues();
    SQLiteDatabase db;

    public TextView editTextWeight,editTextHr,editTextDbp,editTextSbp,editTextHeight,timestamp;

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
        //讀取資料
        DH=new dataBase(this);
        DH.close();
        db =DH.getReadableDatabase();
        setContentView(R.layout.activity_connect_device);

        editTextWeight = (TextView) findViewById(R.id.Weight);
        editTextHr = (TextView) findViewById(R.id.bpm);
        editTextDbp = (TextView) findViewById(R.id.dbp);
        editTextSbp = (TextView) findViewById(R.id.sdp);
        timestamp = (TextView) findViewById(R.id.CurrentDate);
        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        spinnerDoEat = findViewById(R.id.DoEat);


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

        btnSave = (Button) findViewById(R.id.btnCSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db=DH.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues values2 = new ContentValues();
                cur=db.rawQuery(" SELECT " + id + "  FROM  customer " ,null);

                values.put(weight,editTextWeight.getText().toString() );
                values.put(sbp, editTextSbp.getText().toString());
                values.put(dbp, editTextDbp.getText().toString());
                values.put(hr, editTextHr.getText().toString());
                values.put(time, timestamp.getText().toString());
                values.put(record_status, spinnerDoEat.getSelectedItem().toString());

                values2.put(height, editTextHeight.getText().toString());

                db.insert(TABLE_e, null, values);

                JSONObject postData=new JSONObject();

                //            postData
                String[] arrayData = timestamp.getText().toString().split("-");
                try {
                    postData.put("weight_kg",editTextWeight.getText().toString() );
                    postData.put("heart_rate", editTextHr.getText().toString());
                    postData.put("diastolic_bp", editTextDbp.getText().toString());
                    postData.put("systolic_bp", editTextSbp.getText().toString());
                    postData.put("height_cm", editTextHeight.getText().toString());
                    postData.put("record_status", getValue("record_status",spinnerDoEat.getSelectedItem().toString()));
                    postData.put("record_date", arrayData[0]);
                    postData.put("record_time", arrayData[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences SharedPreferences = getSharedPreferences("bodyInformation",MODE_PRIVATE);

                //編輯文件
                SharedPreferences.Editor editor = SharedPreferences.edit();

                //將json檔案存入字串
                String a = postData.toString();

                //存入sharepreference
                editor.putString(result,a);

                editor.commit();
                new bodyInformation().execute();
                
                if(cur.getCount()>0){
                    cur.moveToLast();
                    db.update(TABLE_c,values2,id+"="+cur.getString(0),null);
                }else{
                    db.insert(TABLE_c, null, values2);
                }
                Toast tos = Toast.makeText(ConnectDeviceActivity.this, "新增成功!", Toast.LENGTH_SHORT);
                tos.show();
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
        cur=db.rawQuery(" SELECT " + height + "  FROM  customer " ,null);
        EditText HeightText = (EditText) findViewById(R.id.editTextHeight);
        if(cur.getCount()>0){
            cur.moveToLast();
            HeightText.setText(cur.getString(0));
        }else{
            HeightText.setHint("請輸入身高");
        }
    }

    //get value
    public String getValue(String model,String key){
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

    private class bodyInformation extends AsyncTask<String,Void,String> {
        SharedPreferences SharedPreferences2 = getSharedPreferences("bodyInformation",MODE_PRIVATE);

        String Json2 =  SharedPreferences2.getString(result,"");

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
                    jsonString.put("formId",1020);
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
                Log.e("bodyInformation/Back",s);
            }else if("005".equals(errorCode)){
                Log.e("bodyInformation/Back","JSON error./t"+s);
            }else if("007".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid protocol./t"+s);
            }else if("010".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid form./t"+s);
            }else if("011".equals(errorCode)){
                Log.e("bodyInformation/Back","Invalid date format./t"+s);
            }else if("012".equals(errorCode)){
                Log.e("bodyInformation/Back","Patient not found./t"+s);
            }else{
                Log.e("bodyInformation/Back",s);
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

