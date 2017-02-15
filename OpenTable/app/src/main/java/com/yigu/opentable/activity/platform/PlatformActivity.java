package com.yigu.opentable.activity.platform;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.opentable.R;
import com.yigu.opentable.adapter.TabFragmentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.fragment.order.LiveOrderFragment;
import com.yigu.opentable.fragment.order.TenantOrderFragment;
import com.yigu.opentable.fragment.order.UnitOrderFragment;
import com.yigu.opentable.fragment.platform.PlatformFoodFragment;
import com.yigu.opentable.fragment.platform.PlatformLifeFragment;
import com.yigu.opentable.fragment.platform.PlatformNewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlatformActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    PlatformFoodFragment platformFoodFragment;
    PlatformLifeFragment platformLifeFragment;
    PlatformNewsFragment platformNewsFragment;

    private List<Fragment> list = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    TabFragmentAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        center.setText("平台信息");
        back.setImageResource(R.mipmap.back);

        platformFoodFragment = new PlatformFoodFragment();
        platformLifeFragment = new PlatformLifeFragment();
        platformNewsFragment = new PlatformNewsFragment();

        list.add(platformFoodFragment);
        list.add(platformLifeFragment);
        list.add(platformNewsFragment);

        list_title.add("饮食营养知识");
        list_title.add("养生常识");
        list_title.add("新闻信息");

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

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
