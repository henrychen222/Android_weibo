package com.example.weibotest08_31.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class MyToast {
	private static Toast mToast;
	private static Handler handler = new Handler();

	private static Runnable runnable = new Runnable() {

		@Override
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text) {
		showToast(mContext, text, 1000);
	}

	public static void showToast(Context context, String text, int duration) {
		if (context == null && text != null) {
			return;
		}
		handler.removeCallbacks(runnable);
		if (mToast == null) {
			mToast = Toast.makeText(context, text, duration);
		}
		mToast.setText(text);
		if (duration == Toast.LENGTH_SHORT) {
			duration = 1000;
		} else if (duration == Toast.LENGTH_LONG) {
			duration = 2000;
		} else {
			duration = 2000;
		}
		handler.postDelayed(runnable, duration);
		mToast.show();
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getResources().getString(resId), 1000);
	}

	public static void showToast(Context context, int resId, int duration) {
		showToast(context, context.getResources().getString(resId), duration);
	}
}
