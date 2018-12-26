package com.example.user.medical6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SubscribeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        WebView wvSubscribe = (WebView)findViewById(R.id.wvSubscribe);
        WebSettings wsText = wvSubscribe.getSettings();
        wsText.setJavaScriptEnabled(true);
        wvSubscribe.setWebChromeClient(new WebChromeClient());
        wvSubscribe.loadUrl("https://tlbinfo.cims.tw/pages/guan-yu-chan-pin");
    }
}
