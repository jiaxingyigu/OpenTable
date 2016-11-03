package com.yigu.opentable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.api.UserApi;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.view.HomeSliderLayout;
import com.yigu.updatelibrary.UpdateFunGo;
import com.yigu.updatelibrary.config.DownloadKey;
import com.yigu.updatelibrary.config.UpdateKey;
import com.yigu.updatelibrary.utils.GetAppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.homeSliderLayout)
    HomeSliderLayout homeSliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!userSP.checkLogin()) {
            ControllerUtil.go2Login();
            finish();
        } else {
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            initView();
            load();
        }

    }

    private void initView() {
        center.setText("首页");
        ivRight.setImageResource(R.mipmap.person_white);
    }


    @OnClick({R.id.iv_right, R.id.ll_order, R.id.ll_sign, R.id.ll_info, R.id.shop_info, R.id.company_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                ControllerUtil.go2Person();
                break;
            case R.id.ll_order:
                MainToast.showShortToast("敬请期待");
//                ControllerUtil.go2Order();
                break;
            case R.id.ll_sign:
                ControllerUtil.go2Campaign();
                break;
            case R.id.ll_info:
                MainToast.showShortToast("敬请期待");
                break;
            case R.id.shop_info:
                MainToast.showShortToast("敬请期待");
                break;
            case R.id.company_info:
                MainToast.showShortToast("敬请期待");
                break;
        }
    }

    private void load(){
        showLoading();
        CommonApi.loadResources(this, userSP.getUserBean().getUSER_ID(), new RequestCallback<String>() {
            @Override
            public void success(String success) {
                hideLoading();
                userSP.saveResource(success);
                if(null!=userSP.getResource()){
                    JSONObject jsonObject = JSONObject.parseObject(userSP.getResource());

                    List<MapiResourceResult> list = JSON.parseObject(jsonObject.getJSONObject("data").getJSONArray("version").toJSONString(), new TypeReference<List<MapiResourceResult>>(){
                    });
                    if(null!=list&&!list.isEmpty())
                        checkVersion(list.get(0));
                    List<MapiResourceResult> images = JSON.parseObject(jsonObject.getJSONObject("data").getJSONArray("poster").toJSONString(), new TypeReference<List<MapiResourceResult>>(){
                    });
                    if(null!=images&&!images.isEmpty())
                        homeSliderLayout.load(images);

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    /**
     * 检查版本，若不是最新版本则显示弹框
     *
     * @param result
     */
    private void checkVersion(MapiResourceResult result) {
        if (!GetAppInfo.getAppVersionCode(this).equals(result.getVersion())) {
            DownloadKey.version = result.getVersion();
            DownloadKey.changeLog = result.getRemark();
            DownloadKey.apkUrl = result.getUrl();
            //如果你想通过Dialog来进行下载，可以如下设置
            UpdateKey.DialogOrNotification= UpdateKey.WITH_DIALOG;
            DownloadKey.ToShowDownloadView = DownloadKey.showUpdateView;
            UpdateFunGo.init(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGo.onResume(this);//现在只能弹框下载
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGo.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int type = intent.getIntExtra("type",0);
        if(type==3){
            ControllerUtil.go2Login();
            finish();
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
