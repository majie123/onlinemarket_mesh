package com.online.market.admin.fragment;

import cn.bmob.v3.BmobQuery;

import com.online.market.admin.adapter.UnpackedOrderAdapter;
import com.online.market.admin.fragment.base.BaseOrderFragment;
import com.online.market.beans.OrderBean;
/***
 * 未处理订单
 * @author majie
 *
 */
public class UnpackedOrderFragment extends BaseOrderFragment {
	
	@Override
	protected void setBmobQueryCondition(BmobQuery<OrderBean> query) {
		query.addWhereEqualTo("state", OrderBean.STATE_UNTREATED);
	}


	@Override
	protected void initAdapter() {
		adapter=new UnpackedOrderAdapter(getActivity(), orders);
	};

}
