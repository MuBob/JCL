package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.AddGoodsActivity;
import com.jcl.android.activity.CompanyInfoActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.FindOfferPriceActivity;
import com.jcl.android.activity.GetCompanyInfoActivity;
import com.jcl.android.activity.GetPersonalInfoActivity;
import com.jcl.android.activity.GoodsManageActivity;
import com.jcl.android.activity.MyWalletActivity;
import com.jcl.android.activity.OfferOnlineActivity;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.activity.GoodsManageActivity.GoodsListFilters;
import com.jcl.android.activity.GoodsManageActivity.GoodsListRequest;
import com.jcl.android.alipay.PayResult;
import com.jcl.android.alipay.PayUtils;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.bean.GoodsListBean;
import com.jcl.android.bean.PricesListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 报价列表
 * 
 * @author xueleilin
 *
 */
public class OfferPriceListFragment extends BaseFragment implements OnClickListener {
	
	private View root;
	ListView lv_prices;// 报价列表
	private List<PricesListBean.Price> dataList;
	
	private String goodsid;
	private String chufadi;
	private String mudidi;
	private String chexing;
	private String chufashijian;
	private String detailgood;
	
	private PriceLisrAdapter priceLisrAdapter;

	public static OfferPriceListFragment newInstance(String goodsid,String chufadi,String mudidi,String chexing,String chufashijian,String detailgood) {
		OfferPriceListFragment f = new OfferPriceListFragment();
		Bundle args = new Bundle();
		args.putString("goodsid", goodsid);
		args.putString("chufadi", chufadi);
		args.putString("mudidi", mudidi);
		args.putString("chexing", chexing);
		args.putString("chufashijian", chufashijian);
		args.putString("detailgood", detailgood);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		goodsid = getArguments().getString("goodsid");
		chufadi = getArguments().getString("chufadi");
		mudidi = getArguments().getString("mudidi");
		chexing = getArguments().getString("chexing");
		chufashijian = getArguments().getString("chufashijian");
		detailgood = getArguments().getString("detailgood");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragmen_offerprice,
				container, false);
		initView();
		return root;
	}
	
	private void initView() {
		lv_prices = (ListView) root.findViewById(R.id.lv_prices);
		dataList = new ArrayList<>();
		priceLisrAdapter = new PriceLisrAdapter();
		lv_prices.setAdapter(priceLisrAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getInfo();
	}

	public class PricesListRequest {
		private String Filters = "";
		private String type = "3002";

		public PricesListRequest(String Filters) {
			this.Filters = Filters;
		}
	}

	public class PricesListFilters {
		private String EQ_goodsid;

		public PricesListFilters(String EQ_goodsid) {
			this.EQ_goodsid = EQ_goodsid;
		}
	}

	String priceListFilters;
	String jsonRequest;

	private void getInfo() {
		showLD("加载中...");
		priceListFilters = new Gson().toJson(new PricesListFilters(goodsid));
		jsonRequest = new Gson().toJson(new PricesListRequest(priceListFilters));
		executeRequest(new GsonRequest<PricesListBean>(Request.Method.GET,
				UrlCat.getSearchUrl(jsonRequest), PricesListBean.class, null,
				null, new Listener<PricesListBean>() {
					@Override
					public void onResponse(PricesListBean arg0) {
						// TODO Auto-generated method stub
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								if (arg0 != null) {
									setList(arg0.getData());
								}
							} else {
								Toast.makeText(getActivity(),
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(getActivity(), "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						cancelLD();
						Toast.makeText(getActivity(), "无法连接服务器！",
								1000).show();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void setList(List<PricesListBean.Price> list) {
		if (dataList != null) {
			dataList.clear();
			dataList = list;
		}
		
		priceLisrAdapter.notifyDataSetChanged();
	}

	class PriceLisrAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity())
						.inflate(R.layout.listitem_offer_price, null);
				holder.tv_chufadi = (TextView) convertView
						.findViewById(R.id.tv_chufadi);
				holder.tv_mudidi = (TextView) convertView
						.findViewById(R.id.tv_mudidi);
				holder.tv_chufashijian = (TextView) convertView
						.findViewById(R.id.tv_chufashijian);
				holder.tv_chexing = (TextView) convertView
						.findViewById(R.id.tv_chexing);
				holder.tv_baojia = (TextView) convertView
						.findViewById(R.id.tv_baojia);
				holder.tv_qiang = (TextView) convertView
						.findViewById(R.id.tv_qiang);
				holder.tv_goodsinfo = (TextView) convertView
						.findViewById(R.id.tv_goodsinfo);
				holder.tv_info = (TextView) convertView
						.findViewById(R.id.tv_info);
				holder.img_call = (ImageView)convertView.findViewById(R.id.img_call);
				//添加采用报价的监听事件
				holder.tv_qiang.setOnClickListener(new MyOnClickListnener(dataList.get(position).get_id(),dataList.get(position).getPrice()));
				final String submittype = dataList.get(position).getSubmittype();
				holder.tv_info.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (TextUtils.equals("0", submittype)) {
							getActivity().startActivityForResult(
									new Intent(getActivity(),
											GetPersonalInfoActivity.class), 0);
						} else if (TextUtils.equals("1", submittype)) {
							getActivity()
									.startActivityForResult(
											new Intent(getActivity(),
													GetCompanyInfoActivity.class), 0);
						} else {

						}

					}
				});
				convertView.setTag(holder);
			} else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
				holder = (ViewHolder) convertView.getTag();
			}
			final PricesListBean.Price price = dataList.get(position);
			holder.tv_chufadi.setText(chufadi);
			holder.tv_mudidi.setText(mudidi);
			holder.tv_chufashijian.setText(chufashijian);
			holder.tv_goodsinfo.setText(detailgood);
			//holder.tv_chexing.setText((chexing == null ? "":chexing));
			holder.tv_baojia.setText("报价：￥" + price.getPrice()+"  报价人："+price.getLinkman());
			holder.img_call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String number = price.getPhone();  
	                //用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
	                startActivity(intent); 
				}
			});
			return convertView;
		}

		public final class ViewHolder {
			TextView tv_chufadi, tv_mudidi, tv_chufashijian, tv_chexing,
			tv_baojia,tv_qiang,tv_goodsinfo,tv_info;
			ImageView img_call;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList != null && dataList.size() > 0 ? dataList.size()
					: 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

	}
	
	//采用报价
	class OfferOnlineRequest {
		private String operate;
		private String type;
		private String data;

		public OfferOnlineRequest(String data) {
			this.operate = "A";
			this.type = "3003";
			this.data = data;
		}
	}
	
	class BjData {
		private String userid;//货主id
		private String goodsid;//货源id
		private String priceid;//报价单id
		
		public BjData(String userid,String goodsid,String priceid){
			this.userid = userid;
			this.goodsid = goodsid;
			this.priceid = priceid;
		}
	}
	
	public class MyOnClickListnener implements OnClickListener{
		String priceid;
		String price;
		public  MyOnClickListnener(String priceid,String price){
			this.priceid = priceid;
			this.price = price;
		}
		@Override
		public void onClick(View arg0) {
			
			showDialog(priceid,price);
			
		}
		
	}
	public void usePrice(String priceid){
		BjData bjdata = new BjData(JCLApplication.getInstance().getUserId(),goodsid,priceid);
		String bjDataJson = new Gson().toJson(bjdata);
		OfferOnlineRequest offeronlineRequest = new OfferOnlineRequest(bjDataJson);
		String postStr = new Gson().toJson(offeronlineRequest);
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				ParamsBuilder.submitParams(postStr),
				new Listener<BaseBean>() {

					@Override
					public void onResponse(BaseBean arg0) {
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								MyToast.showToast(getActivity(),
										"提交成功");
								Intent intent = new Intent();
				                getActivity().setResult(1, intent); 
								getActivity().finish();
							} else {
								MyToast.showToast(getActivity(),
										"提交失败");
							}
						}

						cancelLD();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						MyToast.showToast(getActivity(), "提交失败");
					}
				}));
	}
	protected void showDialog(final String priceid,final String price) {
		  AlertDialog.Builder builder = new Builder(getActivity());
		  builder.setMessage("1.担保交易支付：运费付款到平台，货物送达后，确认付款，平台支付担保运费付给车主。\n2.运费直接给付:货物送达后货主线下付费给车主。\n3.点击空白处返回。");  
		  builder.setTitle("请选择支付方式!"); 
		  builder.setPositiveButton("担保交易支付", new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int which) {
	        	   dialog.dismiss();
	        	  //调用支付宝支付
					new PayUtils().pay(getActivity(), mHandler, price,"1",priceid);
	           }
	       });  
		  builder.setNegativeButton("运费直接给付", new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int which) {
	        	   usePrice(priceid); 
	        	   dialog.dismiss();
	           }
	       });  
		  builder.create().show();
		}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1://支付宝支付返回
                PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(getActivity(), "支付成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
	                getActivity().setResult(1, intent); 
					getActivity().finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(getActivity(), "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(getActivity(), "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}}};


}
