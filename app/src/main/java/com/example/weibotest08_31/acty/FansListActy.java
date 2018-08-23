package com.example.weibotest08_31.acty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.UserAdapter;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

public class FansListActy extends BaseActy {
    private ListView fansList;
    private TitleBar titleBar;
    private DotNetManager dotNetManager;
    private List<User> userList=new ArrayList<User>();
    private final int GET_USER_OK = 1;
    private final int GET_USER_FAIL = 2;
    private final int QUXIAO_OK = 3;
    private final int QUXIAO_FAIL = 4;
    private UserAdapter fansAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_USER_OK:
                    List<User> userList = (List<User>) msg.obj;
                    fansAdapter.setUserList(userList);
                    fansAdapter.notifyDataSetChanged();
                    break;
                case GET_USER_FAIL:
                    MyToast.showToast(FansListActy.this, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
            ProgressDialogManager.stopProgressBar();
        }
    };
    private EditText searchET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanslist);
        initVar();

        bindViews();
        clickItem();

        requestData();

    }

    private void bindViews() {
        fansAdapter=new UserAdapter(FansListActy.this);
        fansList.setAdapter(fansAdapter);
    }

    private void initVar() {
        fansList=(ListView) findViewById(R.id.af_lv_listview);
        initTitleBar(R.id.af_tb_title, null, null, "粉丝列表");
        titleBar=(TitleBar) findViewById(R.id.af_tb_title);
        titleBar.leftIV.setOnClickListener(this);
        dotNetManager=new DotNetManager();
        searchET=(EditText)findViewById(R.id.af_et_search);
        findViewById(R.id.af_iv_search).setOnClickListener(this);


    }

    private void requestData() {
        ProgressDialogManager.startProgressBar(FansListActy.this, "请稍等，努力加载中--->");
        new Thread(new Runnable() {

            @Override
            public void run() {
			String backStr=dotNetManager.getFansList(getWeiboApp().getUser().getId()+"");
                try {
                    JSONObject jsonObject=new JSONObject(backStr);
                    if("0".equals(jsonObject.getString("result"))){
                        JSONArray jsonArray=jsonObject.getJSONArray("fansList");
                        JSONObject weiObject;
                        User user;
                        for (int j = 0; j < jsonArray.length(); j++) {
                            weiObject=jsonArray.getJSONObject(j);
                            user=Constant.gson.fromJson(weiObject.toString(), User.class);
                            userList.add(user);
                        }
                        Message msg = Message.obtain();
                        msg.what = GET_USER_OK;
                        msg.obj = userList;
                        handler.sendMessage(msg);
                    }else {
                        String msg = jsonObject.getString("message");
                        Message msg1 = Message.obtain();
                        msg1.what = GET_USER_FAIL;
                        msg1.obj = msg;
                        handler.sendMessage(msg1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg1 = Message.obtain();
                    msg1.what = GET_USER_FAIL;
                    msg1.obj = e;
                    handler.sendMessage(msg1);
                }
            }
        }).start();
    }

    private void clickItem() {
         fansList.setOnItemClickListener(new OnItemClickListener() {

             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                                     int position, long id) {
                 Intent intent = new Intent(FansListActy.this, OtherDetialActy.class);
                 MyLog.e("zxl", userList.get(position).getId() + "");
                 intent.putExtra("otherId", userList.get(position).getId());
                 startActivity(intent);
             }
         });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.af_iv_search:
                Intent intent=new Intent(FansListActy.this,AddCaresActy.class);
                intent.putExtra("userId",searchET.getText().toString());
                startActivity(intent);
                break;
        }
        super.onClick(v);
    }

}
