package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.OrderBean;

public class DepartedOrderAdapter extends BaseOrderAdapter {

	public DepartedOrderAdapter(Context context, List<OrderBean> orderBeans) {
		super(context, orderBeans);
	}

	@Override
	protected void action(final OrderBean bean) {
		btDelive.setText("送达");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "你确认已经送达了吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean,OrderBean.STATE_DELIVED);
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
