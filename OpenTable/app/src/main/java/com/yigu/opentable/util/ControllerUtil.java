package com.yigu.opentable.util;

import android.content.Intent;

import com.yigu.commom.application.AppContext;
import com.yigu.opentable.MainActivity;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 由主页进入购物车
     */
    public static void go2Purcase() {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }


}
