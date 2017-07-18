package com.yigu.opentable.activity.food;

import android.content.Intent;
import android.net.Uri;
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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.order.OrderDetailActivity;
import com.yigu.opentable.activity.purcase.FoodPurcaseActivity;
import com.yigu.opentable.activity.purcase.TenantPurcaseActivity;
import com.yigu.opentable.adapter.order.OrderListAadpter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodMenuActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;
    @Bind(R.id.purcase)
    TextView purcase;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.rl_purcase)
    RelativeLayout rl_purcase;

    private List<MapiOrderResult> mList;
    OrderListAadpter mAdapter;
    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    MapiOrderResult mapiOrderResult;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            mapiOrderResult = (MapiOrderResult) getIntent().getSerializableExtra("item");
        }
        if (null != mapiOrderResult) {
            initView();
            initListener();
            showLoading();
            load();
//            loadTip();
        }
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText(mapiOrderResult.getNAME());
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + mapiOrderResult.getPIC());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(62), DPUtil.dip2px(62)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);

        content.setText(mapiOrderResult.getINTRODUCTION());

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new OrderListAadpter(this, mList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setShopCart(rl_purcase);

    }

    private void initListener() {
        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(AppContext.getInstance(), OrderDetailActivity.class);
                intent.putExtra("id", mList.get(position).getID());
                intent.putExtra("title", mapiOrderResult.getNAME());
                intent.putExtra("position", position);
                intent.putExtra("all", TextUtils.isEmpty(account.getText()) ? "0" : account.getText().toString());
                intent.putExtra("type","food");
                intent.putExtra("SHOP",mapiOrderResult.getID());
                intent.putExtra("seat",mapiOrderResult.getSeatId());
                intent.putExtra("companyId",mapiOrderResult.getCompanyId());
                startActivityForResult(intent, RequestCode.order_detail);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mAdapter.setNunerListener(new OrderListAadpter.NumberListener() {
            @Override
            public void numerAdd(View view,int position) {
                String num = TextUtils.isEmpty(account.getText().toString())?"0":account.getText().toString();
                int numInt = Integer.parseInt(num);
                account.setText(++numInt+"");
                if(account.getVisibility()==View.INVISIBLE)
                    account.setVisibility(View.VISIBLE);

                String id = mList.get(position).getID();

                try {
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "==", id).and("sid","=",mapiOrderResult.getID()).findFirst();
                    if(null!=history){
                        String numStr = TextUtils.isEmpty(history.getNum())?"0":history.getNum();
                        int numInteger = Integer.parseInt(numStr);
                        history.setNum(++numInteger+"");
                        db.update(history);
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void numberCut(View view,int position) {


                String id = mList.get(position).getID();

                try {
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "==", id).and("sid","=",mapiOrderResult.getID()).findFirst();
                    if(null!=history){
                        DebugLog.i("history=>"+history.getNum());
                        String numStr = TextUtils.isEmpty(history.getNum())?"0":history.getNum();
                        int numInteger = Integer.parseInt(numStr);
                        if(numInteger==0) {
                            return;
                        }else{
                            history.setNum(--numInteger+"");

                            String num = TextUtils.isEmpty(account.getText().toString())?"0":account.getText().toString();
                            int numInt = Integer.parseInt(num);
                            DebugLog.i("numInt==>"+numInt);
                            if(numInt<=1) {
                                account.setText("0");
                                account.setVisibility(View.INVISIBLE);
                            }else{

                                account.setText(--numInt+"");
                            }
                            db.update(history);

                        }

                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void load() {
        showLoading();
        OrderApi.getMFoodmenu(this, mapiOrderResult.getID(), pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                hideLoading();
                swipRefreshLayout.setRefreshing(false);
                ISNEXT = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                try {
                    List<MapiOrderResult> hisList = db.selector(MapiOrderResult.class).where("num","<>","0").and("sid","=",mapiOrderResult.getID()).findAll();
                    if(null!=hisList&&hisList.size()>0){
                        for(MapiOrderResult orderResult : mList){
                            if(hisList.contains(orderResult)){
                                for(MapiOrderResult hisOrder : hisList){
                                    if(hisOrder.getID().equals(orderResult.getID())){
                                        orderResult.setNum(hisOrder.getNum());
                                    }
                                }
                            }
                        }

                        int count = 0;
                        for(MapiOrderResult orderResult : hisList){
                            String numStr = TextUtils.isEmpty(orderResult.getNum())?"0":orderResult.getNum();
                            count += Integer.parseInt(numStr);
                        }
                        if(count==0){
                            account.setText("0");
                            account.setVisibility(View.INVISIBLE);
                        }else{
                            account.setText(count+"");
                            account.setVisibility(View.VISIBLE);
                        }
                    }


                } catch (DbException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
                try {
                    db.saveOrUpdate(mList);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                swipRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });

    }

   /* private void loadTip(){
        showLoading();
        CommonApi.getRemark(this, mapiOrderResult.getID(), new RequestCallback<String>() {
            @Override
            public void success(String success) {
                hideLoading();
                MapiUserResult userResult = userSP.getUserBean();
                userResult.setTip(success);
                userSP.saveUserBean(userResult);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                userSP.getUserBean().setTip("");
                MainToast.showShortToast(message);
            }
        });
    }*/

    private void loadNext() {
        if (ISNEXT != null && ISNEXT == 0) {
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {

            account.setText("0");
            account.setVisibility(View.INVISIBLE);

            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @OnClick({R.id.back, R.id.purcase, R.id.deel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.purcase:
                Intent intent = new Intent(AppContext.getInstance(), FoodPurcaseActivity.class);
                intent.putExtra("SHOP",mapiOrderResult.getID());
                intent.putExtra("seat",mapiOrderResult.getSeatId());
                intent.putExtra("companyId",mapiOrderResult.getCompanyId());
                startActivityForResult(intent,RequestCode.purcase_list);
                break;
            case R.id.deel:
                ControllerUtil.go2FoodPay(mapiOrderResult.getID(),mapiOrderResult.getSeatId(),mapiOrderResult.getCompanyId());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RequestCode.order_detail:
                    /*if(null!=data){
                        int position = data.getIntExtra("position",0);
                        String numStr = data.getStringExtra("num");
                        int nowNumInt = Integer.parseInt(numStr);

                        int itemNum = data.getIntExtra("itemNum",0);

                        DebugLog.i("nowNumInt=>"+nowNumInt);

                        if(nowNumInt>0){
                            account.setVisibility(View.VISIBLE);
                            account.setText(nowNumInt+"");
                        }else{
                            account.setVisibility(View.INVISIBLE);
                            account.setText("0");
                        }

                        mList.get(position).setNum(itemNum+"");

                        mAdapter.notifyItemChanged(position);
                    }*/
                    refreshData();
                    break;
                case RequestCode.purcase_list:
                    /*try {
                        List<MapiOrderResult> hisList = db.selector(MapiOrderResult.class).where("sid","=",mapiOrderResult.getID()).findAll();
                        mList.clear();
                        mList.addAll(hisList);
                        int count = 0;
                        for(MapiOrderResult orderResult : mList){
                            String numStr = TextUtils.isEmpty(orderResult.getNum())?"0":orderResult.getNum();
                            count += Integer.parseInt(numStr);
                        }
                        if(count==0){
                            account.setText("0");
                            account.setVisibility(View.INVISIBLE);
                        }else{
                            account.setText(count+"");
                            account.setVisibility(View.VISIBLE);
                        }

                        mAdapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }*/

                    refreshData();


                    break;
            }

        }
    }

}
