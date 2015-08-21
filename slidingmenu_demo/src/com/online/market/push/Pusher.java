package com.online.market.push;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
/***
 * 推送器
 * @author majie
 *
 */
public class Pusher {
	
	private BmobPushManager<BmobInstallation> bmobPush;
	
	public Pusher(Context context){
		bmobPush = new BmobPushManager<BmobInstallation>(context);
	}
	
	public void pushMessageByInstallId(String message, String installId){
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", installId);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(message);
	}

}
