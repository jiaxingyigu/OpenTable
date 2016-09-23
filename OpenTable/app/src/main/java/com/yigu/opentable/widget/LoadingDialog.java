package com.yigu.opentable.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.yigu.shop.R;

/**
 * Created by brain on 2016/6/16.
 */
public class LoadingDialog extends Dialog{
    private Activity activity;
//    private ImageView loadingIv;
    public LoadingDialog(Context context) {
        super(context);
        activity = (Activity)context;
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        activity = (Activity)context;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
//        loadingIv = (ImageView) findViewById(R.id.loading_iv);
//        Animation animation = AnimationUtils.loadAnimation(activity,
//                R.anim.anim_dialog_loading);
//        loadingIv.startAnimation(animation);
    }
}
