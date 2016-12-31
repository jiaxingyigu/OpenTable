package com.yigu.opentable.activity.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.MainOrderAdapter;
import com.yigu.opentable.base.BaseActivity;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private final static String SCROLL = "SCROLL";
    private final static String ITEM = "ITEM";
    List<IndexData> mList = new ArrayList<>();
    MainOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initView();
        load();
    }

    private void initView() {
        mList.add(new IndexData(0, SCROLL, new ArrayList<MapiResourceResult>()));
        mList.add(new IndexData(1, ITEM, new ArrayList<MapiResourceResult>()));
        center.setText("订餐系统");
        back.setImageResource(R.mipmap.back);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainOrderAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void load(){
        showLoading();
        CommonApi.getAdvertisement(this, userSP.getUserBean().getCOMPANY(), new RequestCallback< List<MapiResourceResult>>() {
            @Override
            public void success( List<MapiResourceResult> success) {
                hideLoading();
                if(success.isEmpty())
                    return;
                IndexData indexData =  mList.get(0);
                indexData.setData(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
