package com.example.weibotest08_31.acty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.item.LeaveMsgList;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhouxiaolong on 15/9/9.
 */
public class LeaveMsgActy extends BaseActy {
    private DotNetManager dotNetManager;
    private Button commit;
    private EditText leavemsg;
    private final int LEAVEMSG_OK=1;
    private final int LEAVEMSG_FAIL=2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LEAVEMSG_OK:
                    MyToast.showToast(LeaveMsgActy.this,msg.obj.toString());
                    finish();
                    break;
                case LEAVEMSG_FAIL:
                    MyToast.showToast(LeaveMsgActy.this,msg.obj.toString());

                    break;


                default:
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavemsg);
        initTitleBar(R.id.al_tb_title, null, null, "留言");
        mTitleBar.leftIV.setOnClickListener(this);
        dotNetManager=getDotNetManager();
        commit=(Button)findViewById(R.id.al_bt_commit);
        leavemsg=(EditText)findViewById(R.id.al_et_msg);
        findViewById(R.id.al_bt_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.al_bt_commit:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       String backStr=dotNetManager.leaveMsgToPeople(getWeiboApp().getUser().getId()+"",
                               getIntent().getStringExtra("userId")+"",leavemsg.getText().toString());
                        try {
                            JSONObject jsonObject=new JSONObject(backStr);
                            if("0".equals(jsonObject.getString("result"))){
                                Message msg=Message.obtain();
                                msg.what=LEAVEMSG_OK;
                                msg.obj=jsonObject.getString("message");
                                handler.sendMessage(msg);
                            }
                            else{
                                Message msg=Message.obtain();
                                msg.what=LEAVEMSG_FAIL;
                                msg.obj=jsonObject.getString("message");
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
