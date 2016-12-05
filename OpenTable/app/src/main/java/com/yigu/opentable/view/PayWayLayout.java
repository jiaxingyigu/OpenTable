package com.yigu.opentable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yigu.opentable.R;

import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/2.
 */
public class PayWayLayout extends LinearLayout{

    private Context mContext;
    private View view;

    public PayWayLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PayWayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public PayWayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_way, this);
        ButterKnife.bind(this, view);
    }

}
