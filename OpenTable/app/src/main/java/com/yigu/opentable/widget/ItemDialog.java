package com.yigu.opentable.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yigu.commom.util.FileUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zaoren on 2015/6/26.
 */
public class ItemDialog extends Dialog {


    @Bind(R.id.typeOne)
    TextView typeOne;
    @Bind(R.id.typeTwo)
    TextView typeTwo;
    @Bind(R.id.typeThree)
    TextView typeThree;

    private String imagePath;
    private BaseActivity mActivity;

    public ItemDialog(Context context, int theme) {
        super(context, theme);
        mActivity = (BaseActivity) context;
        initView();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private void initView() {
        setContentView(R.layout.dialog_item);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.typeOne, R.id.typeTwo, R.id.typeThree})
    public void onClick(View view) {
        int type=0;
        switch (view.getId()) {
            case R.id.typeOne:
                type = 0;
                break;
            case R.id.typeTwo:
                type = 1;
                break;
            case R.id.typeThree:
                type = 2;
                break;
        }
        if(null!=dialogItemClickListner)
            dialogItemClickListner.onItemClick(view,type);
        dismiss();
    }

    public interface DialogItemClickListner {
        void onItemClick(View view,int type);
    }

    private DialogItemClickListner dialogItemClickListner;

    public void setDialogItemClickListner(DialogItemClickListner dialogItemClickListner){
        this.dialogItemClickListner = dialogItemClickListner;
    }

}
