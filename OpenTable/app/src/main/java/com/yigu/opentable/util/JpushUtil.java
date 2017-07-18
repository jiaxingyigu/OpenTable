package com.yigu.opentable.util;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;


import com.yigu.commom.application.AppContext;
import com.yigu.commom.sharedpreferences.UserSP;
import com.yigu.commom.util.DebugLog;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushUtil {
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private volatile static JpushUtil jpushUtil;
	protected UserSP userSP;

	public static  JpushUtil getInstance() {
		if (jpushUtil == null) {
			synchronized (JpushUtil.class) {
				jpushUtil = new JpushUtil();
			}
		}
		return jpushUtil;
	}

	private JpushUtil() {
		userSP = new UserSP(AppContext.getInstance());
	}

	public boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public void verifyInit(Context context){
		// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
		if(isEmpty(JPushInterface.getRegistrationID(context))){
			JPushInterface.init(context.getApplicationContext());
		}
	}

	public void stopPush(Context context){
		JPushInterface.stopPush(context);
	}

	// 校验Tag Alias 只能是数字,英文字母和中文
	public boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\\u4E00-\\u9FA50-9a-zA-Z_@!#$&*+=.|￥¥]+$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 设置别名
	 * @param alias
	 */
	public void setAlias(String alias){
		if(!TextUtils.isEmpty(alias)){
			if (!isValidTagAndAlias(alias)) {
				userSP.setAlias(false);
				return;
			}
			//调用JPush API设置Alias
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
		}
	}

	public void setTag(){

	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs ;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				DebugLog.i(logs);
				userSP.setAlias(true);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				DebugLog.i(logs);
				userSP.setAlias(false);
				if (isConnected(AppContext.getInstance())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				} else {
					DebugLog.i( "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				DebugLog.e(logs);
				userSP.setAlias(false);
			}

		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs ;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				DebugLog.i(logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				DebugLog.i(logs);
				if (isConnected(AppContext.getInstance())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
				} else {
					DebugLog.i( "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				DebugLog.e( logs);
			}
		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				DebugLog.d( "Set alias in handler.");
				JPushInterface.setAliasAndTags(AppContext.getInstance(), (String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				DebugLog.d( "Set tags in handler.");
				JPushInterface.setAliasAndTags(AppContext.getInstance(), null, (Set<String>) msg.obj, mTagsCallback);
				break;

			default:
				DebugLog.d("Unhandled msg - " + msg.what);
			}
		}
	};

}
