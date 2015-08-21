package com.online.market;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.online.market.admin.AdminActivity;
import com.online.market.beans.MyUser;
import com.online.market.utils.MobileUtil;
import com.online.market.utils.ProgressUtil;
import com.umeng.update.UmengUpdateAgent;

public class LoginActivity extends BaseActivity {
	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";
	
	private EditText etPhoneNum, etUserpsw;
	private Button signin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		setListeners();
		
		initData();
	}

    protected void setListeners(){
    	signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phonenum = etPhoneNum.getText().toString();
				String psw = etUserpsw.getText().toString();
				
				boolean isMoblieLogic=MobileUtil.isMobileNO(phonenum);
				if(!isMoblieLogic){
					toastMsg("手机号非法，请输入正确的手机号");
					return;
				}
				login(phonenum, psw);
			}
		});

		mBtnTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(RegisterActivity.class);
				finish();
			}
		});
		
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
    }

	@Override
	protected void initView() {
		signin = (Button) findViewById(R.id.signin);
		etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
		etUserpsw = (EditText) findViewById(R.id.userpsw);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("登录");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		mBtnTitleRight.setText("注册");
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {
		initBmob();
		updateVersion();
		
		if(user!=null&&user.getGroup()==MyUser.GROUP_USER){
			startActivity(MainActivity.class);
			finish();
		}else if(user!=null){
			startActivity(AdminActivity.class);
			finish();
		}
	}
	
	private void initBmob(){
		Bmob.initialize(getApplicationContext(),APPID);
	      //开启debug服务后，可知晓push服务是否正常启动和运行
		BmobPush.setDebugMode(true);
  		BmobPush.startWork(this, APPID);			
	    	BmobInstallation.getCurrentInstallation(this).save();
	}
	
	private void updateVersion(){
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.forceUpdate(this);;
	}
	
	/**
	 * 登陆用户
	 */
	private void login(String phonenum,String psw) {
		ProgressUtil.showProgress(this, "");
		final MyUser bu = new MyUser();
		bu.setUsername(phonenum);
		bu.setPassword(psw);
		bu.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg(bu.getUsername() + "登陆成功");
				ProgressUtil.closeProgress();
				MyUser u=BmobUser.getCurrentUser(LoginActivity.this, MyUser.class);
				if(u.getGroup()!=MyUser.GROUP_USER){
					startActivity(AdminActivity.class);
				}
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg("登陆失败:" + msg);
				
			}
		});
	}
	
}
