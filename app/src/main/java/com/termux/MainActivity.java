package com.termux;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.termux.api.ToastAPI;
import com.termux.app.TermuxActivity;

public class MainActivity extends Activity {

    public static MainActivity activity;
    public Button btnConsole;
    private Intent info_intent;
    public Button btnInfo;
    private Intent console;
    public static int ActualActivity = 1;
    //ActualActivity = 1 means that Main Activity is being used so onResume has to show it back.
    //ActualActivity = 2  means that TermuxConsole is being used so onResume has to show it back.




    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        // if Termux has been installed and console is off turn it on.
        if(TermuxActivity.installed && !btnConsole.isEnabled())btnConsole.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // bisogna ricordare che l'ultimo Activity visibile era MainActivity
        ActualActivity = 1;
        // minimize App serve per far finta di chiudere l'app dopo il BackPressed della schermata principale.
        minimizeApp();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;


        btnConsole = (Button) findViewById(R.id.btn_console);
        RelativeLayout.LayoutParams lyt = (RelativeLayout.LayoutParams) btnConsole.getLayoutParams();
        btnConsole.setEnabled(false);
        lyt.topMargin = getStatusBarHeight();

        btnConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualActivity = 2;
                moveToConsole();

            }
        });

        info_intent = new Intent(this, InfoActivity.class);

        btnInfo = (Button) findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(info_intent);
            }
        });
    }

    public void moveToConsole() {
        super.onBackPressed();
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onDestroy() {
        TermuxActivity.first_activity_options=false;
        super.onDestroy();
    }
}
