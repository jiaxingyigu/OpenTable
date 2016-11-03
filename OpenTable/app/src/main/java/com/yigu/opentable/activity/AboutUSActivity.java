package com.yigu.opentable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.UserApi;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.widget.MainAlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUSActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.tv_right)
    TextView tvRight;
    MainAlertDialog callDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("关于我们");
        tvRight.setText("后台地址");
        callDialog = new MainAlertDialog(this);
        callDialog.setLeftBtnText("取消").setRightBtnText("").setTitle("http://fengzhiyue.com:8081");

    }

    private void initListener(){
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });
    }

    private void load() {
        UserApi.aboutus(this, new RequestCallback<String>() {
            @Override
            public void success(String success) {
                image.setImageURI(Uri.parse(BasicApi.BASIC_IMAGE + success));
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back,R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                callDialog.show();
                break;
        }
    }


}
