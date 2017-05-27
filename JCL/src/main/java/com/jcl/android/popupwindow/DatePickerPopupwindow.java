package com.jcl.android.popupwindow;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.view.CalendarView;
import com.jcl.android.view.CalendarView.OnItemClickListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class DatePickerPopupwindow extends PopupWindow implements OnClickListener{
	private Context context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private int type;
	private String date;
	private CalendarView calendar;
	private ImageButton calendarLeft;
	private TextView calendarCenter;
	private ImageButton calendarRight;
	private SimpleDateFormat format;
	private Button mBtnConfirm, mBtnCancel;
	
	private boolean choise=false;
	
	public DatePickerPopupwindow(Context context,  View parentView,
			final Handler myHandler,AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.context = context;
		setTouchable(true);
		setOutsideTouchable(true);
		root = LayoutInflater.from(context).inflate(
				R.layout.popupwindow_calendar, null);
		initView();
		initLayout();
		setContentView(root);
		
	}

	private void initView()
	{
		format = new SimpleDateFormat("yyyy-MM-dd");
		//获取日历控件对象
		calendar = (CalendarView)root.findViewById(R.id.calendar);
		calendar.setSelectMore(false); //单选  
		
		calendarLeft = (ImageButton)root.findViewById(R.id.calendarLeft);
		calendarCenter = (TextView)root.findViewById(R.id.calendarCenter);
		calendarRight = (ImageButton)root.findViewById(R.id.calendarRight);
		
		mBtnConfirm = (Button) root.findViewById(R.id.btn_confirm);
		mBtnCancel = (Button) root.findViewById(R.id.btn_cancel);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		try {
			//设置日历日期
			Date now = new Date();
			calendar.setCalendarData(now);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-"); 
		calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
		initLayout();
		calendarLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击上一月 同样返回年月 
				String leftYearAndmonth = calendar.clickLeftMonth(); 
				String[] ya = leftYearAndmonth.split("-"); 
				calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
			}
		});
		
		calendarRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-"); 
				calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
			}
		});
		
		//设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		calendar.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void OnItemClick(Date selectedStartDate,
					Date selectedEndDate, Date downDate) {
				choise=true;
				if(calendar.isSelectMore()){
					Toast.makeText(JCLApplication.getContext(), format.format(selectedStartDate)+"到"+format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(JCLApplication.getContext(), format.format(downDate), Toast.LENGTH_SHORT).show();
					date=format.format(downDate);
				}
			}
		});
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if(!choise)
			{
				Toast.makeText(JCLApplication.getContext(), "请先选择日期", Toast.LENGTH_SHORT).show();
				return;
			}
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
