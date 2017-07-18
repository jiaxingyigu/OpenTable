package com.yigu.opentable.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiPlatformResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.MainNewAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.broadcast.ReceiverAction;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.util.JpushUtil;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainNewActivity extends BaseActivity {

    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    MainNewAdapter mAdapter;
    List<IndexData> mList;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!userSP.checkLogin()) {
            ControllerUtil.go2Login();
            finish();
        } else {
            setContentView(R.layout.activity_main_new);
            ButterKnife.bind(this);
            initView();
            load();
            JpushUtil.getInstance().verifyInit(this);
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }
            if (!userSP.getAlias()) {
                DebugLog.i("getAlias===>false");
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getUSER_ID());
            }
            registerMessageReceiver();  // used for receive msg
        }
    }

    private void initView() {
        center.setText("首页");
        tvRight.setText("个人中心");
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this, FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainNewAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void load() {
        showLoading();
        CommonApi.loadResources(this, userSP.getUserBean().getUSER_ID(), new RequestCallback<String>() {
            @Override
            public void success(String success) {
                hideLoading();
                userSP.saveResource(success);
                if (null != userSP.getResource()) {
                    JSONObject jsonObject = JSONObject.parseObject(userSP.getResource());

                    List<MapiResourceResult> list = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("version").toJSONString(), MapiResourceResult.class);
                    if (null != list && !list.isEmpty())
                        checkVersion(list.get(0));
                    List<MapiResourceResult> images = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("poster").toJSONString(), MapiResourceResult.class);
                    if (null != images && !images.isEmpty()) {
//                        homeSliderLayout.setSlider(true);
//                        homeSliderLayout.load(images);
                        mList.add(new IndexData(0, "SLIDER_IMAGE", images));
                    }

                    mList.add(new IndexData(1, "SERVICE", new Object()));

                    if(null!=jsonObject.getJSONObject("data").getJSONArray("ptxx")){
                        List<MapiPlatformResult> plats = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("ptxx").toJSONString(), MapiPlatformResult.class);
                        if (null != plats && !plats.isEmpty()) {
                            mList.add(new IndexData(2, "ITEM_FLAT", plats));
                        }
                    }

                    if(null!=jsonObject.getJSONObject("data").getJSONArray("com")){
                        List<MapiCampaignResult> units = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("com").toJSONString(), MapiCampaignResult.class);
                        if (null != units && !units.isEmpty()) {
                            mList.add(new IndexData(3, "ITEM_UNIT", units));
                        }
                    }

                    if(null!=jsonObject.getJSONObject("data").getJSONArray("sp")){
                        List<MapiOrderResult> tenants = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("sp").toJSONString(), MapiOrderResult.class);
                        if (null != tenants && !tenants.isEmpty()) {
                            mList.add(new IndexData(4, "ITEM_TENANT", tenants));
                        }
                    }

                    if(null!=jsonObject.getJSONObject("data").getJSONArray("msf")){
                        List<MapiOrderResult> foods = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("msf").toJSONString(), MapiOrderResult.class);
                        if (null != foods && !foods.isEmpty()) {
                            mList.add(new IndexData(5, "ITEM_FOOD", foods));
                        }
                    }

                    mAdapter.notifyDataSetChanged();

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
            UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;
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
        int type = intent.getIntExtra("type", 0);
        if (type == 3) {
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

    @OnClick(R.id.tv_right)
    public void onClick() {
        ControllerUtil.go2Person();
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
                if (!TextUtils.isEmpty(messge)) {
                    JSONObject jsonObject = JSONObject.parseObject(messge);

                    String result = jsonObject.getString("result");
                    String data = jsonObject.getString("data");

                    MainToast.showShortToast(data);

                    if ("00".equals(result)) {
                        MapiUserResult userResult = userSP.getUserBean();
                        userResult.setCOMPANY("");
                        userResult.setLogo("");
                        userSP.saveUserBean(userResult);
                        Intent i = new Intent(MainNewActivity.this, MainNewActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else if ("11".equals(result)) {
                        Intent foodIntent = new Intent(ReceiverAction.FOOD_COMPLETE_ACTION);
                        sendBroadcast(foodIntent);
                    } else if ("22".equals(result)) {
                        Intent foodIntent = new Intent(ReceiverAction.FOOD_FAIL_ACTION);
                        sendBroadcast(foodIntent);
                    }

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMessageReceiver)
            unregisterReceiver(mMessageReceiver);
        if (userSP.checkLogin()) {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
