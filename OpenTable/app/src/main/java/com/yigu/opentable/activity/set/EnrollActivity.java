package com.yigu.opentable.activity.set;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.opentable.R;
import com.yigu.opentable.adapter.TabFragmentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.fragment.set.CompanyEnrollFragment;
import com.yigu.opentable.fragment.set.PersonEnrollFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnrollActivity extends BaseActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    PersonEnrollFragment personEnrollFragment;
    CompanyEnrollFragment companyEnrollFragment;
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
        setContentView(R.layout.activity_enroll);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("我的报名");

        personEnrollFragment = new PersonEnrollFragment();
        companyEnrollFragment = new CompanyEnrollFragment();
        list.add(personEnrollFragment);
        list.add(companyEnrollFragment);
        list_title.add("个人报名");
        list_title.add("企业报名");
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
