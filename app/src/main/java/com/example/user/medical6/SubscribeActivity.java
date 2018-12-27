package com.example.user.medical6;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SubscribeActivity extends AppCompatActivity {
    WebView wvSubscribe;
    WebSettings wsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        wvSubscribe = (WebView) findViewById(R.id.wvSubscribe);
        wsText = wvSubscribe.getSettings();
        wsText.setJavaScriptEnabled(true);
        wsText.setBuiltInZoomControls(true);
        wvSubscribe.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.i("TAG",url);
//            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        wvSubscribe.loadUrl("https://tlbinfo.cims.tw/");
    }
    @Override
    public void onBackPressed(){
        if (wvSubscribe.canGoBack()){
            wvSubscribe.goBack();
            return;
        }
        super.onBackPressed();
    }
}