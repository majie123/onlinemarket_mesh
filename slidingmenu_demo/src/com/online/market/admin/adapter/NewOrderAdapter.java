package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.online.market.admin.util.DialogUtil;
import com.online.market.beans.MyUser;
import com.online.market.beans.OrderBean;

public class NewOrderAdapter extends BaseOrderAdapter {

	public NewOrderAdapter(Context context, List<OrderBean> orderBeans) {
		super(context, orderBeans);
	}

	@Override
	protected void action(final OrderBean bean) {
		btDelive.setText("接单");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                DialogUtil.dialog(mContext, "你确认接单吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						if(user.getGroup()==MyUser.GROUP_PACKER){
							update(bean, FIELD_PACKER, user.getUsername());
						}else if(user.getGroup()==MyUser.GROUP_DISPATCHER){
							update(bean, FIELD_DISPATCHER, user.getUsername());
						}
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
