package com.yigu.opentable.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.yigu.commom.api.CommonApi;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiDepartmentResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/6/8.
 */
public class PayWayTwoLayout extends LinearLayout {

    @Bind(R.id.send_ll)
    LinearLayout sendLl;
    @Bind(R.id.bz_et)
    EditText bzEt;
    @Bind(R.id.type_one)
    TextView typeOne;
    @Bind(R.id.type_two)
    TextView typeTwo;
    @Bind(R.id.type_three)
    TextView typeThree;
    @Bind(R.id.type_four)
    TextView typeFour;
    @Bind(R.id.maxLayout)
    MaxHeightLayout maxLayout;
    @Bind(R.id.radioOne)
    RadioButton radioOne;
    @Bind(R.id.radioTwo)
    RadioButton radioTwo;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.addrLL)
    LinearLayout addrLL;

    OptionsPickerView positionOptions;
    ArrayList<MapiDepartmentResult> posOptions1Items = new ArrayList<>();
    ArrayList<ArrayList<MapiDepartmentResult>> posOptions2Items = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<MapiDepartmentResult>>> posOptions3Items = new ArrayList<>();

    private Context mContext;
    private View view;
    BaseActivity activity;

    String ctype1 = "";
    String ctype2 = "";
    String ctype3 = "";

    public String getCtype1() {
        return ctype1;
    }

    public String getCtype2() {
        return ctype2;
    }

    public String getCtype3() {
        return ctype3;
    }

    public String getBz(){
        return TextUtils.isEmpty(bzEt.getText()) ? "" : bzEt.getText().toString();
    }

    public PayWayTwoLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
    }

    public PayWayTwoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
    }

    public PayWayTwoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_way_two, this);
        ButterKnife.bind(this, view);
        maxLayout.setMaxHeight(1350);
        //选项选择器
        positionOptions = new OptionsPickerView(mContext);
    }

    public void setHiddenSend(){
        sendLl.setVisibility(View.GONE);
    }

    public void load(String companyId){
        posOptions1Items.clear();
        posOptions2Items.clear();
        posOptions3Items.clear();
        activity.showLoading();
        CommonApi.getDepartment(activity, companyId, new RequestCallback<List<MapiDepartmentResult>>() {
            @Override
            public void success(List<MapiDepartmentResult> success) {
                activity.hideLoading();
                if(null==success||success.isEmpty())
                    return;

               initData(success);

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                activity.hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    public void loadSend(String SHOP){
        activity.showLoading();
        OrderApi.getDelivery(activity, SHOP, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                activity.hideLoading();
                if(null!=success){

                    try {
                        String delivery1 = success.getJSONObject("data").getString("delivery1");
                        String delivery2 = success.getJSONObject("data").getString("delivery2");

                        if(delivery1.equals("1")){
                            showRadioOne();
                        }

                        if(delivery2.equals("1")){
                            showRadioTwo();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                activity.hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }
    ArrayList<MapiDepartmentResult> options2Items_01;
    ArrayList<ArrayList<MapiDepartmentResult>> options3Items_01;
    ArrayList<MapiDepartmentResult> options3Items_01_01;

    private void initData(List<MapiDepartmentResult> success){
        for(MapiDepartmentResult departmentResult : success){
            //选项1
            posOptions1Items.add(departmentResult);
            options3Items_01 = new ArrayList<>();
            options2Items_01 = new ArrayList<>();
            if (null != departmentResult.getDepartment()) {
                for (MapiDepartmentResult departmentResult2 : departmentResult.getDepartment()) {

                    //选项2
                    options2Items_01.add(departmentResult2);
                    options3Items_01_01 = new ArrayList<>();
                    if (null != departmentResult2.getDepartment()) {
                        for (MapiDepartmentResult departmentResult3 : departmentResult2.getDepartment()) {
                            //选项3
                            options3Items_01_01.add(departmentResult3);
                        }
                    }
                    options3Items_01.add(options3Items_01_01);
                }
            }
            posOptions3Items.add(options3Items_01);
            posOptions2Items.add(options2Items_01);
        }
        //三级联动效果
        positionOptions.setPicker(posOptions1Items, posOptions2Items, posOptions3Items,true);//posOptions3Items,
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
        positionOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        positionOptions.setSelectOptions(0,0,0);
        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                String options1Str = posOptions1Items.get(options1).getPickerViewText();
                ctype1 = posOptions1Items.get(options1).getId();
                String options2Str = "";
                if(posOptions2Items.get(options1).isEmpty()){
                    options2Str = "";
                    ctype2 = "";
                }else{
                    options2Str = "-" +  posOptions2Items.get(options1).get(option2).getPickerViewText();
                    ctype2 = posOptions2Items.get(options1).get(option2).getId();
                }

                String options3Str = "";

                if(posOptions3Items.get(options1).isEmpty()||posOptions3Items.get(options1).get(option2).isEmpty()){
                    options3Str = "";
                    ctype3 = "";
                }else{
                    options3Str = "-" +  posOptions3Items.get(options1).get(option2).get(options3).getPickerViewText();
                    ctype3 = posOptions3Items.get(options1).get(option2).get(options3).getId();
                }

                addrTv.setText(options1Str + options2Str + options3Str);

            }
        });

    }

    public String getSendPay() {
        String sendPay = "";

        if (radioOne.getVisibility()==View.VISIBLE&&radioOne.isChecked())
            sendPay = "自提";
        else if(radioTwo.getVisibility()==View.VISIBLE&&radioTwo.isChecked())
            sendPay = "送货上门";

        return sendPay;
    }

    public void setTypeOne() {
        typeOne.setVisibility(View.VISIBLE);
    }

    public void setTypeTwo() {
        typeTwo.setVisibility(View.VISIBLE);
    }

    public void setTypeThree() {
        typeThree.setVisibility(View.VISIBLE);
    }

    public void setTypeFour() {
        typeFour.setVisibility(View.VISIBLE);
    }

    public void showRadioOne(){
        if(null!=radioOne)
            radioOne.setVisibility(View.VISIBLE);
    }

    public void showRadioTwo(){
        if(null!=radioTwo)
            radioTwo.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.send_ll, R.id.type_one, R.id.type_two, R.id.type_three, R.id.type_four,R.id.addrLL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_ll:
                break;
            case R.id.type_one:
                if (null != payWayListener)
                    payWayListener.preorder();
                break;
            case R.id.type_two:
                if (null != payWayListener)
                    payWayListener.balancepay();
                break;
            case R.id.type_three:
                if (null != payWayListener)
                    payWayListener.zhifubaopay();

                break;
            case R.id.type_four:
                if (null != payWayListener)
                    payWayListener.weixinpay();
                break;
            case R.id.addrLL:
                positionOptions.show();
                break;
        }
    }

    PayWayListener payWayListener;

    public interface PayWayListener {
        /**
         * 预购
         */
        void preorder();

        /**
         * 职工卡支付
         */
        void balancepay();

        /**
         * 微信支付
         */
        void weixinpay();

        /**
         * 支付宝支付
         */
        void zhifubaopay();
    }

    public void setPayWayListener(PayWayListener payWayListener) {
        this.payWayListener = payWayListener;
    }

}
