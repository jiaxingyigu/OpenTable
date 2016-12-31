package com.yigu.opentable.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCompleteActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        center.setText("订单完成");
    }

    @OnClick({R.id.order, R.id.back_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order:
                ControllerUtil.go2HistoryOrder();
                break;
            case R.id.back_tv:
                startActivity(new Intent(this, OrderActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }
}
