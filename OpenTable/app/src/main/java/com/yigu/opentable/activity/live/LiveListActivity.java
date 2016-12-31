package com.yigu.opentable.activity.live;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.live.LiveListAdapter;
import com.yigu.opentable.adapter.tenant.TenantListAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;

    private List<MapiOrderResult> mList;
    LiveListAdapter mAdapter;

    private Integer pageIndex=0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView(){
        center.setText("生活馆");
        back.setImageResource(R.mipmap.back);

        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new LiveListAdapter(this,mList);
        recyclerView.setAdapter(mAdapter);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);


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
                ControllerUtil.go2LiveMenu(mList.get(position));
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

        OrderApi.getLivinglist(this, userSP.getUserBean().getCOMPANY(), pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiOrderResult>>() {
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

    @OnClick(R.id.back)
    public void onClick() {
        try {
            db.delete(MapiOrderResult.class);
        } catch (DbException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }
}
