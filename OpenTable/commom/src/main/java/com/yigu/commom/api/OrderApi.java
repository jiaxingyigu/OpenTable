package com.yigu.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiHistoryResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.MapiUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/12/13.
 */
public class OrderApi extends BasicApi{

    /**
     * 获取食堂列表
     * @param activity
     * @param COMPANY
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getCanteenlist(Activity activity, String COMPANY, String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getCanteenlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("canteen").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 用餐时间列表
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getDinnertime(Activity activity, String SHOP, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getDinnertime,params,new MapiUtil.MapiSuccessResponse(){
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
     * 获取菜单列表
     * @param activity
     * @param SHOP
     * @param DATE
     * @param TYPE
     * @param STYLE
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getFoodmenu(Activity activity,String SHOP,String DATE,String TYPE,String STYLE,String PAGENO,String SIZE,
                                   final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        params.put("DATE",DATE);
        params.put("TYPE",TYPE);
        params.put("STYLE",STYLE);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getFoodmenu,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("food").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 预购
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void preorder(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                String ctype2,String ctype3,String roomservice,final RequestCallback callback,
                                final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        DebugLog.i(params.toString());
        MapiUtil.getInstance().call(activity,preorder,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 食堂预购
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void preordercanteen(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                String ctype2,String ctype3,String roomservice,final RequestCallback callback,
                                final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        DebugLog.i(params.toString());
        MapiUtil.getInstance().call(activity,preordercanteen,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 职工卡支付
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void balancepay(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                  String ctype2,String ctype3,String roomservice,final RequestCallback callback,
                                final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,balancepay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 食堂职工卡支付
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param callback
     * @param exceptionCallback
     */
    public static void balancepaycanteen(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                  String ctype2,String ctype3,String roomservice,final RequestCallback callback,
                                  final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,balancepaycanteen,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 获取商户列表
     * @param activity
     * @param COMPANY
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getMerchantlist(Activity activity, String COMPANY, String PAGENO, String SIZE,String USERNAME, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        if(!TextUtils.isEmpty(USERNAME))
            params.put("USERNAME",USERNAME);
        MapiUtil.getInstance().call(activity,getMerchantlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("merchant").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 获取商户菜单列表
     * @param activity
     * @param SHOP
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getMFoodmenu(Activity activity,String SHOP,String PAGENO,String SIZE,
                                   final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("SHOP", SHOP);
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        MapiUtil.getInstance().call(activity, getMFoodmenu, params, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json=" + json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("food").toJSONString(), MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 获取生活馆列表
     * @param activity
     * @param COMPANY
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getLivinglist(Activity activity, String COMPANY, String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getLivinglist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("living").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 获取生活馆菜单列表
     * @param activity
     * @param SHOP
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getSFoodmenu(Activity activity,String SHOP,String eid,String PAGENO,String SIZE,
                                    final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("eid", eid);
        params.put("SHOP", SHOP);
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        MapiUtil.getInstance().call(activity, getSFoodmenu, params, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json=" + json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("food").toJSONString(), MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 获取订单
     * @param activity
     * @param appuserid
     * @param TYPE
     * @param created
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getSaleslist(Activity activity,String appuserid,String TYPE,String created,String isfinish,String state,String PAGENO,String SIZE,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String, String> params = new HashMap<>();
        params.put("appuserid", appuserid);
        params.put("TYPE", TYPE);
        params.put("created", created);
        params.put("isfinish", isfinish);
        params.put("state", state);
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        MapiUtil.getInstance().call(activity, getSaleslist, params, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json=" + json);
                List<MapiHistoryResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("sales").toJSONString(), MapiHistoryResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     *获取订单详情
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void getSalesdetailslist(Activity activity,String id, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,getSalesdetailslist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiOrderResult.class);
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
     *获取订单详情
     * @param activity
     * @param COMPANY
     * @param callback
     * @param exceptionCallback
     */
    public static void getCooklist(Activity activity,String COMPANY, String PAGENO,String SIZE,final RequestPageCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        MapiUtil.getInstance().call(activity,getCooklist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiHistoryResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("cook").toJSONString(),MapiHistoryResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 支付宝支付
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param bz
     * @param callback
     * @param exceptionCallback
     */
    public static void zhifubaoPay(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                   String ctype2,String ctype3,String roomservice ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("total_amount",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,zhifubaoPay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 食堂支付宝支付
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param bz
     * @param callback
     * @param exceptionCallback
     */
    public static void zhifubaoPaycanteen(Activity activity,String company,String appuserid,String SHOP,String money,String sales,String bz,String seat,String living,String ctype1,
                                   String ctype2,String ctype3,String roomservice ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("total_amount",money);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,zhifubaoPaycanteen,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 微信支付
     * @param activity
     * @param appuserid
     * @param total_fee
     * @param callback
     * @param exceptionCallback
     */
    public static void weixinPay(Activity activity,String company,String appuserid,String SHOP,String money,String total_fee,String sales,String bz,String seat,String living,String ctype1,
                                 String ctype2,String ctype3,String roomservice ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("total_fee",total_fee);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,weixinPay,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 食堂微信支付
     * @param activity
     * @param appuserid
     * @param total_fee
     * @param callback
     * @param exceptionCallback
     */
    public static void weixinPaycanteen(Activity activity,String company,String appuserid,String SHOP,String money,String total_fee,String sales,String bz,String seat,String living,String ctype1,
                                 String ctype2,String ctype3,String roomservice ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("total_fee",total_fee);
        params.put("sales",sales);
        if(!TextUtils.isEmpty(bz))
            params.put("bz",bz);
        if(!TextUtils.isEmpty(living))
            params.put("living",living);
        if(!TextUtils.isEmpty(seat))
            params.put("seat",seat);
        if(!TextUtils.isEmpty(ctype1))
            params.put("ctype1",ctype1);
        if(!TextUtils.isEmpty(ctype2))
            params.put("ctype2",ctype2);
        if(!TextUtils.isEmpty(ctype3))
            params.put("ctype3",ctype3);
        if(!TextUtils.isEmpty(roomservice))
            params.put("roomservice",roomservice);
        MapiUtil.getInstance().call(activity,weixinPaycanteen,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 上传订单
     * @param activity
     * @param appuserid
     * @param SHOP
     * @param money
     * @param sales
     * @param bz
     * @param orderId
     * @param type
     * @param callback
     * @param exceptionCallback
     */
    public static void zhifu(Activity activity,String appuserid,String SHOP,String money,String sales,String bz,String orderId,String type,final RequestCallback callback,
                                final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("money",money);
        params.put("sales",sales);
        params.put("bz",bz);
        params.put("orderId",orderId);
        params.put("type",type);

        MapiUtil.getInstance().call(activity,zhifu,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 获取美食坊列表
     * @param activity
     * @param COMPANY
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getWorkersHomelist(Activity activity, String COMPANY, String PAGENO, String SIZE,String USERNAME ,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("COMPANY",COMPANY);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        if(!TextUtils.isEmpty(USERNAME))
            params.put("USERNAME",USERNAME);
        MapiUtil.getInstance().call(activity,getWorkersHomelist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("wrkershome").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    public static void reservation(Activity activity,String company,String appuserid,String SHOP,String usedate,String dinnertime,String seat,String bz,final RequestCallback callback,
                                   final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("company",company);
        params.put("appuserid",appuserid);
        params.put("SHOP",SHOP);
        params.put("usedate",usedate);
        params.put("dinnertime",dinnertime);
        params.put("seat",seat);
        params.put("bz",bz);
        MapiUtil.getInstance().call(activity,reservation,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 美食坊订座列表
     * @param activity
     * @param appuserid
     * @param state
     *              0-申请中 1-申请成功  2-申请失败
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getSeatlist(Activity activity,String appuserid,String state, String PAGENO, String SIZE,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("state",state);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getSeatlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiHistoryResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("seat").toJSONString(),MapiHistoryResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 删除订单
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void delesales(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,delesales,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    public static void fukuan(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,fukuan,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 取消订单
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void cancelOrder(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,cancelOrder,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 获取店铺送货方式
     * @param activity
     * @param SHOP
     * @param callback
     * @param exceptionCallback
     */
    public static void getDelivery(Activity activity,String SHOP,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("SHOP",SHOP);
        MapiUtil.getInstance().call(activity,getDelivery,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 获取商户列表
     * @param activity
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getCompanys(Activity activity,String PAGENO, String SIZE,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("PAGENO", PAGENO);
        params.put("SIZE", SIZE);
        MapiUtil.getInstance().call(activity, getCompanys, params, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json=" + json);
                List<MapiCampaignResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("company").toJSONString(), MapiCampaignResult.class);
                Integer count = json.getJSONObject("data").getInteger("ISNEXT");
                if (null != count) {
                    callback.success(count, result);
                }

            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }
}
