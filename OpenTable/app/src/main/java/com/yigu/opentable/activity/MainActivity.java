package com.yigu.opentable.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.broadcast.ReceiverAction;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.util.JpushUtil;
import com.yigu.opentable.view.HomeSliderLayout;
import com.yigu.updatelibrary.UpdateFunGo;
import com.yigu.updatelibrary.config.DownloadKey;
import com.yigu.updatelibrary.config.UpdateKey;
import com.yigu.updatelibrary.utils.GetAppInfo;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.homeSliderLayout)
    HomeSliderLayout homeSliderLayout;
    private DbManager db;
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
            JpushUtil.getInstance().verifyInit(this);
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }
            if (!userSP.getAlias()) {
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getUSER_ID());
            }
            registerMessageReceiver();  // used for receive msg
        }

    }

    private void initView() {
        center.setText("首页");
        ivRight.setImageResource(R.mipmap.person_white);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

    }


    @OnClick({R.id.iv_right, R.id.ll_order, R.id.ll_sign, R.id.ll_info, R.id.shop_info, R.id.company_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                ControllerUtil.go2Person();
                break;
            case R.id.ll_order:
                ControllerUtil.go2Order();
                break;
            case R.id.ll_sign://
                ControllerUtil.go2Campaign();
                break;
            case R.id.ll_info:
                ControllerUtil.go2Platform();
//                MainToast.showShortToast("敬请期待");
                break;
            case R.id.shop_info://商家入驻
                ControllerUtil.go2ShopEnter();
                break;
            case R.id.company_info://单位入驻
                ControllerUtil.go2CampaignEnter();
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

                    List<MapiResourceResult> list = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("version").toJSONString(), MapiResourceResult.class);
                    if(null!=list&&!list.isEmpty())
                        checkVersion(list.get(0));
                    List<MapiResourceResult> images = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("poster").toJSONString(),  MapiResourceResult.class);
                    if(null!=images&&!images.isEmpty())
                        homeSliderLayout.load(images);

                    String CNAME = jsonObject.getJSONObject("data").getString("CNAME");
                    String PATH = jsonObject.getJSONObject("data").getString("PATH");
                    MapiUserResult userResult = userSP.getUserBean();
                    userResult.setCOMPANY(CNAME);
                    userResult.setLogo(PATH);
                    userSP.saveUserBean(userResult);

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

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ReceiverAction.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ReceiverAction.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(JpushUtil.KEY_MESSAGE);
                /*String extras = intent.getStringExtra(JpushUtil.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(JpushUtil.KEY_MESSAGE + " : " + messge + "\n");
                if (!JpushUtil.getInstance().isEmpty(extras)) {
                    showMsg.append(JpushUtil.KEY_EXTRAS + " : " + extras + "\n");
                }*/
                DebugLog.i(messge);
                if(!TextUtils.isEmpty(messge)){
                    JSONObject jsonObject = JSONObject.parseObject(messge);

                    String result = jsonObject.getString("result");
                    String data = jsonObject.getString("data");

                    MainToast.showShortToast(data);

                    if("00".equals(result)){
                        MapiUserResult userResult = userSP.getUserBean();
                        userResult.setCOMPANY("");
                        userResult.setLogo("");
                        userSP.saveUserBean(userResult);
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mMessageReceiver)
            unregisterReceiver(mMessageReceiver);
        if(userSP.checkLogin()){
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
