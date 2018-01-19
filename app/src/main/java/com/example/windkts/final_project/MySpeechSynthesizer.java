package com.example.windkts.final_project;

import android.content.Context;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by windtksLin on 2018/1/19 0019.
 */

public class MySpeechSynthesizer {
    private Context mContext;
    private static SpeechSynthesizer mTts;
    private SynthesizerListener mTtsListener;

    public MySpeechSynthesizer(Context context,SynthesizerListener Listener){
        mContext = context;
        mTts = SpeechSynthesizer.createSynthesizer(mContext,null);
        mTtsListener = Listener;
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 引擎类型 网络
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }
    public void speaking(String text){
        if(TextUtils.isEmpty(text)) {
            return;
        }
        mTts.startSpeaking(text,mTtsListener);

    }
    public  void stopSpeaking(){
        if(null != mTts && mTts.isSpeaking()){
            mTts.stopSpeaking();
        }
    }
    public  boolean isSpeaking() {
        if (null != mTts) {
            return mTts.isSpeaking();
        } else {
            return false;
        }
    }
}
