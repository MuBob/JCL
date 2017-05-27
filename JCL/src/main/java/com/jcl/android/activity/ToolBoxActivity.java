package com.jcl.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyUINavigationView;

public class ToolBoxActivity extends BaseActivity implements OnClickListener{

	private MyUINavigationView uINavigationView;
	private View rl_startarea;//出发地布局
	private View rl_endarea;//目的地布局
	private TextView tv_startarea;//出发地
	private TextView tv_endarea;//目的地
	private TextView tv_cesuan;//测算结果
	private Button btn_cesuan;//测算按钮
	
	private CityPickerPopupwindow cityPickerPopupwindow;//城市选择器
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_toolbox);
		initView();
		initNavigation();
	}
	
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setVisibility(View.GONE);
	}
	
	
	private void initView()
	{
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		rl_startarea=findViewById(R.id.rl_startarea);
		rl_endarea=findViewById(R.id.rl_endarea);
		tv_startarea=(TextView) findViewById(R.id.tv_startarea);
		tv_endarea=(TextView) findViewById(R.id.tv_endarea);
		tv_cesuan=(TextView) findViewById(R.id.tv_cesuan);
		btn_cesuan=(Button) findViewById(R.id.btn_cesuan);
		
		rl_startarea.setOnClickListener(this);
		rl_endarea.setOnClickListener(this);
		btn_cesuan.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cesuan:
			if(TextUtils.isEmpty(tv_startarea.getText().toString()))
			{
				Toast.makeText(ToolBoxActivity.this, "请先选择车发地", 1000).show();;
				return;
			}
			if(TextUtils.isEmpty(tv_endarea.getText().toString()))
			{
				Toast.makeText(ToolBoxActivity.this, "请先选择目的地", 1000).show();;
				return;
			}
			Double distance= Utils.getDistance(m_start,m_end);
			Double km=(distance/1000)*1.3;
			tv_cesuan.setText(new java.text.DecimalFormat("#.00").format(km)+"km");
			
			break;
		case R.id.rl_startarea:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
			break;
		case R.id.rl_endarea:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2,"submit");
			break;

		default:
			break;
		}
		
	}
	
	
	private String startareaCode="", endareaCode="";
	String startlnt,startlat,endlnt,endlat;
	LatLng m_start, m_end;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = null;
			String info = "";
			if (msg.obj instanceof Bundle) {
				bundle = (Bundle) msg.obj;
				if (bundle != null)
					info = bundle.getString("cityName");
			} else {
				info = (String) msg.obj;
			}
			switch (msg.what) {
			case 1:
				startareaCode = bundle.getString("cityCode");
				startlnt = bundle.getString("DistrictLnt");
				startlat = bundle.getString("DistrictLat");
				if(TextUtils.isEmpty(startlnt))
				{
					return;
				}
				if(TextUtils.isEmpty(startlat))
				{
					return;
				}
				m_start=new LatLng( Double.parseDouble(startlat), Double.parseDouble(startlnt));
				tv_startarea.setText(info);
				break;
			case 2:
				endareaCode = bundle.getString("cityCode");
				endlnt = bundle.getString("DistrictLnt");
				endlat = bundle.getString("DistrictLat");
				if(TextUtils.isEmpty(endlnt))
				{
					return;
				}
				if(TextUtils.isEmpty(endlat))
				{
					return;
				}
				m_end=new LatLng( Double.parseDouble(endlat), Double.parseDouble(endlnt));
				tv_endarea.setText(info);
				break;
			default:
				break;
			}
		}

	};
	
}
