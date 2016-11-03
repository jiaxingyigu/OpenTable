package com.yigu.opentable.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by brain on 2016/9/22.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;                         //fragment列表
    private List<String> list_Title;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> list_Title) {
        super(fm);
        this.list = list;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position%list_Title.size());
    }
}
