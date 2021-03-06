package com.online.market;

import com.online.market.utils.DialogUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity {

	private TextView tvAbout;
	private TextView tvFeedback;
	private Button btLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initView();
		setListeners();
	}

	@Override
	protected void initView() {
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		tvFeedback=(TextView) findViewById(R.id.feedback);
		btLogout = (Button) findViewById(R.id.logout);
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mBtnTitleMiddle.setText("设置");

		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
	}

	@Override
	protected void initData() {

	}

	protected void setListeners() {
		tvAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(AboutActivity.class);
			}
		});
		
		mImgLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		tvFeedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(FeedbackActivity.class);
			}
		});

		btLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.dialog(SettingActivity.this, "确认退出吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						BmobUser.logOut(SettingActivity.this);
						dialog.dismiss();
						finish();
					}
				}, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
				
			}
		});
	}


}
