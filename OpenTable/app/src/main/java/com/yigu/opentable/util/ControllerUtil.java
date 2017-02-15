package com.yigu.opentable.util;

import android.content.Intent;

import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.opentable.activity.AboutUSActivity;
import com.yigu.opentable.activity.BandActivity;
import com.yigu.opentable.activity.BandNextActivity;
import com.yigu.opentable.activity.CampaignEnterActivity;
import com.yigu.opentable.activity.ExpendInfoActivity;
import com.yigu.opentable.activity.ForgetPsdActivity;
import com.yigu.opentable.activity.HistoryOrderActivity;
import com.yigu.opentable.activity.LoginActivity;
import com.yigu.opentable.activity.MainActivity;
import com.yigu.opentable.activity.ModifyPsdActivity;
import com.yigu.opentable.activity.PersonActivity;
import com.yigu.opentable.activity.RegisterActivity;
import com.yigu.opentable.activity.ShopEnterActivity;
import com.yigu.opentable.activity.ShowBigPicActivity;
import com.yigu.opentable.activity.TroubleActivity;
import com.yigu.opentable.activity.WithdrawActivity;
import com.yigu.opentable.activity.campaign.CampaignActivity;
import com.yigu.opentable.activity.campaign.CampaignMsgActivity;
import com.yigu.opentable.activity.campaign.CompanyAddActivity;
import com.yigu.opentable.activity.campaign.JobDetailActivity;
import com.yigu.opentable.activity.campaign.PersonAddActivity;
import com.yigu.opentable.activity.campaign.SelJobActivity;
import com.yigu.opentable.activity.cook.CookListActivity;
import com.yigu.opentable.activity.history.TenantHistoryDetailActivity;
import com.yigu.opentable.activity.history.UnitHistoryDetailActivity;
import com.yigu.opentable.activity.live.LiveListActivity;
import com.yigu.opentable.activity.live.LiveMenuActivity;
import com.yigu.opentable.activity.order.OrderActivity;
import com.yigu.opentable.activity.order.OrderDetailActivity;
import com.yigu.opentable.activity.order.OrderListActivity;
import com.yigu.opentable.activity.order.UnitOrderActivity;
import com.yigu.opentable.activity.pay.LivePayActivity;
import com.yigu.opentable.activity.pay.PaymentActivity;
import com.yigu.opentable.activity.pay.TenantPayActivity;
import com.yigu.opentable.activity.platform.PlatformActivity;
import com.yigu.opentable.activity.purcase.PurcaseActivity;
import com.yigu.opentable.activity.set.EnrollActivity;
import com.yigu.opentable.activity.tenant.TenantListActivity;
import com.yigu.opentable.activity.tenant.TenantMenuActivity;
import com.yigu.opentable.activity.webview.WebviewControlActivity;
import com.yigu.opentable.adapter.set.PersonEnrollAdapter;

import java.util.ArrayList;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 主页
     */
    public static void go2Main() {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 由登录页进入注册
     */
    public static void go2Register() {
        Intent intent = new Intent(AppContext.getInstance(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 密码找回
     */
    public static void ForgetPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ForgetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 绑定单位
     */
    public static void go2Band() {
        Intent intent = new Intent(AppContext.getInstance(), BandActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 绑定单位下一步
     */
    public static void go2BandNext() {
        Intent intent = new Intent(AppContext.getInstance(), BandNextActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订餐管理
     */
    public static void go2Order() {
        Intent intent = new Intent(AppContext.getInstance(), OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 活动报名
     */
    public static void go2Campaign() {
        Intent intent = new Intent(AppContext.getInstance(), CampaignActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 个人中心
     */
    public static void go2Person() {
        Intent intent = new Intent(AppContext.getInstance(), PersonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * h5页面
     */
    public static void go2WebView(String url, String title,String shareTitle,String shareContext,String shareLOGO, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
        intent.putExtra("shareTitle", shareTitle);

        intent.putExtra("shareContext", shareContext);
        intent.putExtra("shareLOGO", shareLOGO);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 活动报名-个人
     */
    public static void go2PersonAdd(String actid,String type) {
        Intent intent = new Intent(AppContext.getInstance(), PersonAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("actid",actid);
        intent.putExtra("type",type);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 活动报名-个人-岗位选择
     */
    public static void go2SelJob(String actid) {
        Intent intent = new Intent(AppContext.getInstance(), SelJobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("actid",actid);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 活动报名-企业
     */
    public static void go2CompanyAdd(String actid,String type) {
        Intent intent = new Intent(AppContext.getInstance(), CompanyAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("actid",actid);
        intent.putExtra("type",type);
        AppContext.getInstance().startActivity(intent);
    }


    /**
     * 显示大图
     */
    public static void go2ShopPic(int position, ArrayList<MapiImageResult> list) {
        Intent intent = new Intent(AppContext.getInstance(), ShowBigPicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("position",position);
        intent.putExtra("list",list);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录
     */
    public static void go2Login() {
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 密码找回
     */
    public static void go2ModifyPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 我的报名
     */
    public static void go2Enroll() {
        Intent intent = new Intent(AppContext.getInstance(), EnrollActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 关于我们
     */
    public static void go2AboutUS() {
        Intent intent = new Intent(AppContext.getInstance(), AboutUSActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 活动入口
     */
    public static void go2CampaignMsg(String actid,String info,String title,String pic) {
        Intent intent = new Intent(AppContext.getInstance(), CampaignMsgActivity.class);
        intent.putExtra("actid",actid);
        intent.putExtra("info",info);
        intent.putExtra("title",title);
        intent.putExtra("pic",pic);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 岗位详情
     */
    public static void go2JobDetail(MapiCampaignResult result) {
        Intent intent = new Intent(AppContext.getInstance(), JobDetailActivity.class);
        intent.putExtra("item",result);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-列表
     */
    public static void go2UnitOrder() {
        Intent intent = new Intent(AppContext.getInstance(), UnitOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-列表
     */
    public static void go2OrderList(MapiOrderResult mapiOrderResult) {
        Intent intent = new Intent(AppContext.getInstance(), OrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", mapiOrderResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-详情
     */
    public static void go2OrderDetail(String id,String title) {
        Intent intent = new Intent(AppContext.getInstance(), OrderDetailActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-购物车
     */
    public static void go2Purcase() {
        Intent intent = new Intent(AppContext.getInstance(), PurcaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位食堂-结算
     */
    public static void go2Payment(String SHOP) {
        Intent intent = new Intent(AppContext.getInstance(),PaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SHOP",SHOP);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 商户-结算
     */
    public static void go2TenantPay(String SHOP,boolean hasAddr) {
        Intent intent = new Intent(AppContext.getInstance(),TenantPayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SHOP",SHOP);
        intent.putExtra("hasAddr",hasAddr);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 生活馆-结算
     */
    public static void go2LivePay(String SHOP,boolean hasBZ) {
        Intent intent = new Intent(AppContext.getInstance(),LivePayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("SHOP",SHOP);
        intent.putExtra("hasBZ",hasBZ);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 绑定单位下一步
     */
    public static void go2CampaignEnter() {
        Intent intent = new Intent(AppContext.getInstance(), CampaignEnterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 商家入驻
     */
    public static void go2ShopEnter() {
        Intent intent = new Intent(AppContext.getInstance(),ShopEnterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 商户订餐-列表
     */
    public static void go2TenantList() {
        Intent intent = new Intent(AppContext.getInstance(), TenantListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 商户订餐-菜单列表
     */
    public static void go2TenantMenu(MapiOrderResult mapiOrderResult) {
        Intent intent = new Intent(AppContext.getInstance(), TenantMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", mapiOrderResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 生活馆-列表
     */
    public static void go2LiveList() {
        Intent intent = new Intent(AppContext.getInstance(), LiveListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 生活馆-菜单列表
     */
    public static void go2LiveMenu(MapiOrderResult mapiOrderResult) {
        Intent intent = new Intent(AppContext.getInstance(), LiveMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", mapiOrderResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订单中心
     */
    public static void go2HistoryOrder() {
        Intent intent = new Intent(AppContext.getInstance(), HistoryOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 单位订单详情
     */
    public static void go2UnitHistoryDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), UnitHistoryDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 商户订单详情
     */
    public static void go2TenantHistoryDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), TenantHistoryDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 厨师-列表
     */
    public static void go2CookList() {
        Intent intent = new Intent(AppContext.getInstance(), CookListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 提现
     */
    public static void go2Withdraw() {
        Intent intent = new Intent(AppContext.getInstance(), WithdrawActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 平台信息
     */
    public static void go2Platform() {
        Intent intent = new Intent(AppContext.getInstance(), PlatformActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 问题反馈
     */
    public static void go2Trouble() {
        Intent intent = new Intent(AppContext.getInstance(), TroubleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 推广政策
     */
    public static void go2ExpendInfo() {
        Intent intent = new Intent(AppContext.getInstance(), ExpendInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

}
