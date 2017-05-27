package com.jcl.android.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.AddCarActivity.AddCarData;
import com.jcl.android.activity.AddCarActivity.AddCarRequest;
import com.jcl.android.adapter.MyBaseAdpter;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.CarInfo;
import com.jcl.android.bean.CarListBean;
import com.jcl.android.interfaces.OnItemBtnClickListener;
import com.jcl.android.interfaces.OnItemOnCheckedChangeListener;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.CheckSwitchButton;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 登录页
 * 
 * @author pb
 */
public class CarManageActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycar);
		afterCreate();
		initView();
		initNavigation();
		getInfo() ;
		setListener();
		
	}
	boolean isForCode =false;
	boolean showdongtai=false;
	private String load="2";
	public void afterCreate()
	{
		if(getIntent().hasExtra("key"))
		{
			if(C.FOR_CAR_CODE==getIntent().getIntExtra("key", 0))
			{
				isForCode=true;
			}
		}
		if(getIntent().hasExtra("fromhome")){
			if("fromhome".endsWith(getIntent().getStringExtra("fromhome"))){
				showdongtai=true;
			}
		}
	}

	private MyUINavigationView uINavigationView;
	ListView lv_car_manage;//车辆列表
	private View no_list;
	private Button add_car;
	


	private void initView() {
		lv_car_manage=(ListView)findViewById(R.id.lv_car_manage);
		no_list=findViewById(R.id.no_list);
		add_car=(Button) findViewById(R.id.add_car);
		add_car.setOnClickListener(this);
		
		
	}

	private void setListener()
	{
		lv_car_manage.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(isForCode)
				{
					Intent data=new Intent();
					data.putExtra("carinfo", (CarInfo)carLisrAdapter.getItem(arg2));
					setResult(RESULT_OK, data);
					finish();
				}
				
			}
		});
		
	}
	
	class AddCarRequest {
		private String type;
		private String data;
		private String key;
		private String operate;
		public AddCarRequest(String data) {
			this.type = "1004";
			this.data = data;
		}

		public AddCarRequest(String data,String key,String operate) {
			this.type = "1004";
			this.data = data;
			this.key=key;
			this.operate=operate;
		}
	}

	class AddCarData {
//		private String userid;
//		private String companyname;
//		private String platenum;
//		private String cartype;
//		private String wx;
//		private String approveweight;
//		private String carsize;
//		private String carlength;
//		private String officeplace;
//		
//		private String xslicence;
//		private String yylicence;
//		private String bd;
//		private String carimage1;//车头
//		private String carimage2;//45°
//		private String carimage3;//车尾
//
//		private String status;
//		private String createtime;
//		private String effectivetime;
//		private String effectiveflag;
		
		


		private String userid;
		private String platenum;
		private String cartype;
		private String cartypecode;
		private String wx;
		private String wxcode;
		private String approveweight;
		private String carsize;
		private String carlength;
		private String officeplace;
		private String officeplacecode;
		private String xslicence;
		private String yylicence;
		private String bd;
		private String carimage1;//车头
		private String carimage2;//45°
		private String carimage3;//车尾
		private String status;
		private String createtime;
		private String effectivetime;
		private String effectiveflag;
		private String distance;
		private String distancecode;
		private String longitude;
		private String latitude;
		private String link;
		private String phone;
		private String isshow;//是否显示备注信息
		private String position;//当前位置
		private String load;//装载状态    开始装载为1
		
		private String linecode1;//长跑线编码 (370200,370100)
		private String linearea1;//长跑线(山东青岛,山东济南)
		
		private String linecode2;//长跑线编码 (370200,370100)
		private String linearea2;//长跑线(山东青岛,山东济南)
		
		private String linecode3;//长跑线编码 (370200,370100)
		private String linearea3;//长跑线(山东青岛,山东济南)
		
		private String linecode4;//长跑线编码 (370200,370100)
		private String linearea4;//长跑线(山东青岛,山东济南)
		private String ischeck;//审核
		
		
		
		public AddCarData(String userid,  String platenum,
				String cartype,String cartypecode,  String wx,String wxcode, 
				String approveweight, String carlength,
				String carsize,String officeplace,String officeplacecode,
				String carimage1, String carimage2,String carimage3,
				String xslicence, String yylicence, String bd,String distance,String distancecode,
				String longitude,String latitude,String link,String phone,String isshow,
				String position,String load
				,String linecode1,String linearea1
				,String linecode2,String linearea2
				,String linecode3,String linearea3
				,String linecode4,String linearea4) {
			this.userid = userid;
			this.platenum = platenum;
			this.cartype = cartype;
			this.wx = wx;
			this.approveweight = approveweight;
			this.carsize = carsize;
			this.carimage1 = carimage1;
			this.carimage2 = carimage2;
			this.carimage3 = carimage3;
			this.carlength = carlength;
			this.officeplace = officeplace;
			this.officeplacecode=officeplacecode;
			this.xslicence = xslicence;
			this.yylicence = yylicence;
			this.bd = bd;
			this.distance=distance;
			this.distancecode=distancecode;
			this.wxcode=wxcode;
			this.cartypecode=cartypecode;
			this.longitude=longitude;
			this.latitude=latitude;
			this.link = link;
			this.phone = phone;
			this.isshow=isshow;
			this.position=position;
			this.load=load;
			
			this.linecode1=linecode1;
			this.linearea1=linearea1;
			this.linecode2=linecode2;
			this.linearea2=linearea2;
			this.linecode3=linecode3;
			this.linearea3=linearea3;
			this.linecode4=linecode4;
			this.linearea4=linearea4;
		}
		
	}
	
	
	String data;
	String deletejsonRequest;
	private void delete(String carid)
	{
		deletejsonRequest= new Gson().toJson(new AddCarRequest("{}", carid,"R"));
		
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(deletejsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(CarManageActivity.this, arg0.getMsg(),
										1000).show();
								getInfo();
							}else{
								Toast.makeText(CarManageActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(CarManageActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(CarManageActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
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
				if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
					startActivityForResult(new Intent(CarManageActivity.this,AddCarActivity.class),C.FROM_CARMANAGE);
				}else if(SharePerfUtil.getLinkMan().isEmpty()){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(CarManageActivity.this, PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(CarManageActivity.this, CompanyInfoActivity.class));
					}
					Toast.makeText(CarManageActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					startActivity(new Intent(CarManageActivity.this, LoginActivity.class));
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(RESULT_OK==resultCode)
		{
			getInfo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public class CarListRequest {
		private String Filters = "";
		private String type = "1005";

		public CarListRequest(String Filters) {
			this.Filters = Filters;
		}
	}

	public class CarListFilters{
		private String EQ_userid;
//		private String EQ_verify="0";

		public CarListFilters(String EQ_userid,String EQ_verify) {
			this.EQ_userid = EQ_userid;
//			this.EQ_verify=EQ_verify;
		}
	}

	String carListFilters;
	String jsonRequest;

	private void getInfo() {
		carListFilters = new Gson().toJson(new CarListFilters(SharePerfUtil.getUserId(),"0"));
		jsonRequest = new Gson().toJson(new CarListRequest(carListFilters));
		executeRequest(new GsonRequest<CarListBean>(Request.Method.GET,
				UrlCat.getSearchUrl(jsonRequest), CarListBean.class, null,
				null, new Listener<CarListBean>() {
			@Override
			public void onResponse(CarListBean arg0) {
				// TODO Auto-generated method stub
				if(arg0!=null){
					if ("1".equals(arg0.getCode())) {
						if(arg0!=null)
						{
							setList(arg0.getData());
							
							
						}
						Toast.makeText(CarManageActivity.this, "获取车辆列表",
								1000).show();
					}else{
						Toast.makeText(CarManageActivity.this, arg0.getMsg(),
								1000).show();
					}
				}else{
					Toast.makeText(CarManageActivity.this, "暂无数据！",
							1000).show();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(CarManageActivity.this, "无法连接服务器！",
						1000).show();
			}
		}));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_car:
			
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivityForResult(new Intent(CarManageActivity.this,AddCarActivity.class),C.FROM_CARMANAGE);
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(CarManageActivity.this, PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(CarManageActivity.this, CompanyInfoActivity.class));
				}
				Toast.makeText(CarManageActivity.this, "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(CarManageActivity.this, LoginActivity.class));
			}
			break;

		default:
			break;
		}

	}
	
	private CarLisrAdapter carLisrAdapter;
	public void setList(final List<CarInfo> list)
	{
		if(list.size()>0)
		{
			if(carLisrAdapter==null)
			{
				carLisrAdapter=new CarLisrAdapter(CarManageActivity.this, list);
				lv_car_manage.setAdapter(carLisrAdapter);
			}else
			{
				carLisrAdapter.setList(list);
			}
			carLisrAdapter.setOnItemBtnClickListener(new OnItemBtnClickListener() {
						
				@Override
				public void onItemBtnClick(View v, int position) {
					// TODO Auto-generated method stub
					String carid=((CarInfo)carLisrAdapter.getItem(position)).get_id();
					switch (v.getId()) {
					case R.id.btn_edit:
						edit(carid);
						break;
					case R.id.btn_delete:
						delete(carid);
						break;
					case R.id.btn_carpublic:
						startActivity(new Intent(CarManageActivity.this,PublishCarActivity.class)
						.putExtra("carinfo", (CarInfo)carLisrAdapter.getItem(position)));
						break;
	
					default:
						break;
					}
					
				}
			});
			carLisrAdapter.setOnItemOnCheckedChangeListener(new OnItemOnCheckedChangeListener() {
				
				@Override
				public void onItemBtnClick(View v, int position, boolean isChecked) {
					// TODO Auto-generated method stub
					CarInfo item=list.get(position);
					switch (v.getId()) {
					case R.id.csb_submit_location:
						if(isChecked)
						{
							if("".equals(item.get_id()))
							{
								return;
							}
							Toast.makeText(CarManageActivity.this, "已开启", 1000).show();
							SharePerfUtil.updataLoaction(isChecked);
							SharePerfUtil.saveCarid(item.get_id());
							JCLApplication.getInstance().checkLocationSetting();
						}else{
							if("".equals(item.get_id()))
							{
								return;
							}
							Toast.makeText(CarManageActivity.this, "已关闭", 1000).show();
							SharePerfUtil.saveCarid(item.get_id());
							SharePerfUtil.updataLoaction(isChecked);
						}
						break;
					case R.id.csb_load:
						if(isChecked)
						{
							item.setLoad("1");
						}else{
							item.setLoad("0");
						}
					

						submit(item.get_id());
						
						break;
					default:
						break;
					}
					
				}
			});
			carLisrAdapter.notifyDataSetChanged();
			no_list.setVisibility(View.GONE);
		}else
		{
			no_list.setVisibility(View.VISIBLE);
			
		}
	}
	
	public void submit(String carid)
	{
		
		data = new Gson().toJson(new AddCarData(SharePerfUtil.getUserId(),
				"","","","",
				"","",
				"",
				"","","",
				"","","","","","","",""
				,"","","","","",load
				,"",""
				,"",""
				,"",""
				,"","",""));
		if(TextUtils.isEmpty(carid))
		{
			jsonRequest = new Gson().toJson(new AddCarRequest(data));
		}else{
			jsonRequest = new Gson().toJson(new AddCarRequest(data,carid,"M"));
		}
		
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								
								
								MyToast.showToast(CarManageActivity.this, "提交成功");
//								if("1".equals(loadkey))
//								{
//									return;
//								}
								setResult(RESULT_OK);
								finish();
								
				
								
							}else{
								Toast.makeText(CarManageActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(CarManageActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(CarManageActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	private static int CAR_EDIT=0x00001; 
	private void edit(String carid)
	{
		startActivityForResult(new Intent(CarManageActivity.this,AddCarActivity.class)
				.putExtra("carid", carid), CAR_EDIT);
	}

	class CarLisrAdapter extends MyBaseAdpter<CarInfo>{

		public CarLisrAdapter(Context context, List<CarInfo> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		private OnItemBtnClickListener onItemBtnClickListener;
	    public void setOnItemBtnClickListener(OnItemBtnClickListener onItemBtnClickListener) {
	        this.onItemBtnClickListener = onItemBtnClickListener;
	    }
	    
	    private OnItemOnCheckedChangeListener onCheckedChangeListener;
	    public void setOnItemOnCheckedChangeListener(OnItemOnCheckedChangeListener onCheckedChangeListener) {
	        this.onCheckedChangeListener = onCheckedChangeListener;
	    }
	    
	    public void setList(List<CarInfo> list)
	    {
	    	this.myList=list;
	    }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;  
	         if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)  
	             holder = new ViewHolder();  
	             convertView = mInflater.inflate(R.layout.item_car_manage_list, null);  
	             holder.iv_car = (ImageView) convertView.findViewById(R.id.iv_car);  
	             holder.iv_next = (ImageView) convertView.findViewById(R.id.iv_next);  
	             holder.tv_platenum = (TextView) convertView.findViewById(R.id.tv_platenum); 
	             holder.tv_car_type = (TextView) convertView.findViewById(R.id.tv_car_type); 
	             holder.tv_length_and_weight = (TextView) convertView.findViewById(R.id.tv_length_and_weight);
	             holder.btn_edit = (TextView) convertView.findViewById(R.id.btn_edit); 
	             holder.btn_delete = (TextView) convertView.findViewById(R.id.btn_delete); 
	             holder.btn_carpublic = (TextView) convertView.findViewById(R.id.btn_carpublic); 
	             
	             holder.iv_name_check=(ImageView) convertView.findViewById(R.id.iv_name_check);
	             holder.iv_car_check=(ImageView) convertView.findViewById(R.id.iv_car_check);
	             
	             holder.csb_submit_location=(CheckSwitchButton) convertView.findViewById(R.id.csb_submit_location);
	             holder.csb_load=(CheckSwitchButton)  convertView.findViewById(R.id.csb_load);
	             convertView.setTag(holder);  
	         } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏  
	        	 holder = (ViewHolder) convertView.getTag();  
	         }
	        	 CarInfo carinfo=(CarInfo)getItem(position);
	             
	        	 String ischeckStr="未认证";
	        	 if("1".equals(carinfo.getIscheck())){
	        		 ischeckStr="已认证";
	        	 }
	        	 if(showdongtai){
	        		 holder.btn_carpublic.setVisibility(View.VISIBLE) ;
	        	 }else{
	        		 holder.btn_carpublic.setVisibility(View.GONE) ;
	        	 }
	        	 if("1".equals(carinfo.getLoad()))
	     		{
	     			holder.csb_load.setChecked(true);
	     		}else{
	     			holder.csb_load.setChecked(false);
	     		}
	        	 
	        	 if(carinfo.get_id().equals(SharePerfUtil.getCarid()))
	     		{
	        		 holder.csb_submit_location.setChecked(SharePerfUtil.getUpdataLoaction());
	     		}else{
	     			 holder.csb_submit_location.setChecked(false);
	     		}
	        	 
	        	 
	             holder.tv_platenum.setText(carinfo.getPlatenum()+"("+ischeckStr+")");
	             holder.tv_car_type.setText(carinfo.getCartype());
            	 holder.tv_length_and_weight.
            	 			setText(carinfo.getCarlength()+"米      "
	            			+carinfo.getApproveweight()+"吨");
 	             if(TextUtils.equals("1", carinfo.getIscheck()))
	             {
	            	 holder.iv_name_check.setVisibility(View.VISIBLE);
	             }else{
	            	 holder.iv_name_check.setVisibility(View.GONE);
	             }
	             if(TextUtils.equals("2", carinfo.getIscheck()))
	             {
	            	 holder.iv_car_check.setVisibility(View.VISIBLE);
	             }else{
	            	 holder.iv_car_check.setVisibility(View.GONE);
	             }
	             ImageLoaderUtil.getInstance(mContext).loadImage(carinfo.getCarimage1(), holder.iv_car);
	             
	             final int p=position;
	             holder.btn_edit.setOnClickListener(new View.OnClickListener() {
	                 @Override
	                 public void onClick(View v) {
	                     if(onItemBtnClickListener!=null){
	                         onItemBtnClickListener.onItemBtnClick(v,p);
	                     }
	                 }
	             });
	             holder.btn_delete.setOnClickListener(new View.OnClickListener() {
	                 @Override
	                 public void onClick(View v) {
	                     if(onItemBtnClickListener!=null){
	                         onItemBtnClickListener.onItemBtnClick(v,p);
	                     }
	                 }
	             });
	             
	             holder.btn_carpublic.setOnClickListener(new View.OnClickListener() {
	                 @Override
	                 public void onClick(View v) {
	                     if(onItemBtnClickListener!=null){
	                         onItemBtnClickListener.onItemBtnClick(v,p);
	                     }
	                 }
	             });
	             
	             holder.csb_load.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		     			@Override
		     			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		     				
		     				if(onCheckedChangeListener!=null){
		     					onCheckedChangeListener.onItemBtnClick(v,p,isChecked);
		                     }
		     				
		     				
		     			}
		     		});
        	 holder.csb_submit_location.setOnCheckedChangeListener(new OnCheckedChangeListener() {
     			@Override
     			public void onCheckedChanged(CompoundButton v, boolean isChecked) {

     				if(onCheckedChangeListener!=null){
     					onCheckedChangeListener.onItemBtnClick(v,p,isChecked);
                     }
     				
     			}
     		});
			return convertView;
		}
		
		public final class ViewHolder {  
	        public ImageView iv_car;  
	        public ImageView iv_next;  
	        public TextView tv_platenum;  
	        public TextView tv_car_type;  
	        public TextView tv_length_and_weight;
	        public TextView btn_edit;
	        public TextView btn_delete;
	        public TextView btn_carpublic;
	        
	        public ImageView iv_name_check;  
	        public ImageView iv_car_check;
	        public CheckSwitchButton csb_submit_location;//实时定位
	        public CheckSwitchButton csb_load;
	    } 
		
		
		
	}
}
