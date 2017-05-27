package com.jcl.android.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jcl.android.R;
/**
 * 车单重新报价
 * @author xuelei
 *
 */
public class BaojiaOnlinePopupwindow extends PopupWindow implements
		OnClickListener {

	private Activity context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private EditText et_pay_num;
	private Button btn_confirm, btn_cancel;
	private String priceid;

	public BaojiaOnlinePopupwindow(Context context, View parentView,
			final Handler myHandler, AttributeSet attrs,String priceid) {
		super(context, attrs);
		this.context = (Activity) context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.parentView = parentView;
		this.priceid = priceid;
		setTouchable(true);
		setOutsideTouchable(true);
		setFocusable(true); // 设置PopupWindow可获得焦点
		setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		root = LayoutInflater.from(context).inflate(R.layout.popupwindow_baojia_online,
				null);
		et_pay_num = (EditText) root.findViewById(R.id.et_pay_num);
		btn_confirm = (Button) root.findViewById(R.id.btn_confirm);
		btn_cancel = (Button) root.findViewById(R.id.btn_cancel);
		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
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
		if(et_pay_num!=null){
			et_pay_num.setText("");
		}
		showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
				0, 0);
	}
	
	private void closeInput() {
		/**隐藏软键盘**/
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if(myHandler!=null){
				Message msg = myHandler.obtainMessage();
				msg.obj = et_pay_num.getText().toString();
				msg.what = 0;//确认充值
				Bundle bundle = new Bundle();
				bundle.putString("priceid", priceid);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			}
			closeInput();
			dismiss();
			break;
		case R.id.btn_cancel:
			closeInput();
			dismiss();
			break;

		default:
			break;
		}

	}
}
