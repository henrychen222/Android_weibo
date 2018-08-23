package com.example.weibotest08_31.acty;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyToast;

public class BackPwdActy extends BaseActy {
    private DotNetManager dotNetManager;
    private EditText loginnameET;
    private EditText phoneET;
    private final int REGISTER_OK = 0x12;
    private final int REGISTER_FAIL = 0x112;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_OK:
                    pwdTV.setText("密码是:"+msg.obj.toString());
                    break;
                case REGISTER_FAIL:
                    MyToast.showToast(BackPwdActy.this, msg.obj.toString());
                    break;

                default:
                    break;
            }
        }

    };

    private TextView pwdTV;	@Override
                               protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_backpwd);
        loginnameET=(EditText)findViewById(R.id.ab_et_loginname);
        phoneET=(EditText)findViewById(R.id.ab_et_phone);
        pwdTV=(TextView)findViewById(R.id.ab_et_pwd);
        findViewById(R.id.ab_bt_getpwd).setOnClickListener(this);
        loginnameET.setText(getIntent().getStringExtra("userName").toString());
        initTitleBar(R.id.ab_tb_title, null, null, "返回密码");
        dotNetManager=new DotNetManager();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ab_bt_getpwd:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String backStr=dotNetManager.backPwd(loginnameET.getText().toString(), phoneET.getText().toString());
                        try {
                            JSONObject jsonObject=new JSONObject(backStr);

                            if("0".equals(jsonObject.getString("result"))){
                                Message msg = Message.obtain();
                                msg.what = REGISTER_OK;
                                msg.obj = jsonObject.getString("passWord");;
                                handler.sendMessage(msg);
                            }
                            else{
                                String message = jsonObject.getString("message");
                                Message msg = Message.obtain();
                                msg.what = REGISTER_FAIL;
                                msg.obj = message;
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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
