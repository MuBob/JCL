package com.jcl.android.fragment;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;

/**
 * 联系人
 * syl
 * */
public class MyContactFragment extends BaseFragment implements
	OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;
	private ContactAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	
	Context mContext = null;  
	 
    /**获取库Phon表字段**/  
    private static final String[] PHONES_PROJECTION = new String[] {  
        Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };  
    /**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**头像ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**联系人的ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3;  
      
 
    /**联系人名称**/  
    private ArrayList<String> mContactsName = new ArrayList<String>();  
      
    /**联系人号码**/  
    private ArrayList<String> mContactsNumber = new ArrayList<String>();  
 
    /**联系人头像**/  
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();  

	public static MyContactFragment newInstance() {
	MyContactFragment f = new MyContactFragment();
	Bundle args = new Bundle();
	f.setArguments(args);
	return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	root = inflater.inflate(R.layout.fragment_setting_public_others,
			container, false);
	mContext = getActivity();
	initView();
	/**得到手机通讯录联系人信息**/  
    getPhoneContacts();  
	return root;
	}

	private void initView() {
	lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
	srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
	// 添加刷新事件
	srLayout.setOnRefreshListener(this);
	srLayout.setOnLoadListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	mAdapter = new ContactAdapter();
	lv_find_by_list.setAdapter(mAdapter);

	}

	class ContactAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContactsName != null ? mContactsName.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mContactsName.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.listitem_contact, null);
		}
		final ViewHolder holder = ViewHolder.getViewHolder(convertView);
		holder.tv_name.setText(mContactsName.get(position));
		holder.tv_tel.setText(mContactsNumber.get(position));
		//打电话
		holder.iv_tel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse
						("tel:"+holder.tv_tel.getText().toString().trim()));  
	            startActivity(intent);  	
			}
		});
		
		holder.tv_invite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final AlertDialog.Builder d=new AlertDialog.Builder(getActivity());
				d.setTitle("邀请").setMessage("确定邀请该好友？").setPositiveButton
				("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String data = new Gson().toJson(new inviteData(holder.tv_tel.getText().toString().trim()));
						String jsonRequest = new Gson().toJson(new inviteRequest(data));
						showLD("提交中...");
						executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
								UrlCat.URL_SUBMIT, BaseBean.class, null,
								new ParamsBuilder().submitParams(jsonRequest),
								new Listener<BaseBean>() {
									@Override
									public void onResponse(BaseBean arg0) {
										// TODO Auto-generated method stub
										cancelLD();
										if (arg0 != null) {
											if ("1".equals(arg0.getCode())) {
													Toast.makeText(getActivity(),
															"邀请成功", Toast.LENGTH_SHORT).show();
											} else {
												Toast.makeText(getActivity(),
														arg0.getMsg(), Toast.LENGTH_SHORT).show();
											}
										} else {
											Toast.makeText(getActivity(), "暂无数据！", Toast.LENGTH_SHORT)
													.show();
										}
									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(VolleyError arg0) {
										cancelLD();
										Toast.makeText(getActivity(), "网络连接异常！", Toast.LENGTH_SHORT)
												.show();
									}
								}));
					}
					
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).create().show();
			}
		});
		
		return convertView;
	}

	}

	static class ViewHolder {

	TextView tv_name, tv_tel,tv_invite;
	ImageView iv_tel;
	/**
	 * 封装holder获取方法
	 * 
	 * @param convertView
	 * @return
	 */
	public static ViewHolder getViewHolder(View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			convertView.setTag(holder);
		}
		holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		holder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
		holder.iv_tel = (ImageView) convertView.findViewById(R.id.iv_tel);
		holder.tv_invite = (TextView) convertView.findViewById(R.id.tv_invite);
		return holder;
	}

	}

class inviteRequest {
		
		private String type;
		private String data;
		private String operate;

		public inviteRequest(String data) {
			this.operate = "A";
			this.type = "0013";
			this.data = data;
		}
	}
	
class inviteData {
		
		private String userid;
		private String mobile;
		private String tel;

		public inviteData(String tel) {
			this.userid = JCLApplication.getInstance().getUserId();
			this.mobile = SharePerfUtil.getLoginName();
			this.tel = tel;
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		srLayout.setRefreshing(false);
	}

	 /**得到手机通讯录联系人信息**/  
    private void getPhoneContacts() {  
    ContentResolver resolver = mContext.getContentResolver();  
    // 获取手机联系人  
    Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);  
    if (phoneCursor != null) {  
        while (phoneCursor.moveToNext()) {  
        //得到手机号码  
        String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
        //当手机号码为空的或者为空字段 跳过当前循环  
        if (TextUtils.isEmpty(phoneNumber))  
            continue;  
        //得到联系人名称  
        String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);  
        //得到联系人ID  
        Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);  
        //得到联系人头像ID  
        Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);  
        //得到联系人头像Bitamp  
        Bitmap contactPhoto = null;  
        //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
        if(photoid > 0 ) {  
            Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
            contactPhoto = BitmapFactory.decodeStream(input);  
        }else {  
            contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.logo);  
        }  
          
        mContactsName.add(contactName);  
        mContactsNumber.add(phoneNumber);  
        mContactsPhonto.add(contactPhoto);  
        }  
 
        phoneCursor.close();  
    }  
    }  
      
    /**得到手机SIM卡联系人人信息**/  
    private void getSIMContacts() {  
    ContentResolver resolver = mContext.getContentResolver();  
    // 获取Sims卡联系人  
    Uri uri = Uri.parse("content://icc/adn");  
    Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,  
        null);  
 
    if (phoneCursor != null) {  
        while (phoneCursor.moveToNext()) {  
 
        // 得到手机号码  
        String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
        // 当手机号码为空的或者为空字段 跳过当前循环  
        if (TextUtils.isEmpty(phoneNumber))  
            continue;  
        // 得到联系人名称  
        String contactName = phoneCursor 
            .getString(PHONES_DISPLAY_NAME_INDEX);  
 
        //Sim卡中没有联系人头像  
          
        mContactsName.add(contactName);  
        mContactsNumber.add(phoneNumber);  
        }  
 
        phoneCursor.close();  
    }  
    }  

	}
