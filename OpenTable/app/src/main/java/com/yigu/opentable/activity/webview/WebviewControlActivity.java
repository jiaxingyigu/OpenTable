package com.yigu.opentable.activity.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.StringUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.webview.WebChromeClientImpl;
import com.yigu.opentable.util.webview.WebViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/18.
 */
public class WebviewControlActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    private String title;
//    private WebBroadCast webBroadCast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);
        ExitApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }


    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void initView() {
        boolean isShare = getIntent().getBooleanExtra("isShare", false);
        this.title = getIntent().getStringExtra("title");
        back.setImageResource(R.mipmap.back);
        if(TextUtils.isEmpty(title))
            layHeader.setVisibility(View.GONE);
        else
             center.setText(title);

        WebSettings webSetting = webview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webview.addJavascriptInterface(new WebViewUtil(this, webview), "app");
        loadData();
    }

    private void loadData() {
        String linkUrl = getIntent().getStringExtra("url");
        DebugLog.i("linkUrl=" + linkUrl);
        webview.loadUrl(linkUrl, WebViewUtil.getWebviewHeader());//加载网页
        webview.setWebViewClient(new WebViewClient() {

            /*@Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                WebViewUtil.shouldOverrideUrlLoading(WebviewControlActivity.this, view, url);
            }*/

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                DebugLog.i("shouldOverrideUrlLoading=");
                return WebViewUtil.shouldOverrideUrlLoading(WebviewControlActivity.this, view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                DebugLog.i("title=" + view.getTitle());
                center.setText(view.getTitle());
            }
        });
        webview.setWebChromeClient(new WebChromeClientImpl() {
            @Override
            public void onReceivedTitle(WebView view, String newTitle) {
                super.onReceivedTitle(view, title);
                DebugLog.i("onReceivedTilt=" + newTitle);
                if (StringUtil.isEmpty(title)) {
                    center.setText(newTitle);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.back)
    void back() {
        if (webview.canGoBack())
            webview.goBack();
        else {
            finish();
        }
    }


}
