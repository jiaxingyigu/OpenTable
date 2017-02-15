package com.yigu.opentable.activity.purcase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.yigu.opentable.view.PayWayLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LivePurcaseActivity extends BaseActivity {
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
    boolean hasBZ = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_purcase);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            SHOP = getIntent().getStringExtra("SHOP");
            hasBZ = getIntent().getBooleanExtra("hasBZ",false);
        }
        if(!TextUtils.isEmpty(SHOP)){
            initView();
            initListener();
            load();
            loadPay();
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

        if(hasBZ)
            payWayLayout.showBZ();

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) payWayLayout.getLayoutParams();
        lp.width= RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height= DPUtil.dip2px(106);
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
                showLoading();
                OrderApi.preorder(LivePurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,payWayLayout.getBZ(), new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("预购成功");
                            startActivity(new Intent(LivePurcaseActivity.this, OrderCompleteActivity.class));
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
                showLoading();
                OrderApi.balancepay(LivePurcaseActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales, payWayLayout.getBZ(),new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("支付成功");
                            startActivity(new Intent(LivePurcaseActivity.this, OrderCompleteActivity.class));
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

            }

            @Override
            public void zhifubaopay() {

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
        CommonApi.getPayment(this, SHOP, new RequestCallback<List<MapiResourceResult>>() {
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

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

}
