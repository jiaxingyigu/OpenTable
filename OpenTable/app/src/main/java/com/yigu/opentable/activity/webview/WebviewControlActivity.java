package com.yigu.opentable.activity.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
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

import com.yigu.commom.api.BasicApi;
import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.StringUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.campaign.CampaignMsgActivity;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ShareModule;
import com.yigu.opentable.util.webview.WebChromeClientImpl;
import com.yigu.opentable.util.webview.WebViewUtil;
import com.yigu.opentable.widget.ShareDialog;

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
    @Bind(R.id.iv_right_two)
    ImageView ivRightTwo;

    private String title;
//    private WebBroadCast webBroadCast;

    ShareDialog shareDialog;

    String linkUrl = "";

    String shareTitle = "";
    String shareContext = "";
    String shareLOGO = "";
    AudioManager mAudioManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);
        ExitApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        initView();
        initListener();
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

        linkUrl = getIntent().getStringExtra("url");
        DebugLog.i("linkUrl=" + linkUrl);

        shareTitle = getIntent().getStringExtra("shareTitle");
        shareContext = getIntent().getStringExtra("shareContext");
        shareLOGO = getIntent().getStringExtra("shareLOGO");
        if(isShare){
            ivRightTwo.setImageResource(R.mipmap.share_logo);
            ivRightTwo.setVisibility(View.VISIBLE);
        }

        WebSettings webSetting = webview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webview.addJavascriptInterface(new WebViewUtil(this, webview), "app");
        loadData();

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);

    }

    private void initListener(){
        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position) {
                    case 0://微信好友
                        ShareModule shareModule1 = new ShareModule(WebviewControlActivity.this, shareTitle, shareContext,shareLOGO , linkUrl);
                        shareModule1.startShare(1);
                        break;
                    case 1:
                        ShareModule shareModule2 = new ShareModule(WebviewControlActivity.this, shareTitle, shareContext, shareLOGO, linkUrl);
                        shareModule2.startShare(2);
                        break;
                    case 2:
                        ShareModule shareModule3 = new ShareModule(WebviewControlActivity.this, shareTitle, shareContext, shareLOGO, linkUrl);
                        shareModule3.startShare(3);
                        break;
                    case 3:
                        ShareModule shareModule4 = new ShareModule(WebviewControlActivity.this, shareTitle, shareContext, shareLOGO, linkUrl);
                        shareModule4.startShare(4);
                        break;
                }
            }
        });
    }

    private void loadData() {

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
//                center.setText(view.getTitle());
            }
        });
        webview.setWebChromeClient(new WebChromeClientImpl() {
            @Override
            public void onReceivedTitle(WebView view, String newTitle) {
                super.onReceivedTitle(view, title);
                DebugLog.i("onReceivedTilt=" + newTitle);
                if (StringUtil.isEmpty(title)) {
//                    center.setText(newTitle);
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

    @OnClick(R.id.iv_right_two)
    void share(){
        shareDialog.showDialog();
    }

    boolean isPause = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        webview.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        requestAudioFocus();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
        mAudioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    private void requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            DebugLog.i("audio focus been granted");
        }
    }
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            DebugLog.i("focusChange: " + focusChange);
            if (isPause && focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                requestAudioFocus();
            }
        }
    };

}
