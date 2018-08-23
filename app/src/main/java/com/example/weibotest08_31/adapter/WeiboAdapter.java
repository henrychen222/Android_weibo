package com.example.weibotest08_31.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.item.Weibo;
import com.example.weibotest08_31.utils.SyncImgLoader;
import com.example.weibotest08_31.utils.SyncImgLoader.OnloadImage;

public class WeiboAdapter extends BaseAdapter {
	private Context context;
	private List<Weibo> weiboList = new ArrayList<Weibo>();
	private SyncImgLoader syncImgLoader;
	private OnViewClickListener onViewClickListener;
	
	public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
		this.onViewClickListener = onViewClickListener;
	}

	public WeiboAdapter(Context context) {
		this.context = context;
		syncImgLoader = new SyncImgLoader(context);
	}
	public void setWeiboList(List<Weibo> weiboList){
		
		this.weiboList = weiboList;
	}
	
	@Override
	public int getCount() {
		return weiboList.size();
	}

	@Override
	public Object getItem(int position) {
		return weiboList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_weibo, null);
			viewHoder.headIV = (ImageView) convertView
					.findViewById(R.id.iw_im_head);
			viewHoder.photoIV = (ImageView) convertView
					.findViewById(R.id.iw_iv_photo);
			viewHoder.timeTV = (TextView) convertView
					.findViewById(R.id.iw_tv_time);
			viewHoder.nameTV = (TextView) convertView
					.findViewById(R.id.iw_tv_username);
			viewHoder.contentTV = (TextView) convertView
					.findViewById(R.id.iw_tv_content);
			viewHoder.commontTV = (TextView) convertView
					.findViewById(R.id.iw_tv_commot);
			viewHoder.zanTV = (TextView) convertView
					.findViewById(R.id.iw_tv_zan);
			convertView.setTag(viewHoder);
		}
		Weibo weiboItem = weiboList.get(position);

		String head ="http://192.168.15.253/WS_SuweiWeibo"+weiboItem.getHeadUrl();
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
		String photo ="http://192.168.15.253/WS_SuweiWeibo"+weiboItem.getImageUrl();
		syncImgLoader.loadImage(photo, new OnloadImage() {

			@Override
			public void loadFinish(Bitmap bitmap) {
				viewHoder.photoIV.setImageBitmap(bitmap);

			}

			@Override
			public void loadFail() {
				viewHoder.photoIV.setImageResource(R.drawable.public_img_fail);

			}
		});

		viewHoder.nameTV.setText(weiboItem.getName());
		viewHoder.contentTV.setText(weiboItem.getContent());
		if ("0".equals(weiboItem.getHasPraise())) {
			viewHoder.zanTV.setText("赞(" + weiboItem.getPraiseNum() + ")");
		}
		else{
			viewHoder.zanTV.setText("已赞(" + weiboItem.getPraiseNum() + ")");
		}
		viewHoder.commontTV.setText("评论(" + weiboItem.getCommentNum() + ")");
		viewHoder.timeTV.setText(weiboItem.getCreatTime());
		//绑定
		viewHoder.zanTV.setTag(position);
		viewHoder.commontTV.setTag(position);
		//定制坚挺
		viewHoder.zanTV.setOnClickListener(listener);
		viewHoder.commontTV.setOnClickListener(listener);
		return convertView;
	}

	static class ViewHoder {
		public ImageView headIV;
		public ImageView photoIV;
		public TextView timeTV;
		public TextView nameTV;
		public TextView contentTV;
		public TextView commontTV;
		public TextView zanTV;
	}
	
	private View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (onViewClickListener!=null) {
				onViewClickListener.OnViewClick(v, (Integer)v.getTag());
			}
		}
	};
	
	public interface OnViewClickListener{
		void OnViewClick(View v, int position);
			
	}

}
