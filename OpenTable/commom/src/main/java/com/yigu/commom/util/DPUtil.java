/**
 * www.ypn.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package com.yigu.commom.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import com.yigu.commom.application.AppContext;


/**
 * @Filename MapiUtil.java
 * @Description
 * @Version 1.0
 * @Author brain
 */
public class DPUtil {

    public static int dip2px(float dipValue) {
        final float scale = AppContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f*(dipValue>=0?1:-1));
    }

    public static int px2dip(float pxValue) {
        final float scale = AppContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f*(pxValue>=0?1:-1));
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    /**
     * 获取屏幕长宽比
     * @param context
     * @return
     */
    public static float getScreenRate(Context context){
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H/W);
    }


}
