package com.online.market.admin.adapter.base;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.utils.BmobLog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.admin.util.BitmapHelp;
import com.online.market.beans.MyUser;

public abstract class MyBaseAdapter extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected BitmapUtils bitmapUtils;
	protected BitmapDisplayConfig config;
	protected DbUtils dbUtils;
	protected MyUser user;
	
	protected MyBaseAdapter(Context context) {
		this.mContext=context;
		mInflater=LayoutInflater.from(context);
		user=BmobUser.getCurrentUser(context, MyUser.class);
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		config=BitmapHelp.getDisplayConfig(mContext, 150, 150);
		dbUtils=DbUtils.create(context);
	}
	
	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(mContext, text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

	public void ShowLog(String msg) {
		BmobLog.i(msg);
	}

}
