package com.example.weibotest08_31.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.item.LeaveMsgList;
import com.example.weibotest08_31.utils.SyncImgLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaolong on 15/9/9.
 */
public class LeaveMsgAdapter extends BaseAdapter {
    private Context context;
    private List<LeaveMsgList> leaveMsgLists=new ArrayList<LeaveMsgList>();
    private SyncImgLoader syncImgLoader;
    public LeaveMsgAdapter(Context context) {
        this.context = context;
        syncImgLoader=new SyncImgLoader(context);
    }

    public void setLeaveMsgLists(List<LeaveMsgList> leaveMsgLists) {
        this.leaveMsgLists = leaveMsgLists;
    }

    @Override
    public int getCount() {
        return leaveMsgLists.size();
    }

    @Override
    public Object getItem(int position) {
        return leaveMsgLists.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_leavemsg,null);
            viewHolder.headIV=(ImageView)convertView.findViewById(R.id.il_iv_head);
            viewHolder.nameTV=(TextView)convertView.findViewById(R.id.il_tv_nick);
            viewHolder.contentTV=(TextView)convertView.findViewById(R.id.il_tv_leavemsg);
            viewHolder.timeTV=(TextView)convertView.findViewById(R.id.il_tv_time);
        }
        LeaveMsgList leaveMsgList=leaveMsgLists.get(position);
        String head = "http://192.168.15.253/WS_SuweiWeibo" + leaveMsgList.getHeadUrl();
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

        viewHolder.nameTV.setText(leaveMsgList.getName());
        viewHolder.contentTV.setText(leaveMsgList.getContent());
        viewHolder.timeTV.setText(leaveMsgList.getCreateTime());
        if("0".equals(leaveMsgList.getIsRead())){
            viewHolder.contentTV.setTextColor(Color.rgb(239,63, 47));
        }
        return convertView;
    }
    static class ViewHolder{
        public ImageView headIV;
        public TextView nameTV;
        public TextView contentTV;
        public TextView timeTV;

    }
}
