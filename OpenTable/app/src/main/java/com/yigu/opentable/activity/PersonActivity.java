package com.yigu.opentable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.MainAlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.exit)
    TextView exit;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.tel)
    TextView tel;

    MainAlertDialog callDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        initView();
        initListener();
    }

    private void initView() {
        center.setText("个人中心");
        back.setImageResource(R.mipmap.back);

        if(null!=userSP.getUserBean())
            phone.setText("账号："+userSP.getUserBean().getPHONE());

        callDialog = new MainAlertDialog(this);
        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(tel.getText().toString());

    }



    private void initListener(){
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +tel.getText().toString() ));
                startActivity(intent);
                callDialog.dismiss();
            }
        });
    }


    @OnClick({R.id.back, R.id.exit, R.id.modifyRL, R.id.enrollRL,R.id.aboutUsRL,R.id.serviceRL,R.id.bandRL,R.id.orderRL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.exit:
                userSP.clearUserData();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("type",3);
                startActivity(i);
                finish();
                break;
            case R.id.modifyRL:
                ControllerUtil.go2ModifyPsd();
                break;
            case R.id.enrollRL:
                ControllerUtil.go2Enroll();
                break;
            case R.id.aboutUsRL:
                ControllerUtil.go2AboutUS();
                break;
            case R.id.serviceRL:
                callDialog.show();
                break;
            case R.id.bandRL:
                if(!TextUtils.isEmpty(userSP.getUserBean().getCOMPANY())){
                    MainToast.showShortToast("您已绑定单位！");
                    return;
                }
                ControllerUtil.go2BandNext();
                break;
            case R.id.orderRL:
                ControllerUtil.go2HistoryOrder();
                break;
        }
    }

}
