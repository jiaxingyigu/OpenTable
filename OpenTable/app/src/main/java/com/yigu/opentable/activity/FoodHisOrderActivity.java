package com.yigu.opentable.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.TabFragmentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.broadcast.ReceiverAction;
import com.yigu.opentable.fragment.food.FoodCompleteFragment;
import com.yigu.opentable.fragment.food.FoodFailFragment;
import com.yigu.opentable.fragment.food.FoodWaitFragment;
import com.yigu.opentable.fragment.order.HomeOrderFragment;
import com.yigu.opentable.fragment.order.LiveOrderFragment;
import com.yigu.opentable.fragment.order.TenantOrderFragment;
import com.yigu.opentable.fragment.order.UnitOrderFragment;
import com.yigu.opentable.util.JpushUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodHisOrderActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    FoodWaitFragment foodWaitFragment;
    FoodCompleteFragment foodCompleteFragment;
    FoodFailFragment foodFailFragment;

    private List<Fragment> list = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    TabFragmentAdapter mAdapter;
    MessageReceiver mMessageReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_his_order);
        ButterKnife.bind(this);
        initView();
        registerMessageReceiver();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("我的订座");

        foodWaitFragment = new FoodWaitFragment();
        foodCompleteFragment = new FoodCompleteFragment();
        foodFailFragment = new FoodFailFragment();

        list.add(foodWaitFragment);
        list.add(foodCompleteFragment);
        list.add(foodFailFragment);

        list_title.add("申请中");
        list_title.add("申请成功");
        list_title.add("申请失败");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(2)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

        viewpager.setOffscreenPageLimit(3);

    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ReceiverAction.FOOD_COMPLETE_ACTION);
        filter.addAction(ReceiverAction.FOOD_FAIL_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ReceiverAction.FOOD_COMPLETE_ACTION.equals(intent.getAction())) {
                if(null!=foodWaitFragment)
                    foodWaitFragment.refreshData();
                if(null!=foodCompleteFragment)
                    foodCompleteFragment.refreshData();
            }else if(ReceiverAction.FOOD_FAIL_ACTION.equals(intent.getAction())){
                if(null!=foodWaitFragment)
                    foodWaitFragment.refreshData();
                if(null!=foodFailFragment)
                    foodFailFragment.refreshData();
            }
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null!=foodWaitFragment)
            foodWaitFragment.refreshData();
        if(null!=foodCompleteFragment)
            foodCompleteFragment.refreshData();
        if(null!=foodFailFragment)
            foodFailFragment.refreshData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mMessageReceiver)
            unregisterReceiver(mMessageReceiver);
    }

}
