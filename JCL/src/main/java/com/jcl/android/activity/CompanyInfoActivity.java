package com.jcl.android.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.LoginBean;
import com.jcl.android.bean.PersonalInfoBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


/**
 * 个人资料
 * 
 * @author pb
 *
 */
public class CompanyInfoActivity extends BaseActivity implements
		OnClickListener {

	private MyUINavigationView uINavigationView;
	private CityPickerPopupwindow cityPickerPopupwindow;
	

	private ImageView img_logo;//头像
	private TextView register_num,edt_type;//注册码
	private TextView register_date;//注册日期
	private EditText edt_zhname;//公司中文名称
	private EditText edt_address_c;//公司中文地址
	private EditText edt_link_c;//公司联系人
	private EditText edt_enname;//公司英文名称
	private EditText edt_address_e;//公司英文地址
	private EditText edt_email;//公司邮箱
	private EditText edt_areanum;//区号
	private EditText edt_companynum;//公司电话
	private EditText edt_newaddress;//公司网址
	private EditText edt_advantage;//优势自荐
	private EditText edt_content;//公司简介
	private EditText edt_invoice_title;//发票抬头
	private TextView tv_company_authentication;//企业资质认证
	private TextView tv_invitation_code;//公司邀请码
	private View ll_showmap;
	private View ll_address;
	private TextView tv_showmap;
	private TextView tv_address;
	
	String authstatus = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		initNavigation();
		initView();
		getCompanyInfo();
	}

	private void initView()
	{
		cityPickerPopupwindow = new CityPickerPopupwindow(this,findViewById(R.id.ll_parent), mHandler, null);
		tv_invitation_code=(TextView) findViewById(R.id.tv_invitation_code);
		img_logo=(ImageView)findViewById(R.id.img_logo);
		register_num=(TextView)findViewById(R.id.register_num);
		register_date=(TextView)findViewById(R.id.register_date);
		edt_zhname=(EditText)findViewById(R.id.edt_zhname);
		edt_address_c=(EditText)findViewById(R.id.edt_address_c);
		edt_link_c=(EditText)findViewById(R.id.edt_link_c);//公司联系人
		edt_enname=(EditText)findViewById(R.id.edt_enname);
		edt_address_e=(EditText)findViewById(R.id.edt_address_e);
		edt_email=(EditText)findViewById(R.id.edt_email);
		edt_areanum=(EditText)findViewById(R.id.edt_areanum);
		edt_companynum=(EditText)findViewById(R.id.edt_companynum);
		edt_newaddress=(EditText)findViewById(R.id.edt_newaddress);
		edt_advantage=(EditText)findViewById(R.id.edt_advantage);
		edt_content=(EditText)findViewById(R.id.edt_content);
		edt_invoice_title=(EditText)findViewById(R.id.edt_invoice_title);
		tv_company_authentication=(TextView)findViewById(R.id.tv_company_authentication);
		tv_showmap=(TextView)findViewById(R.id.tv_showmap);
		tv_address=(TextView)findViewById(R.id.tv_address);
		
		ll_showmap=findViewById(R.id.ll_showmap);
		ll_showmap.setOnClickListener(this);
		
		ll_address=findViewById(R.id.ll_address);
		ll_address.setOnClickListener(this);
		
		img_logo.setOnClickListener(this);
		tv_company_authentication.setOnClickListener(this);
		
		String userinfoJson = SharePerfUtil.getLoginUserInfo();
		LoginBean loginBean = new Gson()
				.fromJson(userinfoJson, LoginBean.class);
		String type = loginBean.getData().getType();
		if("0".equals(type)){
			type = "国内物流企业/车主";
		}else if("1".equals(type)){
			type = "发货企业/货主";
		}else if("2".equals(type)){
			type = "配货站";
		}else if("3".equals(type)){
			type = "国际物流企业/货代";
		}else if("4".equals(type)){
			type = "货车生产商";
		}else{
			type = "仓储";
		}
		edt_type=(TextView)findViewById(R.id.edt_type);
		edt_type.setText(type);
	}
	
	String cityCode;
	String lnt,lat;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = null;
			String info = "";
			if (msg.obj instanceof Bundle) {
				bundle = (Bundle) msg.obj;
				if (bundle != null)
					info = bundle.getString("cityName");
			} else {
				info = (String) msg.obj;
			}
			switch (msg.what) {
			case 1:
				cityCode = bundle.getString("cityCode");
				LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + cityCode);
				lnt = bundle.getString("DistrictLnt");
				lat = bundle.getString("DistrictLat");
				LogUtil.logWrite("MSZ_TAG", "lnt==>" + lnt);
				LogUtil.logWrite("MSZ_TAG", "lat==>" + lat);
				tv_address.setText(info);
				break;

			default:
				break;
			}
		}

	};

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setCompanyInfo();
			}
		});
	}

	class UserinfoRequest {
		private String filters;
		private String type;
		private String sorts;

		public UserinfoRequest(String filters, String sorts) {
			this.filters = filters;
			this.type = "000301";
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

	public void getCompanyInfo() {
		filters = new Gson().toJson(new UserinfoFilters(SharePerfUtil
				.getUserId()));
		sorts = new Gson().toJson(new UserinfoSorts());
		jsonRequest = new Gson().toJson(new UserinfoRequest(filters, sorts));

		executeRequest(new GsonRequest<PersonalInfoBean>(Request.Method.GET,
				UrlCat.getInfoUrl(jsonRequest), PersonalInfoBean.class,
				null, null, new Listener<PersonalInfoBean>() {
					@Override
					public void onResponse(PersonalInfoBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(CompanyInfoActivity.this, arg0.getMsg(),
										1000).show();
								authstatus = arg0.getData().getIsauth()+"";
								String statusstr = "";
								if("0".equals(authstatus)){
									statusstr = "未认证";
								}else if("1".equals(authstatus)){
									statusstr = "认证中";
								}else if("2".equals(authstatus)){
									statusstr = "认证成功";
								}else if("3".equals(authstatus)){
									statusstr = "未通过认证";
								}
								tv_company_authentication.setText(statusstr);
								ImageLoaderUtil.getInstance(CompanyInfoActivity.this)
									.loadImage(C.BASE_URL+"/"+arg0.getData().getHead(), img_logo);
								register_num.setText(arg0.getData().get_id());
								register_date.setText(arg0.getData().getCreatetime());
								edt_zhname.setText(arg0.getData().getZhname());
								edt_address_c.setText(arg0.getData().getAddress());
								edt_link_c.setText(arg0.getData().getLinkman());
								edt_enname.setText(arg0.getData().getEnname());
								edt_address_e.setText(arg0.getData().getAddress());
								edt_email.setText(arg0.getData().getEmail());
								edt_areanum.setText(arg0.getData().getAreanum());
								edt_companynum.setText(arg0.getData().getCompanynum());
								edt_newaddress.setText(arg0.getData().getNetaddress());
								edt_advantage.setText(arg0.getData().getAdvantage());
								edt_content.setText(arg0.getData().getContent());
								edt_invoice_title.setText(arg0.getData().getInvoicetitle());
								tv_invitation_code.setText(SharePerfUtil.getInvitecode());
								if("2".equals(SharePerfUtil.getIsauth())){
									tv_invitation_code.setVisibility(View.VISIBLE);
								}else{
									tv_invitation_code.setVisibility(View.INVISIBLE);
								}
								JCLApplication.getInstance().setInviteCode(arg0.getData().getInvitecode());
								
								cityCode= arg0.getData().getAddresscode();
								lnt = arg0.getData().getLongitude();
								lat = arg0.getData().getLatitude();
								tv_address.setText(arg0.getData().getCity());
								String lat = arg0.getData().getLatitude();
								String lon = arg0.getData().getLongitude();
								if(!StringUtils.isEmpty(lat) && !StringUtils.isEmpty(lon)){
									tv_showmap.setText("纬度:"+lat.substring(0, lat.indexOf(".")+3)+",经度"+lon.substring(0, lat.indexOf(".")+3));
								}
								
							}else{
								Toast.makeText(CompanyInfoActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(CompanyInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(CompanyInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	class SetCompanyinfoRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public SetCompanyinfoRequest(String data) {
			this.data = data;
			this.type = "0005";
			this.operate = "M";
			this.key = "";
		}
	}

	class CompanyinfoData {
		private String userid;
		private String head;
		private String zhname;//中文名
		private String enname;//英文名
		private String address;//中文地址
		private String addresse;//英文地址
		private String linkman;//联系人
		private String email;
		private String areanum;//电话区号
		private String companynum;//公司座机号
		private String netaddress;//公司网址
		private String content;
		private String invoicetitle;//发票抬头
		private String advantage;//优势自荐
		
		private String longitude;//经度
		private String latitude;//纬度
		private String city;//城市
		private String citycode;//城市编码

		public CompanyinfoData(String userid,String head,String zhname,String enname,String address,
				String email,String areanum,String linkman,String companynum,String netaddress,String content,String invoiceTitle,
				String advantage,String addressE,String longitude,String latitude,String city,String citycode) {
			this.userid = userid;
			this.head = head;
			this.zhname = zhname;
			this.enname = enname;
			this.address = address;
			this.email = email;
			this.areanum = areanum;
			this.companynum = companynum;
			this.netaddress = netaddress;
			this.content = content;
			this.invoicetitle=invoiceTitle;
			this.advantage=advantage;
			this.addresse=addressE;
			this.linkman = linkman;
			this.longitude = longitude;
			this.latitude = latitude;
			this.city = city;
			this.citycode = citycode;
		}

	}
	String data;
	String headerPath="";
	public void setCompanyInfo()
	{
		data=new Gson().toJson(new CompanyinfoData(SharePerfUtil.getUserId(),headerPath,edt_zhname.getText().toString(),
				edt_enname.getText().toString(),edt_address_c.getText().toString(),edt_email.getText().toString(),
				edt_areanum.getText().toString(),edt_link_c.getText().toString(),edt_companynum.getText().toString(),edt_newaddress.getText().toString(),
				edt_content.getText().toString(),edt_invoice_title.getText().toString(),edt_advantage.getText().toString()
				,edt_address_e.getText().toString(),lnt,lat,tv_address.getText().toString(),cityCode));
		jsonRequest=new Gson().toJson(new SetCompanyinfoRequest(data));
		
		executeRequest(new GsonRequest<PersonalInfoBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, PersonalInfoBean.class, 
				null, new ParamsBuilder().submitParams(jsonRequest), new Listener<PersonalInfoBean>() {
					@Override
					public void onResponse(PersonalInfoBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(CompanyInfoActivity.this, arg0.getMsg(),
										1000).show();
								SharePerfUtil.saveLinkMan(edt_link_c.getText().toString());
								finish();
							}else{
								Toast.makeText(CompanyInfoActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(CompanyInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(CompanyInfoActivity.this, "网络连接异常！",
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
		case R.id.img_logo:
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
			startActivity(new Intent(CompanyInfoActivity.this,
					AuthenticationCompanyActivity.class));
			break;
		case R.id.tv_real_name:
			startActivity(new Intent(CompanyInfoActivity.this,
					AuthenticationPersonalActivity.class));
			break;
			
		case R.id.tv_company_authentication:
			if(authstatus.equals("0") || authstatus.equals("3")){
				startActivity(new Intent(this,AuthenticationCompanyActivity.class));
			}
			break;
		case R.id.ll_showmap:
			if(TextUtils.isEmpty(tv_address.getText()))
			{
				Toast.makeText(CompanyInfoActivity.this, "请先选择城市区域", 1000).show();;
				return;
			}
			startActivityForResult(new Intent(CompanyInfoActivity.this,GetLatLotActivity.class)
					.putExtra("lat", lat)
					.putExtra("lnt", lnt),C.GET_LATLOT);
			break;
			
		case R.id.ll_address:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
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
                String path = FileUtils.getAbsoluteImagePath(uri,
                        this);
                File file = new File(path);
                if (FileUtils.getFileSizes(file) > 1048576) {
                    MyToast.showToast(this,"尺寸过大");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                avatar = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), uri);
                headerPath=FileUtils.Bitmap2StrByBase64(avatar);
                img_logo.setImageBitmap(avatar);
//                uploadAvatarToService(avatar);
            } catch (Exception e) {
                e.printStackTrace();
                MyToast.showToast(this,"处理失败");
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
                headerPath=FileUtils.Bitmap2StrByBase64(avatar);
                img_logo.setImageBitmap(avatar);
//                uploadAvatarToService(avatar);
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
			case C.GET_LATLOT://获取经纬度
				Double lat=data.getDoubleExtra("Lat", 0d);
				Double lng=data.getDoubleExtra("Lng", 0d);
				tv_showmap.setText("纬度:"+lat+",经度"+lng);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

}
