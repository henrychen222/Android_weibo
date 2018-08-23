package com.example.weibotest08_31.acty;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.ConfigSPF;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.utils.Utils;
import com.example.weibotest08_31.view.ScrollLayout;
import com.example.weibotest08_31.view.ScrollLayout.OnViewChangeListener;
import com.example.weibotest08_31.widget.ProgressDialogManager;

public class LogoActy extends BaseActy{
	private ScrollLayout yingdaoSL;
	private ImageView logoIV;
	private SharedPreferences baseDataSPF;
	private Context mContext;
	private DotNetManager dotnetManager;
	
	
	private final int LOGIN_OK = 0x12;
	private final int LOGIN_FAIL = 0x112;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
			case LOGIN_OK:
				MyToast.showToast(mContext, "登陆成功",1000);
				getWeiboApp().setUser((User)msg.obj);
				Intent intent = new Intent(mContext, MainTabActy.class);
				startActivity(intent);
				finish();
				break;
			case LOGIN_FAIL:
				String message = msg.obj.toString();
				MyToast.showToast(mContext, message,1000);
				Intent	intent2 = new Intent(mContext, LoginActy.class);
				startActivity(intent2);
				finish();
				
				break;
			default:
				break;
			}
			ProgressDialogManager.stopProgressBar();
		}
		
	};
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_logo);
		
		mContext = this;
		
		initVar();
		
		findViews();
		
		int curVer = Utils.getVersion(mContext).versionCode;
		int preVer = baseDataSPF.getInt("preVersion", 0);
		
		if (curVer != preVer) {
			// 未登录显示引导页
			yingdaoSL.setVisibility(View.VISIBLE);
			yingdaoSL.SetOnViewChangeListener(new OnViewChangeListener() {
				
				@Override
				public void OnViewChange(int index) {
					if (index == 2) {
						// 进入登录页面或者自动登陆
						goLoginActyOrAutoLogin();
					}
				}

			});
		}
		// 进入登录页面或者自动登陆
		else{
			goLoginActyOrAutoLogin();
		}
		
		// 保存当前运行版本
		baseDataSPF.edit().putInt("preVersion", curVer).commit();
		
		
	}

	private void initVar() {
		baseDataSPF = ConfigSPF.getInstance().getConfigSPF(ConfigSPF.NAME_BASEDATA);
		dotnetManager = new DotNetManager();
	}
	

	private void findViews() {
		yingdaoSL =(ScrollLayout)findViewById(R.id.al_sl_yigdao);
		logoIV =(ImageView)findViewById(R.id.al_iv_logo);
	}
	
	private void goLoginActyOrAutoLogin() {
		boolean isLoginOut = baseDataSPF.getBoolean(Constant.SPF_KEY_ISLOGINOUT, false);
		// ��¼�û���
		final String loginName = baseDataSPF.getString(Constant.SPF_KEY_LOGINNAME, null);
		// ����ǵǳ������������û�����δ��¼������¼ҳ��
		if (isLoginOut || TextUtils.isEmpty(loginName)) {
			// �����¼ҳ��
			Intent intent = new Intent(mContext, LoginActy.class);
			startActivity(intent);
			finish();
		}
		// �Զ���½
		else{
			// �Զ���¼ǰ��չʾһ��͸��������2.5�룬��������֮���Զ���¼
			AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
			alphaAnimation.setDuration(500);
			logoIV.setAnimation(alphaAnimation);
			logoIV.startAnimation(alphaAnimation);

			alphaAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// �Զ���¼
					autoLogin(loginName);
				}

			});

		}
		
	}
	
	private void autoLogin(final String loginName) {
		final String passWord = baseDataSPF.getString(Constant.SPF_KEY_LOGINPWD, null);
		ProgressDialogManager.startProgressBar(mContext, "登录中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ��ʱ����
					String backStr = dotnetManager.peopleLogin(loginName, passWord);
					JSONObject jsonObject = new JSONObject(backStr);
					String result = jsonObject.getString("result");
					// 0�ɹ���1ʧ��
					if ("0".equals(result)) {
						JSONObject peopleJO = jsonObject.getJSONObject("people");
						// ������Gson����
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
					msg.obj = "登陆失败";
					mHandler.sendMessage(msg);
				}
			}
		}).start();
				
	}


}
