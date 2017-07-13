package com.jcl.android.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.alipay.PayResult;
import com.jcl.android.alipay.PayUtils;
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
	private Button btn_wechat, btn_my, btn_alipay;
	private MyPayUtil myPayUtil;
	private WXPayUtil wxPayUtil;
	private PayUtils payUtils;
	private String goodsid,   vanorderid,   phone,   price;
	private PayInputPopupwindow payInputPopupwindow;
	private Handler alipayHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case 1://支付宝支付返回
					PayResult payResult = new PayResult((String) msg.obj);

					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();

					String resultStatus = payResult.getResultStatus();

					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(context, "支付成功",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						context.setResult(1, intent);
						context.finish();
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(context, "支付结果确认中",
									Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(context, "支付失败",
									Toast.LENGTH_SHORT).show();

						}
					}
					break;
			}
			return true;
		}
	});
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
				case 3:
					payUtils.pay(context, alipayHandler, price,"1", goodsid);
					return true;
				default:
					return false;
			}
		}
	});

	public PayTypePopupwindow(Context context, View parentView, AttributeSet attrs,
							  String goodsid,  String vanorderid,  String phone,  String price) {
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
		btn_alipay = (Button) root.findViewById(R.id.btn_menu_pay_alipay);
		myPayUtil=new MyPayUtil(context);
		wxPayUtil=new WXPayUtil(context);
		payUtils=new PayUtils();
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
			case R.id.btn_menu_pay_alipay:
				if(!TextUtils.isEmpty(price)){
					Message message = handler.obtainMessage();
					message.what=3;
					message.obj=price;
					handler.sendMessage(message);
				}else {
					payInputPopupwindow.show(3);
				}
				break;
		default:
			break;
		}

	}
}
