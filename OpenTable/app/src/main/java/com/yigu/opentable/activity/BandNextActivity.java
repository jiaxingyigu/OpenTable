package com.yigu.opentable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BandNextActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.card_num)
    EditText cardNum;
    @Bind(R.id.unit_name)
    TextView unit_name;

    MapiCampaignResult campaignResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_next);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("绑定单位");
    }

    @OnClick({R.id.back, R.id.band, R.id.reset,R.id.search_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.band:
                String nameStr = name.getText().toString();
                String cardStr = cardNum.getText().toString();
                String unitStr = unit_name.getText().toString();
                if(TextUtils.isEmpty(unitStr)){
                    MainToast.showShortToast("请输入您的单位名称");
                    return;
                }
                if(TextUtils.isEmpty(nameStr)){
                    MainToast.showShortToast("请输入您的姓名");
                    return;
                }
                if(TextUtils.isEmpty(cardStr)){
                    MainToast.showShortToast("请输入您的唯一识别码");
                    return;
                }
                showLoading();
                CampaignApi.binding(this, userSP.getUserBean().getUSER_ID(), nameStr, cardStr, campaignResult.getId(), new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
                        String PATH = success.getJSONObject("data").getString("PATH");
                        MapiUserResult userResult = userSP.getUserBean();
                        userResult.setCOMPANY(campaignResult.getId());
                        userResult.setLogo(PATH);
                        userSP.saveUserBean(userResult);
                        MainToast.showShortToast("单位绑定成功");
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
                break;
            case R.id.reset:
                campaignResult = null;
                unit_name.setText("");
                name.setText("");
                cardNum.setText("");
                break;
            case R.id.search_ll:
                Intent intent = new Intent(this,SearchCampaignActivity.class);
                startActivityForResult(intent, RequestCode.search_campaign);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RequestCode.search_campaign:
                    if(null!=data)
                        campaignResult = (MapiCampaignResult) data.getSerializableExtra("item");
                        unit_name.setText(campaignResult.getName());
                        break;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", campaignResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        campaignResult = (MapiCampaignResult) savedInstanceState.getSerializable("item");
    }

}
