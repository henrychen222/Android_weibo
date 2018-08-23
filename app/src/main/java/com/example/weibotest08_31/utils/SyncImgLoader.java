package com.example.weibotest08_31.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class SyncImgLoader {
	private Context context;
	private FileCache fileCache;
	public SyncImgLoader(Context ctx){
		this.context=ctx;
		fileCache=new FileCache(ctx);
	}
	/**
	 * 加载图片
	 * 
	 * @param imageUrl
	 * @param onloadImage
	 */
	public void loadImage(final String imageUrl, final OnloadImage onloadImage) {
		// 非法url
		if (TextUtils.isEmpty(imageUrl) || ("null").equals(imageUrl)) {
			onloadImage.loadFail();
			return;
		}

		// 文件缓存
		 String file = fileCache.getCacheFilePath(imageUrl);
		 if (null != file) {
		 // 获取bitmap
		 Bitmap bit2 = BitmapFactory.decodeFile(file);
		 if (null != bit2) {
		 onloadImage.loadFinish(bit2);
		 return;
		 }
		 }

		// 没有网络
		 if (!Utils.hasNetByQuery(context)) {
		 onloadImage.loadFail();
		 return;
		 }

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					onloadImage.loadFinish((Bitmap) msg.obj);
					break;
				case 2:
					onloadImage.loadFail();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		// 下载
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				// 下载
//				Bitmap bitmap = downloadBitmapFromNet(imageUrl);
				Message msg;
				String filePath=fileCache.addBitmapCache(imageUrl);
				if(null!=filePath){
					File file=new File(filePath);
					if (file.exists()) {
						Bitmap bitmap=BitmapFactory.decodeFile(filePath);
						msg = Message.obtain(handler, 1, bitmap);
						handler.sendMessage(msg);
						 
					}else{
						msg = Message.obtain(handler, 2, null);
						handler.sendMessage(msg);
					}
				}else{
					msg = Message.obtain(handler, 2, null);
					handler.sendMessage(msg);
				}
				
			}
		}).start();
	}

	/**
	 * �������ȡInputStream manifest�мӷ�������Ȩ��android.permission.INTERNET
	 * 
	 * @param urlStr
	 * @throws MalformedURLException
	 * @throws IOException
	 * @return InputStream
	 */
	//����ע��
	@Deprecated
	public Bitmap downloadBitmapFromNet(String urlStr) {
		try {
			Log.e("", "getInputSteamFromU-------->urlStr：" + urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			InputStream inputStream = urlConn.getInputStream();
			Log.e("",
					"getInputSteamFromUrl-------->图片流长度"
							+ urlConn.getContentLength());
			// ֱ�ӱ����bitmap
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			// bitmap.compress(CompressFormat.PNG, 100, new
			// FileOutputStream(createFile));
			// String filePath = createFile.getAbsolutePath();
			return bitmap;
		} catch (Exception e) {
			Log.e("", "getInputSteamFromNet方法中-------->为" + e);
			return null;
		}
	}

	/**
	 * 加载图片回调
	 * 
	 */
	public interface OnloadImage extends Serializable {
		/**加载完成 */
		void loadFinish(Bitmap bitmap);

		/**加载失败 */
		void loadFail();
	}

}