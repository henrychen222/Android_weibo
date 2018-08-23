package com.example.weibotest08_31.utils;

import android.util.Log;

import com.example.weibotest08_31.config.Constant;

public class MyLog {

	public static void v(String tag, String msg) {
		if (Constant.mDebug) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (Constant.mDebug) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (Constant.mDebug) {
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (Constant.mDebug) {
			Log.w(tag, msg);
		}
	}
	public static void e(String tag, String msg) {
		if (Constant.mDebug) {
			Log.e(tag, msg);
		}
	}
}
