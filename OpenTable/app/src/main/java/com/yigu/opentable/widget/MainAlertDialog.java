package com.yigu.opentable.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class MainAlertDialog extends Dialog {
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.left_button)
    TextView mLeftButton;
    @Bind(R.id.right_button)
    TextView mRightButton;
    @Bind(R.id.dell_ll)
    LinearLayout dellLl;
    @Bind(R.id.divider_view)
    View divider_view;


    public MainAlertDialog(Context context) {
        super(context, R.style.dialog_theme);
        initView();
    }

    public MainAlertDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    public MainAlertDialog setLeftBtnText(String str) {
        if (str.length() > 0) {
            mLeftButton.setText(str);
            mLeftButton.setVisibility(View.VISIBLE);
        } else {
            mLeftButton.setVisibility(View.GONE);
        }
        return this;
    }

    public MainAlertDialog setRightBtnText(String str) {
        if (str.length() > 0) {
            mRightButton.setText(str);
            mRightButton.setVisibility(View.VISIBLE);
        } else {
            mRightButton.setVisibility(View.GONE);
        }
        return this;
    }

    public MainAlertDialog setDellHide(boolean hide) {
        if(hide) {
            dellLl.setVisibility(View.GONE);
            divider_view.setVisibility(View.GONE);
        }else {
            dellLl.setVisibility(View.VISIBLE);
            divider_view.setVisibility(View.VISIBLE);
        }
        return this;
    }


    private void initView() {
        setContentView(R.layout.dialog_alert);
        ButterKnife.bind(this);
    }

    public MainAlertDialog setTitle(String str) {
        mTitle.setText(str);
        return this;
    }

    public String getTitle(){
        return mTitle.getText().toString();
    }

    public MainAlertDialog setLeftClickListener(View.OnClickListener clickListener) {
        mLeftButton.setOnClickListener(clickListener);
        return this;
    }

    public MainAlertDialog setRightClickListener(View.OnClickListener clickListener) {
        mRightButton.setOnClickListener(clickListener);
        return this;
    }

    public void setShopCartTextSize() {
        mLeftButton.setTextSize(DPUtil.dip2px(8));
        mTitle.setTextSize(DPUtil.dip2px(7));
    }
}
