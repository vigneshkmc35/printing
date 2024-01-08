package com.example.bluetoothprinter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class BluetoothPairedDeviceAdapter extends RecyclerView.Adapter<BluetoothPairedDeviceAdapter.ViewHolder>{
    private List<PairedDeviceData> arrayList;
    Context context;
    com.example.bluetoothprinter.BluetoothPairedDeviceDialog.pairedDevice bluetoothPairedDeviceDialog;
    Dialog dialog;

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_paired_device_items, parent, false);
        context=view.getContext();
        return new ViewHolder(view);
    }
    public BluetoothPairedDeviceAdapter(List<PairedDeviceData> arrList, com.example.bluetoothprinter.BluetoothPairedDeviceDialog.pairedDevice bluetoothPairedDeviceDialog, Dialog dialog){
        this.arrayList= (ArrayList<PairedDeviceData>) arrList;
        this.bluetoothPairedDeviceDialog=bluetoothPairedDeviceDialog;
        this.dialog=dialog;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PairedDeviceData data=arrayList.get(position);

        String msg = "Paired Device";
        holder.pairedDeviceNameTV.setText(msg+" : "+data.getDeviceName());

        holder.pairedDeviceNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothPairedDeviceDialog.onBluetootPairedDevicetReturned(arrayList.get(position).getDevice(),dialog);
            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pairedDeviceNameTV;
        public ViewHolder(View itemView) {
            super(itemView);
            this.pairedDeviceNameTV= (TextView) itemView.findViewById(R.id.pairedDeviceNameTV);
        }
    }
}
