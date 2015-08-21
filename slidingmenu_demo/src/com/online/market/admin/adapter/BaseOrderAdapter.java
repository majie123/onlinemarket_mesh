package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.online.market.R;
import com.online.market.admin.adapter.base.MyBaseAdapter;
import com.online.market.admin.adapter.base.ViewHolder;
import com.online.market.admin.util.DateUtil;
import com.online.market.beans.CouponBean;
import com.online.market.beans.MyUser;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.push.Pusher;
import com.online.market.utils.ProgressUtil;

public abstract class BaseOrderAdapter extends MyBaseAdapter {
//	public static final int FIELD_STATE=0;
	public static int FIELD_PACKER=1;
	public static int FIELD_DISPATCHER=2;

	private List<OrderBean > orderBeans;
	protected TextView tvOrderTime;
	protected Button btDelive;

	public BaseOrderAdapter(Context context,List<OrderBean > orderBeans) {
		super(context);
		this.orderBeans=orderBeans;
	}

	@Override
	public int getCount() {
		return orderBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.admin_myorder_item, null);
		}
		TextView tvOrderDetail=ViewHolder.get(convertView, R.id.orderdetail);
		TextView tvOrderName=ViewHolder.get(convertView, R.id.ordername);
		TextView tvOrderAddress=ViewHolder.get(convertView, R.id.orderaddress);
		TextView tvOrderPhonenum=ViewHolder.get(convertView, R.id.orderphonenum);
		TextView tvOrderPaymethod=ViewHolder.get(convertView, R.id.orderpaymethod);
		tvOrderTime=ViewHolder.get(convertView, R.id.ordertime);
		btDelive=ViewHolder.get(convertView, R.id.delive);
		ImageView ivCall=ViewHolder.get(convertView, R.id.iv_call);
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText(bean.getPhonenum());
        if(bean.getPayMethod()==OrderBean.PAYMETHOD_PAYFAILED){
        	tvOrderPaymethod.setText("付款失败");
        }else if(bean.getPayMethod()==OrderBean.PAYMETHOD_CASHONDELIVEY){
        	tvOrderPaymethod.setText("货到付款：需支付 "+bean.getPrice()+" 元");
        }else {
        	tvOrderPaymethod.setText("在线已支付");
        }        
        String time=DateUtil.getDate(bean.getCreatedAt());
        tvOrderTime.setText(time);
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	detail+=p.getName()+" X "+p.getNumber()+"\n";
        }
    	tvOrderDetail.setText("商品： "+detail);
    	ivCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bean.getPhonenum()));  
                mContext.startActivity(intent); 
			}
		});
    	
    	action(bean);
    	
		return convertView;
	}
	
	/**getview中的抽象方法*/
	protected abstract void action(final OrderBean bean);
	
	protected void update(final OrderBean bean,int field,String value){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		if(field==FIELD_PACKER){
			p.setPacker(value);
			p.setDispatcher(bean.getDispatcher());
		}else if(field==FIELD_DISPATCHER){
			p.setDispatcher(value);
			p.setPacker(bean.getPacker());
		}
		p.setPayMethod(bean.getPayMethod());
		p.setState(bean.getState());
		p.setPrice(bean.getPrice());
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				notifyDataSetChanged();
				ShowToast("成功");
				ProgressUtil.closeProgress();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("失败 "+arg1);
				ProgressUtil.closeProgress();

			}
		});
	}
	
	/**更新order的状态*/
	protected void update(final OrderBean bean,int state){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		p.setDispatcher(bean.getDispatcher());
		p.setPacker(bean.getPacker());
		p.setPayMethod(bean.getPayMethod());
		p.setState(state);
		p.setPrice(bean.getPrice());
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				notifyDataSetChanged();
				ShowToast("成功");
				ProgressUtil.closeProgress();
				if(bean.getState()==OrderBean.STATE_DEPART){
					rewardInvitor(bean.getUsername());
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("失败 "+arg1);
				ProgressUtil.closeProgress();

			}
		});
		
	}
	
	/***
	 * 奖励推荐人
	 */
	private void rewardInvitor(String username){
		BmobQuery<MyUser> bmobQuery=new BmobQuery<MyUser>();
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(mContext, new FindListener<MyUser>() {
			
			@Override
			public void onSuccess(List<MyUser> users) {
				if(users.size()!=0&&!users.get(0).isCousumed()){
					//更新已消费过，下次消费不再奖励邀请人优惠券
					MyUser user=users.get(0);
					MyUser myUser=new MyUser();
					myUser.setUsername(user.getUsername());
					myUser.setInviteCode(user.getInviteCode());
					myUser.setByInviteCode(user.getByInviteCode());
					myUser.setCousumed(true);
					myUser.setNickname(user.getNickname());
					myUser.setGroup(user.getGroup());
					myUser.setObjectId(user.getObjectId());
					myUser.update(mContext);
					
					BmobQuery<MyUser> bmobQuery=new BmobQuery<MyUser>();
					bmobQuery.addWhereEqualTo("inviteCode", users.get(0).getByInviteCode());
					bmobQuery.findObjects(mContext, new FindListener<MyUser>() {

						@Override
						public void onError(int arg0, String arg1) {
							Log.i("majie", "query invitecode fail");
						}

						@Override
						public void onSuccess(final List<MyUser> users) {
							if(users.size()!=0){
								CouponBean coupon=new CouponBean();
								coupon.setLimit(10);
								coupon.setAmount(5);
								coupon.setType(CouponBean.COUPON_TYPE_ONSALE);
								coupon.setUsername(users.get(0).getUsername());
								coupon.save(mContext, new SaveListener() {
									
									@Override
									public void onSuccess() {
										Log.i("majie", "coupon insert success");
										Pusher pusher=new Pusher(mContext);
										pusher.pushMessageByInstallId("奖励你了。", users.get(0).getInstallId());
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										Log.i("majie", "coupon insert fail");
									}
								});
							}
						}
					});
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i("majie", "query user fail");
			}
		});
	}

}
