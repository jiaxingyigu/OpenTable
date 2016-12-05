package com.yigu.opentable.activity.pay;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.result.MapiCartResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.pay.PaymentAdapter;
import com.yigu.opentable.adapter.purcase.PurcaseAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.AnimationUtil;
import com.yigu.opentable.view.PayWayLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.deel)
    TextView deel;
    @Bind(R.id.payWayLayout)
    PayWayLayout payWayLayout;
    @Bind(R.id.backgound)
    View backgound;

    private List<MapiCartResult> mList = new ArrayList<>();

    private Integer pageIndex = 0;
    private Integer pageSize = 10;
    private Integer ISNEXT = 0;

    PaymentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("提交订单");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PaymentAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
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
    }


    public void load() {
        mList.clear();
        List<MapiItemResult> itemList = new ArrayList<>();
        itemList.add(new MapiItemResult());
        mList.add(new MapiCartResult(itemList));
        itemList = new ArrayList<>();
        itemList.add(new MapiItemResult());
        itemList.add(new MapiItemResult());
        itemList.add(new MapiItemResult());
        itemList.add(new MapiItemResult());
        mList.add(new MapiCartResult(itemList));
        mAdapter.notifyDataSetChanged();
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


    @OnClick({R.id.back, R.id.deel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.deel:
                album();
                break;
        }
    }

    private void album() {
        if (payWayLayout.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /** 弹出 */
    private void popAlbum() {
        payWayLayout.setVisibility(View.VISIBLE);
        new AnimationUtil(this, R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(payWayLayout);
        backgound.setVisibility(View.VISIBLE);
    }

    /** 隐藏 */
    private void hideAlbum() {
        new AnimationUtil(this, R.anim.translate_down)
                .setLinearInterpolator().startAnimation(payWayLayout);
        payWayLayout.setVisibility(View.GONE);
        backgound.setVisibility(View.GONE);
    }

}
