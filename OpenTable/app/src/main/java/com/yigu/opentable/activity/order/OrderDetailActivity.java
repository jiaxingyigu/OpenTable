package com.yigu.opentable.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.view.PurcaseSheetLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.purcaseSheetLayout)
    PurcaseSheetLayout purcaseSheetLayout;
    @Bind(R.id.purcase)
    TextView purcase;
    @Bind(R.id.deel)
    TextView deel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("一食堂");
    }

    @OnClick({R.id.back, R.id.purcase, R.id.deel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.purcase:
                break;
            case R.id.deel:
                break;
        }
    }
}
