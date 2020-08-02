package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.MainActivity;
import com.termux.api.util.ResultReturner;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class BluetoothLowEnergyAPI {

    private static boolean scanning = false;
    private static Set<String> deviceList = new HashSet<String>();
    public static BluetoothManager btManager;
    public static BluetoothAdapter btAdapter;
    public static BluetoothLeScanner btScanner;
    public final static int REQUEST_ENABLE_BT = 1;

    // Device scan callback.
    private static ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (device != null) {
                String deviceName = device.getName();
                // you can get more info from device here
                // ...

                if (deviceName != null && !deviceName.equals("null") && !deviceName.trim().equals("")) {
                    deviceList.add(deviceName);

                }
            }

        }
    };


    static void onReceiveBluetoothScanInfo(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {

        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {


            @Override
            public void writeJson(final JsonWriter out) throws Exception {
                out.beginObject();
                if(!scanning) {
                    if(btManager==null) {
                        // stuff for BLE, you can move this in a init function inside BluetoothLowEnergyAPI
                        btManager = (BluetoothManager) MainActivity.activity.getSystemService(Context.BLUETOOTH_SERVICE);
                        if(btManager == null) Log.d("termux-bl","MANAGER IS NULL");
                        btAdapter = btManager.getAdapter();
                        if(btAdapter == null) Log.d("termux-bl","ADAPTER IS NULL");
                        btScanner = btAdapter.getBluetoothLeScanner();
                        if(btScanner == null) Log.d("termux-bl","SCANNER IS NULL");
                    }

                    // start scanning with a broadcast receiver
                    out.name("message").value("scanning ble devices, please type termux-bluetooth-scaninfo again to stop the scanning and print the results");
                    scanning = true;
                    deviceList.clear();
                    System.out.println("start scanning");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            btScanner.startScan(leScanCallback);
                        }
                    });
                }
                else{
                    System.out.println("stop scanning");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            btScanner.stopScan(leScanCallback);
                        }
                    });
                    for(String s: deviceList)
                        out.name("device").value(s);
                    scanning =false;
                }
                out.endObject();

            }
        });
    }


    static void onReceiveBluetoothConnect(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        // To be implemented, the idea is to receive the command 'termux-bluetooth-connect args'
        // be careful to do not send only termux-bluetooth-connect without args because the termux-api-package didnt control 0 args so it will block
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
