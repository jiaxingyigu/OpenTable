package com.yigu.opentable.util;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.mob.tools.utils.UIHandler;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.widget.MainToast;

/**
 * 分享到第三方平台公共模块 <br/>
 * 
 * @author brain
 * 
 */
public class ShareModule implements PlatformActionListener, Callback {
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	private Context context;

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 标题对应的url -- QQ空间中使用
	 */
	private String titleUrl;
	/**
	 * 图片路径
	 */
	private String imageUrl;
	/**
	 * 分享的文本内容 -- 所有平台必须
	 */
	private String text;
	/**
	 * 文本内容对应的url -- 微信中和朋友圈中使用
	 */
	private String url;
	/**
	 * 网站名称 -- QQ空间中使用
	 */
	private String site;
	/**
	 * 网站对应地址 -- QQ空间中使用
	 */
	private String siteUrl;

	/**
	 * 
	 * @param context
	 */
	public ShareModule(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @param context
	 * 
	 * @param text
	 *            分享的文本内容
	 * @param title
	 *            分享的标题
	 * @param imageUrl
	 *            图片地址
	 * @param url
	 *            分享的地址
	 */
	public ShareModule(Context context, String title, String text,
			String imageUrl, String url) {
		this.context = context;
		this.text = text;
		this.title = title;
		this.imageUrl = imageUrl;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 临时写死<br />
	 * QQ空間中才使用此參數
	 * 
	 * @return
	 */
	public String getSite() {
		return "鲇鱼（上海）软件有限公司";
	}

	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * 临时写死<br />
	 * QQ空間中才使用此參數
	 * 
	 * @return
	 */
	public String getSiteUrl() {
		return BasicApi.SHARE_APP_URL;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	/**
	 * 初始化ShareSDK
	 * 
	 * @param type
	 *            分享类型 <br />
	 *            0-表示分享楼盘 <br />
	 *            1-表示分享平台
	 */
	/*public void startShare(int type) {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 设置通知显示的图标和文字
		oks.setNotification(R.drawable.icon_app, "通知");
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 设置内容的标题
		oks.setTitle(title);
		// 标题对应的URL QQ空间 + qq 中使用
		oks.setTitleUrl(titleUrl);
		// 设置分享的文本内容
		oks.setText(text);
		// 设置分享照片的路径
		oks.setImageUrl(imageUrl);
		// 在微信中使用，否则可以不适用 --------内容的连接
		oks.setUrl(url);
		// 分享此内容的网站名称，仅在qq空间中使用
		oks.setSite(site);
		// 站点名称的url ~~~ QQ空间中使用(目前不知道具体用处)。
		oks.setSiteUrl(siteUrl);
		if (1 == type) {
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

				@Override
				public void onShare(Platform platform, ShareParams paramsToShare) {
					if (SinaWeibo.NAME.equals(platform.getName())) {
						String shareStr = Constants.SHARE_APP_URL;
						// +"?subjoin=" +
						// Base64.encodeToString("200154".getBytes(),
						// Base64.DEFAULT);
						// L.d("ShareModule", shareStr);
						paramsToShare.setText(shareStr);
					} else {

					}
				}
			});
		}

		// 设置是否直接分享
		oks.setSilent(false);
		oks.setCallback(this);
		oks.show(context);
	}*/

	public void startShare(int type) {
		ShareSDK.initSDK(context);
		ShareParams sp = new ShareParams();
		sp.setText(text);
		// text是分享文本，所有平台都需要这个字段
		sp.setTitle(title);// 标题,朋友圈中只显示此title
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		sp.setTitleUrl(url);
		// sp.setTitleUrl("");
		//		if (type == 4) {
		//			sp.setImagePath(imageUrl); // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		//		} else {
		sp.setImageUrl(imageUrl);
		//		}
		// url仅在微信（包括好友和朋友圈）中使用
//		sp.setUrl(url);// 网页地址

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		//			 sp.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
//		sp.setSite("喜掌柜");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		sp.setSiteUrl(url);
		Platform platform = null;
		switch (type) {
		case 1:
			sp.setUrl(url);
			platform = ShareSDK.getPlatform(Wechat.NAME);
			sp.setShareType(Wechat.SHARE_WEBPAGE);
			break;
		case 2:
			sp.setUrl(url);
			platform = ShareSDK.getPlatform(WechatMoments.NAME);
			sp.setShareType(Wechat.SHARE_WEBPAGE);
			break;
			case 3:
				sp.setSite("峰之约");
				sp.setTitleUrl(url);
				sp.setUrl(url);
				platform = ShareSDK.getPlatform(QQ.NAME);
				sp.setShareType(Wechat.SHARE_WEBPAGE);
				break;
		case 4:
			//			platform = ShareSDK.getPlatform(SinaWeibo.NAME);
			sp.setSite("峰之约");
			sp.setTitleUrl(url);
			sp.setSiteUrl(url);
			platform = ShareSDK.getPlatform(QZone.NAME);
			sp.setShareType(Wechat.SHARE_WEBPAGE);
			break;
		default:
			sp.setUrl(url);
			platform = ShareSDK.getPlatform(Wechat.NAME);
			sp.setShareType(Wechat.SHARE_WEBPAGE);
			break;
		}

		platform.setPlatformActionListener(this);
		platform.share(sp);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1:
			MainToast.showShortToast("分享成功");
			break;
		case 2:
			String expName = msg.obj.getClass().getSimpleName();
			if ("WechatClientNotExistException".equals(expName)
					|| "WechatTimelineNotSupportedException".equals(expName)
					|| "WechatFavoriteNotSupportedException".equals(expName)) {
				MainToast.showShortToast("微信客户端不存在");
			} else if("QQClientNotExistException".equals(expName)){
				MainToast.showShortToast("QQ客户端不存在");
			} else
			{
				MainToast.showShortToast("分享失败");
				Log.d("ShareModule", msg.obj.toString());
			}
			break;
		case 3:
			MainToast.showShortToast("分享取消");
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 分享成功的回调方法
	 */
	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		if (arg0.getName().equals("SinaWeibo")) { // 新浪微博客户端分享的时候回调函数调用不成功（官方人员说的）
			return;
		}
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

	/**
	 * 分享失败(出错)后的回调方法
	 */
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = arg1;
		msg.obj = arg2;
		UIHandler.sendMessage(msg, this);
	}

	/**
	 * 取消分享的回调方法
	 */
	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

}
