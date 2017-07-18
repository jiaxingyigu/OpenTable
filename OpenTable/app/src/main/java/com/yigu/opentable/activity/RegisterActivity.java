package com.yigu.opentable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.UserApi;
import com.yigu.commom.result.MapiUserResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.SMSUtils;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.receiver.SMSBroadcastReceiver;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.commom.util.SMSUtils.EventHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.psd)
    EditText psd;
    @Bind(R.id.psdtwo)
    EditText psdtwo;
    @Bind(R.id.requestCode)
    TextView requestCode;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.reset)
    TextView reset;

    /**
     * 短信验证倒计时--时长
     */
    private int i = 60;
    // 读取短信广播
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    EventHandler eventHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("注册");
    }

    @OnClick({R.id.back, R.id.requestCode, R.id.confirm,  R.id.reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.requestCode:
                String phoneStr = phone.getText().toString();
                if(TextUtils.isEmpty(phoneStr)){
                    MainToast.showShortToast("请输入手机号");
                    return;
                }
                requestCode();
                break;
            case R.id.confirm:
                String phoneStr2 = phone.getText().toString();
                String codeStr = code.getText().toString();
                String psdStr = psd.getText().toString();
                String psdTwoStr = psdtwo.getText().toString();
                if(TextUtils.isEmpty(phoneStr2)){
                    MainToast.showShortToast("请输入您的手机号");
                    return;
                }
                if(TextUtils.isEmpty(codeStr)){
                    MainToast.showShortToast("请输入验证码");
                    return;
                }
                if(TextUtils.isEmpty(psdStr)){
                    MainToast.showShortToast("请输入您的密码");
                    return;
                }
                if(TextUtils.isEmpty(psdTwoStr)){
                    MainToast.showShortToast("请再次输入您的密码");
                    return;
                }
                if(!psdStr.equals(psdTwoStr)){
                    MainToast.showShortToast("两次密码输入不一致");
                    return;
                }
                showLoading();
                UserApi.register(this, phoneStr2, codeStr, psdStr, new RequestCallback<MapiUserResult>() {
                    @Override
                    public void success(MapiUserResult success) {
                        hideLoading();
                        MainToast.showShortToast("注册成功");
                        userSP.saveUserBean(success);
                        Intent cancelIntent = new Intent(RegisterActivity.this,MainNewActivity.class);
                        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cancelIntent);
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
                break;
            case R.id.forget:
                ControllerUtil.ForgetPsd();
                break;
            case R.id.reset:
                phone.setText("");
                psd.setText("");
                psdtwo.setText("");
                code.setText("");
                break;
        }
    }

    /**
     * 向服务器请求验证码
     */
    private void requestCode() {
        SMSUtils.requestCode(this,phone.getText().toString());
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        requestCode.setClickable(false);
        requestCode.setFocusableInTouchMode(false);
        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_soild_dark_red_round_4));
        requestCode.setText("重新发送(" + i + ")");
        initHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        i = 30;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    private void initHandler(){
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = -7;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSUtils.registerEventHandler(eventHandler);
    }

    /**
     * 处理服务器返回的信息
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -9:
                    requestCode.setText("重新发送(" + i + ")");
                    break;
                case -8:
                    requestCode.setText("获取验证码");
                    requestCode.setClickable(true);
                    requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_pressed_color_red));
                    i = 60;
                    break;
                case -7:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    DebugLog.e("event=" + event);
                    if (result == SMSUtils.RESULT_COMPLETE) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE) {
                            MainToast.showShortToast((String) data);

                        }
                    }else if(result == SMSUtils.RESULT_ERROR){
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE_ERROR) {
                            MainToast.showShortToast((String) data);

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        if(null!=eventHandler)
            SMSUtils.unregisterEventHandler(eventHandler);
//        if(null!=smsBroadcastReceiver)
//            unregisterReceiver(smsBroadcastReceiver);
        super.onDestroy();
    }

}
