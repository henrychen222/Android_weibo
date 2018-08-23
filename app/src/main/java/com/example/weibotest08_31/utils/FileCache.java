package com.example.weibotest08_31.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.weibotest08_31.config.ConfigSPF;

public class FileCache {
	private Context context;
	private ConfigSPF configSPF;
	private SharedPreferences cacheSPF;
	public final String cachePath = "/mnt/sdcard/Weibo/";
	public final String imageCacheDir = "image/";
	private File cacheFile;

	public FileCache(Context context) {
		this.context = context;
		cacheSPF = configSPF.getInstance().getConfigSPF(ConfigSPF.NAME_CACHE);
		if (hasSDCard()) {
			cacheFile = creatFilePath(cachePath + imageCacheDir);
		} else {
			cacheFile = creatFilePath(context.getCacheDir() + imageCacheDir);
		}
	}

	private File creatFilePath(String filePath) {
		return this.creatFilePath(new File(filePath));
	}

	private File creatFilePath(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/*
	 * 是否有SD卡
	 */
	public boolean hasSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public String addBitmapCache(String imageUrl) {
		String path = cacheSPF.getString(imageUrl, null);
		if (null != path) {
			File file = new File(path);
			if (file.exists()) {
				return path;
			}
		}
		// 缓存图片
		InputStream inputStream = getInputStreamFromNet(imageUrl);
		return addBitmapCache(imageUrl, inputStream);
	}

	public String addBitmapCache(String imageUrl, InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}
		// 文件是否存在
		String path = cacheSPF.getString(imageUrl, null);
		if (null != path) {
			File file = new File(path);
			if (file.exists()) {
				return path;
			}
		}

		String fileName = System.currentTimeMillis() + ".png";
		File createFile = new File(cacheFile.getAbsolutePath(), fileName);
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			// 缓存本地
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(
					createFile));
			String filePath = createFile.getAbsolutePath();
			//保存下载图片的路径
			cacheSPF.edit().putString(imageUrl, filePath).commit();
			return filePath;
		} catch (Exception e) {
			MyLog.e("", "缓存图片出错---->" + e);
		}
		return null;
	}
	 public void saveFile(Bitmap bitmap,String fileName,String imageUrl){
		 File createFile=new File(cacheFile.getAbsoluteFile(),fileName);
		 try {
			 bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(
						createFile));
				String filePath = createFile.getAbsolutePath();
				// 保存下载路径
				cacheSPF.edit().putString(imageUrl, filePath).commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }
	
	/**
	 * 加InputStream manifest权限android.permission.INTERNET
	 * @param urlStr
	 * @throws MalformedURLException
	 * @throws IOException
	 * @return InputStream
	 */
	public InputStream getInputStreamFromNet(String urlStr) {
		try {
			Log.e("", "getInputSteamFromUrl方法中-------->：urlStr为" + urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			InputStream inputStream = urlConn.getInputStream();
			Log.e("",
					"getInputSteamFromUrl方法中-------->：图片流长度："
							+ urlConn.getContentLength());
			// ֱ�ӱ����bitmap
			// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

			return inputStream;
		} catch (Exception e) {
			Log.e("", "getInputSteamFromNet方法中-------->错误" + e);
			return null;
		}
	}

	public String getCacheFilePath(String imageUrl) {
		String path = cacheSPF.getString(imageUrl, null);
		if (null != path) {
			File file = new File(path);
			if (file.exists()) {
				return path;
			}
		} else {
			cacheSPF.edit().putString(imageUrl, null).commit();
		}
		return null;
	}
}
