package com.example.weibotest08_31.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weibotest08_31.R;

public class MyDialog extends Dialog {

	private TextView titletv;
	private TextView tipstv;
	private Button positivebt;
	private Button negativebt;

	public MyDialog(Context context) {
		super(context,R.style.MyDialog);
		setContentView(R.layout.dialog_my);

		titletv = (TextView) findViewById(R.id.dm_tv_title);
		tipstv = (TextView) findViewById(R.id.dm_tv_tips);
		positivebt = (Button) findViewById(R.id.dm_bt_positive);
		negativebt = (Button) findViewById(R.id.dm_bt_negative);

		positivebt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Log.e("","----dismissִ执行");
			}
		});
		negativebt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Log.e("","----dismissִ执行");
			}
		});
	}

	public void setOnPositiveClick(String po,
			View.OnClickListener onClickListener) {
		positivebt.setText(po);
		Log.e("","----setOnClickListener进入");
		positivebt.setOnClickListener(onClickListener);
		Log.e("","----setOnClickListenerִ结束");
	}

	public void setOnNegativeClick(String na,
			View.OnClickListener onClickListener) {
		if (TextUtils.isEmpty(na)) {
			negativebt.setVisibility(View.GONE);
		} else {
			negativebt.setText(na);
			negativebt.setOnClickListener(onClickListener);
		}
	}
	public void setTitle(String title){
		titletv.setText(title);
	}
	public void setTips(String tips){
		tipstv.setText(tips);
	}
}
