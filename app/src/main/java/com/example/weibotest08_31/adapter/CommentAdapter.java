package com.example.weibotest08_31.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.item.Comment;
import com.example.weibotest08_31.item.LeaveMsgList;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.utils.SyncImgLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaolong on 15/9/9.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> commentLists=new ArrayList<Comment>();
    private SyncImgLoader syncImgLoader;
    public CommentAdapter(Context context) {
        this.context = context;
        syncImgLoader=new SyncImgLoader(context);
    }

    public void setCommentLists(List<Comment> commentLists) {
        this.commentLists = commentLists;
    }

    public CommentAdapter(List<Comment> commentLists) {
        this.commentLists = commentLists;
    }

    @Override
    public int getCount() {
        return commentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return commentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView!=null&&convertView.getTag()!=null){
            viewHolder=(ViewHolder)convertView.getTag();
        }
        else {
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_comments,null);
            viewHolder.headIV=(ImageView)convertView.findViewById(R.id.ic_iv_head);
            viewHolder.nameTV=(TextView)convertView.findViewById(R.id.ic_tv_nick);
            viewHolder.contentTV=(TextView)convertView.findViewById(R.id.ic_tv_content);
            viewHolder.timeTV=(TextView)convertView.findViewById(R.id.ic_tv_time);
        }
        Comment comment=commentLists.get(position);
        String head = "http://192.168.15.253/WS_SuweiWeibo" + comment.getHeadUrl();
        syncImgLoader.loadImage(head, new SyncImgLoader.OnloadImage() {

            @Override
            public void loadFinish(Bitmap bitmap) {
                viewHolder.headIV.setImageBitmap(bitmap);
            }

            @Override
            public void loadFail() {
                viewHolder.headIV.setImageResource(R.drawable.public_img_fail);
            }
        });

        viewHolder.nameTV.setText(comment.getName());
        viewHolder.contentTV.setText(comment.getContent());
        viewHolder.timeTV.setText(comment.getCreateTime().toString());
        MyToast.showToast(context,comment.getCreateTime());
        return convertView;
    }
    static class ViewHolder{
        public ImageView headIV;
        public TextView nameTV;
        public TextView contentTV;
        public TextView timeTV;
    }
}
