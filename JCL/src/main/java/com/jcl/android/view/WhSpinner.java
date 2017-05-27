package com.jcl.android.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcl.android.R;

public class WhSpinner extends LinearLayout {

	private String title = "选择";
	private Builder builder = null;
	private List<Item> items;
	private int checkItem = -1;

	private WhSpinnerOnCheckedListener whSpinnerOnCheckedListener;
	private WhSpinnerOnItemChangedListener whSpinnerOnItemChangedListener;
	/**
	 * 设置弹窗标题
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public void setValue(String date) {
		btn_left.setText(date);
	}
	/**
	 * 设置选项
	 * @param items
	 */
	public void setItems(List<Item> items, int check) {
		this.items = items;
		if (this.items != null &&  check < this.items.size()) {
			checkItem = check;
			btn_left.setText(items.get(check).key);
		} else {
			this.items = new ArrayList<WhSpinner.Item>();
		}
		strings = new String[items.size()];
		int idx = 0;
		for (Item i : items) {
			strings[idx++] = i.key;
		}
	}
	/**
	 * 设置选项
	 * @param items
	 */
	public void setItems(List<Item> items) {
		this.items = items;
		if (this.items == null) {
			this.items = new ArrayList<WhSpinner.Item>();
		}
		strings = new String[items.size()];
		int idx = 0;
		for (Item i : items) {
			strings[idx++] = i.key;
		}
	}
	String[] strings = new String[] {};
	/**
	 * 设置选择监听器
	 * 
	 * @param whSpinnerOnCheckedListener
	 */
	public void setWhSpinnerOnCheckedListener(

	WhSpinnerOnCheckedListener whSpinnerOnCheckedListener) {

		this.whSpinnerOnCheckedListener = whSpinnerOnCheckedListener;

	}
	
	/**
	 * 设置选择监听器
	 * 
	 * @param whSpinnerOnItemChangedListener
	 */
	public void setWhSpinnerOnItemChangedListener(

			WhSpinnerOnItemChangedListener whSpinnerOnItemChangedListener) {

		this.whSpinnerOnItemChangedListener = whSpinnerOnItemChangedListener;

	}

	private Button btn_left;
	private ImageView btn_right;

	public WhSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_spinner, this);
		btn_left = (Button) findViewById(R.id.btn_spinner_left);
		btn_right = (ImageView) findViewById(R.id.btn_spinner_right);
		btn_left.setOnClickListener(clickListener);
		btn_right.setOnClickListener(clickListener);
	}

	public void closeOnClick(){
		btn_left.setOnClickListener(null);
		btn_right.setOnClickListener(null);
	}
	public void openOnClick(){
		btn_left.setOnClickListener(clickListener);
		btn_right.setOnClickListener(clickListener);
	}
	public WhSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Item getChoiceItem() {
		if (items != null)
			return items.get(checkItem);
		else {
			return new Item(btn_left.getText().toString(), btn_left.getText()
					.toString());
		}
	}
	public void setChoiceText(String str)
	{
		if(checkItem==-1){
			btn_left.setText("");
		}
		btn_left.setText(str);
	}

	public String getChoiceText() {
//		if(checkItem==-1){
//			return "";
//		}
//		if (items != null)
//			return items.get(checkItem).key;
//		else
		if(TextUtils.isEmpty(btn_left.getText())){
			return "";
		}else{
			return btn_left.getText().toString();
		}
			
	}

	
	public Object getChoiceValue() {
//		if(checkItem==-1){
//			return -1;
//		}
		if (items != null){
			if(checkItem<0){
				return "";
			}else{
				return items.get(checkItem).val;
			}
			
		}
		else{
			
			return btn_left.getText().toString();
		}
	}

	public int getChoicePosition() {
		return checkItem;
	}
	
	public void setChoicePosition(String str){
		checkItem=Integer.parseInt(str);
	}

	boolean isList = true;

	public void setType(boolean isList) {
		this.isList = isList;
	}

	private void showView() {

		if (builder == null) {
			builder = new AlertDialog.Builder(getContext());
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(strings, checkItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								checkItem = item;
								btn_left.setText(items.get(checkItem).key);
								if (whSpinnerOnCheckedListener != null) {
									whSpinnerOnCheckedListener.onChecked(
											WhSpinner.this, checkItem);
								}
								if (whSpinnerOnItemChangedListener != null) {
									whSpinnerOnItemChangedListener.onItemChanged(WhSpinner.this, item);
								}
								dialog.dismiss();
							}
						}).show();
	}

	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (isList) {
				showView();
			}
		}
	};

	public static class Item implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1174195078694429669L;
		public String key;
		public Object val;

		@Override
		public String toString() {

			return key;
		}

		public Item(String key, Object val) {
			super();
			this.key = key;
			this.val = val;
		}
	}
}
