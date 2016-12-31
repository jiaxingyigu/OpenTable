package com.yigu.commom.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.MapiUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/7/22.
 */
public class CommonApi extends BasicApi{

    //导入资源
    public static void loadResources(final Activity act,String appuserid, final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        MapiUtil.getInstance().call(act,getmain, params,
                new MapiUtil.MapiSuccessResponse() {
                    @Override
                    public void success(JSONObject json) {
                        DebugLog.i("resourcejson"+json);
                       /* Map<String, ArrayList<MapiResourceResult>> userBean = JSON.parseObject(json
                                        .getJSONObject("data").toJSONString(),
                                new TypeReference<Map<String, ArrayList<MapiResourceResult>>>() {
                                });*/
                        callback.success(json.toJSONString());
                    }
                }, new MapiUtil.MapiFailResponse() {
                    @Override
                    public void fail(String code,String message) {
                        exceptionCallback.error(code,message);
                    }
                });
    }

    /**
     * 支付方式列表
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getPayment(Activity activity, String SHOP, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getPayment,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     *商户地址填写说明备注
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getRemark(Activity activity,String SHOP,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getRemark,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                String tip = json.getString("data");
                callback.success(tip);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 内部广告
     * @param act
     * @param COMPANY
     * @param callback
     * @param exceptionCallback
     */
    public static void getAdvertisement(final Activity act,String COMPANY, final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        MapiUtil.getInstance().call(act,getAdvertisement, params,
                new MapiUtil.MapiSuccessResponse() {
                    @Override
                    public void success(JSONObject json) {
                        DebugLog.i("json="+json);
                        List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                        callback.success(result);
                    }
                }, new MapiUtil.MapiFailResponse() {
                    @Override
                    public void fail(String code,String message) {
                        exceptionCallback.error(code,message);
                    }
                });
    }

}
