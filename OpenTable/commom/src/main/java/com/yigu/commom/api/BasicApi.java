package com.yigu.commom.api;

/**
 * Created by brain on 2016/6/14.
 */
public class BasicApi {
    public static String BASIC_URL = "http://122.225.92.10:8081";//http://115.159.118.182:8081/dinner
    public static String BASIC_IMAGE = "http://122.225.92.10:8081/uploadFiles/uploadImgs/";
    public static String loginUrl = "/appuser/getLogin.do?";
    /**注册-验证码*/
    public static String normal = "/appuser/normal";
    /**注册*/
    public static String register= "/appuser/register.do";
    /**忘记密码-修改密码验证码*/
    public static String upnormal= "/appuser/upnormal";
    /**修改密码*/
    public static String editPassword= "/appuser/editPassword";
    /**获取活动列表*/
    public static String getActivitylist= "/appuser/getActivitylist";
    /**活动岗位信息列表*/
    public static String getPostlist= "/appuser/getPostlist";
    /**图片新增*/
    public static String saveImages= "/appuser/saveImages";
    /**企业报名*/
    public static String comsign= "/appuser/comsign";
    /**企业新增岗位*/
    public static String addpost= "/appuser/addpost";
    /**个人报名*/
    public static String persign= "/appuser/persign";
    /**个人报名历史*/
    public static String getPersignlist= "/appuser/getPersignlist.do";
    /**企业报名历史*/
    public static String getComsignlist= "/appuser/getComsignlist.do";
    /**关于我们*/
    public static String aboutus= "/appuser/aboutus";
    /**首页*/
    public static String getmain= "/appuser/getmain.do";
    /**活动入口*/
    public static String activityUrl = "/appuser/findActivity";
    /**个人报名-判断*/
    public static String findPersign = "/appuser/findPersign";
    /**企业报名-判断*/
    public static String findComsign = "/appuser/findComsign";
}
