package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.api.util.ResultReturner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BluetoothAPI {

    // tempo massimo per uno scan
    private static final long SCAN_PERIOD = 15000;

    static void onReceiveBluetoothScanInfo(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {
            // lista che conterrà i dispositivi scannerizzati
            List<ScanResult> scans = new ArrayList<>();
            BluetoothLeScanner mScan;
            // i risultati di uno scan vengono dati attraverso un CallBack
            // per cui è necessario instanziare un CallBack per poi fare l'override dei suoi metodi.
            ScanCallback mLeCallBack;
            // dato che non è stata aggiunta la possibilità di scannerizzare
            // per un tempo predefinito (quello di default è 30 minuti)
            // è necessario utilizzare un Handler che dopo il nostro timeout fermerà lo scan
            Handler mHandler;
            @Override
            public void writeJson(final JsonWriter out) throws Exception {
                out.beginObject().name("message:").value("BluetoothScanInfo da implementare").endObject();
                /*

                // chiamata necessaria prima di instanziare un Handler
                Looper.prepare();
                // Bisogna prendere l'handler dal main UI
                mHandler = new Handler(Looper.getMainLooper());
                BluetoothAdapter mBluetoothAdapter;
                BluetoothManager manager = (BluetoothManager) context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = manager.getAdapter();
                // Bluetooth scan uses a CallBack to provide results so
                // we need to override the method onScanResult or onBatchResults
                mLeCallBack = new ScanCallback() {

                    // questo metodo viene chiamato ogni volta che trova un dispositivo
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        // 10 sarà il numero massimo di dispositivi che permetteremo di visualizzare
                        if(scans.size()<=10)
                            scans.add(result);
                        }
                    // in alternativa è possibile utilizzare onBatchResults
                    // metodo che viene chiamato solo quando abbiamo finito di scannerizzare

                };

                mScan = mBluetoothAdapter.getBluetoothLeScanner();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScan.stopScan(mLeCallBack);
                        try {
                            // probabilmente bisogna riprendere out come è stato fatto dentro ResultReturner
                            // dato che dopo i nostri 15 secondi l'oggetto out sarà già stato chiuso
                            // ovvero bisogna fare:
                            //LocalSocket outputSocket = new LocalSocket()
                            //String outputSocketAdress = intent.getStringExtra(SOCKET_OUTPUT_EXTRA);
                            //outputSocket.connect(new LocalSocketAddress(outputSocketAdress));
                            //PrintWriter writer = new PrintWriter(outputSocket.getOutputStream();
                            //JsonWriter out = new JsonWriter(writer);
                            //out.setIndent("  ");
                            out.beginArray();
                            for (ScanResult sr : scans) {
                                Log.d("myBluetooth result:", sr.toString());
                                out.beginObject();
                                out.name("name").value(sr.getDevice().getName());
                                out.name("mac").value(sr.getDevice().getAddress());
                                out.endObject();
                            }
                            out.endArray();
                            // writer.println();
                        }catch(Exception e){
                            Log.d("myBluetooth result:", "EXCEPTION..."+e.getMessage());
                        }
                     // fine di metodo run

                    }
                }, SCAN_PERIOD);
                mScan.startScan(mLeCallBack);

                // a questo punto scans dovrebbe contenere tutti i results di mLeCallBack
                if (scans == null) {
                    //    out.beginObject().name("API_ERROR").value("Failed getting scan results").endObject();
                } else if (scans.isEmpty() && !mBluetoothAdapter.isEnabled()) {
                    String errorMessage = "Bluetooth needs to be enabled on the device";
                    //    out.beginObject().name("API_ERROR").value(errorMessage).endObject();
                } else if (scans.isEmpty()) {
                    String errorMessage = "No Bluetooth Devices found";
                    //out.beginObject().name("API_ERROR").value(errorMessage).endObject();
                } else {

                  /* out.beginArray();
                    for (ScanResult scan : scans) {
                        out.beginObject();
                        out.name("name").value(scan.getDevice().getName());
                        out.name("address").value(scan.getDevice().getAddress());
                        out.name("type").value(scan.getDevice().getType());
                        out.name("state").value(scan.getDevice().getBondState());
                        out.name("uuid").value(scan.getDevice().getUuids().toString());
                        out.name("rssi").value(scan.getRssi());
                        out.name("bond_state").value(scan.getDevice().getBondState());

                        out.endObject();
                    }
                    out.endArray();

                    }
    */
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
                    Log.d("***ciao3", "boh"+inputString);

                    if(inputString.equals("")) {

                        writer.beginObject().name("message:").value("invalid input").endObject();
                    }else
                    	writer.beginObject().name("message:").value("BluetoothConnect da implementare, dovrebbe connettersi a " + inputString).endObject();
                    out.println(); // To add trailing newline.
                }catch(Exception e){
                    Log.d("termux", "errorreeeeee bluetooth connect");

                }
            }
        });
    }
}
