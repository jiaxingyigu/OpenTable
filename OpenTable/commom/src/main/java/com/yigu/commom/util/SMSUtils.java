package com.yigu.commom.util;

import android.app.Activity;

import com.yigu.commom.api.UserApi;

/**
 * Created by brain on 2016/10/18.
 */
public class SMSUtils {

    public static int RESULT_COMPLETE = 0x11;// 访问成功
    public static int RESULT_ERROR = 0x22;// 访问失败
    public static int EVENT_GET_VERIFICATION_CODE = 0x33;// 验证码已经发送
    public static int EVENT_GET_VERIFICATION_CODE_ERROR = 0x44;// 验证码发送失败
    public interface EventHandler {
        /*
         * @param event
         *
         * 参数1 事件类型
         *
         * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.
         * RESULT_ERROR表示操作失败
         *
         * @param data 事件操作的结果
         * 如果result=SMSSDK.RESULT_ERROR，则类型为Throwable，如果result
         * =SMSSDK.RESULT_COMPLETE需根据event判断：
         */
        void afterEvent(int event,int result, Object data);
    }

    private static EventHandler eventHandler;

    public static void registerEventHandler(EventHandler hanlder) {
        eventHandler = hanlder;
    }

    public static void requestCode(Activity activity,String phone){
        UserApi.normal(activity, phone, new RequestCallback() {
            @Override
            public void success(Object success) {
                eventHandler.afterEvent(EVENT_GET_VERIFICATION_CODE,
                        RESULT_COMPLETE,"验证码已经发送");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                eventHandler.afterEvent(EVENT_GET_VERIFICATION_CODE_ERROR,
                        RESULT_ERROR,message);
            }
        });
    }

    public static void requestForgerCode(Activity activity,String phone){
        UserApi.upnormal(activity, phone, new RequestCallback() {
            @Override
            public void success(Object success) {
                eventHandler.afterEvent(EVENT_GET_VERIFICATION_CODE,
                        RESULT_COMPLETE,"验证码已经发送");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                eventHandler.afterEvent(EVENT_GET_VERIFICATION_CODE_ERROR,
                        RESULT_ERROR,message);
            }
        });
    }

    public static void unregisterEventHandler(EventHandler hanlder){
        if(null!=eventHandler)
            eventHandler = null;
    }

}
