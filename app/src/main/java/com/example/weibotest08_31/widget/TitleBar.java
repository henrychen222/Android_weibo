package com.example.weibotest08_31.widget;

import com.example.weibotest08_31.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBar extends RelativeLayout {
	public Button leftBT;
	public Button rightBT;
	public ImageView leftIV;
	public ImageView rightIV;
	public TextView titleTV;

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.include_titlebar, this);
		rightBT = (Button)this.findViewById(R.id.it_bt_right);
		leftBT = (Button)this.findViewById(R.id.it_bt_left);
		titleTV = (TextView) this.findViewById(R.id.it_tv_title);
		leftIV = (ImageView) this.findViewById(R.id.it_iv_left);
		rightIV = (ImageView) this.findViewById(R.id.it_iv_right);
	}
//	private void setRightImage(int resid) {
//		rightIV.setBackgroundResource(resid);
//	}
//	
	
}
