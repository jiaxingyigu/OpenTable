package com.yigu.opentable.util;


import com.yigu.commom.result.MapiResourceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2016/7/29.
 */
public class TableDataSource {

    public static final int TYPE_UNIT = 0x01;
    public static final int TYPE_TENANT = 0x02;
    public static final int TYPE_LIVE = 0x03;
    public static final int TYPE_WORKERS = 0x04;
    public static final int TYPE_COOK = 0x05;
    public static final int TYPE_nutrition = 0x06;
    public static final int TYPE_personal= 0x07;

    /**
     * 菜单
     * @return
     */
    public static List<MapiResourceResult> getRootResource(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult(TYPE_UNIT,"单位食堂"));
        list.add(new MapiResourceResult(TYPE_TENANT,"商户订餐"));
        list.add(new MapiResourceResult(TYPE_LIVE,"生活馆"));
        list.add(new MapiResourceResult(TYPE_WORKERS,"职工之家"));
        list.add(new MapiResourceResult(TYPE_COOK,"厨师上门"));
        list.add(new MapiResourceResult(TYPE_nutrition,"点营养餐"));
        list.add(new MapiResourceResult(TYPE_personal,"私人营养师"));
        return list;
    }


}
