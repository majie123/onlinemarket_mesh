package com.online.market.admin.util;

import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {
	
	public static void vibra(Context context){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
        vibrator.vibrate(2000); 
	}

}
