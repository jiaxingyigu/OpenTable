package com.yigu.opentable.fragment.set;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.campaign.CompanyMsgActivity;
import com.yigu.opentable.adapter.set.CompanyEnrollAdapter;
import com.yigu.opentable.adapter.set.PersonEnrollAdapter;
import com.yigu.opentable.base.BaseFrag;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class CompanyEnrollFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;

    List<MapiItemResult> mList;

    CompanyEnrollAdapter mAdapter;

    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    public CompanyEnrollFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_person_enroll, container, false);
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
        mAdapter = new CompanyEnrollAdapter(getActivity(),mList);
        recyclerView.setAdapter(mAdapter);
    }

    public void load() {
            CampaignApi.getComsignlist(getActivity(), userSP.getUserBean().getUSER_ID(), pageIndex+"", pageSize+"", new RequestPageCallback<List<MapiItemResult>>() {
                @Override
                public void success(Integer isNext, List<MapiItemResult> success) {
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
                Intent intent = new Intent(getActivity(), CompanyMsgActivity.class);
                intent.putExtra("item",mList.get(position));
                startActivity(intent);
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

}
