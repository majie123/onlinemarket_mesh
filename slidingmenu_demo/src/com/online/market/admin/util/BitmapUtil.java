package com.online.market.admin.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class BitmapUtil {
	
	public static Bitmap getOriginBitmap(String path){
		try {
			Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(path));
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap getThumbilBitmap(String srcPath,int width)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
        if (bitmap == null)
        {
//        	Log.e("majie", "bitmap为空");
//        	return null;
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
//        Log.e("majie", "真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        int scale = (int) ((realHeight < realWidth ? realHeight : realWidth) / width);
        if (scale <= 0)
        {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        bitmap = BitmapFactory.decodeFile(srcPath, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
//        Log.e("majie", "缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }
	
	public static void saveBitmapToSdcard(Bitmap bitmap ,String desPath){
		File file=new File(desPath);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	   /***
     * 将图片画在画布中间
     * @param bm
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getCanvasBitmap(Bitmap bm,int width,int height){
    	float xscale=width/bm.getWidth();
    	float yscale=height/bm.getHeight();
    	if(xscale>yscale){
    		bm=zoomByWidth(bm, width);
    	}else{
    		bm=zoomByHeight(bm, height);
    	}
    	Bitmap bitmap=Bitmap.createBitmap(width, height, Config.ARGB_8888);
    	Canvas canvas=new Canvas(bitmap);
    	int left=(width-bm.getWidth())/2;
    	int top=(height-bm.getHeight())/2;
    	canvas.drawBitmap(bm, left, top, null);
    	canvas.save(Canvas.ALL_SAVE_FLAG);
    	canvas.restore();
    	return bitmap;
    }
    
    public static Bitmap getBitmapFromRes(Context context,int resId){
    	Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), resId);
    	return bitmap;
    }
    
    /**
	 * 缩放图片
	 * @param bitmap	需要进行缩放的图片
	 * @param wf		在宽度上缩放的比例
	 * @param hf		在高度上缩放的比例
	 * @return			返回缩放后的图片对象
	 */
	public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
		Matrix matrix = new Matrix();
		matrix.postScale(wf, hf);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * 缩放图片
	 * @param bitmap	需要进行缩放的图片
	 * @param newWidth	新图片的宽度，如果只按照高度进行缩放，请传入-1
	 * @param newHeight	新图片的高度，如果只按照宽度进行缩放，请传入-1
	 * @return			返回缩放后的图片对象
	 */
	public static Bitmap zoom(Bitmap bitmap, int newWidth, int newHeight){
		float wf = 1.0f;
		float hf = 1.0f;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if(newWidth <= 0){//按照高度进行缩放
			hf = ((float)newHeight) / height;
			wf = hf;
		} else if(newHeight <= 0){//按照宽度进行缩放
			wf = ((float)newWidth) / width;
			hf = wf;
		} else {
			wf = ((float)newWidth) / width;
			hf = ((float)newHeight) / height;
		}
		return zoom(bitmap, wf, hf);
	}
	
	/**
	 * 按照宽度等比例缩放图片
	 * @param bitmap	需要进行缩放的图片
	 * @param newWidth	新图片的宽度，如果只按照高度进行缩放，请传入-1
	 * @return			返回缩放后的图片对象
	 */
	public static Bitmap zoomByWidth(Bitmap bitmap, int newWidth){
		return zoom(bitmap, newWidth, -1);
	}
	
	/**
	 * 按照宽度等比例缩放图片
	 * @param bitmap	需要进行缩放的图片
	 * @param newHeight	新图片的宽度，如果只按照高度进行缩放，请传入-1
	 * @return			返回缩放后的图片对象
	 */
	public static Bitmap zoomByHeight(Bitmap bitmap, int newHeight){
		return zoom(bitmap, -1, newHeight);
	}
	
	public static  Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  

}
