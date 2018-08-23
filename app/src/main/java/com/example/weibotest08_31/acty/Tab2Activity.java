package com.example.weibotest08_31.acty;
//我的关注列表，添加关注，取消关注
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.UserAdapter;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab2Activity extends BaseActy {

	private TitleBar titleBar;
	private DotNetManager dotNetManager;
	private final int GET_DETIAL_OK = 1;
	private final int GET_DETIAL_FAIL = 2;
	private final int GET_USER_OK = 3;
	private final int GET_USER_FAIL = 4;
	private EditText namesearchET;
	private UserAdapter caresAdapter;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GET_DETIAL_OK:
					MyToast.showToast(Tab2Activity.this,"该用户存在");
					Intent intent=new Intent(Tab2Activity.this,AddCaresActy.class);
					intent.putExtra("userId",namesearchET.getText().toString());
					startActivity(intent);
					break;
				case GET_DETIAL_FAIL:
					MyToast.showToast(Tab2Activity.this,msg.obj+"");

					break;
				case GET_USER_OK:
					List<User> userlist= (List<User>) msg.obj;
					caresAdapter.setUserList(userlist);
					caresAdapter.notifyDataSetChanged();
					break;
				case GET_USER_FAIL:
					break;

				default:
					break;
			}
			super.handleMessage(msg);
            ProgressDialogManager.stopProgressBar();
		}
	};
	private ListView userlV;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab2);
		initVar();
		clickItem();
		requestDatas();

	}

	private void clickItem() {
		userlV=(ListView)findViewById(R.id.at2_lv_listview);
		caresAdapter=new UserAdapter(Tab2Activity.this);
		userlV.setAdapter(caresAdapter);
		userlV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User clickuser=(User)caresAdapter.getItem(position);
				Intent intent=new Intent(Tab2Activity.this,UserDetialsActy.class);
				intent.putExtra("userId",clickuser.getId());
				startActivity(intent);
			}
		});
	}

	private void requestDatas() {
		ProgressDialogManager.startProgressBar(Tab2Activity.this,"加载中");
		new Thread(new Runnable() {
			@Override
			public void run() {

				String backStr=dotNetManager.getCaresList(getWeiboApp().getUser().getId()+"");
				try {
					List<User> userlist=new ArrayList<User>();
					JSONObject jsonObject=new JSONObject(backStr);
					if("0".equals(jsonObject.getString("result"))){
						JSONArray jsonArray=jsonObject.getJSONArray("caresList");
						JSONObject jouser;
						User user;
						for(int i=0;i<jsonArray.length();i++) {
							jouser =jsonArray.getJSONObject(i);
							user=Constant.gson.fromJson(jouser.toString(), User.class);
							userlist.add(user);
						}
						Message msg = Message.obtain();
						msg.what = GET_USER_OK;
						msg.obj = userlist;
						handler.sendMessage(msg);
					} else {
						String msg = jsonObject.getString("message");
						Message msg1 = Message.obtain();
						msg1.what = GET_USER_FAIL;
						msg1.obj = msg;
						handler.sendMessage(msg1);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg1 = Message.obtain();
					msg1.what = GET_USER_FAIL;
					msg1.obj = "获取失败";
					handler.sendMessage(msg1);
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void initVar() {
		initTitleBar(R.id.at2_tb_title, null, null, "关注列表");
		titleBar=(TitleBar) findViewById(R.id.at2_tb_title);
		titleBar.leftIV.setVisibility(View.GONE);
		dotNetManager=getDotNetManager();
		findViewById(R.id.at2_iv_search).setOnClickListener(this);
		namesearchET=(EditText)findViewById(R.id.at2_et_search);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.at2_iv_search:
				new Thread(new Runnable() {
					@Override
					public void run() {
						String backStr=dotNetManager.isUserExist(namesearchET.getText().toString());
						try {
							JSONObject jsonObject=new JSONObject(backStr);
							if("0".equals(jsonObject.getString("result"))){
								if("1".equals(jsonObject.getString("isExist"))){
									Message msg = Message.obtain();
									msg.what = GET_DETIAL_OK;
									handler.sendMessage(msg);
								}else{
									Message msg = Message.obtain();
									msg.what = GET_DETIAL_FAIL;
									msg.obj = "不存在";
									handler.sendMessage(msg);
								}
							}else{
								String msg = jsonObject.getString("message");
								Message msg1 = Message.obtain();
								msg1.what = GET_DETIAL_FAIL;
								msg1.obj = msg;
								handler.sendMessage(msg1);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();

				break;
		}
		super.onClick(v);
	}

	

	
}
