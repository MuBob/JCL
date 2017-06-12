package com.jcl.android.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.jcl.android.adapter.TagBaseAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/7/29.
 */
public class TagCloudLayout extends ViewGroup {
    public TagCloudLayout(Context context) {
        this(context, null);
    }

    public TagCloudLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TagCloudLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private int mLineSpacing;
    private int mTagSpacing;
    private TagBaseAdapter mAdapter;
    private TagItemClickListener mListener;
    private TagItemSelectedListener selectedListener;
    private DataChangeObserver mObserver;
    private Map<Integer, Boolean> mItemSelection;
    private int maxLength;
    private int selectNum;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TagCloudConfiguration config = new TagCloudConfiguration(context, attrs);
        mLineSpacing = config.getLineSpacing();
        mTagSpacing = config.getTagSpacing();
        mItemSelection = new HashMap<>();
        setMaxLength(config.getMaxSelectItems());
    }

    public void setItemSelection(List<Integer> selections) {
//        Log.i("TAG", "TagCloudLayout.setItemSelection:  adapter.size=" + mAdapter.getCount());
        initSelections();
        Set<Integer> integers = mItemSelection.keySet();
        Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            mItemSelection.put(next, false);
        }
        Iterator<Integer> iterator1 = selections.iterator();
        while (iterator1.hasNext()) {
            mItemSelection.put(iterator1.next(), true);
        }
        drawLayout();
    }

    private static final String TAG = "TagCloudLayout";
    private void drawLayout() {
//        Log.i("TAG", "TagCloudLayout.drawLayout: childCountBefore=" + getChildCount());
        this.removeAllViews();
//        Log.i("TAG", "TagCloudLayout.drawLayout: childCountAfter=" + getChildCount());
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

//        Log.i("TAG", "TagCloudLayout.drawLayout: adapter.size=" + mAdapter.getCount());

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, null);
            final int position = i;
            if (view.getTag() instanceof TagBaseAdapter.ViewHolder) {

                TagBaseAdapter.ViewHolder holder = (TagBaseAdapter.ViewHolder) view.getTag();
//                mItemSelection.put(position, false);
                RadioButton radioButton = holder.tagBtn;
//                radioButton.setTag(false);
//                Log.i("TAG", "TagCloudLayout.drawLayout: radioButton="+radioButton);
//                Log.i("TAG", "TagCloudLayout.drawLayout: mItemSelection=" + mItemSelection + "position=" + position);
                Log.i("TAG", "TagCloudLayout.drawLayout: selection=" + mItemSelection + ", position=" + position);
                if (mItemSelection.containsKey(position)) {
                    boolean selected = mItemSelection.get(position);
                    radioButton.setSelected(selected);
                    if (selected) {
                        ++selectNum;
                    }
                }
//                Log.i("TAG", "TagCloudLayout.drawLayout: 允许选择最大长度"+maxLength+", 当前以选中"+selectNum);
                radioButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton radioButton = (RadioButton) view;
                        int position = (int) radioButton.getTag();
                        Log.i("TAG", "TagCloudLayout.onClick: radioButton.tag=" + position);
                        boolean isSelect;
                        if (mItemSelection.containsKey(position)) {
                            isSelect = !mItemSelection.get(position);
                            Log.i(TAG, "TagCloudLayout.onClick: p="+position+", select="+isSelect);
                            if (isSelect && selectNum >= maxLength) {
                                return;
                            } else if (!isSelect && selectNum < 0) {
                                return;
                            }
                        }else {
                            isSelect=true;
                        }
                        mItemSelection.put(position, isSelect);
                        radioButton.setSelected(isSelect);
                        Log.i(TAG, "TagCloudLayout.onClick: radio.select?="+radioButton.isSelected());
                        if (isSelect) {
                            ++selectNum;
                        } else {
                            --selectNum;
                        }
                        if (selectedListener != null) {
                            selectedListener.itemSelected(radioButton, mItemSelection);
                        }
                    }
                });
            }
            this.addView(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wantHeight = 0;
        int wantWidth = resolveSize(0, widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;
        //固定列的数量所需要的代码
//        Log.i("TAG", "TagCloudLayout.onMeasure: count="+getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            LayoutParams params = childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, params.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, params.height)
            );
            int childHeight = childView.getMeasuredHeight();
            int childWidth = childView.getMeasuredWidth();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > wantWidth) {
                childLeft = paddingLeft;
                childTop += mLineSpacing + childHeight;
                lineHeight = childHeight;
            }

            childLeft += childWidth + mTagSpacing;
        }
        wantHeight += childTop + lineHeight + paddingBottom;
        setMeasuredDimension(wantWidth, resolveSize(wantHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //固定列的数量所需要的代码

        int width = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;
//        Log.i("TAG", "TagCloudLayout.onLayout: count="+getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > width) {
                childLeft = paddingLeft;
                childTop += mLineSpacing + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mTagSpacing;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    public void setAdapter(TagBaseAdapter adapter) {
//        if (mAdapter == null){
        mAdapter = adapter;
        if (mObserver == null) {
            mObserver = new DataChangeObserver();
            mAdapter.registerDataSetObserver(mObserver);
        }
        drawLayout();
//        }
    }

    public TagBaseAdapter getAdapter() {
        return mAdapter;
    }

    private void initSelections() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, null);
            if (view.getTag() instanceof TagBaseAdapter.ViewHolder) {
                TagBaseAdapter.ViewHolder holder = (TagBaseAdapter.ViewHolder) view.getTag();
                mItemSelection.put(i, false);
                holder.tagBtn.setTag(i);
            }
        }
    }

    public void setItemClickListener(TagItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setItemSelectedListener(TagItemSelectedListener listener) {
        this.selectedListener = listener;
    }

    public interface TagItemClickListener {
        void itemClick(int position);

    }

    public interface TagItemSelectedListener {
        void itemSelected(RadioButton radioButton, Map<Integer, Boolean> itemSelection);
    }

    class DataChangeObserver extends DataSetObserver {
        @Override
        public void onChanged() {
//            Log.i("TAG", "DataChangeObserver.onChanged: adapter.size=" + mAdapter.getCount());
            if (mAdapter != null && mAdapter.getCount() > 0) {
                initSelections();
            }
            TagCloudLayout.this.drawLayout();
//            onInvalidated();
        }

        @Override
        public void onInvalidated() {
//            Log.i("TAG", "DataChangeObserver.onInvalidated: adapter.size="+mAdapter.getCount());
//            TagCloudLayout.this.invalidate();
        }
    }

}
