package com.yigu.commom.api;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.MapiUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/10/19.
 */
public class CampaignApi extends BasicApi{

    /**
     * 获取活动列表
     * @param activity
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getActivitylist(Activity activity, String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getActivitylist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiCampaignResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("activity").toJSONString(),MapiCampaignResult.class);
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
     * 活动岗位信息列表
     * @param activity
     * @param NAME
     * @param actid
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getPostlist(Activity activity,String NAME,String actid,String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("NAME",NAME);
        params.put("actid",actid);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getPostlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiCampaignResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("posts").toJSONString(),MapiCampaignResult.class);
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
     * 上传图片
     * @param activity
     * @param file
     * @param callback
     * @param exceptionCallback
     */
    public static void
    uploadImage(Activity activity, File file, final RequestCallback callback, final RequestExceptionCallback exceptionCallback) {
        MapiUtil.getInstance().uploadFile(activity, saveImages, file, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i(json.toString());
                MapiImageResult imageResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiImageResult.class);
                callback.success(imageResult);
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 企业报名
     * @param activity
     * @param actid
     * @param appuserid
     * @param Posts
     * @param name
     * @param scale
     * @param address
     * @param introduction
     * @param license
     * @param tel
     * @param remark
     * @param train
     * @param type
     * @param callback
     * @param exceptionCallback
     */
    public static void comsign(Activity activity,String actid,String appuserid,String Posts,String name,
                               String scale,String address,String introduction,String license,
                               String tel,String remark,String train,
                               String type,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("actid",actid);
        params.put("appuserid",appuserid);
        params.put("Posts",Posts);
        params.put("name",name);
        params.put("scale",scale);
        params.put("address",address);
        params.put("introduction",introduction);
        params.put("license",license);
        params.put("tel",tel);
        params.put("remark",remark);
        params.put("train",train);
        params.put("train",train);
        params.put("type",type);
        MapiUtil.getInstance().call(activity,comsign,params,new MapiUtil.MapiSuccessResponse(){
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
     * 企业新增岗位
     * @param activity
     * @param post
     * @param demand
     * @param callback
     * @param exceptionCallback
     */
    public static void addpost(Activity activity,String post,String demand,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("post",post);
        params.put("demand",demand);
        MapiUtil.getInstance().call(activity,addpost,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                String id = json.getString("data");
                callback.success(id);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(String code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 个人报名
     * @param activity
     * @param posts
     * @param appuserid
     * @param actid
     * @param name
     * @param school
     * @param origo
     * @param speciality
     * @param major
     * @param tel
     * @param remark
     * @param callback
     * @param exceptionCallback
     */
    public static void persign(Activity activity,String posts,String appuserid,String actid,String name,
                               String school,String origo,String speciality,String major,
                               String tel,String remark,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("posts",posts);
        params.put("appuserid",appuserid);
        params.put("actid",actid);
        params.put("name",name);
        params.put("school",school);
        params.put("origo",origo);
        params.put("speciality",speciality);
        params.put("major",major);
        params.put("tel",tel);
        params.put("remark",remark);
        MapiUtil.getInstance().call(activity,persign,params,new MapiUtil.MapiSuccessResponse(){
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
     * 个人报名历史
     * @param activity
     * @param appuserid
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getPersignlist(Activity activity,String appuserid,String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getPersignlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("persigns").toJSONString(),MapiItemResult.class);
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
     * 企业报名历史
     * @param activity
     * @param appuserid
     * @param PAGENO
     * @param SIZE
     * @param callback
     * @param exceptionCallback
     */
    public static void getComsignlist(Activity activity,String appuserid,String PAGENO, String SIZE, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("appuserid",appuserid);
        params.put("PAGENO",PAGENO);
        params.put("SIZE",SIZE);
        MapiUtil.getInstance().call(activity,getComsignlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("consigns").toJSONString(),MapiItemResult.class);
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
     * 活动入口
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void activityUrl(Activity activity,String id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        DebugLog.i(params.toString());
        MapiUtil.getInstance().call(activity,activityUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiCampaignResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiCampaignResult.class);
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
     * 个人报名-判断
     * @param activity
     * @param actid
     * @param appuserid
     * @param callback
     * @param exceptionCallback
     */
    public static void findPersign(Activity activity,String actid,String appuserid,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("actid",actid);
        params.put("appuserid",appuserid);
        DebugLog.i(params.toString());
        MapiUtil.getInstance().call(activity,findPersign,params,new MapiUtil.MapiSuccessResponse(){
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
     * 企业报名-判断
     * @param activity
     * @param actid
     * @param appuserid
     * @param callback
     * @param exceptionCallback
     */
    public static void findComsign(Activity activity,String actid,String appuserid,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("actid",actid);
        params.put("appuserid",appuserid);
        DebugLog.i(params.toString());
        MapiUtil.getInstance().call(activity,findComsign,params,new MapiUtil.MapiSuccessResponse(){
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

}
