package com.online.market.admin.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	public static void dialog(Context context,String msg,String title,String positiveText,OnClickListener positiveListener,String negativeText,OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);

		if(title!=null){
			builder.setTitle(title);
		}
		
		builder.setPositiveButton(positiveText, positiveListener);
		builder.setNegativeButton(negativeText, negativeListener);
		builder.create().show();
	}
	public static void dialog(Context context,String msg,String positiveText,OnClickListener positiveListener,String negativeText,OnClickListener negativeListener) {
		dialog(context, msg, null, positiveText, positiveListener, negativeText, negativeListener);
	}
	
	public static void dialog(Context context,String msg,String [] items,DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(msg);
		builder.setSingleChoiceItems(items,0,listener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		builder.create().show();
	}
	
}
