package com.example.user.medical6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dave Smith
 * Double Encore, Inc.
 * AdRecord
 */
public class AdRecord {

    /* An incomplete list of the Bluetooth GAP AD Type identifiers */
    public static final int TYPE_FLAGS = 0x1;
    public static final int TYPE_UUID16_INC = 0x2;
    public static final int TYPE_UUID16 = 0x3;
    public static final int TYPE_UUID32_INC = 0x4;
    public static final int TYPE_UUID32 = 0x5;
    public static final int TYPE_UUID128_INC = 0x6;
    public static final int TYPE_UUID128 = 0x7;
    public static final int TYPE_NAME_SHORT = 0x8;
    public static final int TYPE_NAME = 0x9;
    public static final int TYPE_TRANSMITPOWER = 0xA;
    public static final int TYPE_CONNINTERVAL = 0x12;
    public static final int TYPE_SERVICEDATA = 0x16;
    public static final int TYPE_MANUFACTURER_SPEC_DATA = 0xFF;


    /*
     * Read out all the AD structures from the raw scan record
     */
    public static List<AdRecord> parseScanRecord(byte[] scanRecord) {
        List<AdRecord> records = new ArrayList<AdRecord>();

        int index = 0;
        while (index < scanRecord.length) {
            int length = scanRecord[index++];
            //Done once we run out of records
            if (length == 0) break;

            int type = scanRecord[index] & 0xFF;
            //Done if our record isn't a valid type
            if (type == 0) break;


            byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);

            records.add(new AdRecord(length, type, data));
            //Advance
            index += length;
        }

        return records;
    }

    /* Helper functions to parse out common data payloads from an AD structure */

    public static String getName(AdRecord nameRecord) {
        return new String(nameRecord.mData);
    }

    public static int getServiceDataUuid(AdRecord serviceData) {
        if (serviceData.mType != TYPE_SERVICEDATA) return -1;

        byte[] raw = serviceData.mData;
        //Find UUID data in byte array
        int uuid = (raw[1] & 0xFF) << 8;
        uuid += (raw[0] & 0xFF);

        return uuid;
    }

    public static byte[] getServiceData(AdRecord serviceData) {
        if (serviceData.mType != TYPE_SERVICEDATA) return null;

        byte[] raw = serviceData.mData;
        //Chop out the uuid
        return Arrays.copyOfRange(raw, 2, raw.length);
    }

    /* Model Object Definition */

    private int mLength;
    private int mType;
    private byte[] mData;

    public AdRecord(int length, int type, byte[] data) {
        mLength = length;
        mType = type;
        mData = data;
    }

    public int getLength() {
        return mLength;
    }

    public int getType() {
        return mType;
    }

    public byte[] getData() {
        return mData;
    }

    public byte[] getManufacturerData() {
        byte[] mfcData = new byte[mData.length - 2];
        System.arraycopy(mData, 2, mfcData, 0, mData.length - 2);
        return mfcData;
    }

    public XenonData getXenonData() {
        // count array items
        XenonData xbData = new XenonData();
        int index = 0;
        int data0 = mData[0] & 0xFF;
        int data1 = mData[1] & 0xFF;
        if ((data0 == 120) && (data1 == 157)) {   // 0x789D  advertising menufacture data
            //index = 5;
            index += 2;
        }
        int productionId = ((mData[index + 1] & 0xFF) << 8) + (mData[index] & 0xFF);
        int stamp = (mData[index + 2]) & 0xFF;
        index += 3;
        int idLength = (byte) (mData[index] >> 5 & 0x07); // device id length
        int dataLength=(byte) (mData[index]& 0x1F); // data  length ;
        int deviceId = 0;
        for (int i = idLength; i > 0; i--) {
            deviceId += (mData[index + i] & 0xFF) << (8 * (i - 1));
        }
        index += idLength;

        xbData.productId = productionId;  // Production ID
        xbData.deviceId = deviceId; //Device ID
        xbData.stamp = stamp; //stamp index
        xbData.values = Arrays.copyOfRange(mData, ++index, index+dataLength);
      //  xbData.values = mData;
 //     Log.e("mData", Arrays.toString(mData));
        return xbData;
    }

    @Override
    public String toString() {
        switch (mType) {
            case TYPE_FLAGS:
                return "Flags";
            case TYPE_NAME_SHORT:
            case TYPE_NAME:
                return "Name";
            case TYPE_UUID16:
            case TYPE_UUID16_INC:
                return "UUIDs";
            case TYPE_TRANSMITPOWER:
                return "Transmit Power";
            case TYPE_CONNINTERVAL:
                return "Connect Interval";
            case TYPE_SERVICEDATA:
                return "Service Data";
            case TYPE_MANUFACTURER_SPEC_DATA:
                return "Manufacturer Specific Data";
            default:
                return "Unknown Structure: " + mType;
        }
    }
}

