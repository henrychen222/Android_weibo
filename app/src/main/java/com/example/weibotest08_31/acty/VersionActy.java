package com.example.weibotest08_31.acty;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.Utils;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class VersionActy extends BaseActy {
    private TitleBar titleBar;
    private DotNetManager dotNetManager;
    private TextView title;
    private TextView content;
    private Context context;
    private TextView version;
    private TextView appUrl;
    private TextView newDescribe;
    private String contentstr;
    private String versionstr;
    private String appUrlstr;
    private String newDescribestr;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    content.setText(contentstr);
                    version.setText(versionstr);
                    appUrl.setText(appUrlstr);
                    newDescribe.setText(newDescribestr);
                    break;
                    default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_version);
        context=this;
        initTitleBar(R.id.av_tb_title, null, null, "版本信息");
        titleBar=(TitleBar)findViewById(R.id.av_tb_title);
        titleBar.leftIV.setOnClickListener(this);
        title= (TextView) findViewById(R.id.av_tv_title);
        content= (TextView) findViewById(R.id.av_tv_versionId);
        version= (TextView) findViewById(R.id.av_tv_version);
        appUrl= (TextView) findViewById(R.id.av_tv_appUrl);
        newDescribe= (TextView) findViewById(R.id.av_tv_newDescribe);
        dotNetManager=new DotNetManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String backStr=dotNetManager.checkVersionUpdate("1", Utils.getVersion(context).versionCode+"","1");
                try {
                    JSONObject jsonObject=new JSONObject(backStr);
                    if("0".equals(jsonObject.getString("result")))
                    {
                        contentstr=jsonObject.getString("versionId");
                        versionstr=jsonObject.getString("version");
                        appUrlstr=jsonObject.getString("appUrl");
                        newDescribestr=jsonObject.getString("newDescribe");
                        handler.sendEmptyMessage(1);
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
