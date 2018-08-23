package com.example.weibotest08_31.acty;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyToast;
public class RegisterActy extends BaseActy {
	private DotNetManager dotNetManager;
	private EditText loginname;
	private EditText password;
	private EditText truename;
	private EditText age;
	private EditText sex;
	private EditText phone;
	private EditText head;
	private final int REGISTER_OK = 0x12;
	private final int REGISTER_FAIL = 0x112;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REGISTER_OK:
				MyToast.showToast(RegisterActy.this, "注册成功");
				break;
			case REGISTER_FAIL:
			MyToast.showToast(RegisterActy.this, "注册失败");
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_register);
		initVar();
		
	}

	private void initVar() {
		initTitleBar(R.id.ar_tb_title, null, null, "注册");
		loginname=(EditText)findViewById(R.id.ar_et_loginname);
		password=(EditText)findViewById(R.id.ar_et_pwd);
		truename=(EditText)findViewById(R.id.ar_et_truename);
		age=(EditText)findViewById(R.id.ar_et_age);
		sex=(EditText)findViewById(R.id.ar_et_sex);
		phone=(EditText)findViewById(R.id.ar_et_phone);
		head=(EditText)findViewById(R.id.ar_et_head);
		findViewById(R.id.ar_bt_register).setOnClickListener(this);
		findViewById(R.id.it_bt_left).setOnClickListener(this);
		findViewById(R.id.it_iv_left).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.ar_bt_register:
			dotNetManager=new DotNetManager();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String backStr=dotNetManager.registerUser(loginname.getText().toString(), password.getText().toString(),
							truename.getText().toString(), age.getText().toString(), sex.getText().toString(), phone.getText().toString(), head.getText().toString());
					try {
						JSONObject jsObject=new JSONObject(backStr);
						if("0".equals(jsObject.getString("result"))){
							handler.sendEmptyMessage(REGISTER_OK);
							finish();
						}
						else{
							String message = jsObject.getString("message");
							Message msg = Message.obtain();
							msg.what = REGISTER_FAIL;
							msg.obj = message;
							handler.sendMessage(msg);
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	
	
	
}
