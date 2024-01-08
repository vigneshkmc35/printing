package com.example.bluetoothprinter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothPairedDeviceDialog extends DialogFragment {

    ImageView closeIV;
    // will show the statuses like bluetooth open, close or data sent
    TextView noRecordFoundTV;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    private RecyclerView.Adapter adapter;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    View view;
    Context context;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    Button printBtn, CancelBtn;
    pairedDevice bluetoothPairedDeviceDialog;
    CheckBox invoicePrinting, TicketPrinting;

    StringBuilder signData = new StringBuilder();
    StringBuilder signData1 = new StringBuilder();

    ArrayList<PairedDeviceData> pairedDeviceList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.reschedule_dialog, container, false);
        view = inflater.inflate(R.layout.bluetooth_paired_device_dialog_fragment, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        context = getContext();
        return view;
    }

    @Override
    public void onStart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((7 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onStart();
    }

    public void BluetoothPairedDeviceDialog(String insDate) {
        this.mmDevice = mmDevice;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        closeIV = (ImageView) view.findViewById(R.id.closeIV);
        recyclerView = (RecyclerView) view.findViewById(R.id.pairedDeviceRR);
        layoutManager = new LinearLayoutManager(context);
        noRecordFoundTV = (TextView) view.findViewById(R.id.noRecordFoundTV);

        CancelBtn = (Button) view.findViewById(R.id.CancelBtn);
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean deviceIsActive = false;
        if (bAdapter != null) {
            //device is online or offline
            deviceIsActive = bAdapter.isEnabled();

            if (deviceIsActive) {
                @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                final ArrayList<BluetoothDevice> btList = new ArrayList<>();
                if(pairedDevices != null && pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        BluetoothClass bluetoothClass = device.getBluetoothClass();
                        if (bluetoothClass != null) {
                            // Not really sure what we want, but I know what we don't want.
                            switch (bluetoothClass.getMajorDeviceClass()) {
                                case BluetoothClass.Device.Major.IMAGING:
                                    btList.add(device);
                                    break;
                                default:
                                    break;

                            }
                        }
                    }
                }
                //btList.addAll(pairedDevices);
                if (btList.size() > 0) {
                    for (BluetoothDevice device : btList) {
                        PairedDeviceData pairedDeviceData = new PairedDeviceData();
                        pairedDeviceData.setDevice(device);
                        pairedDeviceData.setDeviceName(device.getName());
                        pairedDeviceList.add(pairedDeviceData);
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    noRecordFoundTV.setVisibility(View.GONE);
                    adapter = new BluetoothPairedDeviceAdapter(pairedDeviceList, bluetoothPairedDeviceDialog, getDialog());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noRecordFoundTV.setVisibility(View.VISIBLE);
                }
            }

        }
        //  bluetoothPairedDeviceDialog.onBluetootPairedDevicetReturned();
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void bluetoothPairedDeviceDialog(String s) {
    }

    public interface pairedDevice {
        void onBluetootPairedDevicetReturned(BluetoothDevice device, Dialog dialog);
    }

    public void setCallBack(pairedDevice bluetoothPairedDeviceDialog) {
        this.bluetoothPairedDeviceDialog = bluetoothPairedDeviceDialog;
    }
}

