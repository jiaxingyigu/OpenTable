package com.yigu.opentable.activity;

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

public class MainActivity extends BaseActivity {

    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        center.setText("首页");
        ivRight.setImageResource(R.mipmap.person_white);
    }


    @OnClick({R.id.iv_right, R.id.ll_order,R.id.ll_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                ControllerUtil.go2Person();
                break;
            case R.id.ll_order:
                ControllerUtil.go2Order();
                break;
            case R.id.ll_sign:
                ControllerUtil.go2Campaign();
                break;
        }
    }
}
