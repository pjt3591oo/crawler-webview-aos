package com.example.crawler_webview_aos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initWebview();

        this.loginBtnClickHandler();
        this.logoutBtnClickHandler();
        this.testBtnClickHandler();
    }
    public void initWebview() {
        this.myWebView = findViewById(R.id.webview);
        WebSettings webSettings = this.myWebView.getSettings();

        // Enable JavaScript in the WebView
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        // 첫 번째로 전달한 인자를 두번쨰 인자로 자바스크립트 window에 바인딩
        // 바인딩 된 자바 객체는 자바스크립트에서 호출가능 window.androidBridge.[첫 번째 인자 메서드]
        this.myWebView.addJavascriptInterface(new AndroidBridge(), "androidBridge");

        this.myWebView.setWebViewClient(new WebViewClient());
        this.myWebView.setWebChromeClient(new WebChromeClient());
        this.myWebView.loadUrl("https://www.sagewood.co.kr/yeosu/member/login");
    }

    public void loginBtnClickHandler() {
        Log.d("btn event", "loginBtnClickHandler");
        WebView myWebView = this.myWebView;
        Button button = (Button)findViewById(R.id.button1);

        String loginEmail = "email"; // 로그인 이메일
        String loginPassword = "password"; // 로그인 비밀번호

        String script = String.format("document.getElementById('usrId').value='%s'; document.getElementById('usrPwd').value='%s'; fnLoginChk('yeosu') ", loginEmail, loginPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWebView.evaluateJavascript("window.onunload = function () { window.androidBridge.sendMessage('login success'); }", null);
                myWebView.evaluateJavascript(script, null);
                Log.d("btn event", "loginBtnClickHandler");
            }
        });
    }

    public void logoutBtnClickHandler() {
        Log.d("btn event", "logoutBtnClickHandler");
        Button button = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btn event", "logoutBtnClickHandler");
                myWebView.evaluateJavascript("javascript:window.androidBridge.sendMessage('logout')", null);
            }
        });
    }

    public void testBtnClickHandler() {
        Log.d("btn event", "testBtnClickHandler");
        Button button = (Button)findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWebView.evaluateJavascript("javascript:window.androidBridge.sendMessage('test')", null);
            }
        });
    }

    private class AndroidBridge {

        // 웹에서 호출하는 메소드
        @JavascriptInterface
        public void sendMessage(final String arg) {
            Log.d("webview", "자바스크립트에서 호출" + arg);
        }
    }
}