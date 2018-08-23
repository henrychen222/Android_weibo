package com.example.weibotest08_31.acty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.widget.ProgressDialogManager;
import com.example.weibotest08_31.widget.TitleBar;

public class SendWeiboActy extends BaseActy {
	private DotNetManager dotNetManager;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private String savePicPath;
	private String picname="";
	private EditText contentET;
	public static final int TakePhoto = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PhotoResult = 3;// 结果result

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_send_weibo);
		initVar();
		mTitleBar = (TitleBar)findViewById(R.id.asw_tb_title);
		findViewById(R.id.asw_iv_photo).setOnClickListener(this);
		findViewById(R.id.asw_iv_takephoto).setOnClickListener(this);
		mTitleBar.leftIV.setOnClickListener(this);
		mTitleBar.rightBT.setOnClickListener(this);
	}

	private void initVar() {
		initTitleBar(R.id.asw_tb_title, null, "发送", "发微博");
		savePicPath = "mnt/sdcard/download/imageCache/";
		dotNetManager=new DotNetManager();
		contentET=(EditText)findViewById(R.id.asw_et_content);
		File file = new File(savePicPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		System.out.println("TakePhotos中onActivityResult方法-------------->");
		//
		if (resultCode == 0)
			return;
		// 1.拍照
		if (requestCode == TakePhoto) {
			System.out.println("拍照");
			// 获取指定文件/mnt/sdcard getPhotoFileName() "/temp.jpg"+"/"+
			File picture = new File(savePicPath, picname);
			// 取到拍照保存的图片的URL，调用startPhotoZoom方法，设置自定义剪切图片
			startPhotoZoom(Uri.fromFile(picture));
			
		}
		// 拍照后没有数据
		if (intent == null)
			return;
		

		// 2. 读取系统相册缩放图片
		if (requestCode == PHOTOZOOM) {
			System.out.println("读取系统相册缩放图片");
			// 取到系统自带的uri，调用startPhotoZoom剪切图片
			startPhotoZoom(intent.getData());
		}
		// 3. 处理结果
		if (requestCode == PhotoResult) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				// Parcel包裹，打包
				Bitmap photo = extras.getParcelable("data");
				// 设置图片
//				imageIV.setImageBitmap(photo);
				
				try {
					// 保存到本地
					saveFile(photo, picname);
				} catch (Exception e) {
				}

			}
		}
	}

	public void saveFile(Bitmap bm, String fileName) throws IOException {
		Log.e("","----saveFile---->" + fileName);
		File myCaptureFile = new File(savePicPath, fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
		bos.flush();
		bos.close();
		
	}
	/**
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		System.out.println("进入startPhotoZoom(uti)方法uri为------------------->>>" + uri);
		// 进入startPhotoZoom(uti)方法uri为------------------->>>content://media/external/images/media/6
		// crop收割，修剪；调用系统的修剪工具
		Intent intent = new Intent("com.android.camera.action.CROP", null);
		//在intent上绑定图片数据data  "image/*"
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		// crop收割，修剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 宽高
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 裁剪图片的宽高
		intent.putExtra("outputX", 300);// 输出图片款
		intent.putExtra("outputY", 300);// 高
		intent.putExtra("return-data", true);
		// Activity中的方法
		startActivityForResult(intent, PhotoResult);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.it_iv_left:
			finish();
			return;
		case R.id.asw_iv_photo:
			picname = System.currentTimeMillis() + ".png";
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			//系统调用：外部——内部_URI，image/*此intent上携带的相册数据data,(data的uri,image/*)
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			// 启动activity等待返回
			startActivityForResult(intent, PHOTOZOOM);

			break;
		case R.id.asw_iv_takephoto:
			picname = System.currentTimeMillis() + ".png";
//			picName = getPicName();
			// 相册名位数
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 保存过程EXTRA_OUTPUT:额外的输出;//设置图片保存路径：SD卡根目录，文件名
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePicPath, picname)));
			startActivityForResult(intent2, TakePhoto);
			break;
		case R.id.it_bt_right:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
				ProgressDialogManager.startProgressBar(SendWeiboActy.this, "发送中...");
				dotNetManager.sendWeibo(getWeiboApp().getUser().getId()+"", contentET.getText().toString(), savePicPath, picname);
				}
			}).start();
			ProgressDialogManager.stopProgressBar();
			finish();
			break;
		default:
			break;
		}
	}

}
