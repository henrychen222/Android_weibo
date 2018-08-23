package com.example.weibotest08_31.acty;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouxiaolong on 15/9/7.
 */
public class EditPwdActy extends BaseActy {
    private TitleBar titleBar;
    private EditText loginname;
    private EditText oldpassword;
    private EditText newpassword;
    private DotNetManager dotNetManager;
    private final int REGISTER_OK = 0x12;
    private final int REGISTER_FAIL = 0x112;
    private android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_OK:
                    MyToast.showToast(EditPwdActy.this, "修改密码成功");
                    //退出登录
                    finish();
                    break;
                case REGISTER_FAIL:
                    MyToast.showToast(EditPwdActy.this, msg.obj.toString());
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_editpwd);
        initTitleBar(R.id.ae_tb_title, null, null, "修改密码");
        titleBar = (TitleBar) findViewById(R.id.ae_tb_title);
        titleBar.leftIV.setOnClickListener(this);
        loginname = (EditText) findViewById(R.id.ae_et_loginname);
        oldpassword = (EditText) findViewById(R.id.ae_et_oldpwd);
        newpassword = (EditText) findViewById(R.id.ae_et_newpwd);
        dotNetManager = new DotNetManager();
        findViewById(R.id.ae_bt_update).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ae_bt_update:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String backStr = dotNetManager.editPwd(loginname.getText().toString(), oldpassword.getText().toString(),
                                newpassword.getText().toString());
                        try {
                            JSONObject jsObject = new JSONObject(backStr);
                            if ("0".equals(jsObject.getString("result"))) {
                                handler.sendEmptyMessage(REGISTER_OK);
                            } else {
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
        }
        super.onClick(v);
    }
}
