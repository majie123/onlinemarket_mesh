package com.online.market.admin;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.BaseActivity;
import com.online.market.R;
import com.online.market.admin.util.BitmapUtil;
import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.CommodityBean;
import com.online.market.utils.ProgressUtil;

public class PublishCommodityActivity extends BaseActivity {
	/**
	 * SDK初始化建议放在启动页
	 */
	public int PICK_REQUEST_CODE = 0;
	public int 	TAKE_PHOTO_CODE=1;
	
	private String [] categorys={"吃的","喝的","床上用品","用的","其他"};
	private String category;
	
	private EditText etName;
	private EditText etPrice;
	private ImageView ivPic;
	private Button btSubmit;
	private Spinner categorySpinner;
	
	private String name;
	private String price;
	
	private String picPath;
	private BmobFile bmobFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_commodity);

		initView();
		setListeners();
		initData();
	}

	@Override
	public void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("发布商品");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		etName=(EditText) findViewById(R.id.et_name);
		etPrice=(EditText) findViewById(R.id.et_price);
		ivPic=(ImageView) findViewById(R.id.iv_pic);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		categorySpinner=(Spinner) findViewById(R.id.spinner_category);
		
		ArrayAdapter< String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorys);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		categorySpinner.setAdapter(adapter);

	}

	@Override
	public void initData() {
//		Bmob.initialize(getApplicationContext(),APPID);
	}

	@Override
	public void setListeners() {
		mImgLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				category=categorys[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				name=etName.getText().toString();
				price=etPrice.getText().toString();
				if(TextUtils.isEmpty(name)){
					toastMsg("商品名称为空");
					return;
				}
				if(TextUtils.isEmpty(price)){
					toastMsg("商品价格为空");
					return;
				}
				if(picPath==null){
					toastMsg("商品图片为空");
					return;
				}
				if(category==null){
					toastMsg("商品分类为空");
					return;
				}
				 DialogUtil.dialog(PublishCommodityActivity.this, "您确认发布商品吗？", "确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							uploadFile();
							dialog.dismiss();
						}
					}, "取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			}
		});
		
		ivPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(PublishCommodityActivity.this, "选择图片方式", "拍照", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						takePhoto();
						dialog.dismiss();
						
					}
				}, "相册", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						getFileFromSD();
						dialog.dismiss();
					}
				});
				
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK&&requestCode==PICK_REQUEST_CODE) {
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);

					if (path != null) {
						saveThubPic(path);
					}
				}

			} catch (Exception e) {
				
			}

		}else if (resultCode == RESULT_OK&&requestCode==TAKE_PHOTO_CODE){
			String path = Environment.getExternalStorageDirectory()
                    + "/temp.jpg";

			if (path != null) {
				saveThubPic(path);
			}
		}
	}
	
	private void saveThubPic(final String path){
		new Thread(){
			public void run() {
				super.run();
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(path, 350);
//				int wh=Math.min(bitmap.getWidth(), bitmap.getHeight());
//				bitmap=BitmapUtil.getCanvasBitmap(bitmap, wh, wh);
				bitmap=BitmapUtil.compressImage(bitmap);
				picPath=dir+path.substring(path.lastIndexOf("/")+1);
				BitmapUtil.saveBitmapToSdcard(bitmap, picPath);
				bitmap.recycle();
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Bitmap bitmap=BitmapUtil.getOriginBitmap(picPath);
			ivPic.setImageBitmap(BitmapUtil.zoom(bitmap, 150, 150));
			
		};
	};
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_REQUEST_CODE);
	}
	
	private void takePhoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
            .getExternalStorageDirectory(),"temp.jpg")));
        startActivityForResult(intent, TAKE_PHOTO_CODE);
	}
	
    private void uploadFile() {
    	ProgressUtil.showProgress(this, "");
    	bmobFile=new BmobFile(new File(picPath));
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				publishCommodity();
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String msg) {
				toastMsg("失败：" + msg);
			}

		});

	}
    
    private void publishCommodity(){
    	
    	CommodityBean p=new CommodityBean();
    	p.setName(name);
    	p.setCategory(category);
    	p.setPrice(new Float(price));
    	p.setPics(bmobFile);
    	p.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg("发布成功");
				finish();
				ProgressUtil.closeProgress();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("失败：" + msg);
				ProgressUtil.closeProgress();
			}
		});
    	
    }
	
}
