package com.example.weibotest08_31.acty;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouxiaolong on 15/9/7.
 */
public class FeedBackActy extends BaseActy {
    private TitleBar titleBar;
    private DotNetManager dotNetManager;
    private final int REGISTER_OK = 0x12;
    private final int REGISTER_FAIL = 0x112;
    private android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_OK:
                    MyToast.showToast(FeedBackActy.this, "修改密码成功");
                    //退出登录
                    finish();
                    break;
                case REGISTER_FAIL:
                    MyToast.showToast(FeedBackActy.this, msg.obj.toString());
                    break;

                default:
                    break;
            }
        }

    };
    private TextView userIdTV;
    private EditText contentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_feedback);
        initTitleBar(R.id.af_tb_title, null, null, "意见反馈");
        titleBar = (TitleBar) findViewById(R.id.af_tb_title);
        userIdTV=(TextView)findViewById(R.id.af_tv_loginname);
        contentET=(EditText)findViewById(R.id.af_et_context);
        titleBar.leftIV.setOnClickListener(this);
        dotNetManager = new DotNetManager();
        userIdTV.setText(getWeiboApp().getUser().getName()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ae_bt_update:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String backStr =dotNetManager.writeFeedBack(getWeiboApp().getUser().getId()+"",contentET.getText().toString());
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
