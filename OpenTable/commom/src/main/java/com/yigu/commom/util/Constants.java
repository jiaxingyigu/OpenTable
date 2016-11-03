package com.yigu.commom.util;

import org.xutils.common.util.MD5;

import java.util.Date;

/**
 * Created by brain on 2016/6/14.
 * @Description
 *          定义常用字段
 */
public class Constants {
    public static final String Token = "Token";
    public static final String Token_VALUE = MD5.md5("ANT"+DateUtil.getInstance().date2YMD_N(new Date())+",yg,");
}
