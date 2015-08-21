package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.OrderBean;

public class PackedOrderAdapter extends BaseOrderAdapter {

	public PackedOrderAdapter(Context context, List<OrderBean> orderBeans) {
		super(context, orderBeans);
	}

	@Override
	protected void action(final OrderBean bean) {
		btDelive.setText("出发");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "你确认已经出发了吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean,OrderBean.STATE_DEPART);
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
