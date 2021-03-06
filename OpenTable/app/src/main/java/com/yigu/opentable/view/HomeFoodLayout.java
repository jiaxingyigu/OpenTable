package com.yigu.opentable.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yigu.commom.result.MapiHomeResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.HomeTenantAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/6/17.
 */
public class HomeFoodLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context mContext;
    private View view;
    BaseActivity activity;

    List<MapiHomeResult> mList;
    HomeTenantAdapter mAdapter;

    public HomeFoodLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeFoodLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeFoodLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_food, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(activity, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HomeTenantAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
    }

    public void load(List<MapiHomeResult> list) {

        if (null != list) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.more_ll)
    public void onClick() {
        ControllerUtil.go2FoodList();
    }

}
