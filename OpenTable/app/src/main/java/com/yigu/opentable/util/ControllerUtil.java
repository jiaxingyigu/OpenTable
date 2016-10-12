package com.yigu.opentable.util;

import android.content.Intent;

import com.yigu.commom.application.AppContext;
import com.yigu.opentable.activity.BandActivity;
import com.yigu.opentable.activity.BandNextActivity;
import com.yigu.opentable.activity.ForgetPsdActivity;
import com.yigu.opentable.activity.MainActivity;
import com.yigu.opentable.activity.PersonActivity;
import com.yigu.opentable.activity.RegisterActivity;
import com.yigu.opentable.activity.campaign.CampaignActivity;
import com.yigu.opentable.activity.order.OrderActivity;


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
}
