package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.R.color;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.MessageBean;
import com.jcl.android.bean.MessageBean.MessageInfo;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

public class MessageActivity extends BaseActivity implements 
  OnRefreshListener, OnLoadListener, OnClickListener{

	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private MyUINavigationView uINavigationView;
	private ListView lv_message;// 货物列表
	private RadioGroup rg_tab;
	private RadioButton rb_unread;
	private RadioButton rb_isread;
	private List<MessageBean.MessageInfo> dataList=new ArrayList<>();
	private List<MessageBean.MessageInfo> isReadList;
	private List<MessageBean.MessageInfo> unReadList;
	private messageAdapetr adapter;
	private boolean isViewShown = false;
	private boolean isRead=false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mygoods);
		initNavigation();
		initView();
		
		         if (!isViewShown) {
						isReadList=new ArrayList<MessageBean.MessageInfo>();
						unReadList=new ArrayList<MessageBean.MessageInfo>();
						adapter = new messageAdapetr(unReadList);
						lv_message.setAdapter(adapter);
			    		loadunreadData();
			    		lv_message.setOnItemClickListener(new OnItemClickListener() {
			    			@Override
			    			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			    					long arg3) {
			    					MessageBean.MessageInfo item=(MessageInfo) adapter.getItem(position);
			    					startActivity(DetailFindActivity.newInstance(MessageActivity.this,
			    							item.getType(),item.getBizid()));
			    					if(!isRead){
			    						isReadList.add(0, item);
			    						unReadList.remove(position);
			    						adapter.setList(unReadList);
			    						adapter.notifyDataSetChanged();
			    						
			    					}
			    					
			    					
			    			}
			    		});
					}
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			 ViewGroup container,  Bundle savedInstanceState) {
//		root = inflater.inflate(R.layout.activity_mygoods, container,
//				false);
//		initNavigation();
//		initView();
//		if (!isViewShown) {
//
//			isReadList=new ArrayList<MessageBean.MessageInfo>();
//			unReadList=new ArrayList<MessageBean.MessageInfo>();
//			adapter = new messageAdapetr(unReadList);
//			lv_message.setAdapter(adapter);
//    		loadunreadData();
//    		lv_message.setOnItemClickListener(new OnItemClickListener() {
//    			@Override
//    			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//    					long arg3) {
//    					MessageBean.MessageInfo item=(MessageInfo) adapter.getItem(position);
//    					startActivity(DetailFindActivity.newInstance(MessageActivity.this,
//    							item.getType(),item.getBizid()));
//    					if(!isRead){
//    						isReadList.add(0, item);
//    						unReadList.remove(position);
//    						adapter.setList(unReadList);
//    						adapter.notifyDataSetChanged();
//    						
//    					}
//    					
//    					
//    			}
//    		});
//		}
//		return root;
//	}
//	
//
//	@Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getView() != null) {
//        	isViewShown = true;
//        	dataList = new ArrayList<MessageBean.MessageInfo>();
////    		loadData();
////    		adapter = new messageAdapetr(unReadList);
////    		lv_message.setAdapter(adapter);
//
//        } else {
//        	isViewShown = false;
//        }
//    }

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		pagenum = 1;
		if(isRead){
			loadisreadData();
		}else{
			loadunreadData();
		}
	
	}
	
	//加载已读消息
	public void loadisreadData() {
		//拼接filter中数据
				String filters =  new Gson().toJson(new GetFilter(JCLApplication
						.getInstance().getUserId(),"1"));
				String getStr = new Gson().toJson(new GetStr(pagenum, filters));
				//检测是否是第一页
				final boolean isFromTop = pagenum == 1;
				//添加刷新小图标显示
				if (!srLayout.isRefreshing()) {
					srLayout.setRefreshing(true);
				}
				executeRequest(new GsonRequest<MessageBean>(Request.Method.POST,
						UrlCat.URL_SEARCH, MessageBean.class, null,
						ParamsBuilder.pageSearchParams(getStr),
						new Listener<MessageBean>() {
							@Override
							public void onResponse(MessageBean arg0) {
								//清除刷新小图标
								srLayout.setRefreshing(false);
								srLayout.setLoading(false);
								if (arg0 != null) { 
									if (isFromTop) {
										isReadList.clear();
										
									} 
									
									if (TextUtils.equals(arg0.getCode(), "1")) {
										isReadList.addAll(arg0.getData());
										adapter.setList(isReadList);
										adapter.notifyDataSetChanged();
									} else {
										MyToast.showToast(MessageActivity.this, "服务端异常");
									}
								} else {
									MyToast.showToast(MessageActivity.this, "服务端异常");
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								srLayout.setRefreshing(false);
//								MyToast.showToast(MessageActivity.this, arg0.getMessage());
							}
						}));
	}
	//加载未读消息
	public void loadunreadData() {
		//拼接filter中数据
				String filters =  new Gson().toJson(new GetFilter(JCLApplication
						.getInstance().getUserId(),"0"));
				String getStr = new Gson().toJson(new GetStr(pagenum, filters));
				//检测是否是第一页
				final boolean isFromTop = pagenum == 1;
				//添加刷新小图标显示
				if (!srLayout.isRefreshing()) {
					srLayout.setRefreshing(true);
				}
				executeRequest(new GsonRequest<MessageBean>(Request.Method.POST,
						UrlCat.URL_SEARCH, MessageBean.class, null,
						ParamsBuilder.pageSearchParams(getStr),
						new Listener<MessageBean>() {
							@Override
							public void onResponse(MessageBean arg0) {
								//清除刷新小图标
								srLayout.setRefreshing(false);
								srLayout.setLoading(false);
								if (arg0 != null) { 
									if (isFromTop) {
										unReadList.clear();
										
									} 
									
									if (TextUtils.equals(arg0.getCode(), "1")) {
										unReadList.addAll(arg0.getData());
										adapter.setList(unReadList);
										adapter.notifyDataSetChanged();
									} else {
										MyToast.showToast(MessageActivity.this, "服务端异常");
									}
								} else {
									MyToast.showToast(MessageActivity.this, "服务端异常");
								}
								
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								srLayout.setRefreshing(false);
//								MyToast.showToast(MessageActivity.this, arg0.getMessage());
							}
						}));
	}
	
	
//	public void loadData() {
//		//拼接filter中数据
//				String filters =  new Gson().toJson(new GetFilter(JCLApplication
//						.getInstance().getUserId(),"0"));
//				String getStr = new Gson().toJson(new GetStr(pagenum, filters));
//				//检测是否是第一页
//				final boolean isFromTop = pagenum == 1;
//				//添加刷新小图标显示
//				if (!srLayout.isRefreshing()) {
//					srLayout.setRefreshing(true);
//				}
//				executeRequest(new GsonRequest<MessageBean>(Request.Method.POST,
//						UrlCat.URL_SEARCH, MessageBean.class, null,
//						ParamsBuilder.pageSearchParams(getStr),
//						new Listener<MessageBean>() {
//							@Override
//							public void onResponse(MessageBean arg0) {
//								//清除刷新小图标
//								srLayout.setRefreshing(false);
//								if (arg0 != null) { 
////									if (isFromTop) {
////										dataList = arg0.getData();
////									} else {
////										dataList.addAll(arg0.getData());
////										
////									}
//									if (isFromTop) {
//										unReadList.clear();
//										isReadList.clear();
//									} 
//									if (TextUtils.equals(arg0.getCode(), "1")) {
//										for (int i = 0; i < arg0.getData().size(); i++) {
//											if("0".equals(arg0.getData().get(i).getIsread())){
//												unReadList.add(arg0.getData().get(i));
//											}else{
//												isReadList.add(arg0.getData().get(i));
//											}
//										}
//										
//										if(isRead){
//											adapter.setList(isReadList);
//											adapter.notifyDataSetChanged();
//										}else{
//											adapter.setList(unReadList);
//											adapter.notifyDataSetChanged();
//											if(unReadList.size()<3){
//												pagenum++;
//												loadData();
//											}
//										}
//										
//										srLayout.setLoading(false);
//									} else {
//										MyToast.showToast(MessageActivity.this, "服务端异常");
//									}
//								} else {
//									MyToast.showToast(MessageActivity.this, "服务端异常");
//								}
//							}
//						}, new ErrorListener() {
//
//							@Override
//							public void onErrorResponse(VolleyError arg0) {
//								srLayout.setRefreshing(false);
////								MyToast.showToast(MessageActivity.this, arg0.getMessage());
//							}
//						}));
//	}

	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		private String sort;// {"key1":"asc/desc"}排序
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "0011";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}
	
	class GetFilter {
		private String userid;
		private String isread;
		private String _id;


		public GetFilter(String userid, String isread,String _id) {
			this.userid = userid;
			this.isread = isread;
			this._id = _id;
		}
		public GetFilter(String userid,String isread) {
			this.userid = userid;
			this.isread=isread;
		}
//		public GetFilter(String userid) {
//			this.userid = userid;
//		}

	}
	
	private void initView() {
		rg_tab=(RadioGroup) findViewById(R.id.rg_tab);
		rg_tab.setVisibility(View.VISIBLE);
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_unread:
					isRead=false;
					loadunreadData();
					
					break;
				case R.id.rb_isread:
					isRead=true;
					loadisreadData();
					
					break;

				default:
					break;
				}
			}
		});
		rb_unread= (RadioButton) findViewById(R.id.rb_unread);
		rb_isread= (RadioButton) findViewById(R.id.rb_isread);
		
		lv_message = (ListView) findViewById(R.id.lv_goods);
		srLayout = (BaseSwipeRefreshLayout) findViewById(R.id.sr_layout);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
	}

	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnRightText.setVisibility(View.GONE);
		uINavigationView.getTv_title().setText("消息列表");
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	class messageAdapetr extends BaseAdapter {

		private List<MessageBean.MessageInfo> mList;
		public messageAdapetr(List<MessageBean.MessageInfo> list){
			if(list==null){
				mList=new ArrayList<>();
			}else{
				mList=list;
			}
		}
		public void addtoTop(MessageBean.MessageInfo item){
			mList.add(0, item);
		}
		public void setList(List<MessageBean.MessageInfo> list)
		{
			mList=list;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MessageActivity.this)
						.inflate(R.layout.listitem_message, null);
				holder.iv_message = (ImageView) convertView
						.findViewById(R.id.iv_messages);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_contents = (TextView) convertView
						.findViewById(R.id.tv_contents);
				convertView.setTag(holder);
			} else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
				holder = (ViewHolder) convertView.getTag();
			}
			MessageBean.MessageInfo messageInfo = (MessageBean.MessageInfo)getItem(position);
			
			if(isRead){
				holder.tv_title.setTextColor(color.navigation);
				holder.tv_contents.setTextColor(color.navigation);
			}else{
				holder.tv_title.setTextColor(Color.BLUE);
				holder.tv_contents.setTextColor(Color.BLUE);
			}
//			if (messageInfo.getIsread().equals("0")
//				|| messageInfo.getIsread() == "0") {
//				//0未读 
////				holder.tv_title.setTextColor(color.navigation);
////				holder.tv_contents.setTextColor(color.navigation);
//				
//				holder.tv_title.setTextColor(Color.BLUE);
//				holder.tv_contents.setTextColor(Color.BLUE);
//			} 
			
			holder.tv_contents.setText(messageInfo.getContent());
			holder.tv_time.setText(messageInfo.getCreatedate().substring(5, 16));
			holder.tv_title.setText(messageInfo.getTitle());
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList != null ? mList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

	}
	
	public final class ViewHolder {
		public ImageView iv_message;
		public TextView tv_title;
		public TextView tv_time;
		public TextView tv_contents;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		}
	}

	@Override
	public void onLoad() {
		pagenum++;
		if(isRead){
			loadisreadData();
		}else{
			loadunreadData();
		}
	}

	@Override
	public void onRefresh() {
		pagenum = 1;
		if(isRead){
			loadisreadData();
		}else{
			loadunreadData();
		}
	}
}
