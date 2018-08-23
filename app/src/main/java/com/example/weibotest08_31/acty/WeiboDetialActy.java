package com.example.weibotest08_31.acty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.adapter.CommentAdapter;
import com.example.weibotest08_31.adapter.WeiboAdapter;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.config.Constant;
import com.example.weibotest08_31.item.Comment;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.item.Weibo;
import com.example.weibotest08_31.utils.MyLog;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.utils.SyncImgLoader;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiaolong on 15/9/9.
 */
public class WeiboDetialActy extends BaseActy{
    private ListView commentsLV;
    private WeiboAdapter weiboAdapter;
    private CommentAdapter commentAdapter;
    private SyncImgLoader syncImgLoader;
    private DotNetManager dotNetManager;
    private TitleBar titleBar;
    private final int PRAISE_OK = 1;
    private final int PRAISE_FAIL = 2;
    private final int GET_COMMENTS_OK = 3;
    private final int GET_COMMENTS_FAIL = 4;
    private ImageView headIV;
    private TextView nameTV;
    private TextView contentTV;
    private ImageView photoIV;
    private TextView praiseTV;
    private Weibo weibo;
    private int praiseNum;
    List<Comment> commentList=new ArrayList<Comment>();
    private TextView commotTV;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRAISE_OK:
                    praiseTV.setText("已赞"+"("+(praiseNum+1)+")");
                    break;
                case PRAISE_FAIL:
                    praiseTV.setText("赞"+"("+praiseNum+")");

                    break;
                case GET_COMMENTS_OK:
                    commentList=(List<Comment>)msg.obj;
                    CommentAdapter commentAdapter=new CommentAdapter(WeiboDetialActy.this);
                    commentAdapter.setCommentLists(commentList);
                    commentsLV.setAdapter(commentAdapter);
                    break;
                case GET_COMMENTS_FAIL:
                    MyToast.showToast(WeiboDetialActy.this,msg.obj+"");
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
        setContentView(R.layout.acty_weibodetial);
        dotNetManager=new DotNetManager();
        syncImgLoader = new SyncImgLoader(WeiboDetialActy.this);
        initTitleBar(R.id.aw_tb_title, null, null, "微博详情");
        titleBar= (TitleBar) findViewById(R.id.aw_tb_title);
        titleBar.leftIV.setOnClickListener(this);
        commentsLV=(ListView)findViewById(R.id.aw_lv_comments);
        headIV=(ImageView)findViewById(R.id.aw_im_head);
        photoIV=(ImageView)findViewById(R.id.aw_iv_photo);
        nameTV=(TextView)findViewById(R.id.aw_tv_username);
        commotTV=(TextView)findViewById(R.id.aw_tv_commot1);
        contentTV=(TextView)findViewById(R.id.aw_tv_content);
        praiseTV=(TextView)findViewById(R.id.aw_tv_zan);
        praiseTV.setOnClickListener(this);
        findViewById(R.id.aw_tv_commot1).setOnClickListener(this);

        requestData();
    }


    private void requestData() {
        weibo= (Weibo) getIntent().getSerializableExtra("weibo");
        praiseNum=weibo.getPraiseNum();
        String head ="http://192.168.15.253/WS_SuweiWeibo"+weibo.getHeadUrl();
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
        String photo ="http://192.168.15.253/WS_SuweiWeibo"+weibo.getImageUrl();
        syncImgLoader.loadImage(photo, new SyncImgLoader.OnloadImage() {

            @Override
            public void loadFinish(Bitmap bitmap) {
                photoIV.setImageBitmap(bitmap);
            }

            @Override
            public void loadFail() {
                headIV.setImageResource(R.drawable.public_img_fail);
            }
        });
        nameTV.setText(weibo.getName());
        contentTV.setText(weibo.getContent());
        commotTV.setText("评论(" + weibo.getCommentNum() + ")");
        if("0".equals(weibo.getHasPraise())){
            praiseTV.setText("赞"+"("+praiseNum+")");
        }
        else{
            praiseTV.setText("已赞"+"("+praiseNum+")");
        }
        ProgressDialogManager.startProgressBar(context, "请求中---");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String backStr=dotNetManager.getWeiboCommentList(getWeiboApp().getUser().getId()+"",
                        weibo.getId(),20+"");
                try {

                    JSONObject jsonObject=new JSONObject(backStr);
                    if("0".equals(jsonObject.getString("result"))){
                        JSONArray jsonArray=jsonObject.getJSONArray("commentList");
                        JSONObject jsoncomment;
                        Comment comment;
                        for (int i=0;i<jsonArray.length();i++){
                            jsoncomment=jsonArray.getJSONObject(i);
                            comment=Constant.gson.fromJson(jsoncomment.toString(), Comment.class);
                            commentList.add(comment);
                        }
                        Message msg = Message.obtain();
                        msg.what = GET_COMMENTS_OK;
                        msg.obj = commentList;
                        handler.sendMessage(msg);
                    }else{
                        String msg = jsonObject.getString("message");
                        Message msg1 = Message.obtain();
                        msg1.what = GET_COMMENTS_FAIL;
                        msg1.obj = msg;
                        handler.sendMessage(msg1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg1 = Message.obtain();
                    msg1.what = GET_COMMENTS_FAIL;
                    msg1.obj = "解析异常";
                    handler.sendMessage(msg1);
                }
            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aw_tv_zan:
                if("0".equals(weibo.getHasPraise())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String backStr=dotNetManager.praiseWeibo(getWeiboApp().getUser().getId() + "", weibo.getId(), weibo.getHasPraise());
                            try {
                                JSONObject jsonObject=new JSONObject(backStr);
                                if("0".equals(jsonObject.getString("result"))){
                                    weibo.setHasPraise("1");
                                    int praiseNum = Integer.valueOf(weibo.getPraiseNum());
                                    weibo.setPraiseNum((praiseNum + 1));
                                    handler.sendEmptyMessage(PRAISE_OK);
                                }else{
                                    handler.sendEmptyMessage(PRAISE_FAIL);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(PRAISE_FAIL);
                            }
                        }
                    }).start();

                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String backStr=dotNetManager.praiseWeibo(getWeiboApp().getUser().getId() + "", weibo.getId(), weibo.getHasPraise());
                            try {
                                JSONObject jsonObject=new JSONObject(backStr);
                                if("0".equals(jsonObject.getString("result"))){
                                    weibo.setHasPraise("0");
                                    int praiseNum = Integer.valueOf(weibo.getPraiseNum());
                                    weibo.setPraiseNum((praiseNum - 1));
                                    handler.sendEmptyMessage(PRAISE_OK);
                                }
                                else{
                                    handler.sendEmptyMessage(PRAISE_FAIL);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(PRAISE_FAIL);
                            }

                        }
                    }).start();


                }
                break;
            case R.id.aw_tv_commot1:
                Intent intent=new Intent(WeiboDetialActy.this,AddCommentsActy.class);
                intent.putExtra("weiboId",weibo.getId());
                break;
            default:
                break;
        }
        super.onClick(v);
    }
}
