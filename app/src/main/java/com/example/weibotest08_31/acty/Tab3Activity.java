package com.example.weibotest08_31.acty;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.LeaveMsgAdapter;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.LeaveMsgList;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab3Activity extends BaseActy {
	private DotNetManager dotNetManager;
	private final int GET_LEAVEMSG_OK = 1;
	private final int GET_LEAVEMSG_FAIL = 2;
	private ListView leaveMsglV;
	private TitleBar titleBar;
	List<LeaveMsgList> userlist;
	private LeaveMsgAdapter leaveMsgAdapter;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GET_LEAVEMSG_OK:
					userlist= (List<LeaveMsgList>) msg.obj;
					leaveMsgAdapter.setLeaveMsgLists(userlist);
					leaveMsgAdapter.notifyDataSetChanged();
					break;
				case GET_LEAVEMSG_FAIL:

					break;


				default:
					break;
			}
			super.handleMessage(msg);
			ProgressDialogManager.stopProgressBar();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab1);
		initVar();
		clickItem();
		requestDatas();
	}

	private void requestDatas() {
		ProgressDialogManager.startProgressBar(Tab3Activity.this, "加载中");
		new Thread(new Runnable() {
			@Override
			public void run() {
				LeaveMsgList leaveMsgList;
				String backStr=dotNetManager.getLeaveMsgList(getWeiboApp().getUser().getId() + "", 20+"",0+"");
				try {
					List<LeaveMsgList> msglist=new ArrayList<LeaveMsgList>();
					JSONObject jsonObject=new JSONObject(backStr);
					if("0".equals(jsonObject.getString("result"))){
						JSONArray jsonArray=jsonObject.getJSONArray("leaveMsgList");
						JSONObject jouser;
						LeaveMsgList leaveMsgList1;
						for(int i=0;i<jsonArray.length();i++) {
							jouser =jsonArray.getJSONObject(i);
							leaveMsgList1= Constant.gson.fromJson(jouser.toString(), LeaveMsgList.class);
							msglist.add(leaveMsgList1);
						}
						Message msg = Message.obtain();
						msg.what = GET_LEAVEMSG_OK;
						msg.obj = msglist;
						handler.sendMessage(msg);
					} else {
						String msg = jsonObject.getString("message");
						Message msg1 = Message.obtain();
						msg1.what = GET_LEAVEMSG_FAIL;
						msg1.obj = msg;
						handler.sendMessage(msg1);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg1 = Message.obtain();
					msg1.what = GET_LEAVEMSG_FAIL;
					msg1.obj = "获取失败";
					handler.sendMessage(msg1);
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void clickItem() {
		leaveMsglV= (ListView) findViewById(R.id.at_lv_listview);
		leaveMsgAdapter=new LeaveMsgAdapter(Tab3Activity.this);
		leaveMsglV.setAdapter(leaveMsgAdapter);
		leaveMsglV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						dotNetManager.updateLeaveMsgHasRead(getWeiboApp().getUser().getId()+"",userlist.get(position).getId()+"");
					}
				}).start();
				LeaveMsgList leaveMsgList=userlist.get(position);
				Intent intent=new Intent(Tab3Activity.this,LeaveMsgActy.class);
				intent.putExtra("userId",leaveMsgList.getFromUserId());
				startActivity(intent);
			}
		});
	}

	private void initVar() {
		initTitleBar(R.id.at_tb_title, null, null, "留言列表");
		titleBar=(TitleBar) findViewById(R.id.at_tb_title);
		titleBar.leftIV.setVisibility(View.GONE);
		dotNetManager=getDotNetManager();


	}
	

	
}
