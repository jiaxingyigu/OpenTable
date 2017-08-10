package com.yigu.commom.util;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.application.AppContext;

import java.io.File;
import java.util.Map;

/**
 *  Created by brain on 2016/6/14.
 */
public class MapiUtil {
    private volatile static MapiUtil mapiUtil;
    private  Map<String, String> head = null;
    private RequestQueue requestQueue;

    public static  MapiUtil getInstance() {
        if (mapiUtil == null) {
            synchronized (MapiUtil.class) {
                mapiUtil = new MapiUtil();
            }
        }
        return mapiUtil;
    }

    private MapiUtil() {
        requestQueue = Volley.newRequestQueue(AppContext.getInstance());
    }


    /**
     * volley get方式
     *
     * @param activity
     * @param url
     * @param response
     * @param fail
     */
   /* public void getCall(Activity activity, String url, final MapiSuccessResponse response, final MapiFailResponse fail) {
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DebugLog.i("mapi response" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        response.success(jsonObject);
                        Integer code = jsonObject.getInteger("errcode");
                        if (code != null) {
                            fail.fail(code, jsonObject.getString("errmsg"));
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("error=" + error);
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                return super.parseNetworkResponse(response);
                String str = null;
                try {
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(stringRequest);
    }*/

    /**
     * 网络通信volley
     *
     * @param act      类名，广播
     * @param url      接口地址
     * @param params   传递的参数
     * @param response 成功返回数据的接口
     * @param fail     返回异常的接口
     */
    public void call(final Activity act, final String url, final Map<String, String> params,
                     final MapiSuccessResponse response, final MapiFailResponse fail) {
        if (params != null)
//            DebugLog.i("params=" + params.toString());
        DebugLog.i("url=" + BasicApi.BASIC_URL + url);
        DebugLog.i(Constants.Token_VALUE);
        params.put(Constants.Token, Constants.Token_VALUE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BasicApi.BASIC_URL + url,
                new Response.Listener<String>() {
                    public void onResponse(String s) {
                        DebugLog.i("mapi response" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getString("result").equals("01")||jsonObject.getString("result").equals("02")) {
                            response.success(jsonObject);
                        }
                        String code = jsonObject.getString("result");
                        if (code.equals("9998")) {
                            //打开登录UI
                            if (act == null) {
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setAction("com.ypn.mobile.login");
                            act.sendBroadcast(intent);
                            return;
                        }
                        if (fail != null && code.equals("00")) {
                            fail.fail(code, jsonObject.getString("data"));//参数不满足条件
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugLog.e("volleyError=" + volleyError);
                if (volleyError != null) {
                    if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        fail.fail("9999", "oops！网络异常请重新连接");
                    } else {
                        fail.fail("9999", volleyError.getMessage());
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> heads = initHead();
//
//                return heads;
//            }
        };
        requestQueue.add(stringRequest);
//        requestQueue.start();//初始化的时候就已经调用
    }


   /* public void uploadFile(final Activity activity, String url, File file, final MapiSuccessResponse response, final MapiFailResponse fail) {
        DebugLog.i("url=" +BasicApi.BASIC_URL + url );
        String uploadUrl = BasicApi.BASIC_URL + url;
        RequestParams params = new RequestParams(uploadUrl);
//        Map<String, String> heads = initHead();//用户信息
//        for (String key : heads.keySet()) {
//            params.addHeader(key, heads.get(key));
//        }
        // 使用multipart表单上传文件
        params.setMultipart(true);
        params.addBodyParameter("file", file ,null); // 如果文件没有扩展名, 最好设置contentType参数.
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                DebugLog.i("mapi response "+result);
                 JSONObject jsonObject = JSONObject.parseObject(result);
               if (jsonObject.getString("result").equals("01")) {
                    response.success(jsonObject);
                }
               String code = jsonObject.getString("result");
//                if (code == 9998) {//打开登录UI
//                    Intent intent = new Intent();
//                    intent.setAction("com.ypn.mobile.login");
//                    activity.sendBroadcast(intent);
//                    return;
////                }
                if (fail != null && !code.equals("0")) {
                    fail.fail(code, jsonObject.getString("message"));
                }
            }

            @Override
            public void onError(Throwable throwable, boolean isOnCallback) {
                MainToast.showLongToast("error");
                if (throwable instanceof TimeoutError || throwable instanceof NoConnectionError) {
                    DebugLog.i("1111"+throwable.getMessage());
                    fail.fail("9999", "oops！网络异常请重新连接");
                } else {
                    fail.fail("9999", throwable.getMessage());
                    DebugLog.i("9999"+throwable.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });


    }*/

    private static HttpUtils httpUtils;
    private static int out_time = 1000*30;
    /**
     * 返回访问网络的httputils对象（xutils）
     * @return
     */
    public static HttpUtils getHttpUtils(){
        if(httpUtils==null){
            httpUtils = new HttpUtils(out_time);
            httpUtils.configSoTimeout(out_time);
            httpUtils.configResponseTextCharset("UTF-8");
        }
        return httpUtils;
    }

    public void uploadFile(final Activity activity, String url, File file, final MapiSuccessResponse response, final MapiFailResponse fail) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", file);
        DebugLog.i("url=" +BasicApi.BASIC_URL + url );
        HttpHandler<String> httpHandler = getHttpUtils().send(HttpRequest.HttpMethod.POST,
                BasicApi.BASIC_URL + url , params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (arg0.getCause() instanceof TimeoutError || arg0.getCause() instanceof NoConnectionError) {
                            fail.fail("9999", "oops！网络异常请重新连接");
                        } else {
                            fail.fail("9999", arg0.getMessage());
                        }
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        DebugLog.i("mapi response"+arg0.result);
                        JSONObject jsonObject = JSONObject.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("01")||jsonObject.getString("result").equals("02")) {
                            response.success(jsonObject);
                        }
                        String code = jsonObject.getString("result");
//                if (code == 9998) {//打开登录UI
//                    Intent intent = new Intent();
//                    intent.setAction("com.ypn.mobile.login");
//                    activity.sendBroadcast(intent);
//                    return;
////                }
                        if (fail != null && !code.equals("01")) {
                            fail.fail(code, jsonObject.getString("message"));
                        }
                    }
                });
    }

   /* public Map<String, String> initHead() {
        if (head == null) {
            head = new HashMap<>();
            //参考 http://wiki.fredzhu.com/bin/view/Main/HTTP%E4%BA%A4%E4%BA%92%E8%AF%B4%E6%98%8E 说明设置值
            head.put(Constant.APPKEY, Constant.APPKEY_VALUE);
            head.put(Constant.PLATFORM, Constant.PLATFORM_VALUE);
            head.put(Constant.VERSION, Constant.VERSION_VALUE);
            head.put(Constant.NETWORK_TYPE_KEY, Constant.NETWORK_TYPE);
        }
        UserSP sp = new UserSP(AppContext.getInstance());
        MapiUserResult user = sp.getUserBean();
        if (user != null) {
            head.put(Constant.USER_ID, user.getId().toString());
            DebugLog.i("session=" + user.getSession() + "userId=" + user.getId());
            head.put(Constant.USER_SESSION, user.getSession());
        } else {
            head.put(Constant.USER_ID, "");
            head.put(Constant.USER_SESSION, "");
        }
        return head;
    }*/

    public interface MapiSuccessResponse {

        void success(JSONObject json);

    }

    public interface MapiFailResponse {

        void fail(String code, String failMessage);

    }
}
