package com.online.market.admin.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 提供对Activit的管理
 * 
 * @author wupan
 *
 */
public class ActivityControl {
	private static ActivityControl activityManager;
	private static List<Activity> listActivitys;

	public static ActivityControl getInstance() {
		if (activityManager == null) {
			activityManager = new ActivityControl();
			listActivitys = new ArrayList<Activity>();
		}
		return activityManager;
	}

	/**
	 * 加入activity到集合
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		listActivitys.add(activity);
	}

	/**
	 * 从集合移除activity
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		listActivitys.remove(activity);
	}

	/**
	 * 清空Activity管理者里面的activity
	 */
	public void clearActivity() {
		listActivitys.clear();
	}

	/**
	 * 得到Activity集合
	 * 
	 * @return
	 */
	public List<Activity> getListActivitys() {
		return listActivitys;
	}

	/**
	 * 得到当前的Activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		if (listActivitys.size() > 0) {
			return listActivitys.get(listActivitys.size() - 1);
		}
		return null;
	}
}
