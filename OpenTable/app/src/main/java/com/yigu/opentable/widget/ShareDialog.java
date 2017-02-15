package com.yigu.opentable.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.yigu.opentable.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/1/7.
 */
public class ShareDialog extends Dialog {

    Context mContext;

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initView();
        initListtener();
    }

    private void initView() {
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    private void initListtener() {

    }

    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.share_wechat_layout, R.id.share_wechat_moments_layout, R.id.share_qq_layout, R.id.share_qzone_layout, R.id.share_cancel_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_wechat_layout:
                if(null!=dialogItemClickListner)
                    dialogItemClickListner.onItemClick(view,0);
                break;
            case R.id.share_wechat_moments_layout:
                if(null!=dialogItemClickListner)
                    dialogItemClickListner.onItemClick(view,1);
                break;
            case R.id.share_qq_layout:
                if(null!=dialogItemClickListner)
                dialogItemClickListner.onItemClick(view,2);
                break;
            case R.id.share_qzone_layout:
                if(null!=dialogItemClickListner)
                    dialogItemClickListner.onItemClick(view,3);
                break;
            case R.id.share_cancel_layout:
                break;
        }
        dismiss();
    }

    public interface DialogItemClickListner {
        void onItemClick(View view, int position);
    }

    private DialogItemClickListner dialogItemClickListner;

    public void setDialogItemClickListner(DialogItemClickListner dialogItemClickListner) {
        this.dialogItemClickListner = dialogItemClickListner;
    }

}
