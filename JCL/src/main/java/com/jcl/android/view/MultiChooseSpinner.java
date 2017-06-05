package com.jcl.android.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.adapter.TagBaseAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MultiChooseSpinner extends LinearLayout {
    private Context context;
    private TextView titleText, contentText, lineView;
    private ImageView spinnerImage;
    private TagCloudLayout tagView;
    protected RelativeLayout line1Layout, contentLayout, line2Layout;
    private boolean isShowSpinner;
    private List<String> list;
    private TagBaseAdapter adapter;
    private List<Integer> selectPositions;
    private MultiChooseSpinnerConfig config;
    private final String CONTENT_SEPRITE = "/";
    private InputMethodManager inputMethodManager;

    public MultiChooseSpinner(Context context) {
        this(context, null);
    }

    public MultiChooseSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiChooseSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_multi_choose_spinner, this);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        config = new MultiChooseSpinnerConfig(context, attrs);
        initView(view);
        if (config != null) {
            setAttrs();
        }
//        initData();
        initListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams line1LayoutParams = (LayoutParams) line1Layout.getLayoutParams();
        line1LayoutParams.height = config.getLine1Height();
        line1Layout.setLayoutParams(line1LayoutParams);

        RelativeLayout.LayoutParams contentLayoutParams = (RelativeLayout.LayoutParams) contentLayout.getLayoutParams();
        contentLayoutParams.setMargins(config.getTitleWidth(), 0, getResources().getDimensionPixelOffset(R.dimen.item_margin_horizontal), 0);
        contentLayout.setLayoutParams(contentLayoutParams);

        RelativeLayout.LayoutParams lineViewParams = (RelativeLayout.LayoutParams) lineView.getLayoutParams();
        lineViewParams.setMargins(0, config.getContentLineMargin(), 0, 0);
        lineView.setLayoutParams(lineViewParams);
    }

    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    public List<Integer> getSelectItemPositions() {
        return selectPositions;
    }

    /**
     * 获取右侧显示的文本内容
     *
     * @return
     */
    public CharSequence getContentText() {
        return contentText.getText();
    }

    /**
     * 设置右侧显示的文本内容
     *
     * @param text
     */
    public void setContentText(String text) {
        if (text == null) {
            contentText.setText("");
        }
        contentText.setText(text);
//        invalidate();
    }

    /**
     * 获取左侧显示的文本内容
     *
     * @return
     */
    public CharSequence getTitleText() {
        return titleText.getText();
    }

    /**
     * 设置左侧显示的文本内容
     *
     * @param text
     */
    public void setTitleText(CharSequence text) {
        titleText.setText(text);
//        invalidate();
    }

    private void initData() {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (selectPositions == null) {
            selectPositions = new LinkedList<>();
        }
        if (adapter == null) {
            adapter = new TagBaseAdapter(context, list);
            tagView.setAdapter(adapter);
        }
    }

    private void setAttrs() {
        titleText.setVisibility(config.isTitleShow() ? VISIBLE : GONE);
        setTitleText(config.getTitleText());
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getTitleTextSize());
        titleText.setTextColor(config.getTitleTextColor());
        setContentText(config.getDefaultContent());
//        setContentText(config.getContentText());
        contentText.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getContentTextSize());
        contentText.setTextColor(config.getContentTextColor());
        lineView.setVisibility(config.isContentLineShow() ? VISIBLE : GONE);
        tagView.setMaxLength(config.getMaxLength());
    }

    private void initListener() {
        if (config.isLine2LayouAlwaystShow()) {
            line2Layout.setVisibility(VISIBLE);
        }
        contentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
                    requestFocus();
                }
                android.util.Log.i("TAG", "MultiChooseSpinner.onClick: 点击事件adapter=" + adapter);
                if (adapter != null && adapter.getCount() > 0) {
                    isShowSpinner = !isShowSpinner;
                    setSpinnerShow(isShowSpinner);
                } else {
                    if (TextUtils.isEmpty(config.getClickNullPrompt())) {
                        config.setClickNullPrompt("没有可选项");
                    }
                    Toast.makeText(context, config.getClickNullPrompt(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tagView.setItemSelectedListener(new TagCloudLayout.TagItemSelectedListener() {
            @Override
            public void itemSelected(RadioButton radioButton, Map<Integer, Boolean> itemSelection) {
                setSelectedPositions(itemSelection);
                if (selectListener != null) {
                    selectListener.onSelectListener(itemSelection, selectPositions);
                }
            }
        });
    }

    private String getContentFromList(List<Integer> positions) {
        Iterator<Integer> iterator = positions.iterator();
        StringBuffer content = new StringBuffer();
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                Integer position = iterator.next();
                String item = adapter.getItem(position);
                content.append(item);
                content.append(CONTENT_SEPRITE);
            }
            content.deleteCharAt(content.length() - 1);
        }
        return content.toString();
    }

    /**
     * 设置选中的项
     *
     * @param selects
     */
    public void setSelectedPositions(List<Integer> selects) {
        selectPositions.clear();
        if (selects == null) {
            return;
        }
        selectPositions.addAll(selects);
        String item = getContentFromList(selectPositions);
        setContentText(item);
//        for (int i = 0; i < selects.size(); i++) {
//            View view = adapter.getView(i, null, null);
//            Object tag = view.getTag();
//            Log.i("TAG", "MultiChooseSpinner.setSelectedPositions:1 获取到tag");
//            if(tag instanceof TagBaseAdapter.ViewHolder){
//                Log.i("TAG", "MultiChooseSpinner.setSelectedPositions:1 tag是ViewHolder");
//                TagBaseAdapter.ViewHolder holder = (TagBaseAdapter.ViewHolder)tag;
//                holder.tagBtn.setSelected(true);
//            }
//        }
        tagView.setItemSelection(selects);
//        if(adapter!=null){
//            adapter.notifyDataSetChanged();
//        }
    }


    private void setSelectedPositions(Map<Integer, Boolean> selects) {
        selectPositions.clear();
        if (selects == null) {
            setContentText(config.getDefaultContent());
            return;
        }
        selectPositions.addAll(getSelectPositions(selects));
        String item = getContentFromList(selectPositions);
        if (TextUtils.isEmpty(item)) {
            setContentText(config.getDefaultContent());
        } else {
            setContentText(item);
        }

//        Set<Integer> integers = selects.keySet();
//        Iterator<Integer> iterator = integers.iterator();
//        while (iterator.hasNext()){
//            Integer next = iterator.next();
//            if(selects.get(next)){
//                View view = adapter.getView(next, null, null);
//                Object tag = view.getTag();
//                Log.i("TAG", "MultiChooseSpinner.setSelectedPositions:2 获取到tag");
//                if(tag instanceof TagBaseAdapter.ViewHolder){
//                    Log.i("TAG", "MultiChooseSpinner.setSelectedPositions:2 tag是ViewHolder");
//                    TagBaseAdapter.ViewHolder holder = (TagBaseAdapter.ViewHolder)tag;
//                    holder.tagBtn.setSelected(true);
//                }
//            }
//        }

//        if(adapter!=null){
//            adapter.notifyDataSetChanged();
//        }
    }

//    public  Map<Integer, Boolean> getSelectPositions(List<? extends IGetMainKeyValue> selectedList) {
//        if (data == null || data.size() == 0) return null;
//        if (selectedList==null||selectedList.size()==0)return null;
//
//        Map<Object, Object> selectedMap = new HashMap<>();
//        for (IGetMainKeyValue onevalue : selectedList) {
//            Log.d(getClass().getName(), "getSelectPositions: onevalue"+ JsonUtil.toJson(onevalue));
//            selectedMap.put(onevalue.getMainKeyValue(), onevalue);
//        }
//        Map<Integer, Boolean> selectsBooleanMap = new HashMap<>();
//        for (int i = 0; i < data.size(); i++) {
//            IGetMainKeyValue dataItem = (IGetMainKeyValue) data.get(i);
//            Log.d(getClass().getName(), "getSelectPositions: dataItem"+ JsonUtil.toJson(dataItem));
//            boolean flag=selectedMap.containsKey(dataItem.getMainKeyValue());
//            Log.d(getClass().getName(), "getSelectPositions: flag"+ flag);
//            selectsBooleanMap.put(Integer.valueOf(i),Boolean.valueOf(flag));
//        }
//
//        return selectsBooleanMap;
//    }
//        if (data == null || data.size() == 0) return null;
//        if (selectedList==null||selectedList.size()==0)return null;
//
//        Map<Object, Object> selectedMap = new HashMap<>();
//        for (IGetMainKeyValue onevalue : selectedList) {
//            Log.d(getClass().getName(), "getSelectPositions: onevalue"+ JsonUtil.toJson(onevalue));
//            selectedMap.put(onevalue.getMainKeyValue(), onevalue);
//        }
//        List<Integer> selectsBooleanList = new ArrayList<>();
//        for (int i = 0; i < data.size(); i++) {
//            IGetMainKeyValue dataItem = (IGetMainKeyValue) data.get(i);
//            Log.d(getClass().getName(), "getSelectPositions: dataItem"+ JsonUtil.toJson(dataItem));
//            boolean flag=selectedMap.containsKey(dataItem.getMainKeyValue());
//            Log.d(getClass().getName(), "getSelectPositions: flag"+ flag);
//            if (flag){
//                selectsBooleanList.add(i);
//            }
//
//        }
//
//        return selectsBooleanList;
//    }

    private List<Integer> getSelectPositions(Map<Integer, Boolean> itemSelection) {
        Iterator<Integer> iterator = itemSelection.keySet().iterator();
        List<Integer> selects = new ArrayList<>();
        while (iterator.hasNext()) {
            Integer position = iterator.next();
            boolean isSelect = itemSelection.get(position);
            if (isSelect) {
                selects.add(position);
            }
        }
        return selects;
    }

    private void initView(View view) {
        titleText = (TextView) view.findViewById(R.id.title_text);
        contentText = (TextView) view.findViewById(R.id.content_text);
        spinnerImage = (ImageView) view.findViewById(R.id.spinner_image);
        line1Layout = (RelativeLayout) view.findViewById(R.id.line1_layout);
        tagView = (TagCloudLayout) view.findViewById(R.id.tag_view);
        line2Layout = (RelativeLayout) view.findViewById(R.id.line2_layout);
        lineView = (TextView) view.findViewById(R.id.line_content_text);
        contentLayout = (RelativeLayout) view.findViewById(R.id.line_content_layout);
    }

    /**
     * 是否显示右边的内容
     *
     * @param isShow
     */
    public void setContentLayoutShow(boolean isShow) {
        if (contentLayout != null) {
            contentLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 列表选项是否一直显示
     *
     * @param isAlwaysShow
     */
    public void setLine2LayoutAlwaysShow(boolean isAlwaysShow) {
        config.setLine2LayoutAlwaysShow(isAlwaysShow);
    }

    public void setSpinnerShow(boolean isShow) {
        if (config.isLine2LayouAlwaystShow()) {
            line2Layout.setVisibility(VISIBLE);
        } else {
            line2Layout.setVisibility(isShow ? VISIBLE : GONE);
        }
        if (config.isContentLineShow()) {
            lineView.setVisibility(isShow ? GONE : VISIBLE);
        } else {
            lineView.setVisibility(GONE);
        }
        invalidate();
    }


    private OnSelectListener selectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        selectListener = listener;
    }


    public interface OnSelectListener {
        void onSelectListener(Map<Integer, Boolean> selectionPositionMap, List<Integer> selectPositions);
    }
}
