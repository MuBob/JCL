package com.jcl.android.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.android.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class SharePopupwindow extends PopupWindow implements OnClickListener {

	private Activity context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private TextView tv_sina, tv_wechat, tv_wxcircle, tv_sms, tv_email, tv_qq;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private String title;
	private String content;
	private String url;
	private void initShare() {
		
		// 设置分享内容
		mController
				.setShareContent(content);
		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(context,
//				"http://www.baidu.com/img/bdlogo.png"));
		String appID = "wx9d3bff9b821f5917";
//		String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";
		String appSecret = "9b350280c9a7126bfaf4e20a371e6951";
		//--------------------------------------------------------------------------------------------------------
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
		wxHandler.addToSocialSDK();
		//--------------------------------------------------------------------------------------------------------
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		//--------------------------------------------------------------------------------------------------------
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(content);
		// 设置分享title
		qqShareContent.setTitle(title);
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(context, "http://www.chinajuchang.com/home/images/index_logo.jpg"));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		//--------------------------------------------------------------------------------------------------------
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		//--------------------------------------------------------------------------------------------------------
		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}

	public SharePopupwindow(Context context, View parentView,
			final Handler myHandler, AttributeSet attrs,String title,String content,String url) {
		super(context, attrs);
		this.context = (Activity) context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.parentView = parentView;
		this.title = title;
		this.content = content;
		this.url  = url;
		setTouchable(true);
		setOutsideTouchable(true);
		root = LayoutInflater.from(context).inflate(R.layout.popupwindow_share,
				null);
		tv_sina = (TextView) root.findViewById(R.id.tv_sina);
		tv_qq = (TextView) root.findViewById(R.id.tv_qq);
		tv_wechat = (TextView) root.findViewById(R.id.tv_wechat);
		tv_wxcircle = (TextView) root.findViewById(R.id.tv_wxcircle);
		tv_sms = (TextView) root.findViewById(R.id.tv_sms);
		tv_email = (TextView) root.findViewById(R.id.tv_email);
		tv_sina.setOnClickListener(this);
		tv_qq.setOnClickListener(this);
		tv_wechat.setOnClickListener(this);
		tv_wxcircle.setOnClickListener(this);
		tv_sms.setOnClickListener(this);
		tv_email.setOnClickListener(this);
		initShare();
		setContentView(root);
		initLayout();
		ColorDrawable dw = new ColorDrawable(0000000);
		setBackgroundDrawable(dw);
	}

	// 弹出动画、高度等设置
	private void initLayout() {
		// setAnimationStyle(R.style.anim_issue_popwindow);
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.MATCH_PARENT);
	}

	public void show() {
		showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
				0, 0);
	}

	private void doSinaShare() {
		// TODO Auto-generated method stub
		// 设置分享内容
		mController
				.setShareContent(content);
		// 设置分享图片
		mController.setShareMedia(new UMImage(context,"http://www.umeng.com/images/pic/banner_module_social.png"));
		// 直接分享
		mController.directShare(context, SHARE_MEDIA.SINA,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(context, "分享开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == StatusCode.ST_CODE_SUCCESSED) {
							Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

	private void doWechatShare() {
		// TODO Auto-generated method stub
		mController.postShare(context, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == 200) {
							Toast.makeText(context, "分享成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							String eMsg = "";
							if (eCode == -101) {
								eMsg = "没有授权";
							}
							Toast.makeText(context,
									"分享失败[" + eCode + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void doWxcircleShare() {
		// TODO Auto-generated method stub
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == 200) {
							Toast.makeText(context, "分享成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							String eMsg = "";
							if (eCode == -101) {
								eMsg = "没有授权";
							}
							Toast.makeText(context,
									"分享失败[" + eCode + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void doQQShare() {
		mController.postShare(context, SHARE_MEDIA.QQ, new SnsPostListener() {
			@Override
			public void onStart() {
				Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				if (eCode == 200) {
					Toast.makeText(context, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(context, "分享失败[" + eCode + "] " + eMsg,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void doSmsShare() {
		mController.postShare(context, SHARE_MEDIA.SMS, new SnsPostListener() {
			
			@Override
			public void onStart() {
				Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
				if (eCode == 200) {
					Toast.makeText(context, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(context, "分享失败[" + eCode + "] " + eMsg,
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
//		/**
//		 * 
//		 * 发送短信
//		 * 
//		 * @param smsBody
//		 */
//
//		Uri smsToUri = Uri.parse("smsto:");
//
//		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
//
//		intent.putExtra("sms_body", smsBody);
//
//		context.startActivity(intent);

	}
	
	private void doEmailShare(String info) {
		Intent data=new Intent(Intent.ACTION_SENDTO); 
		data.setData(Uri.parse("mailto:way.ping.li@gmail.com")); 
		data.putExtra(Intent.EXTRA_SUBJECT, "誉畅物流"); 
		data.putExtra(Intent.EXTRA_TEXT, info); 
		context.startActivity(data); 

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sina:
			if (!OauthHelper.isAuthenticated(context, SHARE_MEDIA.SINA)) {
				mController.doOauthVerify(context, SHARE_MEDIA.SINA,
						new UMAuthListener() {
							@Override
							public void onStart(SHARE_MEDIA platform) {
								
							}

							@Override
							public void onError(SocializeException e,
									SHARE_MEDIA platform) {
							}

							@Override
							public void onComplete(Bundle value,
									SHARE_MEDIA platform) {
								doSinaShare();
							}

							@Override
							public void onCancel(SHARE_MEDIA platform) {
							}
						});
			} else {
				doSinaShare();
			}
			break;
		case R.id.tv_qq:
			QQShareContent qqShareContent = new QQShareContent();
			// 设置分享文字
			qqShareContent
					.setShareContent(content);
			// 设置分享title
			qqShareContent.setTitle(title);
			// 设置分享图片
//			qqShareContent.setShareImage(new UMImage(context,
//					R.drawable.ic_launcher));
			// 设置点击分享内容的跳转链接
			qqShareContent.setTargetUrl(url);
			mController.setShareMedia(qqShareContent);
			doQQShare();
			break;
		case R.id.tv_wechat:
			// 设置微信好友分享内容
			WeiXinShareContent weixinContent = new WeiXinShareContent();
			// 设置分享文字
			weixinContent.setShareContent(content);
			// 设置title
			weixinContent.setTitle(title);
			// 设置分享内容跳转URL
			weixinContent.setTargetUrl(url);
			// 设置分享图片
//			weixinContent.setShareImage(new UMImage(context,
//					R.drawable.ic_launcher));
			mController.setShareMedia(weixinContent);
			doWechatShare();
			break;
		case R.id.tv_wxcircle:
			// 设置微信朋友圈分享内容
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent(content);
			// 设置朋友圈title
			circleMedia.setTitle(title);
//			circleMedia.setShareImage(new UMImage(context,
//					R.drawable.ic_launcher));
			circleMedia.setTargetUrl(url);
			mController.setShareMedia(circleMedia);
			doWxcircleShare();
			break;
		case R.id.tv_sms:
			SmsShareContent smsContent = new SmsShareContent();
			smsContent.setShareContent(content);
			mController.setShareMedia(smsContent);
			doSmsShare();
			break;
		case R.id.tv_email:
			doEmailShare(content);
			break;

		default:
			break;
		}

	}
}
