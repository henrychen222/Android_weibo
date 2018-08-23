package com.example.weibotest08_31.acty;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.WeiboAdapter;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.item.Weibo;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.utils.SyncImgLoader;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiaolong on 15/9/9.
 */
public class UserDetialsActy extends BaseActy{
    private ListView weibLV;
    private WeiboAdapter weiboAdapter;
    private SyncImgLoader syncImgLoader;
    private DotNetManager dotNetManager;
    private TitleBar titleBar;
    private final int GET_DETIAL_OK = 1;
    private final int GET_DETIAL_FAIL = 2;
    private final int QUXIAO_OK = 3;
    private final int QUXIAO_FAIL = 4;
    private ArrayList<Map<String,Object>> backList=new ArrayList<Map<String,Object>>();
    private ImageView headIV;
    private TextView ageTV;
    private TextView nameTV;
    private TextView sexTV;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DETIAL_OK:
                    backList = (ArrayList<Map<String, Object>>) msg.obj;
                    User backuser = (User) backList.get(0).get("useritem");

                    String head = "http://192.168.15.253/WS_SuweiWeibo" + backuser.getHeadUrl();
                    syncImgLoader.loadImage(head, new SyncImgLoader.OnloadImage() {

                        @Override
                        public void loadFinish(Bitmap bitmap) {
                            headIV.setImageBitmap(bitmap);
                        }

                        @Override
                        public void loadFail() {
                            headIV.setImageResource(R.drawable.public_img_fail);
                        }
                    });
                    ageTV.setText(backuser.getAge());
                    nameTV.setText(backuser.getName());
                    sexTV.setText(backuser.getSex());
                    Weibo backweibo = (Weibo) backList.get(0).get("weiboitem");
//                    List<Weibo> weiboList = (List<Weibo>) backList.get(0).get("weiboitem");
                    List<Weibo> weiboList = new ArrayList<Weibo>();
                    weiboList.add(backweibo);
                    weiboAdapter.setWeiboList(weiboList);
                    weiboAdapter.notifyDataSetChanged();
                    break;
                case GET_DETIAL_FAIL:

                    break;
                case QUXIAO_OK:
                    MyToast.showToast(UserDetialsActy.this,msg.obj+"");
                    break;
                case QUXIAO_FAIL:
                    MyToast.showToast(UserDetialsActy.this,msg.obj+"");

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
        setContentView(R.layout.acty_userdetial);
        dotNetManager=new DotNetManager();
        syncImgLoader = new SyncImgLoader(UserDetialsActy.this);
        initTitleBar(R.id.au_tb_title, null, null, "用户信息");
        titleBar= (TitleBar) findViewById(R.id.au_tb_title);
//        titleBar.leftIV.setOnClickListener(this);
        headIV=(ImageView)findViewById(R.id.au_iv_head);
        ageTV=(TextView)findViewById(R.id.au_tv_age);
        nameTV=(TextView)findViewById(R.id.au_tv_name);
        sexTV=(TextView)findViewById(R.id.au_tv_sex);
        weibLV= (ListView) findViewById(R.id.au_lv_listview);
        findViewById(R.id.au_bt_commit).setOnClickListener(this);
        titleBar.leftIV.setOnClickListener(this);
        bindViews();
        requestData();
    }
        private void bindViews() {
            weiboAdapter = new WeiboAdapter(UserDetialsActy.this);
            weibLV.setAdapter(weiboAdapter);
            weiboAdapter.setOnViewClickListener(new WeiboAdapter.OnViewClickListener() {

                @Override
                public void OnViewClick(View v, int position) {
                    Weibo weibo = (Weibo) weiboAdapter.getItem(position);

                    switch (v.getId()) {
                        case R.id.iw_tv_zan:
                            MyLog.e("", "赞----->" + position);
                            if ("0".equals(weibo.getHasPraise())) {
                                weibo.setHasPraise("1");
                                int praiseNum = Integer.valueOf(weibo.getPraiseNum());
                                weibo.setPraiseNum((praiseNum + 1));
                                weiboAdapter.notifyDataSetChanged();
                            } else {
                                weibo.setHasPraise("0");
                                int praiseNum = Integer.valueOf(weibo.getPraiseNum());
                                weibo.setPraiseNum((praiseNum - 1));
                                weiboAdapter.notifyDataSetChanged();
                            }
                            break;
                        case R.id.iw_tv_commot:
                            MyLog.e("", "评论了----->" + position);

                            break;

                        default:
                            break;
                    }
                }
            });
        }
        private void requestData() {
        ProgressDialogManager.startProgressBar(context, "请求中---");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject;
                    MyLog.e("zxl",getWeiboApp().getUser().getId() + "");
                    MyLog.e("zxl1",getIntent().getIntExtra("otherId", 0)+">>>>>");
                    String backStr=dotNetManager.getThePeopleDetail(getWeiboApp().getUser().getId() + "",
                            getIntent().getIntExtra("userId", 0) + "");
                    try {
                        jsonObject=new JSONObject(backStr);
                        if("0".equals(jsonObject.getString("result"))){
                            User useritem= Constant.gson.fromJson( jsonObject.getString("people"),User.class);
                            Weibo weiboitem=Constant.gson.fromJson(jsonObject.getString("weibo"),Weibo.class);
                            Map<String,Object> map=new HashMap<String,Object>();
                            map.put("useritem",useritem);
                            map.put("weiboitem",weiboitem);
                            backList.add(map);
                            Message msg = Message.obtain();
                            msg.what = GET_DETIAL_OK;
                            msg.obj = backList;
                            handler.sendMessage(msg);
                        }
                        else
                        {
                            String msg = jsonObject.getString("message");
                            Message msg1 = Message.obtain();
                            msg1.what = GET_DETIAL_FAIL;
                            msg1.obj = msg;
                            handler.sendMessage(msg1);
                        }
                    } catch (JSONException e) {
                        Message msg1 = Message.obtain();
                        msg1.what = GET_DETIAL_FAIL;
                        msg1.obj = "获取失败";
                        handler.sendMessage(msg1);
                        e.printStackTrace();
                    }

                }
            }).start();

        }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.au_bt_commit:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String backStr2=dotNetManager.cancelCareSomeOne(getWeiboApp().getUser().getId()+"",
                                getIntent().getIntExtra("userId",0)+"");
                        try {
                            JSONObject jsonObject1=new JSONObject(backStr2);
                            if("0".equals(jsonObject1.getString("result"))){
                                Message msg=Message.obtain();
                                msg.what=QUXIAO_OK;
                                msg.obj=jsonObject1.getString("message");
                                handler.sendMessage(msg);
                            }
                            else{
                                Message msg=Message.obtain();
                                msg.what=QUXIAO_FAIL;
                                msg.obj=jsonObject1.getString("message");
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Message msg=Message.obtain();
                            msg.what=QUXIAO_FAIL;
                            msg.obj="获取失败";
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
        }
        super.onClick(v);
    }
    
}
