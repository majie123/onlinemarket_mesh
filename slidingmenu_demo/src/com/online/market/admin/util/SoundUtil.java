package com.online.market.admin.util;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

public class SoundUtil {
	
	public static void soundRing(Context context) {  
		  
	    MediaPlayer mp = new MediaPlayer();  
	    mp.reset();  
	    try {
			mp.setDataSource(context,  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			 mp.prepare();  		
	    } catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    mp.start();  
	} 

}
