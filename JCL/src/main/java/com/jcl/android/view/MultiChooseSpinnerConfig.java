package com.jcl.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.jcl.android.R;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MultiChooseSpinnerConfig {
    private boolean titleShow;
    private int line1Height;
    private int titleWidth;
    private int titleLeftMargin;
    private int titleRightMargin;
    private float titleTextSize;
    private float contentTextSize;
    private int titleTextColor;
    private int contentTextColor;
    private String titleText;
    private String contentText;
    private int contentLineMargin;
    private int maxLength;
    private String defaultContent;
    private boolean contentLineShow;  //内容显示
    private boolean line2LayoutAlwaysShow;  //多选列表一直显示
    private String clickNullPrompt;  //数据为空时显示的提示语

    public MultiChooseSpinnerConfig(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiChooseSpinner);
        try {
            titleShow=a.getBoolean(R.styleable.MultiChooseSpinner_titleShow_MultiChooseSpinner, true);
            line1Height=a.getLayoutDimension(R.styleable.MultiChooseSpinner_line1Height_MultiChooseSpinner, context.getResources().getDimensionPixelOffset(R.dimen.line1_height));
            titleWidth=a.getLayoutDimension(R.styleable.MultiChooseSpinner_titleWidth_MultiChooseSpinner, context.getResources().getDimensionPixelOffset(R.dimen.content_margin_left));
            titleLeftMargin=a.getDimensionPixelOffset(R.styleable.MultiChooseSpinner_titleLeftMargin_MultiChooseSpinner, context.getResources().getDimensionPixelOffset(R.dimen.item_margin_horizontal));
            titleRightMargin=a.getDimensionPixelOffset(R.styleable.MultiChooseSpinner_titleRightMargin_MultiChooseSpinner, context.getResources().getDimensionPixelOffset(R.dimen.item_margin_horizontal));
            titleTextSize=a.getDimensionPixelSize(R.styleable.MultiChooseSpinner_titleTextSize_MultiChooseSpinner, context.getResources().getDimensionPixelSize(R.dimen.title_text_size));
            contentTextSize=a.getDimensionPixelSize(R.styleable.MultiChooseSpinner_contentTextSize_MultiChooseSpinner, context.getResources().getDimensionPixelSize(R.dimen.content_text_size));
            titleTextColor=a.getColor(R.styleable.MultiChooseSpinner_titleTextColor_MultiChooseSpinner, context.getResources().getColor(R.color.title_text_color));
            contentTextColor=a.getColor(R.styleable.MultiChooseSpinner_contentTextColor_MultiChooseSpinner, context.getResources().getColor(R.color.content_text_color));
            titleText=a.getString(R.styleable.MultiChooseSpinner_titleText_MultiChooseSpinner);
            contentText=a.getString(R.styleable.MultiChooseSpinner_contentText_MultiChooseSpinner);
            contentLineMargin=a.getDimensionPixelOffset(R.styleable.MultiChooseSpinner_contentLineMargin_MultiChooseSpinner, context.getResources().getDimensionPixelOffset(R.dimen.content_line_margin));
            contentLineShow=a.getBoolean(R.styleable.MultiChooseSpinner_contentLineShow_MultiChooseSpinner, true);
            maxLength=a.getInteger(R.styleable.MultiChooseSpinner_maxLength_MultiChooseSpinner, context.getResources().getInteger(R.integer.max_length));
            line2LayoutAlwaysShow=a.getBoolean(R.styleable.MultiChooseSpinner_line2LayoutAlwaysShow_MultiChooseSpinner, false);
            setClickNullPrompt(a.getString(R.styleable.MultiChooseSpinner_clickNullPrompt_MultiChooseSpinner));
            setDefaultContent(a.getString(R.styleable.MultiChooseSpinner_defaultContentText_MultiChooseSpinner));
        } finally {
            a.recycle();
        }
    }

    public boolean isLine2LayouAlwaystShow() {
        return line2LayoutAlwaysShow;
    }

    public void setLine2LayoutAlwaysShow(boolean line2LayoutAlwaysShow) {
        this.line2LayoutAlwaysShow = line2LayoutAlwaysShow;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getContentLineMargin() {
        return contentLineMargin;
    }

    public void setContentLineMargin(int contentLineMargin) {
        this.contentLineMargin = contentLineMargin;
    }

    public boolean isContentLineShow() {
        return contentLineShow;
    }

    public void setContentLineShow(boolean contentLineShow) {
        this.contentLineShow = contentLineShow;
    }

    public boolean isTitleShow() {
        return titleShow;
    }

    public void setTitleShow(boolean titleShow) {
        this.titleShow = titleShow;
    }

    public int getLine1Height() {
        return line1Height;
    }

    public void setLine1Height(int line1Height) {
        this.line1Height = line1Height;
    }

    public int getTitleWidth() {
        return titleWidth;
    }

    public void setTitleWidth(int titleWidth) {
        this.titleWidth = titleWidth;
    }

    public int getTitleLeftMargin() {
        return titleLeftMargin;
    }

    public void setTitleLeftMargin(int titleLeftMargin) {
        this.titleLeftMargin = titleLeftMargin;
    }

    public int getTitleRightMargin() {
        return titleRightMargin;
    }

    public void setTitleRightMargin(int titleRightMargin) {
        this.titleRightMargin = titleRightMargin;
    }

    public float getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public float getContentTextSize() {
        return contentTextSize;
    }

    public void setContentTextSize(float contentTextSize) {
        this.contentTextSize = contentTextSize;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public int getContentTextColor() {
        return contentTextColor;
    }

    public void setContentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }


    public boolean isLine2LayoutAlwaysShow() {
        return line2LayoutAlwaysShow;
    }

    public String getClickNullPrompt() {
        return clickNullPrompt;
    }

    public void setClickNullPrompt(String clickNullPrompt) {
        this.clickNullPrompt = clickNullPrompt;
    }

}
