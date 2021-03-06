package com.yigu.opentable.activity.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.order.OrderCompleteActivity;
import com.yigu.opentable.adapter.pay.PaymentAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.broadcast.ReceiverAction;
import com.yigu.opentable.util.AnimationUtil;
import com.yigu.opentable.util.zhifubao.PayResult;
import com.yigu.opentable.view.PayWayLayout;
import com.yigu.opentable.view.PayWayTwoLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LivePayActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.deel)
    TextView deel;
    @Bind(R.id.payWayLayout)
    PayWayTwoLayout payWayLayout;
    @Bind(R.id.backgound)
    View backgound;
    @Bind(R.id.allPrice)
    TextView allPriceTv;

    private List<IndexData> mList = new ArrayList<>();

    private Integer pageIndex = 0;
    private Integer pageSize = 10;
    private Integer ISNEXT = 0;

    PaymentAdapter mAdapter;

    private DbManager db;

    String SHOP = "";
    double allPrice = 0;
    String sales = "";
    private static final int SDK_PAY_FLAG = 1;
    String orderId = "";
    String eid = "";
    String companyId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_pay);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            SHOP = getIntent().getStringExtra("SHOP");
            companyId = getIntent().getStringExtra("companyId");
            eid = getIntent().getStringExtra("eid");
            if(TextUtils.isEmpty(eid))
                eid = "";
        }if(!TextUtils.isEmpty(SHOP)){
            initView();
            initListener();
            load();
            loadPay();
            registerMessageReceiver();
        }
    }

    private void initView() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        back.setImageResource(R.mipmap.back);
        center.setText("提交订单");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PaymentAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        payWayLayout.loadSend(SHOP);
        payWayLayout.load(companyId);

       /* payWayLayout.setAddrTip("请填写自提或送货地址要求");

        payWayLayout.showAddr();

        *//*if(hasBZ)
            payWayLayout.showBZ();*//*

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) payWayLayout.getLayoutParams();
        lp.width= RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height= DPUtil.dip2px(173);
        payWayLayout.setLayoutParams(lp);*/

    }

    private void initListener() {

        payWayLayout.setPayWayListener(new PayWayTwoLayout.PayWayListener() {
            @Override
            public void preorder() {
                if(TextUtils.isEmpty(payWayLayout.getSendPay())){
                    MainToast.showShortToast("请选择送货方式");
                    return;
                }
                showLoading();
                OrderApi.preorder(LivePayActivity.this,companyId, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,payWayLayout.getBz(),"",eid,
                        payWayLayout.getCtype1(),payWayLayout.getCtype2(),payWayLayout.getCtype3(),payWayLayout.getSendPay(),new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("预购成功");
                            startActivity(new Intent(LivePayActivity.this, OrderCompleteActivity.class).putExtra("back","cook"));
                        } catch (DbException e) {
                            e.printStackTrace();
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

            @Override
            public void balancepay() {
                if(TextUtils.isEmpty(payWayLayout.getSendPay())){
                    MainToast.showShortToast("请选择送货方式");
                    return;
                }
                showLoading();
                OrderApi.balancepay(LivePayActivity.this,companyId, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,payWayLayout.getBz(),"", eid,
                        payWayLayout.getCtype1(),payWayLayout.getCtype2(),payWayLayout.getCtype3(),payWayLayout.getSendPay(),new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("支付成功");
                            startActivity(new Intent(LivePayActivity.this, OrderCompleteActivity.class).putExtra("back","cook"));
                        } catch (DbException e) {
                            e.printStackTrace();
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
            @Override
            public void weixinpay() {
                if(TextUtils.isEmpty(payWayLayout.getSendPay())){
                    MainToast.showShortToast("请选择送货方式");
                    return;
                }
                showLoading();
                int price = (int) (allPrice*100);
                OrderApi.weixinPay(LivePayActivity.this,companyId, userSP.getUserBean().getUSER_ID(), SHOP, allPrice + "", price +"",sales, payWayLayout.getBz(),"",eid,
                        payWayLayout.getCtype1(),payWayLayout.getCtype2(),payWayLayout.getCtype3(),payWayLayout.getSendPay(),new RequestCallback<JSONObject>() {//
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
//                            String orderInfo  = success.getJSONObject("data").getString("orderInfo");
                        orderId = success.getJSONObject("data").getString("orderId");
                        callweixinPay(success);
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
            }

            @Override
            public void zhifubaopay() {
                if(TextUtils.isEmpty(payWayLayout.getSendPay())){
                    MainToast.showShortToast("请选择送货方式");
                    return;
                }
                showLoading();
                OrderApi.zhifubaoPay(LivePayActivity.this,companyId, userSP.getUserBean().getUSER_ID(), SHOP,  allPrice +"", sales, payWayLayout.getBz(),"",eid,
                        payWayLayout.getCtype1(),payWayLayout.getCtype2(),payWayLayout.getCtype3(),payWayLayout.getSendPay(),new RequestCallback<JSONObject>() {//
                    @Override
                    public void success(JSONObject success) {//
                        hideLoading();
                        String orderInfo  = success.getJSONObject("data").getString("orderInfo");
                        orderId = success.getJSONObject("data").getString("orderId");
                        callPay(orderInfo);
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });

            }
        });

    }



    public void load() {
        int count = 0;
        allPrice = 0;
        String priceStr;
        String numStr;
        List<IndexData> list = new ArrayList<>();
        try {
            List<MapiOrderResult> orderList = db.selector(MapiOrderResult.class).where("num","<>","0").and("sid","=",SHOP).orderBy("stardate", true).orderBy("orderby",false).findAll();
//            orderList.toString();
            if(null!=orderList){
                JSONArray jsonArray = new JSONArray();
//            jsonArray.addAll(orderList);
                JSONObject tmpObj = null;
                for(int i = 0; i < orderList.size(); i++)
                {
                    tmpObj = new JSONObject();
                    tmpObj.put("PRICE" , orderList.get(i).getPRICE());
                    tmpObj.put("FOOD", orderList.get(i).getFOOD());
                    tmpObj.put("AMOUNT", orderList.get(i).getNum());
                    tmpObj.put("stardate", orderList.get(i).getStardate());
                    jsonArray.add(tmpObj);
                    tmpObj = null;
                }

                sales = jsonArray.toJSONString();
                if(orderList!=null&&orderList.size()>0){
                    for(int i=0;i<orderList.size();i++){
                        MapiOrderResult orderResult = orderList.get(i);
                        list.add(new IndexData(count++, "item", orderList.get(i)));
                        list.add(new IndexData(count++, "divider", new Object()));
                        priceStr = TextUtils.isEmpty(orderResult.getPRICE())?"0":orderResult.getPRICE();
                        numStr = TextUtils.isEmpty(orderResult.getNum())?"0":orderResult.getNum();
                        allPrice += (Double.parseDouble(priceStr)*Integer.parseInt(numStr));

                    }
                }

                allPriceTv.setText("共"+allPrice+"元");
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void loadPay(){
        showLoading();
        CommonApi.getPayment(this, SHOP,"1", new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null!=success){
                    for(MapiResourceResult mapiResourceResult : success){
                        switch (mapiResourceResult.getTYPE()){
                            case "1":
                                payWayLayout.setTypeOne();
                                break;
                            case "2":
                                payWayLayout.setTypeTwo();
                                break;
                            case "3":
                                payWayLayout.setTypeThree();
                                break;
                            case "4":
                                payWayLayout.setTypeFour();
                                break;
                        }
                    }
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

    private void loadNext() {
        if (ISNEXT != null && ISNEXT == 0) {
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }


    @OnClick({R.id.back, R.id.deel,R.id.backgound})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.deel:
                if(mList==null||mList.size()==0){
                    MainToast.showShortToast("您的购物车是空的，快去添加吧");
                }else
                    album();
                break;
            case R.id.backgound:
                if (payWayLayout.getVisibility() == View.VISIBLE)
                    hideAlbum();
                break;
        }
    }

    private void album() {
        if (payWayLayout.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /** 弹出 */
    private void popAlbum() {
        payWayLayout.setVisibility(View.VISIBLE);
        new AnimationUtil(this, R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(payWayLayout);
        backgound.setVisibility(View.VISIBLE);
    }

    /** 隐藏 */
    private void hideAlbum() {
        new AnimationUtil(this, R.anim.translate_down)
                .setLinearInterpolator().startAnimation(payWayLayout);
        payWayLayout.setVisibility(View.GONE);
        backgound.setVisibility(View.GONE);
    }

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
                PayTask alipay = new PayTask(LivePayActivity.this);
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
                        try {
                            Toast.makeText(LivePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            db.delete(MapiOrderResult.class);
                            startActivity(new Intent(LivePayActivity.this,OrderCompleteActivity.class).putExtra("back","cook"));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        DebugLog.i("支付宝支付成功");
//					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(LivePayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        }
                        else if(TextUtils.equals(resultStatus, "6002 ")){
                            Log.i("info","网络连接出错");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(LivePayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

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
                    /*showLoading();
                    OrderApi.zhifu(TenantPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice + "", sales, payWayLayout.getAddr(),orderId,"4", new RequestCallback() {
                        @Override
                        public void success(Object success) {
                            hideLoading();
                            try {
//                            Toast.makeText(TenantPurcaseActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                db.delete(MapiOrderResult.class);
                                startActivity(new Intent(TenantPurcaseActivity.this,OrderCompleteActivity.class));
                                finish();
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

                    try {
//                            Toast.makeText(TenantPurcaseActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        db.delete(MapiOrderResult.class);
                        startActivity(new Intent(LivePayActivity.this,OrderCompleteActivity.class).putExtra("back","cook"));
                        finish();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    DebugLog.i("微信支付成功");
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
