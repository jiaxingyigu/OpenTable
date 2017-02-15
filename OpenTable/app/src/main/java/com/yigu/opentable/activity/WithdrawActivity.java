package com.yigu.opentable.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.UserApi;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.price)
    EditText price;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.instruction)
    EditText instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("提现");
        tvRight.setText("申请");
    }

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                String priceStr = price.getText().toString();
                String accountStr = account.getText().toString();
                String instructionStr = instruction.getText().toString();
                if(TextUtils.isEmpty(priceStr)){
                    MainToast.showShortToast("请输入提现金额");
                    return;
                }
                if(TextUtils.isEmpty(accountStr)){
                    MainToast.showShortToast("请输入提现账号");
                    return;
                }
                if(TextUtils.isEmpty(instructionStr)){
                    MainToast.showShortToast("请输入账号说明,如：支付宝/微信等");
                    return;
                }
                showLoading();
                UserApi.withdrawals(this, userSP.getUserBean().getUSER_ID(), priceStr, accountStr, instructionStr, new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("提现审核中");
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
                break;
        }
    }


}
