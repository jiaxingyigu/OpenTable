package com.yigu.opentable.activity.campaign;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.company)
    TextView company;

    MapiCampaignResult result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);

        if (null != getIntent())
            result = (MapiCampaignResult) getIntent().getSerializableExtra("item");
        if (result != null)
            initView();
    }

    private void initView() {
        center.setText("岗位详情");
        back.setImageResource(R.mipmap.back);
        company.setText("公司名称："+result.getCompany());
        name.setText("岗位名称："+result.getPost());
        content.setText("岗位要求："+result.getDemand());
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
