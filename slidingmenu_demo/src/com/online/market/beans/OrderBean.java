package com.online.market.beans;

import java.util.List;

import com.online.market.admin.util.DateUtil;

import cn.bmob.v3.BmobObject;

public class OrderBean extends BmobObject {
	/**付款失败*/
	public static final int PAYMETHOD_PAYFAILED=0;
	/**支付宝已付款*/
	public static final int PAYMETHOD_ALIPAY_PAYED=1;
	/**微信已付款*/
	public static final int PAYMETHOD_WEIXIN_PAYED=2;
	/**货到付款*/
	public static final int PAYMETHOD_CASHONDELIVEY=3;
	
	/**未处理*/
	public static final int STATE_UNTREATED=0;
	/**已打包*/
	public static final int STATE_PACKED=1;
	/**已出发*/
	public static final int STATE_DEPART=2;
	/**已送达*/
	public static final int STATE_DELIVED=3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String receiver;
	private String address;
	private String phonenum;
	private float price;
	private List<ShopCartaBean> shopcarts;
	/**订单状态*/
	private int state;
	/**付款方式*/
	private int paymethod;
	/**打包者*/
	private String packer;
	/**配送者*/
	private String dispatcher;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public int getPayMethod() {
		return paymethod;
	}
	public void setPayMethod(int paymethod) {
		this.paymethod = paymethod;
	}
	public List<ShopCartaBean> getShopcarts() {
		return shopcarts;
	}
	public void setShopcarts(List<ShopCartaBean> shopcarts) {
		this.shopcarts = shopcarts;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPacker() {
		return packer;
	}
	public void setPacker(String packer) {
		this.packer = packer;
	}
	public String getDispatcher() {
		return dispatcher;
	}
	public void setDispatcher(String dispatcher) {
		this.dispatcher = dispatcher;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public boolean isOutOfTime(){
		int state = DateUtil.isOutOfTime(getCreatedAt());
		if(state==2){
			return true;
		}
		return false;
	}
	
	public boolean isHurryUp(){
		int state = DateUtil.isOutOfTime(getCreatedAt());
		if(state==1){
			return true;
		}
		return false;
	}


}
