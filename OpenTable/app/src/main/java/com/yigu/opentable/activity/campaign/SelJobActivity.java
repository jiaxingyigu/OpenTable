package com.yigu.opentable.activity.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.campaign.SelJobAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelJobActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefresh)
    BestSwipeRefreshLayout swipRefresh;
    SelJobAdapter mAdapter;
    List<MapiCampaignResult> mList;

    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    String actid;
    String search = "";

    ArrayList<MapiCampaignResult> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_job);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            jobList = (ArrayList<MapiCampaignResult>) getIntent().getSerializableExtra("list");
        }if (!TextUtils.isEmpty(actid)) {
            initView();
            initListener();
            load();
        }

    }

    private void initView() {
        mList = new ArrayList<>();
        back.setImageResource(R.mipmap.back);
        center.setText("岗位选择");
        tvRight.setText("完成");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), getResources().getColor(R.color.divider_line)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new SelJobAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }


    private void initListener() {
        swipRefresh.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2JobDetail(mList.get(position));
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

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString();
                refreshData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void load() {

        CampaignApi.getPostlist(this, search, actid, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiCampaignResult>>() {
            @Override
            public void success(Integer isNext, List<MapiCampaignResult> success) {
                swipRefresh.setRefreshing(false);
                ISNEXT = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);

                if(null!=jobList){
                    mAdapter.setSelList(jobList);
                }


                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                swipRefresh.setRefreshing(false);
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

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                if(null!=mAdapter){

                    ArrayList<MapiCampaignResult> list = mAdapter.getSelList();
                    if(null==list||list.size()<=0){
                        MainToast.showShortToast("请选择岗位");
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("list",list);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }

                break;
        }
    }
}
