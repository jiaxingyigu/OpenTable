package com.yigu.opentable.util.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.StringUtil;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/2/19.
 */
public class WebViewUtil extends BasicWebViewUrl {
    private static Map<String, String> WEB_HEAD;
    private static final String brand_search = "/search\\?brandId=(\\d+)&title=([^&]*)";
    private static final String vcate_search = "/search\\?vcateId=(\\d+)&title=([^&]*)";
    private static final String sort_search = "/search\\?vcateId=(\\d+)&sort=([^&]*)&order=([^&]*)&title=([^&]*)";
    private static final String item_detail = "/item/detail\\?sku=([^&]*)";
    private static final String user_login = "/user/login";
    private static final String user_success = "/user/success";
    private static final String channel_brand_more = "/channel/brand/more";
    private static final String tel = "tel://(\\d+)";
    private static final String password_success = "/ws/password/success";
    private static final String profit = "/ws/profit";
    private static final String order_list = "/ws/order/list";

    private Activity mContext;
    private WebView webview;

    public WebViewUtil(Activity mContext, WebView webview) {
        this.mContext = mContext;
        this.webview = webview;
    }

    public static boolean shouldOverrideUrlLoading(final BaseActivity activity, final WebView webView, String url) {
        DebugLog.i("url=" + url);
        if (StringUtil.isEmpty(url)) {
            return false;
        }
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            DebugLog.e("url decode fail,msg=" + e.getMessage());
        }

        return false;
    }


    /**
     * @return h5页面请求的头部信息，用户信息
     */
    public static Map<String, String> getWebviewHeader() {
        if (WEB_HEAD == null) {
            WEB_HEAD = new HashMap<>();
        }
       /* UserSP sp = new UserSP(AppContext.getInstance());
        MapiUserResult user = sp.getUserBean();
        if (user != null) {
            WEB_HEAD.put(Constant.USER_ID, user.getId().toString());
            WEB_HEAD.put(Constant.USER_SESSION, user.getSession());
        } else {
            WEB_HEAD.put(Constant.USER_ID, "");
            WEB_HEAD.put(Constant.USER_SESSION, "");
        }
        WEB_HEAD.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        WEB_HEAD.put(Constant.PLATFORM, Constant.PLATFORM_VALUE);
        WEB_HEAD.put(Constant.VERSION, Constant.VERSION_VALUE);
        WEB_HEAD.put(Constant.NETWORK_TYPE_KEY, Constant.NETWORK_TYPE);
        WEB_HEAD.put(Constant.CHANNEL_SOURCE, Constant.CHANNEL_SOURCE_VALUE);*/
        return WEB_HEAD;
    }

    /**
     * 该方法被浏览器端调用
     */
    @JavascriptInterface
    public void clickOnAndroid(final String name){
        DebugLog.i("name="+name);
        mHandler.post(new Runnable() {
            public void run() {
                try {

                    if(!"00".equals(name)){
                        String[] strs = name.split("-");
                        if(null!=strs&&strs.length>=3){
                            if(strs[2].equals("1")){
                                ControllerUtil.go2PersonAdd(strs[0],strs[1]);
                            }else{
                                ControllerUtil.go2CompanyAdd(strs[0],strs[1]);
                            }

                        }
                    }else{
                        MainToast.showShortToast("您已报名，请不要重复报名！");
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }



            }
        });
    }

    private Handler mHandler = new Handler();

}
