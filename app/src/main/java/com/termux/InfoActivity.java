package com.termux;

import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        WebView webView = (WebView) findViewById(R.id.infoView);
        webView.loadUrl("file:///android_asset/info.html");
        RelativeLayout.LayoutParams lyt = (RelativeLayout.LayoutParams) webView.getLayoutParams();
        lyt.topMargin = getStatusBarHeight();
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
