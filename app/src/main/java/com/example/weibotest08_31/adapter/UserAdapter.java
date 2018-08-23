package com.example.weibotest08_31.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.item.User;
import com.example.weibotest08_31.utils.SyncImgLoader;
import com.example.weibotest08_31.utils.SyncImgLoader.OnloadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class UserAdapter extends BaseAdapter {
    private List<User> userList = new ArrayList<User>();
    private Context context;
    private SyncImgLoader syncImgLoader;

    public UserAdapter(Context context) {
        this.context = context;
        syncImgLoader = new SyncImgLoader(context);
    }

    public void setUserList(List<User> userList) {

        this.userList = userList;
    }

    @Override
    public int getCount() {

        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoder viewHoder;
        if (convertView != null && convertView.getTag() != null) {
            viewHoder = (ViewHoder) convertView.getTag();
        } else {
            viewHoder = new ViewHoder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fans, null);
            viewHoder.headIV = (ImageView) convertView.findViewById(R.id.if_iv_head);
            viewHoder.nameTV = (TextView) convertView.findViewById(R.id.if_tv_nick);
            viewHoder.contentTV = (TextView) convertView.findViewById(R.id.if_tv_sex);

        }
        User user = (User) userList.get(position);
        String head = "http://192.168.15.253/WS_SuweiWeibo" + user.getHeadUrl();
        syncImgLoader.loadImage(head, new OnloadImage() {

            @Override
            public void loadFinish(Bitmap bitmap) {
                viewHoder.headIV.setImageBitmap(bitmap);
            }

            @Override
            public void loadFail() {
                viewHoder.headIV.setImageResource(R.drawable.public_img_fail);
            }
        });

        viewHoder.nameTV.setText(user.getName());
        viewHoder.contentTV.setText(user.getSex());
        return convertView;
    }

    static class ViewHoder {
        public ImageView headIV;
        public TextView nameTV;
        public TextView contentTV;

    }
}
