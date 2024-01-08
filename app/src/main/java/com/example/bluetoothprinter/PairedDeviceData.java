package com.example.bluetoothprinter;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class PairedDeviceData implements Parcelable  {
    private String deviceName;
    private BluetoothDevice device;

    protected PairedDeviceData(Parcel in) {
        this.deviceName = in.readString();
    }
    public PairedDeviceData() {

    }

    public static final Creator<PairedDeviceData> CREATOR = new Creator<PairedDeviceData>() {
        @Override
        public PairedDeviceData createFromParcel(Parcel in) {
            return new PairedDeviceData(in);
        }

        @Override
        public PairedDeviceData[] newArray(int size) {
            return new PairedDeviceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceName);

    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
