package com.yigu.opentable.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/6/17.
 */
public class HomeServicelayout extends LinearLayout {

    private Context mContext;
    private View view;
    BaseActivity activity;

    public HomeServicelayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeServicelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeServicelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_service, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.company_info, R.id.shop_info, R.id.ll_sign, R.id.type_unit, R.id.type_tenant, R.id.type_workers, R.id.live_info, R.id.cook_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.company_info:
                ControllerUtil.go2CampaignEnter();
                break;
            case R.id.shop_info:
                ControllerUtil.go2ShopEnter();
                break;
            case R.id.ll_sign:
                ControllerUtil.go2CampaignType();
                break;
            case R.id.type_unit:
                if(null!=activity){
                    if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                        MainToast.showShortToast("请先绑定单位");
                        ControllerUtil.go2BandNext();
                        return;
                    }
                    ControllerUtil.go2UnitOrder();
                }
                break;
            case R.id.type_tenant:
                ControllerUtil.go2TenantList();
                break;
            case R.id.type_workers:
                ControllerUtil.go2FoodList();
                break;
            case R.id.live_info:
                if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                    MainToast.showShortToast("请先绑定单位");
                    ControllerUtil.go2BandNext();
                    return;
                }
                ControllerUtil.go2LiveList();
                break;
            case R.id.cook_info:
                if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                    MainToast.showShortToast("请先绑定单位");
                    ControllerUtil.go2BandNext();
                    return;
                }
                ControllerUtil.go2CookList();
                break;
        }
    }
}
