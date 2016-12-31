package com.yigu.commom.api;

/**
 * Created by brain on 2016/6/14.
 */
public class BasicApi {
    public static String BASIC_URL = "http://115.159.118.182:8081/dinner";//http://122.225.92.10:8081
    public static String BASIC_IMAGE = "http://115.159.118.182:8081/dinner/uploadFiles/uploadImgs/";
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
    /**单位入驻*/
    public static String enterCampaign = "/appuser/application";
    /**获取单位列表*/
    public static String getCompanylist = "/appuser/getCompanylist";
    /**绑定单位*/
    public static String binding = "/appuser/binding";
    /**获取食堂列表*/
    public static String getCanteenlist = "/appuser/getCanteenlist";
    /**用餐时间列表*/
    public static String getDinnertime = "/appuser/getDinnertime";
    /**获取菜单列表*/
    public static String getFoodmenu = "/appuser/getFoodmenu";
    /**预购*/
    public static String preorder = "/appuser/preorder";
    /**职工卡支付*/
    public static String balancepay = "/appuser/balancepay";
    /**商家入住*/
    public static String applicationshop = "/appuser/applicationshop";
    /**商户列表*/
    public static String getMerchantlist = "/appuser/getMerchantlist";
    /**商户菜单列表*/
    public static String getMFoodmenu = "/appuser/getMFoodmenu";
    /**生活馆列表*/
    public static String getLivinglist = "/appuser/getLivinglist";
    /**生活馆菜单列表*/
    public static String getSFoodmenu = "/appuser/getSFoodmenu";
    /**支付方式列表*/
    public static String getPayment = "/appuser/getPayment";
    /**商户地址填写说明备注*/
    public static String getRemark = "/appuser/getRemark";
    /**内部广告*/
    public static String getAdvertisement = "/appuser/getAdvertisement";
    /**获取订单*/
    public static String getSaleslist = "/appuser/getSaleslist";
    /**获取订单详情*/
    public static String getSalesdetailslist = "/appuser/getSalesdetailslist";
    /**获取厨师列表*/
    public static String getCooklist = "/appuser/getCooklist";
    /**支付宝*/
    public static String zhifubaoPay = "/appuser/zhifubaoPay";
    /**上传订单*/
    public static String zhifu = "/appuser/zhifu";
}
