package com.yigu.opentable.activity.campaign;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.result.MapiItemResult;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherMsgActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.job_name)
    TextView jobName;
    @Bind(R.id.job_info)
    TextView jobInfo;
    @Bind(R.id.size)
    TextView size;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;

    MapiItemResult itemResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_msg);
        ButterKnife.bind(this);
        if(null!=getIntent())
            itemResult = (MapiItemResult)getIntent().getSerializableExtra("item");
        if (null!=itemResult) {
            initView();
        }
    }

    private void initView(){

        back.setImageResource(R.mipmap.back);
        center.setText("其他报名");

        if(TextUtils.isEmpty(itemResult.getPostname())){
            jobName.setVisibility(View.GONE);
        }else{
            jobName.setVisibility(View.VISIBLE);
            jobName.setText("岗位名称："+(TextUtils.isEmpty(itemResult.getPostname())?"":itemResult.getPostname()));
        }

        if(TextUtils.isEmpty(itemResult.getPostremark())){
            jobInfo.setVisibility(View.GONE);
        }else{
            jobInfo.setVisibility(View.VISIBLE);
            jobInfo.setText("岗位描述："+(TextUtils.isEmpty(itemResult.getPostremark())?"":itemResult.getPostremark()));
        }

        size.setText("参加人数："+(TextUtils.isEmpty(itemResult.getNum())?"":itemResult.getNum()));

        name.setText("联系人："+(TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName()));

        phone.setText("联系电话："+(TextUtils.isEmpty(itemResult.getTel())?"":itemResult.getTel()));

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
