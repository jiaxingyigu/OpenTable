package com.yigu.opentable.activity.food;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DateUtil;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodOrderActivity extends BaseActivity {


    MapiOrderResult mapiOrderResult;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.clear)
    ImageView clear;
    @Bind(R.id.detailTime)
    EditText detailTime;
    @Bind(R.id.number)
    EditText number;
    @Bind(R.id.instruction)
    EditText instruction;

    TimePickerView pvTime;
    private String created = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            mapiOrderResult = (MapiOrderResult) getIntent().getSerializableExtra("item");
        }
        if (null != mapiOrderResult) {
            initView();
        }

    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("订座");
        tvRight.setText("提交");

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
                clear.setVisibility(View.VISIBLE);
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
                created = DateUtil.getInstance().date2YMD_H(date);
            }

        });

        dateTv.setText(DateUtil.getInstance().date2YMD_H(calendar.getTime()));
        clear.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.back, R.id.tv_right, R.id.clear, R.id.date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                upload();
                break;
            case R.id.clear:
                dateTv.setText("请选择用餐日期");
                clear.setVisibility(View.GONE);
                created = "";
                break;
            case R.id.date:
                pvTime.show();
                break;
        }
    }

    private void upload(){
        if(TextUtils.isEmpty(dateTv.getText())||"请选择用餐日期".equals(dateTv.getText().toString())){
            MainToast.showShortToast("请选择用餐日期");
            return;
        }
        if(TextUtils.isEmpty(detailTime.getText())){
            MainToast.showShortToast("请输入用餐时间");
            return;
        }
        if(TextUtils.isEmpty(number.getText())){
            MainToast.showShortToast("请输入用餐人数");
            return;
        }
        showLoading();
        OrderApi.reservation(this,mapiOrderResult.getCompanyId(),userSP.getUserBean().getUSER_ID(), mapiOrderResult.getID(), dateTv.getText().toString(), detailTime.getText().toString()
                , number.getText().toString(),instruction.getText().toString(), new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("商家确认中，请耐心等待！");
                        ControllerUtil.go2FoodHisOrder();
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
    }

}
