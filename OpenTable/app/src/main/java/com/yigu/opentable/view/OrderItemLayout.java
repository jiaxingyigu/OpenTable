package com.yigu.opentable.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.order.OrderActivity;
import com.yigu.opentable.adapter.OrderItemAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.util.TableDataSource;
import com.yigu.opentable.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/11.
 */
public class OrderItemLayout extends RelativeLayout {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context mContext;
    private View view;
    List<MapiResourceResult> mList;
    OrderItemAdapter mAdapter;
    private BaseActivity activity;
    public OrderItemLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public OrderItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public OrderItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_order_item, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, DPUtil.dip2px(0.5f), getResources().getColor(R.color.divider_line)));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        mAdapter = new OrderItemAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);
        initListener();
    }

    public void load(BaseActivity activity) {
        this.activity = activity;
        mList.clear();
        mList.addAll(TableDataSource.getRootResource());
        mAdapter.notifyDataSetChanged();

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (mList.get(position).getId()) {
                    case TableDataSource.TYPE_UNIT:
                        if(null!=activity){
                            if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                                MainToast.showShortToast("请先绑定单位");
                                ControllerUtil.go2BandNext();
                                return;
                            }
                            ControllerUtil.go2UnitOrder();
                        }

                        break;
                    case TableDataSource.TYPE_TENANT:
                        ControllerUtil.go2TenantList();
                        break;
                    case TableDataSource.TYPE_LIVE:
                        if(null!=activity){
                            if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                                MainToast.showShortToast("请先绑定单位");
                                ControllerUtil.go2BandNext();
                                return;
                            }
                            ControllerUtil.go2LiveList();
                        }
                        break;

                    case TableDataSource.TYPE_COOK:
                        if(null!=activity){
                            if(TextUtils.isEmpty(activity.userSP.getUserBean().getCOMPANY())) {
                                MainToast.showShortToast("请先绑定单位");
                                ControllerUtil.go2BandNext();
                                return;
                            }
                            ControllerUtil.go2CookList();
                        }
                        break;

                    case TableDataSource.TYPE_WORKERS://美食坊
                        ControllerUtil.go2FoodList();
                        break;

                }
            }
        });
    }
}
