package com.galaxy.test3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.galaxy.test3.R;

import static android.view.KeyEvent.KEYCODE_BACK;

public class DataAllActivity extends AppCompatActivity {


    //设置标题
    private TextView title;

    //设置网络路径
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_all);


        Intent intent = getIntent();


        title = findViewById(R.id.tv_title);
        title.setText(intent.getStringExtra("title"));

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(intent.getStringExtra("url"));

        /**
         * 在网页中点击了新的链接后，不会启用系统浏览器，而是继续使用当前webview!
         */
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 按返回键不会退出当前Activity，只会在网页中进行后退
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
