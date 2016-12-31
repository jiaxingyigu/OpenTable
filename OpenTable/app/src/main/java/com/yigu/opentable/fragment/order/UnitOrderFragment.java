package com.yigu.opentable.fragment.order;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiHistoryResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DateUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.campaign.CompanyMsgActivity;
import com.yigu.opentable.adapter.order.UnitListAdapter;
import com.yigu.opentable.base.BaseFrag;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class UnitOrderFragment extends BaseFrag {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;

    List<MapiHistoryResult> mList;
    UnitListAdapter mAdapter;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.date)
    LinearLayout date;
    @Bind(R.id.clear)
    ImageView clear;

    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    private String created = "";

    TimePickerView pvTime;

    public UnitOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_unit_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    private void initView() {
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(), OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), getResources().getColor(R.color.divider_line)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new UnitListAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                clear.setVisibility(View.VISIBLE);
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
                created = DateUtil.getInstance().date2YMD_H(date);
                refreshData();
            }

        });

    }

    public void load() {

        OrderApi.getSaleslist(getActivity(), userSP.getUserBean().getUSER_ID(), "1", created, pageIndex + "", pageSize + "",
                new RequestPageCallback<List<MapiHistoryResult>>() {
                    @Override
                    public void success(Integer isNext, List<MapiHistoryResult> success) {
                        swipRefreshLayout.setRefreshing(false);
                        ISNEXT = isNext;
                        if (success.isEmpty())
                            return;
                        mList.addAll(success);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        swipRefreshLayout.setRefreshing(false);
                        MainToast.showShortToast(message);
                    }
                });
    }

    private void initListener() {
        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2UnitHistoryDetail(mList.get(position).getId());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadNext() {
        if (ISNEXT != null && ISNEXT == 0) {
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.clear, R.id.date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                dateTv.setText("请选择时间");
                clear.setVisibility(View.GONE);
                created = "";
                refreshData();
                break;
            case R.id.date:
                pvTime.show();
                break;
        }
    }
}
