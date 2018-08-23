package com.example.weibotest08_31.acty;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.ConfigSPF;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

public class LoginActy extends BaseActy {

	private SharedPreferences baseDataSPF;
	private DotNetManager dotnetManager;
	private EditText userNameET;
	private EditText passWordET;
	private Button loginBN;
	private TitleBar mTitleBar;
	private Context mContext;

	private final int LOGIN_OK = 0x12;
	private final int LOGIN_FAIL = 0x112;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_OK://

				User userItem = (User) msg.obj;
				//放到Application供整个工程用
				getWeiboApp().setUser(userItem);
				//
				MyToast.showToast(mContext, "用户"+userItem.getName() + "登陆成功");
				//
				baseDataSPF.edit().putString(Constant.SPF_KEY_LOGINNAME,
								userItem.getLoginName())
						.putString(Constant.SPF_KEY_LOGINPWD,
								userItem.getPassWord())
						.putBoolean(Constant.SPF_KEY_ISLOGINOUT, false)
						.commit();
				Intent intent = new Intent(mContext, MainTabActy.class);
				startActivity(intent);
				finish();
				break;
			case LOGIN_FAIL:
				String message = msg.obj.toString();
				MyToast.showToast(mContext, message);
				break;
			default:
				break;
			}
			ProgressDialogManager.stopProgressBar();
		}

	};
	private Button registerBN;
	private Button backpwdBN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_login);
		MyLog.e("", "------------->ִ执行了LoginActy");
		mContext = this;

		initVar();

		findViews();

		bindViews();

		setOnclicks();
	}
	private void initVar() {
		baseDataSPF = ConfigSPF.getInstance().getConfigSPF(ConfigSPF.NAME_BASEDATA);
		dotnetManager = new DotNetManager();
	}


	private void findViews() {
		mTitleBar = (TitleBar)findViewById(R.id.al_tb_top);
		userNameET = (EditText)findViewById(R.id.al_et_username);
		passWordET = (EditText)findViewById(R.id.al_et_userpwd);
		loginBN = (Button)findViewById(R.id.al_bt_login);
		registerBN = (Button)findViewById(R.id.al_bt_register);
		backpwdBN = (Button)findViewById(R.id.al_bt_backpwd);
		
	}


	private void bindViews() {
		mTitleBar.titleTV.setText("登陆");
		mTitleBar.rightBT.setVisibility(View.GONE);
		
	}
	

	private void setOnclicks() {
		mTitleBar.leftBT.setOnClickListener(this);
		loginBN.setOnClickListener(this);
		registerBN.setOnClickListener(this);
		backpwdBN.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.al_bt_login:
			final String loginName = userNameET.getText().toString();
			if (TextUtils.isEmpty(loginName)) {
				MyToast.showToast(mContext, "用户名不能为空");
				return;
			}
			final String passWord = passWordET.getText().toString();
			if (TextUtils.isEmpty(passWord)) {
				MyToast.showToast(mContext, "密码不能为空");
				return;
			}
			
			ProgressDialogManager.startProgressBar(mContext, "登陆中---");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String backStr = dotnetManager.peopleLogin(loginName, passWord);
						JSONObject jsonObject = new JSONObject(backStr);
						String result = jsonObject.getString("result");
						if ("0".equals(result)) {
							JSONObject peopleJO = jsonObject.getJSONObject("people");
							User userItem = Constant.gson.fromJson(peopleJO.toString(), User.class);
							Message msg = Message.obtain();
							msg.what = LOGIN_OK;
							msg.obj = userItem;
							mHandler.sendMessage(msg);
						}else{
							String message = jsonObject.getString("message");
							Message msg = Message.obtain();
							msg.what = LOGIN_FAIL;
							msg.obj = message;
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						Message msg = Message.obtain();
						msg.what = LOGIN_FAIL;
						msg.obj = "解析错误";
						mHandler.sendMessage(msg);
					}
				}
			}).start();
			break;
		case R.id.al_bt_register:
			Intent intent=new Intent(this,RegisterActy.class);
			startActivity(intent);
			break;
		case R.id.al_bt_backpwd:
			Intent intent1=new Intent(this,BackPwdActy.class);
			intent1.putExtra("userName", userNameET.getText().toString());
			startActivity(intent1);
			break;
		default:
			break;
		}
		
	}

}
