package com.online.market.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.online.market.BaseActivity;
import com.online.market.LoginActivity;
import com.online.market.R;
import com.online.market.admin.fragment.CompletedOrderFragment;
import com.online.market.admin.fragment.DepartedOrderFragment;
import com.online.market.admin.fragment.PackedOrderFragment;
import com.online.market.admin.fragment.UnpackedOrderFragment;
import com.online.market.admin.fragment.base.BaseOrderFragment;
import com.online.market.beans.MyUser;

public class AdminActivity extends BaseActivity {
//	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private BaseOrderFragment utFragment;
	private BaseOrderFragment cmFragment;
	private BaseOrderFragment paFragment;
	private BaseOrderFragment dpFragment;

	private Button btUnPacked,btPacked,btCompleted,btDeparted;
//	private Button btSet;
	private Button lastBt;
//	private BmobPushManager<BmobInstallation> bmobPush;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		initView();
		setListeners();
		initData();
		
	}

	@Override
	public void initView() {

		btPacked=(Button) findViewById(R.id.bt_packedorder);
		btUnPacked=(Button) findViewById(R.id.bt_unpackedorder);
		btCompleted=(Button) findViewById(R.id.bt_completedorder);
		btDeparted=(Button) findViewById(R.id.bt_departedorder);
		
//		btSet=(Button) findViewById(R.id.bt_set);
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("管理端");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("设置");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));

	}

	@Override
	public void initData() {
//		Bmob.initialize(getApplicationContext(),APPID);
//		MobclickAgent.updateOnlineConfig(this);
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.forceUpdate(this);

		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}

		if(user.getGroup()==MyUser.GROUP_PACKER){
			btPacked.setVisibility(View.GONE);
			btDeparted.setVisibility(View.GONE);
			
			initLastBt(btUnPacked);
			utFragment=new UnpackedOrderFragment();
			replaceFragment(utFragment);
			
		}else if(user.getGroup()==MyUser.GROUP_DISPATCHER){
			btUnPacked.setVisibility(View.GONE);
			
			initLastBt(btPacked);
			paFragment=new PackedOrderFragment();
			replaceFragment(paFragment);
			
		}
		
		if(user.getGroup()!=MyUser.GROUP_ROOT){
//			SharedPrefUtil su=new SharedPrefUtil(this, "tiantianadmin");
//			if(su.getValueByKey(SettingActivity.STATE, SettingActivity.STATE_ONLINE).equals(SettingActivity.STATE_ONLINE)){
//				startService(new Intent(this, HeartService.class));
//			}
		}
	}

	@Override
	public void setListeners() {

		btCompleted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(cmFragment==null){
					cmFragment=new CompletedOrderFragment();
//				}
				initLastBt(btCompleted);
				replaceFragment(cmFragment);
			}
		});
		
        btPacked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(cmFragment==null){
					paFragment=new PackedOrderFragment();
//				}
				initLastBt(btPacked);
				replaceFragment(paFragment);
			}
		});
		
        btUnPacked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(utFragment==null){
					utFragment=new UnpackedOrderFragment();
//				}
				initLastBt(btUnPacked);
				replaceFragment(utFragment);
			}
		});
        
        btDeparted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(utFragment==null){
					dpFragment=new DepartedOrderFragment();
//				}
				initLastBt(btDeparted);
				replaceFragment(dpFragment);
			}
		});
        
        mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(SettingActivity.class);
			}
		});
        
	}
	
//	private void pushAndroidMessage(String message, String installId){
//		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
//		query.addWhereEqualTo("installationId", installId);
//		bmobPush.setQuery(query);
//		bmobPush.pushMessage(message);
//	}
	
	private void initLastBt(Button bt){
		if(lastBt!=null){
			lastBt.setTextColor(0xffffffff);
		}
		bt.setTextColor(0xffFF6100);
		lastBt=bt;
	}
	
	private void replaceFragment(Fragment fragment)  
    {  
        FragmentManager fm = getFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();  
        transaction.replace(R.id.id_content, fragment);
        transaction.commit();  
    } 
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
		}
	}

}
