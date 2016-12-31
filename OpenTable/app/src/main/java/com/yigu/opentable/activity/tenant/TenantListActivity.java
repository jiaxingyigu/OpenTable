package com.yigu.opentable.activity.tenant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.SearchCampaignActivity;
import com.yigu.opentable.adapter.tenant.TenantListAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TenantListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.unit_name)
    TextView unitName;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;

    private List<MapiOrderResult> mList;
    TenantListAdapter mAdapter;

    private Integer pageIndex=0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    MapiCampaignResult campaignResult;
    String companyId = "  ";
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_list);
        ButterKnife.bind(this);
        initView();
        initView();
        initListener();
        load();
    }

    private void initView() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        companyId  = TextUtils.isEmpty(userSP.getUserBean().getCOMPANY())?"  ":userSP.getUserBean().getCOMPANY();
        back.setImageResource(R.mipmap.back);
        center.setText("商户订餐");
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new TenantListAdapter(this,mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2TenantMenu(mList.get(position));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition()>=0&&(manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void load(){

        OrderApi.getMerchantlist(this, companyId, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                swipRefreshLayout.setRefreshing(false);
                ISNEXT = isNext;
                if(success.isEmpty())
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

    private void loadNext() {
        if (ISNEXT != null && ISNEXT==0) {
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
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            db.delete(MapiOrderResult.class);
        } catch (DbException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    @OnClick({R.id.back, R.id.search_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                try {
                    db.delete(MapiOrderResult.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.search_ll:
                Intent intent = new Intent(this,SearchCampaignActivity.class);
                startActivityForResult(intent, RequestCode.search_campaign);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.search_campaign:
                    if (null != data)
                        campaignResult = (MapiCampaignResult) data.getSerializableExtra("item");
                    companyId = campaignResult.getId();
                    unitName.setText(campaignResult.getName());
                    refreshData();
                    break;
            }
        }
    }

}
