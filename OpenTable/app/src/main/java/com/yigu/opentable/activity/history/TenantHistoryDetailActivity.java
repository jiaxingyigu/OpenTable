package com.yigu.opentable.activity.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.pay.PaymentAdapter;
import com.yigu.opentable.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TenantHistoryDetailActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    String id = "";
    private List<IndexData> mList = new ArrayList<>();
    PaymentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_history_detail);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            id = getIntent().getStringExtra("id");
        }
        if(!TextUtils.isEmpty(id)){
            initView();
            load();
        }

    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("订单详情");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PaymentAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    public void load() {
        OrderApi.getSalesdetailslist(this, id, new RequestCallback<List<MapiOrderResult>>() {
            @Override
            public void success(List<MapiOrderResult> success) {
                if(success.isEmpty())
                    return;
                int count = 0;
                if(success!=null&&success.size()>0){
                    for(int i=0;i<success.size();i++){
                        MapiOrderResult orderResult = success.get(i);
                        mList.add(new IndexData(count++, "item", success.get(i)));
                        mList.add(new IndexData(count++, "divider", new Object()));
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

}
