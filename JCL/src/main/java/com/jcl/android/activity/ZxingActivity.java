package com.jcl.android.activity;

import com.jcl.android.R;
import com.jcl.android.alipay.Base64;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.CreateQRImage;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;
import com.umeng.socialize.bean.SHARE_MEDIA;
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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的二维码页面
 * 
 * syl
 * 
 * */

public class ZxingActivity extends BaseActivity implements OnClickListener {
	
	private ImageView iv_zxing;
	private Button btn_invate;
	private SharePopupwindow sharePopupwindow;
	private TextView tv_invited_code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_zxing);
		initNavigation();
		sharePopupwindow = new SharePopupwindow(ZxingActivity.this, findViewById(R.id.ll_parent), null, null,title,content,url);
		initView();
		
	}
	private MyUINavigationView uINavigationView;

	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnRightText.setVisibility(View.GONE);
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	
	
	private void initView()
	{
		iv_zxing = (ImageView) findViewById(R.id.iv_zxing);
		btn_invate = (Button) findViewById(R.id.btn_invate);
		tv_invited_code = (TextView) findViewById(R.id.tv_invited_code);
		btn_invate.setOnClickListener(this);
		btn_invate.setVisibility(View.GONE);
		
		CreateQRImage cqr = new CreateQRImage();
		cqr.createQRImage("http://www.chinajuchang.com/hsdata/d?key="
		+Base64.encode(JCLApplication.getInstance().getUserId().getBytes()), iv_zxing);
		
		
		//分享
		tv_sina = (TextView) findViewById(R.id.tv_sina);
		tv_qq = (TextView) findViewById(R.id.tv_qq);
		tv_wechat = (TextView) findViewById(R.id.tv_wechat);
		tv_wxcircle = (TextView) findViewById(R.id.tv_wxcircle);
		tv_sms = (TextView) findViewById(R.id.tv_sms);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_sina.setOnClickListener(this);
		tv_qq.setOnClickListener(this);
		tv_wechat.setOnClickListener(this);
		tv_wxcircle.setOnClickListener(this);
		tv_sms.setOnClickListener(this);
		tv_email.setOnClickListener(this);
		initShare();
		tv_invited_code.setText("您的邀请码是：" + SharePerfUtil.getInvitecode());
		
	}
	
	
	
	//-------------------分享相关--------------------------------
	private Handler myHandler;
	private TextView tv_sina, tv_wechat, tv_wxcircle, tv_sms, tv_email, tv_qq;
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	String title= "誉畅手机配货下载包";
	String content = "我找到了一个靠谱的配货软件：誉畅配货APP；精准匹配，加急货源车源专人处理！" +
			"推荐好友有好礼！手机电脑双平台，手机端免费使用，点击下载地址http://www.chinajuchang.com/d，" +
			"电脑端现免费开通会员试用一个月，无邀请码来问我昂！";
	String url = "http://www.chinajuchang.com/d";
	
private void initShare() {
		// 设置分享内容
		mController
				.setShareContent(content);
		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(this,
//				"http://www.baidu.com/img/bdlogo.png"));

		String appID = "wx9d3bff9b821f5917";
		String appSecret = "9b350280c9a7126bfaf4e20a371e6951";
		//--------------------------------------------------------------------------------------------------------
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
		wxHandler.addToSocialSDK();
		//--------------------------------------------------------------------------------------------------------
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		//--------------------------------------------------------------------------------------------------------
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(content);
		// 设置分享title
		qqShareContent.setTitle(title);
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(this, "http://www.chinajuchang.com/home/images/index_logo.jpg"));
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


	private void doSinaShare() {
		// TODO Auto-generated method stub
		// 设置分享内容
		mController
				.setShareContent(content);
		// 设置分享图片
//		mController.setShareMedia(new UMImage(this,"http://www.umeng.com/images/pic/banner_module_social.png"));
		// 直接分享
		mController.directShare(this, SHARE_MEDIA.SINA,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(ZxingActivity.this, "分享开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == StatusCode.ST_CODE_SUCCESSED) {
							Toast.makeText(ZxingActivity.this, "分享成功", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(ZxingActivity.this, "分享失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

	private void doWechatShare() {
		// TODO Auto-generated method stub
		mController.postShare(this, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(ZxingActivity.this, "开始分享.", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == 200) {
							Toast.makeText(ZxingActivity.this, "分享成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							String eMsg = "";
							if (eCode == -101) {
								eMsg = "没有授权";
							}
							Toast.makeText(ZxingActivity.this,
									"分享失败[" + eCode + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void doWxcircleShare() {
		// TODO Auto-generated method stub
		mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {
					@Override
					public void onStart() {
						Toast.makeText(ZxingActivity.this, "开始分享.", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == 200) {
							Toast.makeText(ZxingActivity.this, "分享成功.", Toast.LENGTH_SHORT)
									.show(); 
						} else {
							String eMsg = "";
							if (eCode == -101) {
								eMsg = "没有授权";
							}
							Toast.makeText(ZxingActivity.this,
									"分享失败[" + eCode + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void doQQShare() {
		mController.postShare(this, SHARE_MEDIA.QQ, new SnsPostListener() {
			@Override
			public void onStart() {
				Toast.makeText(ZxingActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				if (eCode == 200) {
					Toast.makeText(ZxingActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(ZxingActivity.this, "分享失败[" + eCode + "] " + eMsg,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void doSmsShare() {
		mController.postShare(this, SHARE_MEDIA.SMS, new SnsPostListener() {
			
			@Override
			public void onStart() {
				Toast.makeText(ZxingActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
				if (eCode == 200) {
					Toast.makeText(ZxingActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(ZxingActivity.this, "分享失败[" + eCode + "] " + eMsg,
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
//		this.startActivity(intent);

	}
	
	private void doEmailShare(String info) {
		Intent data=new Intent(Intent.ACTION_SENDTO); 
		data.setData(Uri.parse("mailto:way.ping.li@gmail.com")); 
		data.putExtra(Intent.EXTRA_SUBJECT, "誉畅物流"); 
		data.putExtra(Intent.EXTRA_TEXT, info); 
		this.startActivity(data); 

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sina:
			if (!OauthHelper.isAuthenticated(this, SHARE_MEDIA.SINA)) {
				mController.doOauthVerify(this, SHARE_MEDIA.SINA,
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
//			qqShareContent.setShareImage(new UMImage(this,
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
//			weixinContent.setShareImage(new UMImage(this,
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
//			circleMedia.setShareImage(new UMImage(this,
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mController.getConfig().cleanListeners();
	}
	
}
