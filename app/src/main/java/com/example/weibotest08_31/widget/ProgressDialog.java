package com.example.weibotest08_31.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.weibotest08_31.R;

/**
 * 等待进度条
 * 
 *
 */
public class ProgressDialog extends Dialog {

	private String msg;
	private TextView msgTv;
	private Context context;
	private View view;

	/**
	 * 默认请等待...
	 *
	 * @param context
	 */
	public ProgressDialog(Context context) {
		this(context, "请等待...");

	}

	/**
	 * 可添加提示
	 * 
	 * @param context
	 * @param msg
	 */
	public ProgressDialog(Context context, String msg) {
		this(context, msg, R.style.MyDialog);
	}

	public ProgressDialog(Context context, String msg, int theme) {
		super(context, theme);
		this.msg = msg;
		this.context = context;
		init();
	}

	public void init() {
		view = getLayoutInflater().inflate(R.layout.dlg_progress_waite, null);
		// Drawable myDrawable = context.getResources().getDrawable(R.drawable.eventdetails_bn_participate);
		// myDrawable.setAlpha(200);
		// view.setBackgroundDrawable(myDrawable);
		setContentView(view);
		// 点击空白处不消失
		this.setCanceledOnTouchOutside(false);

		findViews();
		initViews();
	}

	public void findViews() {
		msgTv = (TextView) findViewById(R.id.dpw_tv_msg);
	}

	public void initViews() {
		msgTv.setText(msg);
	}

	/**
	 * 修改提示语
	 * 
	 */
	public void setMsg(String msg) {
		this.msg = msg;
		msgTv.setText(msg);
	}

	/**
	 *设置返回键是否有用
	 */
	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
	}

}
