package com.yigu.commom.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brain on 2016/6/14
 */
public abstract class AbstractSP {
    protected SharedPreferences sharedPreferences;
    protected Context context;
    public AbstractSP(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }
}
