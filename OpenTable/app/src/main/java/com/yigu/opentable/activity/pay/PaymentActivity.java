package com.yigu.opentable.activity.pay;

import android.content.Intent;
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
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiCartResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.order.OrderCompleteActivity;
import com.yigu.opentable.adapter.pay.PaymentAdapter;
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

public class PaymentActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.deel)
    TextView deel;
    @Bind(R.id.payWayLayout)
    PayWayLayout payWayLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        if(null!=getIntent())
            SHOP = getIntent().getStringExtra("SHOP");
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
        center.setText("提交订单");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PaymentAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        payWayLayout.setTypeOneAndTwo();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) payWayLayout.getLayoutParams();
        lp.width= RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height= DPUtil.dip2px(53);
        payWayLayout.setLayoutParams(lp);

    }

    private void initListener() {

        payWayLayout.setPayWayListener(new PayWayLayout.PayWayListener() {
            @Override
            public void preorder() {
                OrderApi.preorder(PaymentActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,"", new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("预购成功");
                            startActivity(new Intent(PaymentActivity.this, OrderCompleteActivity.class));
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

            @Override
            public void balancepay() {
                OrderApi.balancepay(PaymentActivity.this, userSP.getUserBean().getUSER_ID(), SHOP, allPrice+"", sales,"", new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        try {
                            db.delete(MapiOrderResult.class);
                            MainToast.showShortToast("支付成功");
                            startActivity(new Intent(PaymentActivity.this, OrderCompleteActivity.class));
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
            @Override
            public void weixinpay() {

            }

            @Override
            public void zhifubaopay() {

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
                tmpObj.put("dinnertime", orderList.get(i).getDinnertime());
                tmpObj.put("stardate", orderList.get(i).getStardate());
                jsonArray.add(tmpObj);
                tmpObj = null;
            }

            sales = jsonArray.toJSONString();
            if(orderList!=null&&orderList.size()>0){
                list.add(new IndexData(count++, "head", orderList.get(0)));
                list.add(new IndexData(count++, "item", orderList.get(0)));
                priceStr = TextUtils.isEmpty(orderList.get(0).getPRICE())?"0":orderList.get(0).getPRICE();
                numStr = TextUtils.isEmpty(orderList.get(0).getNum())?"0":orderList.get(0).getNum();
                allPrice += (Double.parseDouble(priceStr)*Integer.parseInt(numStr));
                for(int i=1;i<orderList.size();i++){
                    MapiOrderResult orderResult = orderList.get(i);
                    if(orderResult.getStardate().equals(orderList.get(i-1).getStardate())&&orderResult.getDinnertime().equals(orderList.get(i-1).getDinnertime())){
                        list.add(new IndexData(count++, "item", orderList.get(i)));
                    }else{
                        list.add(new IndexData(count++, "divider", new Object()));
                        list.add(new IndexData(count++, "head", orderList.get(i)));
                        list.add(new IndexData(count++, "item", orderList.get(i)));
                    }

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

}
