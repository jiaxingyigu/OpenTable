package com.yigu.commom.api;

/**
 * Created by brain on 2016/6/14.
 */
public class BasicApi {
    public static String BASIC_URL = "http://fengzhiyue.com";//http://115.159.118.182:8081/dinner  http://fengzhiyue.com
    public static String BASIC_IMAGE = "http://fengzhiyue.com/uploadFiles/uploadImgs/";//

    public static String SHARE_APP_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.yigu.opentable";//分享app的下载地址
    public static String LOGO_URL = BASIC_URL+ "/uploadFiles/uploadImgs/ic_launcher.png";//分享app的下载地址
    public static String loginUrl = "/appuser/getLogin.do?";
    public static String SHARE_ORDER_DETAIL = "/apphtml/foodintroduce?id=";//详情分享
    public static String SHARE_SHOP_LIST = "/apphtml/list?SHOP=";//菜单列表分享
    public static String SHARE_ACTIVITY_DETAIL = "/apphtml/activity?id=";//活动详情
    public static String  SHARE_LIVE_LIST = "/apphtml/livinglist?eid=";//生活馆菜单列表分享
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
    /**支付宝支付*/
    public static String zhifubaoPay = "/appuser/zhifubaoPay";
    /**上传订单*/
    public static String zhifu = "/appuser/zhifu";
    /**微信支付*/
    public static String weixinPay = "/appuser/weixinPay";
    /**个人中心*/
    public static String personal = "/appuser/personal";
    /**提现*/
    public static String withdrawals = "/appuser/withdrawals";
    /**平台信息*/
    public static String knowledge = "/appuser/knowledge";
    /**问题反馈*/
    public static String comment = "/appuser/comment";
    /**美食坊列表*/
    public static String getWorkersHomelist = "/appuser/getWorkersHomelist";
    /**美食坊订座*/
    public static String reservation = "/appuser/reservation";
    /**美食坊订座列表*/
    public static String getSeatlist = "/appuser/getSeatlist";
    public static String fukuan = "/appuser/fukuan";
    public static String delesales = "/appuser/delesales";
    /**我的消息*/
    public static String getMessages = "/appuser/getMessages";
    /**获取单位*/
    public static String getNCompanylist = "/appuser/getNCompanylist";
    /**获取部门科室接口*/
    public static String getDepartment = "/appuser/getDepartment";
    /**取消订单*/
    public static String cancelOrder = "/appuser/cancelOrder";
    /**获取店铺送货方式*/
    public static String getDelivery = "/appuser/getDelivery";
    /**单位列表*/
    public static String getCompanys = "/appuser/getCompanys";

    /**食堂预购*/
    public static String preordercanteen = "/appuser/preordercanteen";
    /**食堂职工卡*/
    public static String balancepaycanteen = "/appuser/balancepaycanteen";
    /**食堂支付宝*/
    public static String zhifubaoPaycanteen = "/appuser/zhifubaoPaycanteen";
    /**食堂微信*/
    public static String weixinPaycanteen = "/appuser/weixinPaycanteen";
    /**活动报名-普通*/
    public static String signup = "/appuser/signup";

    /**其他活动报名列表*/
    public static String getsignuplist = "/appuser/getsignuplist";

}
