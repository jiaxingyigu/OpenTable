package com.yigu.opentable.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yigu.commom.api.UserApi;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.psd)
    EditText psd;
    @Bind(R.id.center)
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        center.setText("登录");
    }

    @OnClick({R.id.login, R.id.register,R.id.forget,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String nameStr = name.getText().toString();
                String psdStr = psd.getText().toString();
                if(TextUtils.isEmpty(nameStr)) {
                    MainToast.showShortToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(psdStr)) {
                    MainToast.showShortToast("请输入密码");
                    return;
                }
                showLoading();
                UserApi.login(this, nameStr, psdStr, new RequestCallback<MapiUserResult>() {
                    @Override
                    public void success(MapiUserResult success) {
                        hideLoading();
                        MainToast.showShortToast("登录成功");
                        userSP.saveUserBean(success);
                        ControllerUtil.go2Main();
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
            case R.id.register:
                ControllerUtil.go2Register();
                break;
            case R.id.forget:
                ControllerUtil.ForgetPsd();
                break;
        }
    }
}
