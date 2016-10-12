package com.yigu.opentable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yigu.opentable.R;

import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/11.
 */
public class OrderSliderLayout extends RelativeLayout{
    private Context mContext;
    private View view;

    public OrderSliderLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public OrderSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public OrderSliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_order_slider, this);
        ButterKnife.bind(this, view);

    }

    public void load(){

    }

}
