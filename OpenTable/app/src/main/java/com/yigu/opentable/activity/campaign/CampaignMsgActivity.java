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
import com.yigu.opentable.util.ShareModule;
import com.yigu.opentable.widget.ShareDialog;

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
    @Bind(R.id.iv_right_two)
    ImageView ivRightTwo;

    String actid = "";
    String info = "";
    String title = "";
    String pic = "";
    String type = "";
    MapiCampaignResult campaignResult;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_msg);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            info = getIntent().getStringExtra("info");
            title = getIntent().getStringExtra("title");
            pic = getIntent().getStringExtra("pic");
            type = getIntent().getStringExtra("type");
        }
        if (!TextUtils.isEmpty(actid)) {
            initView();
            initListener();
            load();
        }
    }

    private void initView() {
        center.setText("活动入口");
        back.setImageResource(R.mipmap.back);
        infoTv.setText(info);

        ivRightTwo.setImageResource(R.mipmap.share_logo);
        ivRightTwo.setVisibility(View.VISIBLE);

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);

    }

    private void initListener(){
        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                String SHARE_ACTIVITY_DETAIL =BasicApi.BASIC_URL+ BasicApi.SHARE_ACTIVITY_DETAIL+actid;

                String img_url ="";
                if(TextUtils.isEmpty(pic)){
                    img_url = BasicApi.LOGO_URL;
                }else{
                    img_url = BasicApi.BASIC_IMAGE + pic;
                }

                switch (position) {
                    case 0://微信好友
                        ShareModule shareModule1 = new ShareModule(CampaignMsgActivity.this, title, info, img_url, SHARE_ACTIVITY_DETAIL);
                        shareModule1.startShare(1);
                        break;
                    case 1:
                        ShareModule shareModule2 = new ShareModule(CampaignMsgActivity.this, title, info, img_url, SHARE_ACTIVITY_DETAIL);
                        shareModule2.startShare(2);
                        break;
                    case 2:
                        ShareModule shareModule3 = new ShareModule(CampaignMsgActivity.this, title, info,img_url, SHARE_ACTIVITY_DETAIL);
                        shareModule3.startShare(3);
                        break;
                    case 3:
                        ShareModule shareModule4 = new ShareModule(CampaignMsgActivity.this, title, info, img_url, SHARE_ACTIVITY_DETAIL);
                        shareModule4.startShare(4);
                        break;
                }
            }
        });
    }

    private void load() {
        CampaignApi.activityUrl(this, actid, new RequestCallback<MapiCampaignResult>() {
            @Override
            public void success(MapiCampaignResult success) {
                campaignResult = success;
                if (null != campaignResult) {

                    if("1".equals(type)||"3".equals(type)||"7".equals(type)){
                        if ("0".equals(campaignResult.getSignup()) || "2".equals(campaignResult.getSignup()))
                            name.setText("企业报名");
                        else if ("1".equals(campaignResult.getSignup()))
                            name.setText("个人报名");
                        else if ("3".equals(campaignResult.getSignup()))
                            name.setText("活动已结束");
                    }else{
                        if ("1".equals(campaignResult.getSignup()))
                            name.setText("报名");
                        else if ("3".equals(campaignResult.getSignup()))
                            name.setText("活动已结束");
                    }

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + campaignResult.getBpic());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(384), DPUtil.dip2px(500)))
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

    @OnClick({R.id.back, R.id.name,R.id.iv_right_two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.name:
                if (null != campaignResult) {
                    if("1".equals(type)||"3".equals(type)||"7".equals(type)){
                        if ("0".equals(campaignResult.getSignup()) || "2".equals(campaignResult.getSignup()))
                            company();
                        else if ("1".equals(campaignResult.getSignup()))
                            persign();
                    }else if("2".equals(type)||"4".equals(type)||"8".equals(type)){
                        if ("1".equals(campaignResult.getSignup()))
                            ControllerUtil.go2PersonOneAdd(campaignResult.getId(),campaignResult.getType());
                    }else{
                        if ("1".equals(campaignResult.getSignup()))
                            ControllerUtil.go2PersonTwoAdd(campaignResult.getId(),campaignResult.getType());
                    }


                }

                break;
            case R.id.iv_right_two:
                shareDialog.showDialog();
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
