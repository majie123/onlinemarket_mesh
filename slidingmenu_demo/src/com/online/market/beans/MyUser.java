package com.online.market.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**超级用户*/
	public static int GROUP_ROOT=3;
	/**打包者*/
	public static int GROUP_PACKER=2;
	/**配送者*/
	public static int GROUP_DISPATCHER=1;
	/**普通用户*/
	public static int GROUP_USER=0;
	
	private String nickname;
	private BmobFile avatar;
	private int group;
	
	/***
	 * 我的邀请码
	 */
	private String inviteCode;
	/***
	 * 我的邀请人的邀请码
	 */
	private String byInviteCode;
	/***
	 * 是否消费过
	 */
	private boolean isCousumed=false;
	
	private String installId;
	
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getByInviteCode() {
		return byInviteCode;
	}
	public void setByInviteCode(String byInviteCode) {
		this.byInviteCode = byInviteCode;
	}
	public boolean isCousumed() {
		return isCousumed;
	}
	public void setCousumed(boolean isCousumed) {
		this.isCousumed = isCousumed;
	}
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String realname) {
		this.nickname = realname;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public String getInstallId() {
		return installId;
	}
	public void setInstallId(String installId) {
		this.installId = installId;
	}
	
}
