package com.yigu.opentable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.UserApi;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
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
    @Bind(R.id.company)
    TextView company;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.withdraw)
    TextView withdraw;
    @Bind(R.id.image)
    SimpleDraweeView image;

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

//        if (null != userSP.getUserBean())
//            phone.setText("账号：" + userSP.getUserBean().getPHONE());

        callDialog = new MainAlertDialog(this);
        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(tel.getText().toString());


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userSP.checkLogin()){
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + userSP.getUserBean().getLogo());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(90), DPUtil.dip2px(90)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(image.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            image.setController(controller);
        }
        load();
    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel.getText().toString()));
                startActivity(intent);
                callDialog.dismiss();
            }
        });
    }

    public void load() {
        showLoading();
        UserApi.personal(this, userSP.getUserBean().getUSER_ID(), new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {

                hideLoading();
                String phoneStr = success.getJSONObject("data").getString("PHONE");
                String companyStr = success.getJSONObject("data").getString("CNAME");
                String balanceStr = success.getJSONObject("data").getString("BALANCE");

                phone.setText("账号：" + (TextUtils.isEmpty(phoneStr)?"":phoneStr));
                company.setText("单位："+(TextUtils.isEmpty(companyStr)?"未绑定单位":companyStr));
                balance.setText("职工卡余额："+(TextUtils.isEmpty(balanceStr)?"0":balanceStr));

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }


    @OnClick({R.id.back, R.id.exit, R.id.modifyRL, R.id.enrollRL, R.id.aboutUsRL, R.id.serviceRL, R.id.bandRL, R.id.orderRL
    ,R.id.withdraw,R.id.trouble_rl,R.id.expendRL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.exit:
                userSP.clearUserData();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("type", 3);
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
                if (!TextUtils.isEmpty(userSP.getUserBean().getCOMPANY())) {
                    MainToast.showShortToast("您已绑定单位！");
                    return;
                }
                ControllerUtil.go2BandNext();
                break;
            case R.id.orderRL:
                ControllerUtil.go2HistoryOrder();
                break;
            case R.id.withdraw:
                ControllerUtil.go2Withdraw();
                break;
            case R.id.trouble_rl:
                ControllerUtil.go2Trouble();
                break;
            case R.id.expendRL:
                ControllerUtil.go2ExpendInfo();
                break;
        }
    }

}
