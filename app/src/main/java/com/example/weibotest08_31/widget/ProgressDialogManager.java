package com.example.weibotest08_31.widget;

import android.content.Context;

public class ProgressDialogManager {
	private static ProgressDialog progressDialog;

	/**
	 * 开始进度条
	 * @param mContext
	 * @param message
	 */
	public static void startProgressBar(Context mContext, String message) {
		try {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.setMsg(message);
			} else {
				progressDialog = new ProgressDialog(mContext, message);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ֹͣ停止进度条
	 */
	public static void stopProgressBar() {
		try {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog.hide();
				progressDialog.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否在显示
	 * @return
	 */
	public static boolean isShowing() {
		if (null == progressDialog) {
			return false;
		} else if (progressDialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否能取消
	 * @param bo
	 */
	public static void setCancelable(boolean bo) {
		if (bo == true) {
			progressDialog.setCancelable(true);
		} else {
			progressDialog.setCancelable(false);
		}
	}
}
