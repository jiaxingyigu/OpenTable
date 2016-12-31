package com.yigu.opentable.activity.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DateUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.purcase.PurcaseActivity;
import com.yigu.opentable.adapter.order.OrderListAadpter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BaseItemDialog;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {

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
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.tiem_tv)
    TextView tiemTv;
    @Bind(R.id.types_tv)
    TextView types_tv;
    @Bind(R.id.date)
    LinearLayout date;
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

    TimePickerView pvTime;

    BaseItemDialog baseItemDialog;

    BaseItemDialog typeDialog;

    List<MapiResourceResult> timeList = new ArrayList<>();
    List<MapiResourceResult> typeList = new ArrayList<>();

    MapiOrderResult mapiOrderResult;

    String type = "";
    String style = "";

    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            mapiOrderResult = (MapiOrderResult) getIntent().getSerializableExtra("item");
        }
        if (null != mapiOrderResult) {
            initView();
            initListener();
            showLoading();
            loadTime();
            load();
        }

    }

    private void initView() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

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

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new OrderListAadpter(this, mList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setShopCart(rl_purcase);

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦

        //通过日历获取下一天日期
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        pvTime.setTime(calendar.getTime());

        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
                refreshData();
            }

        });

        dateTv.setText(DateUtil.getInstance().date2YMD_H(calendar.getTime()));


        if (baseItemDialog == null)
            baseItemDialog = new BaseItemDialog(this, R.style.image_dialog_theme);

        if (typeDialog == null)
            typeDialog = new BaseItemDialog(this, R.style.image_dialog_theme);

        typeList.clear();
        typeList.add(new MapiResourceResult("", "全部"));
        typeList.add(new MapiResourceResult("0", "普通菜"));
        typeList.add(new MapiResourceResult("1", "特色菜"));
        typeDialog.setmList(typeList);

        types_tv.setText(typeList.get(0).getNAME());
        style = typeList.get(0).getZD_ID();

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
                intent.putExtra("id",mList.get(position).getID());
                intent.putExtra("title",mapiOrderResult.getNAME());
                intent.putExtra("SHOP",mapiOrderResult.getID());
                intent.putExtra("position",position);
                intent.putExtra("all",TextUtils.isEmpty(account.getText())?"0":account.getText().toString());
                intent.putExtra("type","pay");
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

        baseItemDialog.setDialogItemClickListner(new BaseItemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                tiemTv.setText(timeList.get(position).getTIME());
                type = timeList.get(position).getTYPE();
                refreshData();
            }
        });

        typeDialog.setDialogItemClickListner(new BaseItemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                types_tv.setText(typeList.get(position).getNAME());
                style = typeList.get(position).getZD_ID();
                refreshData();
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
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid","=",mapiOrderResult.getID()).findFirst();
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
                    MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid","=",mapiOrderResult.getID()).findFirst();
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

    private void loadTime() {
        OrderApi.getDinnertime(this, mapiOrderResult.getID(), new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if (success.isEmpty())
                    return;
                timeList.clear();
                timeList.addAll(success);
                timeList.add(0,new MapiResourceResult("","全部"));
                baseItemDialog.setmList(timeList);

                tiemTv.setText(timeList.get(0).getTIME());
                type = timeList.get(0).getTYPE();

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }
    private void load() {
        DebugLog.i("dateTv=>"+dateTv.getText().toString());
        OrderApi.getFoodmenu(this, mapiOrderResult.getID(), dateTv.getText().toString(), type, style, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                swipRefreshLayout.setRefreshing(false);
                ISNEXT = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                try {
                    DebugLog.i("sid=>"+mapiOrderResult.getID());
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
                swipRefreshLayout.setRefreshing(false);
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

            account.setText("0");
            account.setVisibility(View.INVISIBLE);

            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }


    @OnClick({R.id.back, R.id.date, R.id.purcase, R.id.deel, R.id.ll_time, R.id.ll_types})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.date:
                pvTime.show();
                break;
            case R.id.purcase:
                Intent intent = new Intent(AppContext.getInstance(), PurcaseActivity.class);
                intent.putExtra("SHOP",mapiOrderResult.getID());
                startActivityForResult(intent,RequestCode.purcase_list);
                break;
            case R.id.deel:
                ControllerUtil.go2Payment(mapiOrderResult.getID());
                break;
            case R.id.ll_time:
                baseItemDialog.showDialog();
                break;
            case R.id.ll_types:
                typeDialog.showDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pvTime && pvTime.isShowing()) {
            pvTime.dismiss();
            pvTime = null;
        }
        if (null != baseItemDialog && baseItemDialog.isShowing()) {
            baseItemDialog.dismiss();
            baseItemDialog = null;
        }
        /*try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RequestCode.order_detail:
                    refreshData();
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
                    break;
                case RequestCode.purcase_list:
                    refreshData();
                    /*try {
                        List<MapiOrderResult> hisList = db.selector(MapiOrderResult.class).where("sid","=", mapiOrderResult.getID()).findAll();
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


                    break;
            }

        }
    }
}
