package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.MainActivity;
import com.termux.api.util.ResultReturner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BluetoothAPI {

    private static boolean scanning = false;
    private static Set<String> deviceList = new HashSet<String>();
    public static boolean unregistered = true;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                // you can get more info from device here
                // ...

                if(!deviceName.equals("null") && !deviceName.trim().equals("")) {
                    deviceList.add(deviceName);
                }
            }
        }
    };

    public static void bluetoothStartScanning(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        MainActivity.activity.getBaseContext().registerReceiver(mReceiver, filter);
        unregistered = false;
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
    }
    public static void bluetoothStopScanning(){
        if(!unregistered) {
            MainActivity.activity.getBaseContext().unregisterReceiver(mReceiver);
            unregistered=true;
        }
        }

    static void onReceiveBluetoothScanInfo(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {

            /* TODO: implement BLE

             */
            @Override
            public void writeJson(final JsonWriter out) throws Exception {
                out.beginObject();
                if(!scanning) {
                    // start scanning with a broadcast receiver
                    out.name("message").value("scanning bluetooth devices, please type termux-bluetooth-scaninfo again to stop the scanning and print the results");
                    scanning = true;
                    deviceList.clear();
                    bluetoothStartScanning();
                }
                else{
                    // stop scanning, so stop the broadcast receiver
                    bluetoothStopScanning();
                    for(String s: deviceList)
                        out.name("device").value(s);
                    scanning =false;
                }
                out.endObject();

            }
        });
    }


    static void onReceiveBluetoothConnect(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        // Metodo da implementare
        // è possibile ricevere una stringa dopo la chiamata 'termux-bluetooth-connect' attraverso la Classe WithInputString
        // che si trova all'interno di com.termux.api.util.ResultReturner.java
        // così l'unica cosa da fare è aggiungere lo script che risconosce 'termux-blueooth-connect' per poi dalla console
        // chiamare 'termux-bluetooth-connect device_uuid'
        ResultReturner.returnData(apiReceiver, intent,new ResultReturner.WithStringInput(){


            @Override
            public void writeResult(PrintWriter out) throws Exception {
                try {
                    JsonWriter writer = new JsonWriter(out);
                    writer.setIndent("  ");

                    if(inputString.equals("")) {

                        writer.beginObject().name("message:").value("invalid input").endObject();
                    }else
                    	writer.beginObject().name("message:").value("BluetoothConnect to be implemented, it should connect to " + inputString).endObject();
                    out.println(); // To add trailing newline.
                }catch(Exception e){
                    Log.d("Except BluetoothConnect", e.getMessage());

                }
            }
        });
    }
}
