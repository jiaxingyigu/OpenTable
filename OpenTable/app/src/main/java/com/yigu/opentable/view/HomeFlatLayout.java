package com.yigu.opentable.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiPlatformResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.platform.PlatFormAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
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
public class HomeFlatLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context mContext;
    private View view;
    BaseActivity activity;

    PlatFormAdapter mAdapter;
    List<MapiPlatformResult> mList;

    public HomeFlatLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    public HomeFlatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    public HomeFlatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_flat, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(activity, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PlatFormAdapter(activity, mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String url = mList.get(position).getUrl();

                String img_url ="";
                if(TextUtils.isEmpty(mList.get(position).getPATH())){
                    img_url = BasicApi.LOGO_URL;
                }else{
                    img_url = BasicApi.BASIC_IMAGE + mList.get(position).getPATH();
                }

                if(!TextUtils.isEmpty(url))
                    ControllerUtil.go2WebView(url,"平台信息详情","饮食营养知识",mList.get(position).getTitle(), img_url,true);
            }
        });
    }

    public void load(List<MapiPlatformResult> list){

        if(null!=list){
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.more_ll)
    public void onClick() {
        ControllerUtil.go2Platform();
    }
}
