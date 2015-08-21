package com.online.market.admin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 暂时用户配置信息
 * @author wanghaoyuew
 *
 */
public class SharedPrefUtil {

	public static final String PREFERENCE_NAME_CONFIG_APP = "config_app_preference";
	public static final String PREFERENCE_NAME_CONFIG = "config_preference";
	public static final String PREFERENCE_NAME_PRICE_ALL = "price_all_preference";
	public static final String PREFERENCE_COUPON_REGION = "coupon_region_preference";
	public static final String PREFERENCE_BUS_CITY = "bus_city_preference";

	private SharedPreferences mPreferences;
	
	public SharedPrefUtil(Context context, String prereceName) {
		if(null == context || TextUtils.isEmpty(prereceName)) {
			return;
		}
		
		mPreferences = context.getSharedPreferences(prereceName, Context.MODE_PRIVATE);
	}
	
	public String getValueByKey(String key, String defaultValue){
		return mPreferences.getString(key, defaultValue);
	}
	
	public synchronized void putValueByKey(String key, String value){
		mPreferences.edit().putString(key, value).commit();
	}

	public void clear(){
		mPreferences.edit().clear().commit();
	}
	
}