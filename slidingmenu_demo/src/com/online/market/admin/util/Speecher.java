package com.online.market.admin.util;

import android.content.Context;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class Speecher {
	private static Speecher speecher;
	private SpeechSynthesizer mTts;

	private Speecher(Context context){
		SpeechUtility.createUtility(context, "appid=55b23745");
		mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
		set_mTts();
	}
	
	public static Speecher getSpeecher(Context context){
		if(speecher==null){
			speecher=new Speecher(context);
		}
		return speecher;
	}
	
	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
//        		Log.e("majie", "error = "+code);
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};
	
	private void set_mTts() {
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

		mTts.setParameter(SpeechConstant.SPEED, "50");

		mTts.setParameter(SpeechConstant.PITCH, "50");

		mTts.setParameter(SpeechConstant.VOLUME, "100");

		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,

	}
	
	public void speech(String str){
		mTts.startSpeaking(str,mTtsListener);
	}
	
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

		}

		@Override
		public void onCompleted(SpeechError error) {

//			Log.e("majie", error.getErrorDescription());
		}

		@Override
		public void onSpeakBegin() {

		}

		@Override
		public void onSpeakPaused() {

		}

		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {

		}

		@Override
		public void onSpeakResumed() {

		}

	};

	
	public void destroy() {
		if(mTts!=null){
			mTts.stopSpeaking();
			mTts.destroy();
			speecher=null;
		}
	}

}
