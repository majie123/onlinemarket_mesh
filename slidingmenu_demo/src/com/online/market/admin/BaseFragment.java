package com.online.market.admin;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.online.market.beans.MyUser;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {
	protected MyUser user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user=BmobUser.getCurrentUser(getActivity(), MyUser.class);
	}
	
	public abstract void initViews();
	public abstract void initData();
	public abstract void setListeners();
	
	protected void toastMsg(int msgId) {
		toastMsg(getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainScreen"); 
	}

}
