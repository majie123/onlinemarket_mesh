package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.OrderBean;

public class UnpackedOrderAdapter extends BaseOrderAdapter {

	public UnpackedOrderAdapter(Context context, List<OrderBean> orderBeans) {
		super(context, orderBeans);
	}
	
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		
		return super.getView(arg0, convertView, arg2);
	}

	@Override
	protected void action(final OrderBean bean) {
		btDelive.setText("打包");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "你确认已经打包了吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean,OrderBean.STATE_PACKED);
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
	}

}
