package com.yigu.commom.sharedpreferences;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.result.MapiUserResult;

/**
 * Created by brain on 2016/6/14.
 */
public class UserSP extends AbstractSP {

    private final static String KEY_SP_USER = "jgj.user";
    private final static String KEY_SP_USER_GUIDE = "user_guide";
    private final static String KEY_SP_Resources = "jgj.resources";
    private final static String KEY_SP_Alias = "user_Alias";
    public UserSP(Context context) {
        super(context);
    }

    public void saveUserBean(MapiUserResult userbean) {
        sharedPreferences.edit().putString(KEY_SP_USER, JSONObject.toJSONString(userbean)).commit();
    }

    public MapiUserResult getUserBean() {
        String userJsonStr = sharedPreferences.getString(KEY_SP_USER, null);
        if (TextUtils.isEmpty(userJsonStr)) {
            return null;
        }
        return JSONObject.parseObject(userJsonStr, MapiUserResult.class);
    }

    public void saveResource(String json){
        sharedPreferences.edit().putString(KEY_SP_Resources, json).commit();
    }

    public String getResource() {
        String resourceJsonStr = sharedPreferences.getString(KEY_SP_Resources, null);
        if (TextUtils.isEmpty(resourceJsonStr)) {
            return null;
        }
        return resourceJsonStr;
    }

    public void setAlias(boolean isAlias){
        sharedPreferences.edit().putBoolean(KEY_SP_Alias, isAlias).commit();
    }

    public boolean getAlias(){
        boolean isAlias = sharedPreferences.getBoolean(KEY_SP_Alias,false);
        return isAlias;
    }

    public boolean checkLogin() {
        return getUserBean() != null && !TextUtils.isEmpty(getUserBean().getUSER_ID());
    }

    public void clearUserData() {
        sharedPreferences.edit().remove(KEY_SP_USER).commit();
        sharedPreferences.edit().remove(KEY_SP_Alias).commit();
    }

    /**
     * 保存版本
     *
     * @param value
     */
    public void saveUserGuide(String value) {
        sharedPreferences.edit().putString(KEY_SP_USER_GUIDE, value).commit();
    }

    /**
     * 获取版本
     *
     * @return
     */
    public String getUserGuide() {
        String code = sharedPreferences.getString(KEY_SP_USER_GUIDE, null);
        if (TextUtils.isEmpty(code)) {
            return null;
        }
        return code;
    }

}
