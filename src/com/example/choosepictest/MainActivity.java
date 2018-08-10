package com.example.choosepictest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private Button takePhoto;
	private ImageView picture;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		takePhoto = (Button) findViewById(R.id.take_photo);
		picture = (ImageView) findViewById(R.id.picture);
		takePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 创建File对象，用于存储拍照后的图片
				File outputImage = new File(Environment
						.getExternalStorageDirectory(), "tempImage.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO); // 启动相机程序
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 0 .,resultCode:"+resultCode); 
		switch (requestCode) {
		case TAKE_PHOTO:
			Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 0.1 ."); 
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");//图片路径和选择图片
				intent.putExtra("scale", true);//缩放设置
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定拍照的输出地址
				startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
				Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 1 ."); 
			}
			break;
		case CROP_PHOTO:
			Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 1.1 ."); 
				Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 1.2 ."); 
				try {
					
					InputStream in = getContentResolver().openInputStream(imageUri);
					Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 2 .");  
					/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
					in.compress(Bitmap.CompressFormat.JPEG, 100, baos);//arg1为传进来的原始bitmap
					 baos.toByteArray();
					 InputStream is = new ByteArrayInputStream(baos.toByteArray());
					 //进行缩放
					 BitmapFactory.Options newOpts = new BitmapFactory.Options();
					 // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
					 newOpts.inJustDecodeBounds = true;
					 Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, newOpts);// 此时返回bm为空
					
					//Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
					picture.setImageBitmap(bitmap);*/
					Bitmap bitmap = BitmapFactory.decodeStream(in);
					Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 3 .");  
					picture.setImageBitmap(bitmap); // 将裁剪后的照片显示出来
				} catch (FileNotFoundException e) {
					Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 4 .");  
					e.printStackTrace();
				}
				Log.d(MainActivity.ACTIVITY_SERVICE, "James add in onActivityResult Debug 5 ."); 
			
			break;
		default:
			break;
		}
	}
}
