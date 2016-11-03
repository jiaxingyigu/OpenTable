package com.yigu.opentable.activity.campaign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.soundcloud.android.crop.Crop;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.JGJBitmapUtils;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.campaign.CompanyJobAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.Config;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.widget.ItemDialog;
import com.yigu.opentable.widget.MainAlertDialog;
import com.yigu.opentable.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyMsgActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.type)
    TextView typeTV;
    @Bind(R.id.typeLL)
    LinearLayout typeLL;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.addr)
    TextView addr;
    @Bind(R.id.size)
    TextView size;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.train_yes)
    RadioButton trainYes;
    @Bind(R.id.train_no)
    RadioButton trainNo;

    MapiItemResult itemResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_msg);
        ButterKnife.bind(this);
        if(null!=getIntent())
            itemResult = (MapiItemResult) getIntent().getSerializableExtra("item");
        if (null!=itemResult) {
            initView();
            initListener();
        }
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("企业报名");

        if(itemResult.getTrain().equals("0")) {
            trainYes.setChecked(false);
            trainNo.setChecked(true);
        }else {
            trainYes.setChecked(true);
            trainNo.setChecked(false);
        }

        trainYes.setFocusable(false);
        trainYes.setClickable(false);
        trainNo.setFocusable(false);
        trainNo.setClickable(false);

        if (itemResult.getType().equals("0"))
            typeTV.setText("企业类型：企事业单位");
        if (itemResult.getType().equals("1"))
            typeTV.setText("企业类型：机关部门");
        if (itemResult.getType().equals("2"))
            typeTV.setText("企业类型：医院");

        name.setText("企业名称："+itemResult.getName());

        addr.setText("企业地址："+itemResult.getAddress());

        size.setText("企业规模："+itemResult.getScale());

        tel.setText("联系电话："+itemResult.getTel());

        content.setText("简介："+itemResult.getIntroduction());

       /* if(!TextUtils.isEmpty(itemResult.getPATH())){
            image.setVisibility(View.VISIBLE);
            image.setImageURI(Uri.parse(BasicApi.BASIC_IMAGE +itemResult.getPATH()));
        }*/


    }

    private void initListener() {

    }

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


}
