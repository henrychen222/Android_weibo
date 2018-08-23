package com.example.weibotest08_31.acty;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.weibotest08_31.R;

public class MainTabActy extends TabActivity implements View.OnClickListener {


	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_maintab);
		Intent intent1 = new Intent(this, Tab1Activity.class);
		Intent intent2 = new Intent(this, Tab2Activity.class);
		Intent intent3 = new Intent(this, Tab3Activity.class);
		Intent intent4 = new Intent(this, Tab4Activity.class);
		tabHost = getTabHost();

		TabSpec tabSpec = tabHost.newTabSpec("tab1").setIndicator("tab1")
				.setContent(intent1);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tab2").setIndicator("tab2")
				.setContent(intent2);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(intent3);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tab4").setIndicator("tab4")
				.setContent(intent4);
		tabHost.addTab(tabSpec);
		findViewById(R.id.one).setOnClickListener(this);
		findViewById(R.id.two).setOnClickListener(this);
		findViewById(R.id.three).setOnClickListener(this);
		findViewById(R.id.four).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.one:
			tabHost.setCurrentTab(0);
			break;
		case R.id.two:
			tabHost.setCurrentTab(1);
			break;
		case R.id.three:
			tabHost.setCurrentTab(2);
			break;
		case R.id.four:
			tabHost.setCurrentTab(3);
			break;

		default:
			break;
		}
	}

}
