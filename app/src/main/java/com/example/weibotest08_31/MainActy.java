package com.example.weibotest08_31;

import com.example.weibotest08_31.widget.TitleBar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActy extends Activity {

    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_main);

        titleBar=(TitleBar)findViewById(R.id.am_tb_top);
        titleBar.titleTV.setText("微博");
        titleBar.rightBT.setText("设置");
    }




}