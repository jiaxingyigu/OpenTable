package com.yigu.opentable.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.commom.util.DebugLog;
import com.yigu.opentable.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/12/2.
 */
public class PayWayLayout extends LinearLayout {

    @Bind(R.id.type_one)
    TextView typeOne;
    @Bind(R.id.type_two)
    TextView typeTwo;
    @Bind(R.id.type_three)
    TextView typeThree;
    @Bind(R.id.type_four)
    TextView typeFour;
    @Bind(R.id.bz_et)
    EditText bzEt;
    @Bind(R.id.ll_bz)
    LinearLayout llBz;
    @Bind(R.id.addr_et)
    EditText addrEt;
    @Bind(R.id.ll_addr)
    LinearLayout llAddr;
    @Bind(R.id.addrTip)
    TextView addrTip;
    @Bind(R.id.bzTip)
    TextView bzTip;

    private Context mContext;
    private View view;

    public PayWayLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PayWayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public PayWayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_way, this);
        ButterKnife.bind(this, view);
    }

    public void setTypeOneAndTwo() {
        typeOne.setVisibility(View.VISIBLE);
        typeTwo.setVisibility(View.VISIBLE);
    }

    public void setTypeTwoAndThreeAndFour() {
        typeTwo.setVisibility(View.VISIBLE);
        typeThree.setVisibility(View.VISIBLE);
        typeFour.setVisibility(View.VISIBLE);
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

    public void setBZTip(String tip){
        bzTip.setText(tip);
    }

    public void showBZ() {
        if (llBz.getVisibility() == View.GONE)
            llBz.setVisibility(View.VISIBLE);
    }

    public void showAddr(){
        if (llAddr.getVisibility() == View.GONE)
            llAddr.setVisibility(View.VISIBLE);
    }

    public String getBZ() {
        return TextUtils.isEmpty(bzEt.getText()) ? "" : bzEt.getText().toString();
    }

    public void setAddrText(String tip) {
       addrTip.setText(tip);
    }

    public String getAddr(){
        return TextUtils.isEmpty(addrEt.getText()) ? "" : addrEt.getText().toString();
    }

    public void setAddrTip(String tip){
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(tip);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        addrEt.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失

    }

    @OnClick({R.id.type_one, R.id.type_two, R.id.type_three, R.id.type_four})
    public void onClick(View view) {
        switch (view.getId()) {
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
                    payWayListener.weixinpay();
                break;
            case R.id.type_four:
                DebugLog.i("zhifubaopay");
                if (null != payWayListener)
                    payWayListener.zhifubaopay();
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
         * */
        void zhifubaopay();
    }

    public void setPayWayListener(PayWayListener payWayListener) {
        this.payWayListener = payWayListener;
    }

}
