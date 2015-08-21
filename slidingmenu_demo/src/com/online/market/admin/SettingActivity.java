package com.online.market.admin;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bmob.v3.BmobUser;

import com.online.market.BaseActivity;
import com.online.market.R;
import com.online.market.admin.util.ActivityControl;
import com.online.market.admin.util.DialogUtil;
import com.online.market.admin.util.SharedPrefUtil;
import com.online.market.beans.MyUser;

public class SettingActivity extends BaseActivity {
	public static final String STATE="state";
	public static final String STATE_ONLINE="online";
	public static final String STATE_OFFLINE="offline";

	private Button btPublish,btLogout,btSetOffline,btEdit;
	private SharedPrefUtil su;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	public void initView() {
		btEdit=(Button) findViewById(R.id.bt_edit_commodity);
		btPublish=(Button) findViewById(R.id.bt_publish_commodity);
		btLogout=(Button) findViewById(R.id.bt_logout);
		btSetOffline=(Button) findViewById(R.id.bt_setoffline);

	}

	@Override
	public void initData() {
		su=new SharedPrefUtil(this, "tiantianadmin");
		setOnlineState();

		if(user.getGroup()!=MyUser.GROUP_ROOT){
			btPublish.setVisibility(View.GONE);
			btEdit.setVisibility(View.GONE);
		}else{
			btSetOffline.setVisibility(View.GONE);
		}
	}
	
	private void setOnlineState(){
		if(su.getValueByKey(STATE,STATE_ONLINE).equals(STATE_ONLINE)){
			btSetOffline.setText("设置为离线");
		}else{
			btSetOffline.setText("设置为在线");
		}
	}

	@Override
	public void setListeners() {
		
		btEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(EditCommodityActivity.class);
			}
		});
		
		btSetOffline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent=new Intent(SettingActivity.this, HeartService.class);
				if(su.getValueByKey(STATE,STATE_ONLINE).equals(STATE_ONLINE)){
					su.putValueByKey(STATE, STATE_OFFLINE);
					toastMsg("已离线,将不再接收新订单");
//					stopService(intent);
				}else{
					su.putValueByKey(STATE, STATE_ONLINE);
					toastMsg("已在线,将接收新订单");
//					startService(intent);
				}
				setOnlineState();
			}
		});

		btPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(SettingActivity.this, PublishCommodityActivity.class));
			}
		});
		
		btLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(SettingActivity.this, "您确认退出吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						BmobUser.logOut(SettingActivity.this);
						//终止掉轮询进程
//						stopService(new Intent(SettingActivity.this, HeartService.class));
						arg0.dismiss();
						List<Activity> activities=ActivityControl.getInstance().getListActivitys();
						for(Activity activity:activities){
							activity.finish();
						}
					}

				}, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
				
			}
		});
	}
	
}
