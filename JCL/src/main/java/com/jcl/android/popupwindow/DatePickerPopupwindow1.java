package com.jcl.android.popupwindow;

import java.util.Calendar;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.PopupWindow;

import com.jcl.android.R;

public class DatePickerPopupwindow1 extends PopupWindow implements
		OnClickListener {

	private Context context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private int type;
	private DatePicker datePicker;
	private Button mBtnConfirm, mBtnCancel;
	private String date;

	public DatePickerPopupwindow1(Context context, View parentView,
			final Handler myHandler, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.context = context;
		this.parentView = parentView;
		setTouchable(true);
		setOutsideTouchable(true);
		root = LayoutInflater.from(context).inflate(
				R.layout.popupwindow_date_picker, null);

		setUpViews();
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

	public void show(int type) {
		this.type = type;
		showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
				0, 0);
	}

	private void setUpViews() {
		datePicker = (DatePicker) root.findViewById(R.id.datePicker);
		mBtnConfirm = (Button) root.findViewById(R.id.btn_confirm);
		mBtnCancel = (Button) root.findViewById(R.id.btn_cancel);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		date = year + "-" + (monthOfYear + 1) + "-"
				+ dayOfMonth ;
		datePicker.init(year, monthOfYear, dayOfMonth,
				new OnDateChangedListener() {

					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth ;
					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (myHandler != null) {
				Message msg = myHandler.obtainMessage();
				msg.what = type;
				msg.obj = date;
				myHandler.sendMessage(msg);
			}
			dismiss();
			break;

		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			dismiss();
			break;
		}
	}

}
