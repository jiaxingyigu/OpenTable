package com.yigu.opentable.activity.purcase;

import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.api.OrderApi;
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
import com.yigu.opentable.adapter.purcase.PurcaseAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.AnimationUtil;
import com.yigu.opentable.util.zhifubao.PayResult;
import com.yigu.opentable.view.PayWayLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TenantPurcaseActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.backgound)
    View backgound;
    @Bind(R.id.payWayLayout)
    PayWayLayout payWayLayout;
    @Bind(R.id.allPrice)
    TextView allPriceTv;
    @Bind(R.id.deel)
    TextView deel;

    String SHOP = "";
    double allPrice = 0;
    private DbManager db;
    String sales = "";

    PurcaseAdapter mAdapter;
    private List<IndexData> mList = new ArrayList<>();
    private boolean hasAddr = false;
    private static final int SDK_PAY_FLAG = 1;
    String orderId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_purcase);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            SHOP = getIntent().getStringExtra("SHOP");
            hasAddr = getIntent().getBooleanExtra("hasAddr",false);
        }
        if(!TextUtils.isEmpty(SHOP)){
            initView();
            initListener();
            load();
        }
    }

    private void initView() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        back.setImageResource(R.mipmap.back);
        tvRight.setText("清空");
        center.setText("购物车");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PurcaseAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        payWayLayout.setTypeTwo();
        payWayLayout.setTypeThree();
        payWayLayout.setTypeFour();

        payWayLayout.setAddrTip(TextUtils.isEmpty(userSP.getUserBean().getTip())?"":userSP.getUserBean().getTip());

        if(hasAddr)
            payWayLayout.showAddr();


        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) payWayLayout.getLayoutParams();
        lp.width= RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height= DPUtil.dip2px(153);
        payWayLayout.setLayoutParams(lp);

    }

    private void initListener() {

        mAdapter.setNunerListener(new PurcaseAdapter.NumberListener() {
            @Override
            public void numerAdd(View view, int position) {
                MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
                String id = orderResult.getID();

                try {
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "==", id).and("sid","=",SHOP).findFirst();
                    if(null!=history){
                        DebugLog.i("history=>"+history.getNum());
                        String numStr = TextUtils.isEmpty(history.getNum())?"0":history.getNum();
                        int numInteger = Integer.parseInt(numStr);
                        history.setNum(++numInteger+"");
                        db.update(history);
                        refreshData();
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void numberCut(View view, int position) {
                MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
                String id = orderResult.getID();

                try {
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "==", id).and("sid","=",SHOP).findFirst();
                    if(null!=history){
                        DebugLog.i("history=>"+history.getNum());
                        String numStr = TextUtils.isEmpty(history.getNum())?"0":history.getNum();
                        int numInteger = Integer.parseInt(numStr);
                        if(numInteger==0) {
                            return;
                        }else{
                            history.setNum(--numInteger+"");
                            db.update(history);
                            if(numInteger==0){
                                history.setNum("0");
                                db.update(history);

                            }
                        }
                        refreshData();
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

        payWayLayout.setPayWayListener(new PayWayLayout.PayWayListener() {
            @Override
            public void preorder() {
                if(TextUtils.isEmpty(payWayLayout.getAddr())){
                    MainToast.showShortToast("请输入收货地址");
                }else{
                    OrderApi.preorder(TenantPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales, payWayLayout.getAddr(),new RequestCallback() {
                        @Override
                        public void success(Object success) {
                            try {
                                db.delete(MapiOrderResult.class);
                                MainToast.showShortToast("预购成功");
                                startActivity(new Intent(TenantPurcaseActivity.this, OrderCompleteActivity.class));
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new RequestExceptionCallback() {
                        @Override
                        public void error(String code, String message) {
                            MainToast.showShortToast(message);
                        }
                    });
                }

            }

            @Override
            public void balancepay() {
                if(TextUtils.isEmpty(payWayLayout.getAddr())){
                    MainToast.showShortToast("请输入收货地址");
                }else{
                    OrderApi.balancepay(TenantPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,payWayLayout.getAddr(), new RequestCallback() {
                        @Override
                        public void success(Object success) {
                            try {
                                db.delete(MapiOrderResult.class);
                                MainToast.showShortToast("支付成功");
                                startActivity(new Intent(TenantPurcaseActivity.this, OrderCompleteActivity.class));
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new RequestExceptionCallback() {
                        @Override
                        public void error(String code, String message) {
                            MainToast.showShortToast(message);
                        }
                    });
                }

            }

            @Override
            public void weixinpay() {

            }

            @Override
            public void zhifubaopay() {
                if(TextUtils.isEmpty(payWayLayout.getAddr())){
                    MainToast.showShortToast("请输入收货地址");
                }else{
                    OrderApi.zhifubaoPay(TenantPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), "0.01", new RequestCallback<JSONObject>() {
                        @Override
                        public void success(JSONObject success) {

                            String orderInfo  = success.getJSONObject("data").getString("orderInfo");
                            orderId = success.getJSONObject("data").getString("orderId");
                            callPay(orderInfo);
                        }
                    }, new RequestExceptionCallback() {
                        @Override
                        public void error(String code, String message) {
                            MainToast.showShortToast(message);
                        }
                    });
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.back, R.id.tv_right, R.id.backgound, R.id.deel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tv_right:
                try {
                    db.delete(MapiOrderResult.class);
                    refreshData();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.backgound:
                if (payWayLayout.getVisibility() == View.VISIBLE)
                    hideAlbum();
                break;
            case R.id.deel:
                if(mList==null||mList.size()==0){
                    MainToast.showShortToast("您的购物车是空的，快去添加吧");
                }else
                    album();
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

    public void load() {
        int count = 0;
        allPrice = 0;
        String priceStr;
        String numStr;
        List<IndexData> list = new ArrayList<>();
        try {
            List<MapiOrderResult> orderList = db.selector(MapiOrderResult.class).where("num","<>","0").and("sid","=",SHOP).findAll();
            orderList.toString();

            JSONArray jsonArray = new JSONArray();
//            jsonArray.addAll(orderList);
            JSONObject tmpObj = null;
            for(int i = 0; i < orderList.size(); i++)
            {
                tmpObj = new JSONObject();
                tmpObj.put("PRICE" , orderList.get(i).getPRICE());
                tmpObj.put("FOOD", orderList.get(i).getFOOD());
                tmpObj.put("AMOUNT", orderList.get(i).getNum());
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

        } catch (DbException e) {
            e.printStackTrace();
        }

        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            load();
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
                PayTask alipay = new PayTask(TenantPurcaseActivity.this);
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

                        OrderApi.zhifu(TenantPurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice + "", sales, payWayLayout.getAddr(),orderId,"3", new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                try {
                                    Toast.makeText(TenantPurcaseActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    db.delete(MapiOrderResult.class);
                                    startActivity(new Intent(TenantPurcaseActivity.this,OrderCompleteActivity.class));
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(String code, String message) {
                                MainToast.showShortToast(message);
                            }
                        });

                        DebugLog.i("支付宝支付成功");
//					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(TenantPurcaseActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        }
                        else if(TextUtils.equals(resultStatus, "6002 ")){
                            Log.i("info","网络连接出错");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(TenantPurcaseActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
