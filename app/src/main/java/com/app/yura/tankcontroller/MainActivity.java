package com.app.yura.tankcontroller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import TCPClient.Client;
import TCPClient.Command;
import TCPClient.CommandFactory;
import TCPClient.Commands;

public class MainActivity extends AppCompatActivity {
    WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLeft = (Button)findViewById(R.id.buttonLeft);
        Button buttonRight = (Button)findViewById(R.id.buttonRight);

        final Client client = ConnectActivity.getClient();

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = null;
                try {
                    command = CommandFactory.CreateCommand(Commands.MOVE_LEFT, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.sendCommand(command, false);
                showVideo();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = null;
                try {
                    command = CommandFactory.CreateCommand(Commands.MOVE_RIGHT, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.sendCommand(command, false);
            }
        });

        web = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:callFromActivitySetPassword(\""+"12345678"+"\")");
//                showVideo();
            }
        });

        String script = "<script type=\"text/javascript\">\n" +
                "function setPassword()" +
                "{" +
                "  document.getElementById('password').value = '12345678';" +
//                "  document.myform.submit();" +
                "}" +
                " window.onload = setPassword;" +
                "</script>";
        web.loadUrl("http://192.168.8.1");
//        web.loadDataWithBaseURL("http://192.168.8.1", script, "text/html","utf-8", null);
    }

//    private void login()
//    {
//        String password = "12345678";
//
//        try {
//            Jsoup.connect("http://192.168.8.1").data("password", password).post();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        web.postDelayed(new Runnable() {
            @Override
            public void run() {
                showVideo();
            }
        }, 5000);

//        login();
    }

    private void showVideo()
    {
        web.loadUrl("http://192.168.8.1:8080");
        web.zoomBy(0.1f);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectActivity.client.disconnect();
    }
}

//private class WebViewInterface{
//
//
//    @JavascriptInterface
//    public void onError(String error){
//        throw new Error(error);
//    }
//}