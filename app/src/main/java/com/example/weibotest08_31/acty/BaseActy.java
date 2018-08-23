package com.example.weibotest08_31.acty;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.WeiboApp;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.widget.TitleBar;

public class BaseActy extends Activity implements View.OnClickListener{

	protected TitleBar mTitleBar; 
	protected Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		//把所有页面放到application
		getWeiboApp().addActy((Activity)context);
		
	}
	
	protected DotNetManager getDotNetManager(){
		
		return getWeiboApp().getDotNetManager();
	}
	
	protected WeiboApp getWeiboApp(){
		return (WeiboApp)getApplication();
	}
	
	protected void initTitleBar(int titleId,String left,String right,String title) {
		mTitleBar=(TitleBar) findViewById(titleId);
		mTitleBar.titleTV.setText(title);
		if(TextUtils.isEmpty(left)){
			mTitleBar.leftBT.setVisibility(View.GONE);
		}else{
			mTitleBar.leftBT.setVisibility(View.VISIBLE);		
			mTitleBar.leftBT.setText(left);
			mTitleBar.leftBT.setOnClickListener(this);
		}
		if(TextUtils.isEmpty(right)){
			mTitleBar.rightBT.setVisibility(View.GONE);
		}else{
			mTitleBar.rightBT.setVisibility(View.VISIBLE);		
			mTitleBar.rightBT.setText(right);
			mTitleBar.rightBT.setOnClickListener(this);
		}
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.it_bt_left:
		case R.id.it_iv_left:
			finish();
			break;

		default:
			break;
		}
	}

}
