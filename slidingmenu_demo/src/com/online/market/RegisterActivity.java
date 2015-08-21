package com.online.market;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.beans.MyUser;
import com.online.market.utils.BitmapUtil;
import com.online.market.utils.FileUtils;
import com.online.market.utils.MobileUtil;
import com.online.market.utils.ProgressUtil;

public class RegisterActivity extends BaseActivity {
	/**从sd卡读取图片*/
	public int PICK_REQUEST_CODE = 0;
	
	private EditText etPhoneNum, etUserpsw, etNickname,etCode;
	
	private String phonenum ;
	private String userpsw ;
	private String nickname ;
	private String code;
	
	private ImageView userimg;
	private String avatarPath;
	private Button btSubmit;
	private Button btVerify;
	
//	private String DEVICE_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_register);

		initView();
        setListeners();
        
        initData();
	}
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.setDataAndType(Uri.parse("/mnt/sdcard"), "text/plain");

		startActivityForResult(intent, PICK_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
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
					    Bitmap b= BitmapUtil.getThumbilBitmap(path,200);
					    userimg.setImageBitmap(b);
					    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
					    FileUtils.mkdirs(dir);
					    avatarPath=dir+path.substring(path.lastIndexOf("/")+1);
					    BitmapUtil.saveBitmapToSdcard(b, avatarPath);
					    
					}
				}

			} catch (Exception e) {
			}

		}
	}

	@Override
	protected void initView() {
		
		etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
		etUserpsw = (EditText) findViewById(R.id.userpsw);
		etNickname = (EditText) findViewById(R.id.et_nickname);
		etCode =(EditText) findViewById(R.id.et_code);
		userimg = (ImageView) findViewById(R.id.userimg);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		btVerify=(Button) findViewById(R.id.btn_verify);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("用户注册");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
	}

	@Override
	protected void initData() {
//		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
//		DEVICE_ID = tm.getDeviceId(); 
	}
	
	protected void setListeners(){
		btSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				phonenum = etPhoneNum.getText().toString();
				userpsw = etUserpsw.getText().toString();
				nickname = etNickname.getText().toString();
				code=etCode.getText().toString();
				
				if (TextUtils.isEmpty(phonenum) || TextUtils.isEmpty(userpsw) || TextUtils.isEmpty(nickname)||TextUtils.isEmpty(code)
						) {
					toastMsg("请填写基本资料");
					return;
				}
				signUp();
			}
		});

		userimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFileFromSD();
			}
		});
		
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        
        btVerify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phonenum = etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenum)){
					toastMsg("手机号为空");
					return;
				}
				boolean isMoblieLogic=MobileUtil.isMobileNO(phonenum);
				if(!isMoblieLogic){
					toastMsg("手机号非法，请填写正确的手机号");
					return;
				}
				start();
				
				BmobSMS.requestSMSCode(RegisterActivity.this, phonenum, "register",new RequestSMSCodeListener() {
					
					@Override
					public void done(Integer smsId, BmobException ex) {
						
						if(ex==null){//验证码发送成功
							Log.i("onlinemarket", "短信id："+smsId);
						}
					}
				});
			}
		});
	}
	
	/**
	 * 注册用户
	 */
	private void signUp() {
		ProgressUtil.showProgress(this, "");
		MyUser.signOrLoginByMobilePhone(this, phonenum, code, new LogInListener<MyUser>() {

			@Override
			public void done(MyUser user, BmobException e) {
				if(user!=null){
//					toastMsg("登录成功");
					Log.i("smile", ""+user.getUsername());
					if(avatarPath==null){
						update(null);
					}else{
						uploadAvatarFile();
					}
				}else{
					toastMsg("错误码："+e.getErrorCode());
					ProgressUtil.closeProgress();
				}
			}
		});
	}
	
	private void update(BmobFile file) {
		final MyUser myUser = new MyUser();
		myUser.setUsername(phonenum);
		myUser.setNickname(nickname);
		myUser.setPassword(userpsw);
		myUser.setInviteCode(phonenum.substring(5));
		BmobInstallation in=BmobInstallation.getCurrentInstallation(this);
		myUser.setInstallId(in.getInstallationId());
		if(file!=null){
			myUser.setAvatar(file);
		}
		user=BmobUser.getCurrentUser(this, MyUser.class);
		myUser.setObjectId(user.getObjectId());
		myUser.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
 
				toastMsg("登录成功");
				ProgressUtil.closeProgress();
				startActivity(InviteCodeActivity.class);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastMsg(arg1);
				ProgressUtil.closeProgress();
				
			}
		});
		
	}
	
	private void uploadAvatarFile() {
		final BmobFile bmobFile = new BmobFile(new File(avatarPath));
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				update(bmobFile);
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String arg1) {
			}

		});
	}
	
	private void start(){
		btVerify.setEnabled(false);
		btVerify.setBackgroundColor(getResources().getColor(R.color.text_gray));
		new Thread(){
			@Override
			public void run() {
				super.run();
				int i=120;
				while(i>0){
					i--;
					mHandler.sendEmptyMessage(i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				btVerify.setEnabled(true);
				btVerify.setText("验证");
				btVerify.setBackgroundResource(R.drawable.btn_orange_corner_selector);
				
			}else{
				btVerify.setText(msg.what+"秒");
			}
		};
	};

}
