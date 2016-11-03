package com.yigu.opentable.activity.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.Config;
import com.yigu.opentable.base.RequestCode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddJobActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.content)
    EditText content;

    ArrayList<MapiCampaignResult> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ButterKnife.bind(this);

        if(null!=getIntent())
            list = (ArrayList<MapiCampaignResult>) getIntent().getSerializableExtra("list");
        if(list==null)
            list = new ArrayList<>();
        initView();
    }

    private void initView() {
        center.setText("添加岗位");
        tvRight.setText("确定");
        back.setImageResource(R.mipmap.back);
    }

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("list",list);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.tv_right:
                if(null!=list&&list.size()>= Config.MAX_VALUE){
                    MainToast.showShortToast("最多上传"+Config.MAX_VALUE+"个岗位");
                    return;
                }
                String nameStr = name.getText().toString();
                String contentStr = content.getText().toString();
                if(TextUtils.isEmpty(nameStr)){
                    MainToast.showShortToast("请输入岗位名称");
                    return;
                }
                if(TextUtils.isEmpty(contentStr)){
                    MainToast.showShortToast("请输入岗位要求");
                    return;
                }
                final MapiCampaignResult campaignResult = new MapiCampaignResult();
                campaignResult.setName(nameStr);
                CampaignApi.addpost(this, nameStr, contentStr, new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        MainToast.showShortToast("添加成功");

                        name.setText("");
                        content.setText("");

                        campaignResult.setId((String) success);

                        list.add(campaignResult);

                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        MainToast.showShortToast(message);
                    }
                });
                break;
        }
    }
}
