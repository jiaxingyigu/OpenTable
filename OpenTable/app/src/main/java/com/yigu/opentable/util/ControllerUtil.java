package com.yigu.opentable.util;

import android.content.Intent;

import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.opentable.activity.AboutUSActivity;
import com.yigu.opentable.activity.BandActivity;
import com.yigu.opentable.activity.BandNextActivity;
import com.yigu.opentable.activity.ForgetPsdActivity;
import com.yigu.opentable.activity.LoginActivity;
import com.yigu.opentable.activity.MainActivity;
import com.yigu.opentable.activity.ModifyPsdActivity;
import com.yigu.opentable.activity.PersonActivity;
import com.yigu.opentable.activity.RegisterActivity;
import com.yigu.opentable.activity.ShowBigPicActivity;
import com.yigu.opentable.activity.campaign.CampaignActivity;
import com.yigu.opentable.activity.campaign.CampaignMsgActivity;
import com.yigu.opentable.activity.campaign.CompanyAddActivity;
import com.yigu.opentable.activity.campaign.JobDetailActivity;
import com.yigu.opentable.activity.campaign.PersonAddActivity;
import com.yigu.opentable.activity.campaign.SelJobActivity;
import com.yigu.opentable.activity.order.OrderActivity;
import com.yigu.opentable.activity.set.EnrollActivity;
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
    public static void go2WebView(String url, String title, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
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
    public static void go2CampaignMsg(String actid) {
        Intent intent = new Intent(AppContext.getInstance(), CampaignMsgActivity.class);
        intent.putExtra("actid",actid);
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

}
