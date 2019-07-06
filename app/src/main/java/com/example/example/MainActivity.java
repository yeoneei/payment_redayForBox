package com.example.example;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.CookieManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends AppCompatActivity {

    private WebView mainWebView;
    private static final String APP_SCHEME = "iamportapptest://";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new InicisWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent = getIntent();
        Uri intentData = intent.getData();

        if (intentData == null) {
            Log.e("[[pay]]","check in");
            mainWebView.loadUrl("http://13.209.206.99:3000/api/payment");
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url =intentData.toString();
            Log.e("[[pay, url]]",url.toString());
            Intent test = new Intent(getApplicationContext(), AfterPay.class);
            startActivity(test);
//            if (url.startsWith(APP_SCHEME)) {
//                String redirectURL = url.substring(APP_SCHEME.length() + 3);
//                //String redirectURL = "http://13.209.206.99:3000/api/test";
//                Log.e("[[pay]]",redirectURL);
//                mainWebView.loadUrl(redirectURL);
//            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        Log.e("[[pay, redirect]]",url.toString());
        if (url.startsWith(APP_SCHEME)) {
            String redirectURL = url.substring(APP_SCHEME.length() + 3);
            //String redirectURL = "http://13.209.206.99:3000/api/test";
            Log.e("[[pay]]1234566",redirectURL);
            mainWebView.loadUrl(redirectURL);
        }
    }
}
