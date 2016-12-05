package com.yigu.opentable.activity.campaign;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CampaignMsgActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.info_tv)
    TextView infoTv;

    String actid = "";
    String info = "";
    MapiCampaignResult campaignResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_msg);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            info = getIntent().getStringExtra("info");
        }
        if (!TextUtils.isEmpty(actid)) {
            initView();
            load();
        }
    }

    private void initView() {
        center.setText("活动入口");
        back.setImageResource(R.mipmap.back);
        infoTv.setText(info);
    }

    private void load() {
        CampaignApi.activityUrl(this, actid, new RequestCallback<MapiCampaignResult>() {
            @Override
            public void success(MapiCampaignResult success) {
                campaignResult = success;
                if (null != campaignResult) {
                    if ("0".equals(campaignResult.getSignup()) || "2".equals(campaignResult.getSignup()))
                        name.setText("企业报名");
                    else if ("1".equals(campaignResult.getSignup()))
                        name.setText("个人报名");
                    else if ("3".equals(campaignResult.getSignup()))
                        name.setText("活动已结束");


                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + campaignResult.getBpic());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(360), DPUtil.dip2px(500)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(image.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    image.setController(controller);

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.name:
                if (null != campaignResult) {
                    if ("0".equals(campaignResult.getSignup()) || "2".equals(campaignResult.getSignup()))
                        company();
                    else if ("1".equals(campaignResult.getSignup()))
                        persign();


                }

                break;
        }
    }

    private void persign() {
        CampaignApi.findPersign(this, campaignResult.getId(), userSP.getUserBean().getUSER_ID(), new RequestCallback() {
            @Override
            public void success(Object success) {
                ControllerUtil.go2PersonAdd(campaignResult.getId(), campaignResult.getType());
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    private void company() {
        CampaignApi.findComsign(this, campaignResult.getId(), userSP.getUserBean().getUSER_ID(), new RequestCallback() {
            @Override
            public void success(Object success) {
                ControllerUtil.go2CompanyAdd(campaignResult.getId(), campaignResult.getType());
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

}
