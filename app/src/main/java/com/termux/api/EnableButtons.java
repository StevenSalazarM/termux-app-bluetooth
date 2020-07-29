package com.termux.api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.MainActivity;
import com.termux.api.util.ResultReturner;

public class EnableButtons {

    static void onReceive(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {

            final Handler handler = new Handler();

            @Override
            public void writeJson(final JsonWriter out) throws Exception {
                try{
                    Log.d("termux", "***********setting enableNodeRed to true");

                if(MainActivity.activity!=null) {
                    Log.d("termux", "calling enableButtons()");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("termux", "***********end enabling buttons");

                        }
                    });
                }
                }catch(Exception e){

                    Log.d("termux,","exception "+e.getMessage());
            }}
        });
    }
}
