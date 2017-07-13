package com.jcl.android.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.mypay.MyPayUtil;
import com.jcl.android.wxapi.WXPayUtil;

/**
 * 支付方式
 * @author xuelei
 *
 */
public class PayTypePopupwindow extends PopupWindow implements
		OnClickListener {

	private Activity context;
	private View parentView;
	private View root;
	private EditText et_pay_num;
	private Button btn_wechat, btn_my;
	private MyPayUtil myPayUtil;
	private WXPayUtil wxPayUtil;
	private String goodsid,   vanorderid,   phone,   price;
	private PayInputPopupwindow payInputPopupwindow;
	private Handler handler=new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			String price = (String) msg.obj;
			switch (msg.what){
				case 1:
					float price_yuan = Float.parseFloat(price);
					int price_fen = (int) (price_yuan * 100);
					wxPayUtil.prepay("_mobile:"+phone, price_fen,"goodsid="+goodsid+", vanorderid="+vanorderid, null);
					return true;
				case 2:
					String userId = JCLApplication.getInstance().getUserId();
					myPayUtil.pay(userId, phone, price);
					return true;
				default:
					return false;
			}
		}
	});

	public PayTypePopupwindow(Context context, View parentView, AttributeSet attrs,String goodsid,  String vanorderid,  String phone,  String price) {
		super(context, attrs);
		this.context = (Activity) context;
		this.parentView = parentView;
		this.goodsid = goodsid;
		this.vanorderid = vanorderid;
		this.phone = phone;
		this.price = price;
		if(TextUtils.isEmpty(price)){
			payInputPopupwindow=new PayInputPopupwindow(context, parentView, handler, attrs);
		}
		setTouchable(true);
		setOutsideTouchable(true);
		setFocusable(true); // 设置PopupWindow可获得焦点
//		setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		root = LayoutInflater.from(context).inflate(R.layout.menu_bottom_pay, null);
		btn_wechat = (Button) root.findViewById(R.id.btn_menu_pay_wechat);
		btn_my = (Button) root.findViewById(R.id.btn_menu_pay_my);
		myPayUtil=new MyPayUtil(context);
		wxPayUtil=new WXPayUtil(context);
		btn_wechat.setOnClickListener(this);
		btn_my.setOnClickListener(this);
		setContentView(root);
		initLayout();
		setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
		
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu_pay_wechat:
			dismiss();
			if(!TextUtils.isEmpty(price)){
				Message message = handler.obtainMessage();
				message.what=1;
				message.obj=price;
				handler.sendMessage(message);
			}else {
				payInputPopupwindow.show(1);
			}
			break;
		case R.id.btn_menu_pay_my:
			dismiss();
			if(!TextUtils.isEmpty(price)){
				Message message = handler.obtainMessage();
				message.what=2;
				message.obj=price;
				handler.sendMessage(message);
			}else {
				payInputPopupwindow.show(2);
			}
			break;
		default:
			break;
		}

	}
}
