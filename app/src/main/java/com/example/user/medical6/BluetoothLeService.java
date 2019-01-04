/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.user.medical6;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt = null;
    private ArrayList<BluetoothGatt> mBluetoothGatts = new ArrayList<BluetoothGatt>();
    private HashMap<ByteBuffer, byte[]> mDeviceIdValueMap = new HashMap<ByteBuffer, byte[]>();
    private HashMap<String, Integer> mConnectionState = new HashMap<String, Integer>();

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.kylab.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.kylab.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.kylab.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.kylab.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_SETTING_ID_SUCCESS = "com.kylab.bluetooth.le.ACTION_SETTING_ID_SUCCESS";
    public final static String ACTION_SETTING_RTC_SUCCESS = "com.kylab.bluetooth.le.ACTION_SETTING_RTC_SUCCESS";


    public final static String EXTRA_DATA = "com.kylab.bluetooth.le.EXTRA_DATA";


    // Implements callback methods for GATT events that the app cares about. For
    // example,
    // connection change and services discovered.
    private class LonGattCallback extends BluetoothGattCallback {
        private byte[] commands = null;
        private byte[] deviceId = null;
        private String address = null;
        private boolean isSetTime = false;
        private boolean isSetID = false;
        int xenonDeviceId;

        public LonGattCallback(byte[] commands) {
            super();
            this.commands = commands;
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            address = gatt.getDevice().getAddress();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState.put(address, STATE_CONNECTED);
                broadcastUpdate(intentAction, address);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:"
                        + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState.remove(address);

                Log.i(TAG, "Disconnected from GATT server.");

                gatt.disconnect();
                gatt.close();
                broadcastUpdate(intentAction, address);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i(TAG, "onServicesDiscovered");
            BluetoothGattService gattSrv;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gattSrv = gatt.getService(XenonHelper.UUID_XENON_SERVICE);
                if (gattSrv != null) {
                    setDeviceValueDataNotification(true);
                }

                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
             //   broadcastUpdate(ACTION_DATA_AVAILABLE);

                gatt.readRemoteRssi();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] dataValue = characteristic.getValue();
            Bundle bundle = new Bundle();
            bundle.putString("address", address);
            bundle.putByteArray("value", dataValue);

            String broadcastAction;
            broadcastAction = ACTION_DATA_AVAILABLE;
            broadcastUpdate(broadcastAction, bundle);
            gatt.readRemoteRssi();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (characteristic.getUuid().equals(XenonHelper.UUID_XENON_DATA)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (isSetTime) {
                        broadcastUpdate(ACTION_SETTING_RTC_SUCCESS, address);
                    }
                    if (isSetID) {
                        broadcastUpdate(ACTION_SETTING_ID_SUCCESS, address + ";" + Integer.toString(xenonDeviceId));
                    }
                }
            }
        }


    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final String message) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, message);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, final Bundle bundle) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, bundle);
        sendBroadcast(intent);
    }


    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "onBind");
        return mBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.

        closeAll();
        Log.i(TAG, "onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address, boolean isSetTime, boolean isSetID, byte[] commands) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.

        if (!mConnectionState.containsKey(address)) {
            mBluetoothGatt = device.connectGatt(this, false, new LonGattCallback(commands));
            mBluetoothGatts.add(mBluetoothGatt);
            mConnectionState.put(address, STATE_CONNECTING);
            return true;
        } else {
            return false;
        }

    }

    public void closeAll() {
        for (BluetoothGatt gatt : mBluetoothGatts) {
            gatt.disconnect();
            gatt.close();
        }
        mBluetoothGatts.clear();
        mConnectionState.clear();
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized--readCharacteristic");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);


    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    public void setDeviceValueDataNotification(boolean enable) {
        BluetoothGattService gattSrv = null;
        ArrayList<BluetoothGattCharacteristic> gattChs = new ArrayList<BluetoothGattCharacteristic>();
        if (mBluetoothGatt == null)
            return;


        gattSrv = mBluetoothGatt.getService(XenonHelper.UUID_XENON_SERVICE);
        if (gattSrv != null)
            gattChs.add(gattSrv
                    .getCharacteristic(XenonHelper.UUID_XENON_DATA));

        if (gattChs.size() > 0) {
            for (BluetoothGattCharacteristic gattCh : gattChs) {
                int charaProp = gattCh.getProperties();
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    Log.i(TAG, "gattCh UUID:" + gattCh.getUuid().toString());
                    mBluetoothGatt
                            .setCharacteristicNotification(gattCh, enable);
                    BluetoothGattDescriptor descriptor = gattCh
                            .getDescriptor(XenonHelper.UUID_CLIENT_CHARACTERISTIC_CONFIG);
                    if (enable) {
                        descriptor
                                .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    } else {
                        descriptor
                                .setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    }
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

}
