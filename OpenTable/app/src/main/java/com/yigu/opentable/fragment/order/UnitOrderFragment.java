package com.yigu.opentable.fragment.order;


import android.content.Intent;
import android.net.Uri;
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
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiHistoryResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DateUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.history.TenantHistoryDetailActivity;
import com.yigu.opentable.activity.history.UnitHistoryDetailActivity;
import com.yigu.opentable.adapter.order.UnitListAdapter;
import com.yigu.opentable.base.BaseFrag;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.DividerListItemDecoration;
import com.yigu.opentable.widget.MainAlertDialog;

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

    String isfinish="0";
    String state = "1";
    String type = "0";

    MainAlertDialog cancelDialog;

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
//        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
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

        cancelDialog = new MainAlertDialog(getActivity());
        cancelDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认取消该订单？");

    }

    public void load() {
        showLoading();
        OrderApi.getSaleslist(getActivity(), userSP.getUserBean().getUSER_ID(), "1", created,isfinish,state, pageIndex + "", pageSize + "",
                new RequestPageCallback<List<MapiHistoryResult>>() {
                    @Override
                    public void success(Integer isNext, List<MapiHistoryResult> success) {
                        hideLoading();
                        if(null!=swipRefreshLayout)
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
                        hideLoading();
                        if(null!=swipRefreshLayout)
                            swipRefreshLayout.setRefreshing(false);
                        MainToast.showShortToast(message);
                    }
                });
    }

    private void initListener() {

        cancelDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog.dismiss();
            }
        });

        cancelDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos>=0){
                    cancel();
                }
                cancelDialog.dismiss();
            }
        });

        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ControllerUtil.go2UnitHistoryDetail(mList.get(position).getId());

                Intent intent = new Intent(AppContext.getInstance(), UnitHistoryDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                intent.putExtra("type",type);
                intent.putExtra("bz",mList.get(position).getBz());
                intent.putExtra("zhifu",mList.get(position).getZhifu());
                intent.putExtra("addr",mList.get(position).getCtype());
                intent.putExtra("send",mList.get(position).getRoomservice());
                startActivityForResult(intent, RequestCode.order_detail);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() > 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mAdapter.setRecyOnItemCancelClickListener(new UnitListAdapter.CancelOnItemClickListener() {
            @Override
            public void onItemCancelClick(View view, int position) {
                pos = position;
                cancelDialog.show();
            }
        });

    }

    private void loadNext() {
        if (ISNEXT != null && ISNEXT == 0) {
            MainToast.showShortToast("已经到底部了");
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
        if(null!=cancelDialog){
            cancelDialog.dismiss();
            cancelDialog = null;
        }
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.clear, R.id.date,R.id.pay, R.id.unComplete, R.id.complete,R.id.uncancel,R.id.cancel})
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
            case R.id.pay:
                isfinish="0";
                state = "1";
                type = "0";
                mAdapter.setType(type);
                mAdapter.setCancel(false);
                refreshData();
                break;
            case R.id.unComplete:
                isfinish="0";
                state = "0";
                type = "1";
                mAdapter.setCancel(true);
                mAdapter.setType(type);
                refreshData();
                break;
            case R.id.complete:
                isfinish="1";
                state = "0";
                type = "2";
                mAdapter.setCancel(false);
                mAdapter.setType(type);
                refreshData();
                break;
            case R.id.uncancel:
                isfinish="2";
                state = "0";
                type = "2";
                mAdapter.setCancel(false);
                mAdapter.setType(type);
                refreshData();
                break;
            case R.id.cancel:
                isfinish="3";
                state = "0";
                type = "2";
                mAdapter.setCancel(false);
                mAdapter.setType(type);
                refreshData();
                break;
        }
    }

    private int pos = -1;

    private void cancel(){
        showLoading();
        OrderApi.cancelOrder(getActivity(), mList.get(pos).getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(pos);
                mAdapter.notifyDataSetChanged();
                pos = -1;
                MainToast.showShortToast("提交成功，请等待商家审核...");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                pos = -1;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==getActivity().RESULT_OK){
            if(requestCode==RequestCode.order_detail){
                refreshData();
            }
        }

    }

}
