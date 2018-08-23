package com.example.weibotest08_31.acty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.WeiboAdapter;
import com.example.weibotest08_31.adapter.WeiboAdapter.OnViewClickListener;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.item.Weibo;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;

public class Tab1Activity extends BaseActy {

	private ListView weibLV;
	private WeiboAdapter weiboAdapter;
	private DotNetManager dotNetManager;
	private final int GET_WEIBO_OK = 1;
	private final int GET_WEIBO_FAIL = 2;
	private final int PRAISE_OK = 3;
	private final int PRAISE_FAIL = 4;
	private List<Weibo> weiboList=new ArrayList<Weibo>();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_WEIBO_OK:
				weiboList = (List<Weibo>) msg.obj;
				weiboAdapter.setWeiboList(weiboList);
				weiboAdapter.notifyDataSetChanged();
				break;
			case GET_WEIBO_FAIL:
				MyToast.showToast(Tab1Activity.this,msg.obj+"");
				break;
			case PRAISE_OK:
				weiboAdapter.notifyDataSetChanged();
				break;
			case PRAISE_FAIL:
				MyToast.showToast(Tab1Activity.this,"网络异常");
				break;

			default:
				break;
			}
			super.handleMessage(msg);
			ProgressDialogManager.stopProgressBar();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab1);

		initVar();

		findView();

		bindViews();

		requestData();

	}

	private void initVar() {

		dotNetManager = getDotNetManager();
	}

	private void findView() {
		initTitleBar(R.id.at_tb_title, null, "发微博", "我的微博");
		weibLV = (ListView) findViewById(R.id.at_lv_listview);
		findViewById(R.id.it_iv_left).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.it_bt_right:
			Intent intent = new Intent(this, SendWeiboActy.class);
			startActivity(intent);
			return;
		case R.id.it_iv_left:
			finish();
			return;
		default:
			break;
		}
		super.onClick(v);

	}

	private void bindViews() {
		weiboAdapter = new WeiboAdapter(context);
		weibLV.setAdapter(weiboAdapter);
		weibLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Weibo weibo=weiboList.get(position);
				Intent intent=new Intent(Tab1Activity.this,WeiboDetialActy.class);
				intent.putExtra("weibo",weibo);
				startActivity(intent);
			}
		});
		weiboAdapter.setOnViewClickListener(new OnViewClickListener() {

			@Override
			public void OnViewClick(View v, int position) {
				final Weibo weibo = (Weibo) weiboAdapter.getItem(position);

				switch (v.getId()) {
					case R.id.iw_tv_zan:
						MyLog.e("", "赞----->" + position);
						if ("0".equals(weibo.getHasPraise())) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									String backStr=dotNetManager.praiseWeibo(getWeiboApp().getUser().getId() + "", weibo.getId(), weibo.getHasPraise());
									try {
										JSONObject jsonObject=new JSONObject(backStr);
										if("0".equals(jsonObject.getString("result"))){
											weibo.setHasPraise("1");
											int praiseNum = Integer.valueOf(weibo.getPraiseNum());
											weibo.setPraiseNum((praiseNum + 1));
											handler.sendEmptyMessage(PRAISE_OK);
										}else{
										handler.sendEmptyMessage(PRAISE_FAIL);
										}
									} catch (JSONException e) {
										e.printStackTrace();
										handler.sendEmptyMessage(PRAISE_FAIL);
									}
								}
							}).start();

						} else {

							new Thread(new Runnable() {
								@Override
								public void run() {
									String backStr=dotNetManager.praiseWeibo(getWeiboApp().getUser().getId() + "", weibo.getId(), weibo.getHasPraise());
									try {
										JSONObject jsonObject=new JSONObject(backStr);
										if("0".equals(jsonObject.getString("result"))){
											weibo.setHasPraise("0");
											int praiseNum = Integer.valueOf(weibo.getPraiseNum());
											weibo.setPraiseNum((praiseNum - 1));
											handler.sendEmptyMessage(PRAISE_OK);
										}
										else{
											handler.sendEmptyMessage(PRAISE_FAIL);
										}
									} catch (JSONException e) {
										e.printStackTrace();
										handler.sendEmptyMessage(PRAISE_FAIL);
									}

								}
							}).start();

						}
						break;
					case R.id.iw_tv_commot:
						MyLog.e("", "评论了----->" + position);
						Intent intent=new Intent(Tab1Activity.this,AddCommentsActy.class);
						intent.putExtra("weiboId",weibo.getId());
						startActivity(intent);
						break;

					default:
						break;
				}
			}
		});
	}

	private void requestData() {
		ProgressDialogManager.startProgressBar(context, "请求中---");
		new Thread(new Runnable() {

			@Override
			public void run() {
				User user = getWeiboApp().getUser();
				String backStr = dotNetManager.getWeiboList(user.getId() + "",
						"20", "0", "0");
				try {
					List<Weibo> weiboList = new ArrayList<Weibo>();

					JSONObject jsobj = new JSONObject(backStr);
					if ("0".equals(jsobj.get("result"))) {
						JSONArray jsarray = jsobj.getJSONArray("fileList");
						JSONObject weiboObj;
						Weibo weibo;
						for (int i = 0; i < jsarray.length(); i++) {
							weiboObj = jsarray.getJSONObject(i);
							weibo = Constant.gson.fromJson(weiboObj.toString(),
									Weibo.class);
							weiboList.add(weibo);
						}
						Message msg = Message.obtain();
						msg.what = GET_WEIBO_OK;
						msg.obj = weiboList;
						handler.sendMessage(msg);
					} else {
						String msg = jsobj.getString("message");
						Message msg1 = Message.obtain();
						msg1.what = GET_WEIBO_FAIL;
						msg1.obj = msg;
						handler.sendMessage(msg1);
					}
				} catch (Exception e) {
					Message msg1 = Message.obtain();
					msg1.what = GET_WEIBO_FAIL;
					msg1.obj = "获取失败";
					handler.sendMessage(msg1);
					e.printStackTrace();
				}
			}
		}).start();
	}
}
