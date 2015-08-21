package com.online.market.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

public class DateUtil {
	
	public static String getDate(String str){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date=format.parse(str);
			date.setTime(date.getTime()+30*60*1000);
			Date currentDate=new Date();
			long currentTime=currentDate.getTime();
			long time=date.getTime()-currentTime;
			if(time<0){
				return "订单已超时 下单时间 "+str;
			}else{
				date.setTime(time);
				SimpleDateFormat f=new SimpleDateFormat("需在 mm分ss秒 之内送达");
				return f.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 检验是否超时，返回2为超时，返回1为加急
	 * @param str
	 * @return
	 */
	public static int isOutOfTime(String str){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date=format.parse(str);
			date.setTime(date.getTime()+30*60*1000);
			Date currentDate=new Date();
			long currentTime=currentDate.getTime();
			long time=date.getTime()-currentTime;
			if(time<0){
				return 2;
			}else if(time <10*60*1000){
				return 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
