package com.example.weibotest08_31.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.widget.TitleBar;

public class Tab4Activity extends BaseActy {
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab4);
        initTitleBar(R.id.at4_tb_title, null, null, "我");
        titleBar = (TitleBar) findViewById(R.id.at4_tb_title);
        titleBar.leftIV.setVisibility(View.GONE);
        findViewById(R.id.at4_rl_editpersonal).setOnClickListener(this);
        findViewById(R.id.at4_rl_editpwd).setOnClickListener(this);
        findViewById(R.id.at4_rl_weibo).setOnClickListener(this);
        findViewById(R.id.at4_rl_yijian).setOnClickListener(this);
        findViewById(R.id.at4_rl_version).setOnClickListener(this);
        findViewById(R.id.at4_rl_myfans).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.at4_rl_editpersonal:
                intent = new Intent(Tab4Activity.this, EditPersonalActy.class);
                startActivity(intent);
                break;
            case R.id.at4_rl_editpwd:
                intent = new Intent(Tab4Activity.this, EditPwdActy.class);
                startActivity(intent);
                break;

            case R.id.at4_rl_weibo:
                intent = new Intent(Tab4Activity.this, AboutUsActy.class);
                startActivity(intent);
                break;
            case R.id.at4_rl_yijian:
                intent = new Intent(Tab4Activity.this, FeedBackActy.class);
                startActivity(intent);
                break;
            case R.id.at4_rl_version:
                intent = new Intent(Tab4Activity.this, VersionActy.class);
                startActivity(intent);
                break;
            case R.id.at4_rl_myfans:
                intent = new Intent(Tab4Activity.this, FansListActy.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
