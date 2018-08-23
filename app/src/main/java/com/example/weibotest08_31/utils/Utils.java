package com.example.weibotest08_31.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static PackageInfo getVersion(Context mCtx){
		PackageInfo packageInfo = null;
		try {
			packageInfo = mCtx.getPackageManager().getPackageInfo(mCtx.getPackageName(), 0);
		} catch (Exception e) {
		}
		
		return packageInfo;
	}
	
	public static boolean hasNetByQuery(Context mContext){
		ConnectivityManager cwjManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = cwjManager.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

}
