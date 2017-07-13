package com.jcl.android.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
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
 * 支付输入窗口
 * @author xuelei
 *
 */
public class PayInputPopupwindow extends PopupWindow implements
		OnClickListener {

	private Activity context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private EditText et_pay_num;
	private Button btn_confirm, btn_cancel;
	private int pay_type=1;  //支付方式，1=微信支付，2=余额支付

	public PayInputPopupwindow(Context context, View parentView,
                                Handler myHandler, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity) context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.parentView = parentView;
		setTouchable(true);
		setOutsideTouchable(true);
		setFocusable(true); // 设置PopupWindow可获得焦点
		setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		root = LayoutInflater.from(context).inflate(R.layout.popupwindow_tixian,
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

	public void show(){
		show(1);
	}
	public void show(int type) {
		pay_type=type;
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
				msg.obj = et_pay_num.getText().toString().trim();
				msg.what = pay_type;//确认支付
				myHandler.sendMessage(msg);
			}
			closeInput();
			dismiss();
			break;
		case R.id.btn_cancel:
			if(myHandler!=null){
				Message msg = myHandler.obtainMessage();
				msg.obj = et_pay_num.getText().toString().trim();
				msg.what = -1;//取消支付
				myHandler.sendMessage(msg);
			}
			closeInput();
			dismiss();
			break;

		default:
			break;
		}

	}
}
