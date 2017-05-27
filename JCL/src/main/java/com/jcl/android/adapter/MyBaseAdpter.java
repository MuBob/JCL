package com.jcl.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 抽象适配器（免去一些有共性的代码）
 * 
 * @author pang
 * @param <T>
 */

public abstract class MyBaseAdpter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> myList;

	public MyBaseAdpter(Context context, List<T> list) {
		this.mInflater = LayoutInflater.from(context);
		mContext = context;
		if (list == null) {
			myList = new ArrayList<T>();
		}
		myList = list;
	}

	public void addList(List<T> list) {
		myList.addAll(list);
	}

	public void setList(List<T> list) {
		myList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return myList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
