package com.jcl.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.jcl.android.R;

import java.util.List;

/**
 * 自定义控件TagCloudLayout的适配器
 * Created by Administrator on 2016/7/29.
 */
public class TagBaseAdapter extends BaseAdapter {

    protected Context mContext;
    protected List<String> mList;

    public TagBaseAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static final String TAG = "TagBaseAdapter";
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tag_cloud_view, parent);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tagBtn.setText(getItem(position));
        holder.tagBtn.setTag(position);
        return convertView;
    }

    public static class ViewHolder {
        public RadioButton tagBtn;

        public ViewHolder(View convertView) {
            tagBtn = (RadioButton) convertView.findViewById(R.id.tag_btn);
        }
    }
}
