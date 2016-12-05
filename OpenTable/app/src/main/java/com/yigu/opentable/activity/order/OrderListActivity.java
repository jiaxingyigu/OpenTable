package com.yigu.opentable.activity.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DateUtil;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.order.OrderListAadpter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BaseItemDialog;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.ItemDialog;

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

    private List<MapiItemResult> mList;
    OrderListAadpter mAdapter;
    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    TimePickerView pvTime;

    BaseItemDialog baseItemDialog;

    List<MapiResourceResult> timeList = new ArrayList<>();
    List<MapiResourceResult> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("一食堂");

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new OrderListAadpter(this, mList);
        recyclerView.setAdapter(mAdapter);

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
            }

        });

        if (baseItemDialog == null)
            baseItemDialog = new BaseItemDialog(this, R.style.image_dialog_theme);


        timeList.add(new MapiResourceResult("1","早餐（6：00 - 8：30）"));
        timeList.add(new MapiResourceResult("1","中餐（11：00 - 13：00）"));
        timeList.add(new MapiResourceResult("1","晚餐（16：30 - 19：00）"));
        baseItemDialog.setmList(timeList);
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
                ControllerUtil.go2OrderDetail();
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
                MainToast.showShortToast(timeList.get(position).getNAME());
                tiemTv.setText(timeList.get(position).getNAME());
            }
        });

    }

    private void load() {

      /*  CampaignApi.getActivitylist(this, pageIndex + "", pageSize+"",new RequestPageCallback<List<MapiCampaignResult>>() {
            @Override
            public void success(Integer isNext,List<MapiCampaignResult> success) {
                swipeRefreshLayout.setRefreshing(false);
                ISNEXT = isNext;
                if(success.isEmpty())
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                swipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });*/
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


    @OnClick({R.id.back, R.id.date,R.id.purcase,R.id.deel,R.id.ll_time,R.id.ll_types})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.date:
                pvTime.show();
                break;
            case R.id.purcase:
                ControllerUtil.go2Purcase();
                break;
            case R.id.deel:
                ControllerUtil.go2Payment();
                break;
            case R.id.ll_time:
                timeList.clear();
                timeList.add(new MapiResourceResult("1","早餐（6：00 - 8：30）"));
                timeList.add(new MapiResourceResult("1","中餐（11： 00 - 13：00）"));
                timeList.add(new MapiResourceResult("1","晚餐（16：30 - 19：00）"));
                baseItemDialog.setmList(timeList);
                baseItemDialog.showDialog();
                break;
            case R.id.ll_types:
                timeList.clear();
                timeList.add(new MapiResourceResult("1","普通餐"));
                timeList.add(new MapiResourceResult("1","营养餐"));
                timeList.add(new MapiResourceResult("1","流质餐"));
                baseItemDialog.setmList(timeList);
                baseItemDialog.showDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=pvTime&&pvTime.isShowing()){
            pvTime.dismiss();
            pvTime = null;
        }
        if(null!=baseItemDialog&&baseItemDialog.isShowing()){
            baseItemDialog.dismiss();
            baseItemDialog = null;
        }
    }
}
