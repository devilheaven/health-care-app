package com.example.user.medical6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Brian on 2016/8/4.
 */
public class XenonDecoder {
    private HashMap<String, Queue<Byte>> mDeviceDataQueue = new HashMap<String, Queue<Byte>>();
    private Handler mHandler;

    private Queue<Byte> processQueue;
    private int mRawRows = 0;
    private int mRawChannels = 0;
    private int mRawDataSize = 0;
    private int mRawAllDataSize = 0;
    private boolean[][] mRawMappingArrays;
    private Context context;


    public final static String ACTION_SEND_DATA = "com.kylab.xenondecorder.ACTION_SEND_DATA";
    public final static String EXTRA_DATA = "com.kylab.xenondecorder.EXTRA_DATA";

    public final static String XB_ID = "com.kylab.xenondecorder.XB_ID";
    public final static String SPO2_Array = "com.kylab.xenondecorder.SPO2_Array";

    public XenonDecoder(Context context) {
        this.mHandler = new Handler();
        this.context = context;
    }

    public void start() {

        mHandler.post(doTask);
    }

    public void stop() {
        mHandler.removeCallbacks(doTask);
    }

    public void putData(String xbid, byte[] data) {
        Queue workingQueue = mDeviceDataQueue.get(xbid);
        if (workingQueue == null) {
            workingQueue = new LinkedList<Byte>();
        }
        for (byte d : data) {
            workingQueue.offer(d);
        }

        mDeviceDataQueue.put(xbid, workingQueue);
    }

    public void parseData() {

        for (HashMap.Entry<String, Queue<Byte>> entry : mDeviceDataQueue.entrySet()) {
            processQueue = entry.getValue();
            Log.e("processQueue size", Integer.toString(processQueue.size()));
            byte preByte = 0x00;
            byte commandByte = 0x00;
            if ((mRawAllDataSize == 0) || (processQueue.size() > mRawAllDataSize + 10)) {  // avoid short data

                commandByte = processQueue.poll();
                Bundle bundle = new Bundle();
                Log.e("commandByte", Byte.toString(commandByte));
                switch (commandByte) {

                    case (byte)0xcf:  // command O:SPO2
                       ArrayList<Integer> spo2ArrayList = parseSpo2();
                        bundle.putString(XB_ID, entry.getKey());
                        bundle.putSerializable(SPO2_Array, spo2ArrayList);
                        broadcastUpdate(ACTION_SEND_DATA, bundle);
                        break;
                }

            }
        }
    }


    public ArrayList parseSpo2(){
        ArrayList<Integer> dataArrayList = new ArrayList<Integer>();
        int spo2 = processQueue.poll();
        int bpm = processQueue.poll();
        processQueue.poll();  //skip one byte
        dataArrayList.add(spo2);
        dataArrayList.add(bpm);
        return dataArrayList;
    }

private Runnable doTask = new Runnable() {
        @Override
        public void run() {
            try {
                parseData();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            // mHandler.postDelayed(doTask, 100);
            mHandler.postDelayed(doTask, 500);
        }

    };


    private void broadcastUpdate(final String action, final Bundle bundle) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, bundle);
        context.sendBroadcast(intent);
    }


}
