package com.jcl.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcl.android.R;
import com.jcl.android.utils.MyApplicationUtils;

public class MyUINavigationView extends RelativeLayout {

	private Button btn_right;
	private ImageButton btn_left;
	private TextView tv_title;
	private String strBtnLeft;
	private String strBtnRight;
	private String strTitle;
	private int left_drawable;
	private int right_drawable;
	private int title_background;
	private boolean leftVisible;
	private boolean rightVisible;
	private Button btn_right_special;

	public static final int RIGHT_ID = 1;
	public static final int LEFT_ID = 2;
	public static final int S_RIGHT_ID = 3;

	public MyUINavigationView(Context context) {
		super(context);
		initContent();
	}

	public MyUINavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(attrs);
		initContent();
	}

	private void initAttributes(AttributeSet attributeSet) {

		if (null != attributeSet) {

			final int attrIds[] = new int[] { R.attr.btn_leftText,
					R.attr.btn_rightText, R.attr.tv_title,
					R.attr.left_drawable, R.attr.right_drawable,
					R.attr.left_visible, R.attr.right_visible,R.attr.tv_title_background };

			Context context = getContext();

			TypedArray array = context.obtainStyledAttributes(attributeSet,
					attrIds);
			CharSequence t1 = array
					.getText(R.styleable.navigation_btn_leftText);
			CharSequence t2 = array
					.getText(R.styleable.navigation_btn_rightText);
			CharSequence t3 = array.getText(R.styleable.navigation_tv_title);
			left_drawable = array.getResourceId(
					R.styleable.navigation_left_drawable, 0);
			right_drawable = array.getResourceId(
					R.styleable.navigation_right_drawable, 0);
			leftVisible = array.getBoolean(R.styleable.navigation_left_visible,
					true);
			rightVisible = array.getBoolean(
					R.styleable.navigation_right_visible, true);
			title_background = array.getResourceId(R.styleable.navigation_tv_title_background, 0);
			array.recycle();

			if (null != t1) {
				strBtnLeft = t1.toString();
			}
			if (null != t2) {
				strBtnRight = t2.toString();

			}
			if (null != t3) {
				strTitle = t3.toString();
			}
		}

	}

	private void initContent() {

		// 设置水平方向
		// setOrientation(HORIZONTAL);
		// setGravity(Gravity.CENTER_VERTICAL);
		// 设置背景
		setBackgroundColor(0xf71384B0);
		// setPadding(AppUtils.DPtoPX(getContext(), 9), 0,
		// AppUtils.DPtoPX(getContext(), 11), 0);
		// setBackgroundColor(0Xffffffff);

		Context context = getContext();
		initBtnLeft(context);
		initTitle(context);
		initBtnRight(context);
		initSpeacialRight(context);
		initBottomLine(context);
	}

	private void initClickableTitle(OnClickListener onClick) {
		TextView tvClick = new TextView(getContext());
		LayoutParams centerParam = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		if (btn_right_special != null
				&& btn_right_special.getVisibility() == View.VISIBLE) {
			centerParam.addRule(RelativeLayout.LEFT_OF, S_RIGHT_ID);
		} else {
			centerParam.addRule(RelativeLayout.LEFT_OF, RIGHT_ID);
		}
		centerParam.addRule(RelativeLayout.RIGHT_OF, LEFT_ID);
		tvClick.setLayoutParams(centerParam);
		addView(tvClick);
		tvClick.setOnClickListener(onClick);
	}

	private void initBottomLine(Context context) {
		TextView tvline = new TextView(context);
		LayoutParams centerParam = new LayoutParams(LayoutParams.FILL_PARENT,
				MyApplicationUtils.DPtoPX(context, 1));
		centerParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tvline.setLayoutParams(centerParam);
		tvline.setBackgroundColor(getResources().getColor(R.color.view_bg));
		addView(tvline);
	}

	private void initSpeacialRight(Context context) {
		btn_right_special = new Button(context);
		btn_right_special.setId(S_RIGHT_ID);
		btn_right_special.setTextColor(Color.WHITE);// 字体颜色
		btn_right_special.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		btn_right_special.setGravity(Gravity.CENTER);
		// final int llSize=AppUtils.DPtoPX(context, 55);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.LEFT_OF, RIGHT_ID);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		if (null == strBtnRight) {
			lp.rightMargin = 0;// AppUtils.DPtoPX(context, 22);
		} else {
			lp.rightMargin = 0;// AppUtils.DPtoPX(context, 22);
		}
		lp.rightMargin = MyApplicationUtils.DPtoPX(context, 10);
		btn_right_special.setLayoutParams(lp);
		// 添加这个按钮
		addView(btn_right_special);
		btn_right_special.setVisibility(View.GONE);
	}

	private void initBtnRight(Context context) {
		if (null == strBtnRight) {
			initImageRight(context);
		} else {
			initTextRight(context);
		}

	}

	private void initTextRight(Context context) {
		btn_right = new Button(context);
		// btn_right.setVisibility(View.INVISIBLE);// 设置设置不可见
		// btn_right.setBackgroundResource(R.drawable.buttonbg);// 设置背景
		btn_right.setTextColor(getResources().getColor(
				R.color.nav_right_text_color_selector));// 字体颜色

		if (right_drawable != 0) {
			btn_right.setBackgroundResource(right_drawable);
		} else {
			btn_right.setBackgroundColor(0x00000000);// 设置背景
		}
		if (null != strBtnRight) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			btn_right.setId(RIGHT_ID);
			lp.rightMargin = MyApplicationUtils.DPtoPX(context, 11);
			btn_right.setLayoutParams(lp);
			btn_right.setText(strBtnRight);
			btn_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			// btn_left.setVisibility(View.VISIBLE);
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btn_right.setId(RIGHT_ID);
			btn_right.setLayoutParams(lp);
		}
		btn_right.setGravity(Gravity.CENTER);
		btn_right.setPadding(0, 0, 0, 0);
		// 添加这个按钮
		addView(btn_right);
		if (!rightVisible) {
			btn_right.setVisibility(View.INVISIBLE);
		}
	}

	private void initImageRight(Context context) {
		// ll_right=new LinearLayout(context);
		// final int llSize=AppUtils.DPtoPX(context, 55);
		// LayoutParams llRightLp=new LayoutParams(
		// llSize, llSize);
		// llRightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// ll_right.setId(RIGHT_ID);
		// ll_right.setLayoutParams(llRightLp);
		// ll_right.setGravity(Gravity.RIGHT|Gravity.CENTER);
		btn_right = new Button(context);
		if (right_drawable != 0) {
			btn_right.setBackgroundResource(right_drawable);
		}
		final int llSize = MyApplicationUtils.DPtoPX(context, 55);
		LayoutParams llRightLp = new LayoutParams(llSize, llSize);
		llRightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		btn_right.setId(RIGHT_ID);
		btn_right.setLayoutParams(llRightLp);
		btn_right.setGravity(Gravity.RIGHT | Gravity.CENTER);
		// lp.rightMargin=AppUtils.DPtoPX(context, 11);
		btn_right.setLayoutParams(llRightLp);
		// ll_right.addView(btn_right);
		// 添加这个按钮
		addView(btn_right);
		if (!rightVisible) {
			btn_right.setVisibility(View.INVISIBLE);
		}
	}

	private void initTitle(Context context) {
		tv_title = new TextView(context);

		LayoutParams centerParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// centerParam.gravity=Gravity.CENTER;
		// centerParam.weight=1;
		centerParam.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_title.setLayoutParams(centerParam);
		tv_title.setTextColor(Color.WHITE);
		if(title_background!=0){
			tv_title.setBackgroundResource(title_background);
		}
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

		if (null != strTitle) {
			tv_title.setText(strTitle);
		}

		tv_title.setGravity(Gravity.CENTER);
		// btn_left.setVisibility(View.VISIBLE);
		// 添加这个标题
		addView(tv_title);

	}

	private void initBtnLeft(Context context) {
		// ll_left=new LinearLayout(context);
		// final int llSize=AppUtils.DPtoPX(context, 55);
		// LayoutParams llLeftLp=new LayoutParams(
		// llSize, llSize);
		// llLeftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		// ll_left.setLayoutParams(llLeftLp);
		// ll_left.setGravity(Gravity.LEFT|Gravity.CENTER);
		btn_left = new ImageButton(context);
		btn_left.setBackgroundResource(getResources().getColor(
				R.color.transparent));
		btn_left.setId(LEFT_ID);
		// btn_left.setVisibility(View.INVISIBLE);// 设置设置不可见
		if (left_drawable != 0) {
			// btn_left.setBackgroundResource(left_drawable);
			btn_left.setImageResource(left_drawable);
			// btn_left.setBackground(null);
		}
		// else {
		//
		// // btn_left.setBackgroundResource(R.drawable.backbg);// 设置背景
		// }
		// btn_left.setTextColor(Color.WHITE);// 字体颜色

		if (null != strBtnLeft) {
			final int llSize = MyApplicationUtils.DPtoPX(context, 65);
			LayoutParams llLeftLp = new LayoutParams(llSize, llSize);
			llLeftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			btn_left.setLayoutParams(llLeftLp);
			// btn_left.setGravity(Gravity.LEFT | Gravity.CENTER);
			// lp.gravity=Gravity.LEFT|Gravity.CENTER;
			btn_left.setLayoutParams(llLeftLp);
			// btn_left.setText(strBtnLeft);
			// btn_left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			// btn_left.setVisibility(View.VISIBLE);
		} else {
			final int llSize = MyApplicationUtils.DPtoPX(context, 50);
			LayoutParams llLeftLp = new LayoutParams(llSize, llSize);
			llLeftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			btn_left.setLayoutParams(llLeftLp);
			// btn_left.setGravity(Gravity.LEFT | Gravity.CENTER);
			// lp.gravity=Gravity.LEFT|Gravity.CENTER;
			btn_left.setLayoutParams(llLeftLp);
		}
		// btn_left.setGravity(Gravity.CENTER);
		// btn_left.setPadding(0, 0, 0, 0);
		// ll_left.addView(btn_left);
		// 添加这个按钮
		addView(btn_left);
		if (!leftVisible) {
			btn_left.setVisibility(View.INVISIBLE);
		}
	}

	public ImageButton getBtn_left() {
		return btn_left;
	}

	public Button getBtn_right() {
		return btn_right;
	}

	public TextView getTv_title() {
		return tv_title;
	}

	public String getStrBtnLeft() {
		return strBtnLeft;
	}

	public void setStrBtnLeft(String strBtnLeft) {
		this.strBtnLeft = strBtnLeft;
	}

	public String getStrBtnRight() {
		return strBtnRight;
	}

	public void setStrBtnRight(String strBtnRight) {
		this.strBtnRight = strBtnRight;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		tv_title.setText(strTitle);
		this.strTitle = strTitle;
	}

	public int getLeft_drawable() {
		return left_drawable;
	}

	public void setLeft_drawable(int left_drawable) {
		this.left_drawable = left_drawable;
	}

	public int getRight_drawable() {
		return right_drawable;
	}

	public void setRight_drawable(int right_drawable) {
		this.right_drawable = right_drawable;
	}

	/**
	 * 设置右边特殊的按钮
	 * 
	 * @param text
	 * @param right_drawable
	 * @param onClick
	 */
	public void setSpeacialRight(String text, int right_drawable,
			OnClickListener onClick) {
		btn_right_special.setVisibility(View.VISIBLE);
		if (text != null) {
			btn_right_special.setText(text);
		}
		if (right_drawable != 0) {
			btn_right_special.setBackgroundResource(right_drawable);
		}
		if (onClick != null) {
			btn_right_special.setOnClickListener(onClick);
		}
	}

	public Button getBtn_right_special() {
		return btn_right_special;
	}

	public void setBtn_right_special(Button btn_right_special) {
		this.btn_right_special = btn_right_special;
	}

	public void setClickableTitle(OnClickListener onClick) {
		initClickableTitle(onClick);
	}
}