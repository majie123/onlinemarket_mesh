package com.online.market.admin;

import java.io.File;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.BaseActivity;
import com.online.market.R;
import com.online.market.admin.util.BitmapUtil;
import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.CommodityBean;
import com.online.market.utils.ProgressUtil;

public class EditCommodityActivity extends BaseActivity {
	
	public int PICK_REQUEST_CODE = 0;
	public int 	TAKE_PHOTO_CODE=1;
	
	private String [] categorys={"吃的","喝的","床上用品","用的","其他"};
	private String category;
	
	private EditText etName;
	private EditText etPrice;
	private ImageView ivPic;
	private Button btSubmit;
	private Button btDelete;

	private Spinner categorySpinner;
	private EditText etSearch;
	private CommodityBean commodity;
	
	private String name;
	private String price;
	
	private String picPath;
	private BmobFile bmobFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_commodity);

		initView();
		setListeners();
		initData();
	}

	@Override
	public void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("编辑商品");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		etName=(EditText) findViewById(R.id.et_name);
		etPrice=(EditText) findViewById(R.id.et_price);
		ivPic=(ImageView) findViewById(R.id.iv_pic);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		btDelete=(Button) findViewById(R.id.bt_delete);

		categorySpinner=(Spinner) findViewById(R.id.spinner_category);
		etSearch=(EditText) findViewById(R.id.et_search);
		
		ArrayAdapter< String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorys);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		categorySpinner.setAdapter(adapter);

	}

	@Override
	public void initData() {
	}

	@Override
	public void setListeners() {
		mImgLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				queryCommoditys("name", arg0.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
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
				if(commodity==null){
					toastMsg("请先搜索商品");
					return;
				}
				name=etName.getText().toString();
				price=etPrice.getText().toString();
				if(TextUtils.isEmpty(name)){
					toastMsg("商品名字为空");
					return;
				}
				if(TextUtils.isEmpty(price)){
					toastMsg("商品价格为空");
					return;
				}
				if(category==null){
					toastMsg("商品类别为空");
					return;
				}
                DialogUtil.dialog(EditCommodityActivity.this, "您确认修改商品吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						if(picPath==null){
							editCommodity();
						}else{
							commodity.getPics().delete(EditCommodityActivity.this);
							uploadFile();
						}
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
		
		btDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(commodity==null){
					toastMsg("请先搜索商品");
					return;
				}
				DialogUtil.dialog(EditCommodityActivity.this, "您确认删除商品吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						commodity.getPics().delete(EditCommodityActivity.this);
						commodity.delete(EditCommodityActivity.this, new DeleteListener() {
							
							@Override
							public void onSuccess() {
								toastMsg("删除成功");
								finish();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								toastMsg("删除失败 "+arg1);
							}
						});
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
				DialogUtil.dialog(EditCommodityActivity.this, "选择图片方式", "拍照", new DialogInterface.OnClickListener() {
					
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
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(path, 200);
				int wh=Math.min(bitmap.getWidth(), bitmap.getHeight());
				bitmap=BitmapUtil.getCanvasBitmap(bitmap, wh, wh);
				picPath=dir+path.substring(path.lastIndexOf("/")+1);
				BitmapUtil.saveBitmapToSdcard(bitmap, picPath);
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
				editCommodity();
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
    
    private void editCommodity(){
    	
    	CommodityBean p=new CommodityBean();
    	p.setName(name);
    	p.setCategory(category);
    	p.setPrice(new Float(price));
    	p.setPics(bmobFile);
    	p.setSold(commodity.getSold());
    	p.setObjectId(commodity.getObjectId());
    	p.update(this, new UpdateListener() {
			
    		@Override
			public void onSuccess() {
				toastMsg("编辑成功");
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
    
    private void queryCommoditys(String value,String key){
    	ProgressUtil.showProgress(this, "");
		BmobQuery<CommodityBean> query	 = new BmobQuery<CommodityBean>();
		if(key!=null){
			query.addWhereContains(value, key);
		}
		query.setLimit(10);
		query.findObjects(this, new FindListener<CommodityBean>() {

			@Override
			public void onSuccess(List<CommodityBean> object) {
				ProgressUtil.closeProgress();
				if(object.size()==0){
					toastMsg("暂无相关商品");
					return;
				}
				commodity=object.get(0);
				etName.setText(commodity.getName());
				etPrice.setText(""+commodity.getPrice());
				bitmapUtils.display(ivPic, commodity.getPics().getFileUrl(EditCommodityActivity.this), config);
	            		
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
				
			}
		});	
		
	}

}
