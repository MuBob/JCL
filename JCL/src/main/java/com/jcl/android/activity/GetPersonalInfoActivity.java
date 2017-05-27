package com.jcl.android.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.PersonalInfoBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;

/**
 * 个人资料
 * 
 * @author pb
 *
 */
public class GetPersonalInfoActivity extends BaseActivity implements
		OnClickListener {

	private MyUINavigationView uINavigationView;

	private ImageView img_header;// 头像
	private EditText edt_name;// 昵称
	private EditText edt_tel;// 手机
	private EditText edt_signaname;// 我的签名
	private TextView tv_LV;// 我的等级
	private TextView tv_industry;// 行业
	private EditText edt_wechat;// 微信
	private TextView tv_job;// 公司职务
	private TextView tv_real_name;// 实名认证
	private EditText edt_qq;// 我的qq
	private View view_industry;
	
	String authstatus = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initNavigation();
		initView();
		getPersonalInfo();
	}

	private void initView() {
		view_industry = findViewById(R.id.view_industry);
		img_header = (ImageView) findViewById(R.id.img_header);
		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_tel = (EditText) findViewById(R.id.edt_tel);
		edt_signaname = (EditText) findViewById(R.id.edt_signature);
		tv_LV = (TextView) findViewById(R.id.tv_LV);
		tv_industry = (TextView) findViewById(R.id.tv_industry);
		edt_wechat = (EditText) findViewById(R.id.edt_wechat);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_real_name = (TextView) findViewById(R.id.tv_real_name);
		edt_qq = (EditText) findViewById(R.id.edt_qq);

		view_industry.setOnClickListener(this);
		img_header.setOnClickListener(this);
		tv_LV.setOnClickListener(this);
		tv_industry.setOnClickListener(this);
		tv_real_name.setOnClickListener(this);

	}

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnRightText.setVisibility(View.GONE);
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		btnRightText.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				setPersonalInfo();
//			}
//		});
	}

	private String[] industry_string = new String[] { "交通运输、仓储和邮政业", "制造业",
			"批发和零售业", "农、林、牧、渔业", "采矿业", "房地产业", "电力、热力、燃气及水生产和供应业", "建筑业",
			"住宿和餐饮业", "信息传输、软件和信息技术服务业", "金融业", "租赁和商务服务业", "科学研究和技术服务业",
			"水利、环境和公共设施管理业", "居民服务、修理和其他服务业", "教育", "卫生和社会工作", "文化、体育和娱乐业",
			"公共管理、社会保障和社会组织", "国际组织" };
	private Builder builder = null;
	private int checkItem = 0;// 默认选择项

	private void showView() {

		if (builder == null) {
			builder = new AlertDialog.Builder(GetPersonalInfoActivity.this);
		}
		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle("请选择")
				.setSingleChoiceItems(industry_string, checkItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								checkItem = item;
								tv_industry.setText(industry_string[item]);
								dialog.dismiss();
							}
						}).show();
	}

	class UserinfoRequest {
		private String filters;
		private String type;
		private String sorts;

		public UserinfoRequest(String filters, String sorts) {
			this.filters = filters;
			this.type = "0003";
			this.sorts = sorts;
		}
	}

	class UserinfoFilters {
		private String EQ__id;

		public UserinfoFilters(String EQ__id) {
			this.EQ__id = EQ__id;
		}

	}

	class UserinfoSorts {
		private String _id;

		public UserinfoSorts() {
			this._id = "asc";
		}

	}

	private UserinfoRequest userinfoRequest;
	String filters;
	String sorts;
	String jsonRequest;

	public void getPersonalInfo() {
		filters = new Gson().toJson(new UserinfoFilters(SharePerfUtil
				.getUserId()));
		sorts = new Gson().toJson(new UserinfoSorts());
		jsonRequest = new Gson().toJson(new UserinfoRequest(filters, sorts));

		executeRequest(new GsonRequest<PersonalInfoBean>(Request.Method.GET,
				UrlCat.getInfoUrl(jsonRequest), PersonalInfoBean.class,
				null, null, new Listener<PersonalInfoBean>() {
					@Override
					public void onResponse(PersonalInfoBean arg0) {
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(GetPersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
								ImageLoaderUtil.getInstance(
										GetPersonalInfoActivity.this).loadImage(
										C.BASE_URL+"/"+arg0.getData().getHead(), img_header);
								edt_name.setText(arg0.getData().getNickname());
								edt_tel.setText(arg0.getData().getMobile() + "");
								edt_signaname.setText(arg0.getData()
										.getSignname());
								tv_LV.setText(arg0.getData().getLevel());
								tv_industry.setText(arg0.getData().getTrade());
								edt_wechat.setText(arg0.getData().getWechat());
								tv_job.setText(arg0.getData().getPost());
								authstatus = arg0.getData().getIsauth()+"";
								String authstr = "";
								if("0".equals(authstatus)){
									authstr = "未认证";
								}else if("1".equals(authstatus)){
									authstr = "认证中";
								}else if("2".equals(authstatus)){
									authstr = "认证成功";
								}else if("3".equals(authstatus)){
									authstr = "未通过认证";
								}
								tv_real_name.setText(authstr);
//								tv_real_name.setText(TextUtils.equals(arg0
//										.getData().getIsauth(), "0") ? ""
//										: arg0.getData().getName());
								edt_qq.setText(arg0.getData().getQq());
							} else {
								Toast.makeText(GetPersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(GetPersonalInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(GetPersonalInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	class SetUserinfoRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public SetUserinfoRequest(String data) {
			this.data = data;
			this.type = "0004";
			this.operate = "M";
			this.key = "";
		}
	}

	class UserinfoData {
		private String userid;
		private String head;
		private String nickname;
		private String wechat;
		private String post;
		private String signname;
		private String level;
		private String trade;
		private String qq;
		private String name;

		public UserinfoData(String userid, String head, String nickname,
				String wechat, String post, String signname, String level,
				String trade, String qq, String name) {
			this.userid = userid;
			this.head = head;
			this.nickname = nickname;
			this.wechat = wechat;
			this.post = post;
			this.signname = signname;
			this.level = level;
			this.trade = trade;
			this.qq = qq;
			this.name = name;
		}

	}

	String data;
	String headerPath = "";

	public void setPersonalInfo() {
		data = new Gson().toJson(new UserinfoData(SharePerfUtil.getUserId(),
				headerPath, edt_name.getText().toString(), edt_wechat.getText()
						.toString(), tv_job.getText().toString(), edt_signaname
						.getText().toString(), tv_LV.getText().toString()
						.trim(), tv_industry.getText().toString(), edt_qq
						.getText().toString(), edt_name.getText()
						.toString()));
		jsonRequest = new Gson().toJson(new SetUserinfoRequest(data));

		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest),
				new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								
								SharePerfUtil.saveLinkMan(edt_name.getText().toString());
//								SharePerfUtil.savePersonHead(url)
								finish();
								Toast.makeText(GetPersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();

							} else {
								Toast.makeText(GetPersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(GetPersonalInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(GetPersonalInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	// 修改头像
	public static final String IMAGE_UNSPECIFIED = "image/*";

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_header:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("请选择");
			// 指定下拉列表的显示数据
			final String[] infos = { "拍照", "从相册获取" };
			// 设置一个下拉的列表选择项
			builder.setItems(infos, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						if (FileUtils.existSDcard()) {
							try {
								Intent jumpCamera = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								jumpCamera.putExtra(MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(FileUtils
												.getJCLCachePath(), "temp"
												+ ".jpg")));
								startActivityForResult(jumpCamera,
										C.CAMERA_REQUEST_PHOTOHRAPH);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else if (which == 1) {
						Intent jumpPhoto = new Intent(Intent.ACTION_PICK, null);
						jumpPhoto.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								IMAGE_UNSPECIFIED);
						startActivityForResult(jumpPhoto,
								C.CAMERA_REQUEST_PHOTOZOOM);
					}
				}
			});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder.show();
			break;
		case R.id.tv_company:
			startActivity(new Intent(GetPersonalInfoActivity.this,
					AuthenticationCompanyActivity.class));
			break;
		case R.id.tv_real_name:
//			if(authstatus.equals("0") || authstatus.equals("3")){
//				startActivity(new Intent(GetPersonalInfoActivity.this,
//						AuthenticationPersonalActivity.class));
//			}
			break;
		case R.id.tv_industry:
		case R.id.view_industry:
			showView();
			break;

		default:
			break;
		}

	}

	private Bitmap avatar;

	/**
	 * 修改从相册中返回的头像
	 */
	private void workHeadViewFromPhone(Intent data) {
		if (Build.VERSION.SDK_INT >= 19) {
			Uri uri = data.getData();
			try {
				String path = FileUtils.getAbsoluteImagePath(uri, this);
				File file = new File(path);
				if (FileUtils.getFileSizes(file) > 1048576) {
					MyToast.showToast(this, "尺寸过大");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				avatar = MediaStore.Images.Media.getBitmap(
						getContentResolver(), uri);
				headerPath = FileUtils.Bitmap2StrByBase64(avatar);
				img_header.setImageBitmap(avatar);
				// uploadAvatarToService(avatar);
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.showToast(this, "处理失败");
			}
		} else {
			startPhotoZoom(data.getData()); // 处理结果
		}
	}

	// 返回相册进行裁剪
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");

		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 240);
		intent.putExtra("outputY", 240);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, C.CAMERA_REQUEST_PHOTORESOULT);
	}

	/**
	 * 修复剪切之后的头像
	 */
	private void workHeadViewFromPhoneWithCut(Intent data) {
		try {
			Bundle extras = data.getExtras();
			if (extras != null) {
				avatar = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				avatar.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				headerPath = FileUtils.Bitmap2StrByBase64(avatar);
				img_header.setImageBitmap(avatar);
				// uploadAvatarToService(avatar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理拍照返回的数据
	 *
	 * @param data
	 */
	private void workHeadViewFromCamera(Intent data) {
		File picture = new File(FileUtils.getJCLCachePath() + "temp" + ".jpg");
		startPhotoZoom(Uri.fromFile(picture));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case RESULT_OK: // 登录成功
			switch (requestCode) {
			case C.CAMERA_REQUEST_PHOTOZOOM:// 更改头像返回
				workHeadViewFromPhone(data);
				break;
			case C.CAMERA_REQUEST_PHOTORESOULT:// 处理剪切结果
				workHeadViewFromPhoneWithCut(data);
				break;
			case C.CAMERA_REQUEST_PHOTOHRAPH:// 拍照返回
				workHeadViewFromCamera(data);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

}
