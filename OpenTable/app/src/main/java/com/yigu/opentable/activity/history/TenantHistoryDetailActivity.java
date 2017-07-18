package com.yigu.opentable.activity.history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.pay.PaymentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.broadcast.ReceiverAction;
import com.yigu.opentable.util.zhifubao.PayResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TenantHistoryDetailActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.bz_tv)
    TextView bzTv;
    @Bind(R.id.send_tv)
    TextView sendTv;
    @Bind(R.id.ll_send)
    LinearLayout llSend;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.ll_addr)
    LinearLayout llAddr;

    String id = "";
    String type = "";
    String zhifu = "";
    String bz = "";
    String addr = "";
    String send = "";
    private List<IndexData> mList = new ArrayList<>();
    PaymentAdapter mAdapter;

    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_history_detail);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            zhifu = getIntent().getStringExtra("zhifu");
            bz = getIntent().getStringExtra("bz");
            addr = getIntent().getStringExtra("addr");
            send = getIntent().getStringExtra("send");
        }
        if (!TextUtils.isEmpty(id)) {
            initView();
            load();
            registerMessageReceiver();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("订单详情");

        if("0".equals(type)){
            llBottom.setVisibility(View.VISIBLE);
        }else{
            llBottom.setVisibility(View.GONE);
        }

        bzTv.setText(TextUtils.isEmpty(bz)?"":bz);

        if(!TextUtils.isEmpty(send)){
            sendTv.setText(send);
            llSend.setVisibility(View.VISIBLE);
        }else{
            llSend.setVisibility(View.GONE);
        }

        addrTv.setText(TextUtils.isEmpty(addr)?"":addr);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PaymentAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    public void load() {
        OrderApi.getSalesdetailslist(this, id, new RequestCallback<List<MapiOrderResult>>() {
            @Override
            public void success(List<MapiOrderResult> success) {
                if (success.isEmpty())
                    return;
                int count = 0;
                if (success != null && success.size() > 0) {
                    for (int i = 0; i < success.size(); i++) {
                        MapiOrderResult orderResult = success.get(i);
                        mList.add(new IndexData(count++, "item", success.get(i)));
                        mList.add(new IndexData(count++, "divider", new Object()));
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back,R.id.del,R.id.pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.del:
                showLoading();
                OrderApi.delesales(this, id, new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        setResult(RESULT_OK);
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
            case R.id.pay:
                showLoading();
                OrderApi.fukuan(this, id, new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
                        String orderInfo  = success.getJSONObject("data").getString("orderInfo");

                        if("支付宝支付".equals(zhifu)){
                            callPay(orderInfo);
                        }else if("微信支付".equals(zhifu)){
                            callweixinPay(success);
                        }
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
                break;
        }
    }

    private void callPay(final String orderInfo){
        /*try {
            *//**
         * 仅需对sign 做URL编码
         *//*
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
       /* final String payInfo = orderInfo + "&sign=\"" + sign ;*/

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(TenantHistoryDetailActivity.this);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    Log.i("resultStatus",resultStatus);
                    Log.i("resultInfo",payResult.getResult());
                    Log.i("memo",payResult.getMemo());
                    if (TextUtils.equals(resultStatus, "9000")) {
                       /* showLoading();
                        OrderApi.zhifu(FoodPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice + "", sales, payWayLayout.getBZ(),orderId,"3", new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                try {
                                    Toast.makeText(FoodPurcaseActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    db.delete(MapiOrderResult.class);
                                    startActivity(new Intent(FoodPurcaseActivity.this,OrderCompleteActivity.class));
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(String code, String message) {
                                MainToast.showShortToast(message);
                                hideLoading();
                            }
                        });*/
                        Toast.makeText(TenantHistoryDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        DebugLog.i("支付宝支付成功");
                        setResult(RESULT_OK);
                        finish();

//					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(TenantHistoryDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        }
                        else if(TextUtils.equals(resultStatus, "6002 ")){
                            Log.i("info","网络连接出错");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(TenantHistoryDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 微信支付
     */
    private void callweixinPay(JSONObject json){

        JSONObject jsonObject = json.getJSONObject("data").getJSONObject("orderInfo");
        if(null != jsonObject){
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId			= jsonObject.getString("appid");
            req.partnerId		= jsonObject.getString("partnerid");
            req.prepayId		= jsonObject.getString("prepayid");
            req.nonceStr		= jsonObject.getString("noncestr");
            req.timeStamp		= jsonObject.getString("timestamp");
            req.packageValue	= jsonObject.getString("package");
            req.sign			= jsonObject.getString("sign");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            AppContext.getInstance().api.sendReq(req);
        }

    }

    //for receive customer msg from jpush server
    private WEIXINPAYReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new WEIXINPAYReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ReceiverAction.WEIXIN_PAY_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    int mark = 0;

    public class WEIXINPAYReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(mark==0){
                if (ReceiverAction.WEIXIN_PAY_ACTION.equals(intent.getAction())) {
                    mark = 1;
//                    Toast.makeText(TenantHistoryDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    DebugLog.i("微信支付成功");
                    TenantHistoryDetailActivity.this.setResult(RESULT_OK);
                    finish();
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mMessageReceiver)
            unregisterReceiver(mMessageReceiver);
    }

}
