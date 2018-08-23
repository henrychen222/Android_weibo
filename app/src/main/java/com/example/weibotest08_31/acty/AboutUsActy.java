package com.example.weibotest08_31.acty;

import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.widget.ProgressDialog;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutUsActy extends BaseActy {
    private TitleBar titleBar;
    private DotNetManager dotNetManager;
    private TextView title;
    private TextView content;
    private  String titleStr;
    private String contentStr;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
            title.setText(titleStr);
            content.setText(contentStr);
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
        setContentView(R.layout.acty_aboutus);
        initTitleBar(R.id.aa_tb_title, null, null, "关于我们");
        titleBar=(TitleBar)findViewById(R.id.aa_tb_title);
        titleBar.leftIV.setOnClickListener(this);
        title= (TextView) findViewById(R.id.aa_tv_title);
        content= (TextView) findViewById(R.id.aa_tv_content);
        dotNetManager=new DotNetManager();
        ProgressDialogManager.startProgressBar(AboutUsActy.this, "请稍等");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String backStr=dotNetManager.aboutUs(getWeiboApp().getUser().getLoginName());
                try {
                    JSONObject jsonObject=new JSONObject(backStr);
                    if("0".equals(jsonObject.getString("result")))
                    {
                       titleStr=jsonObject.getString("title");
                        contentStr=jsonObject.getString("content");
                        handler.sendEmptyMessage(1);
                        }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

}
