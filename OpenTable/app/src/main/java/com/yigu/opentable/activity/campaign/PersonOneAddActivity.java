package com.yigu.opentable.activity.campaign;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonOneAddActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.size)
    EditText size;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.phone)
    EditText phone;

    String actid="";
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_one_add);
        ExitApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            type = getIntent().getStringExtra("type");
        }
        if (!TextUtils.isEmpty(actid)) {
            initView();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        tvRight.setText("确定");
        center.setText("报名");

    }


    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:

                String nameStr = name.getText().toString();
                String phoneStr = phone.getText().toString();
                String sizeStr = size.getText().toString();

                if(TextUtils.isEmpty(sizeStr)){
                    MainToast.showShortToast("请输入参加人数");
                    return;
                }

                if(TextUtils.isEmpty(nameStr)){
                    MainToast.showShortToast("请输入联系人");
                    return;
                }

                if(TextUtils.isEmpty(phoneStr)){
                    MainToast.showShortToast("请输入联系电话");
                    return;
                }

                showLoading();
                CampaignApi.signup(this, type, userSP.getUserBean().getUSER_ID(), actid, nameStr, phoneStr, sizeStr, "", ""
                        , new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("报名成功");
                                ExitApplication.getInstance().exit();
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
