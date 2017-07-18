package com.yigu.opentable.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.yigu.commom.util.DateUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.TabFragmentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.fragment.order.HomeOrderFragment;
import com.yigu.opentable.fragment.order.LiveOrderFragment;
import com.yigu.opentable.fragment.order.TenantOrderFragment;
import com.yigu.opentable.fragment.order.UnitOrderFragment;
import com.yigu.opentable.fragment.set.CompanyEnrollFragment;
import com.yigu.opentable.fragment.set.PersonEnrollFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryOrderActivity extends BaseActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    UnitOrderFragment unitOrderFragment;
    TenantOrderFragment tenantOrderFragment;
    LiveOrderFragment liveOrderFragment;
    HomeOrderFragment homeOrderFragment;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    private List<Fragment> list = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    TabFragmentAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("我的订单");

        unitOrderFragment = new UnitOrderFragment();
        tenantOrderFragment = new TenantOrderFragment();
        liveOrderFragment = new LiveOrderFragment();
        homeOrderFragment = new HomeOrderFragment();

        list.add(unitOrderFragment);
        list.add(tenantOrderFragment);
        list.add(liveOrderFragment);
        list.add(homeOrderFragment);

        list_title.add("单位食堂");
        list_title.add("商户订餐");
        list_title.add("生活馆");
        list_title.add("美食坊");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(2)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(3)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

        viewpager.setOffscreenPageLimit(4);

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

}
