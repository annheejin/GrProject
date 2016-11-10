package com.example.heejin.grproject.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by heejin on 2016. 11. 10..
 */
public class VoiceManager {
    private Activity mActivity;
    private SpeechRecognizer mRecognizer;
    private boolean isRecognize = false;
    private Handler mHandler;
    private String[] mStrings;
    public static final int FAILED_RECOGNIZE =1;
    public static final int SUCCEED_RECOGNIZE =2;
    public void startListening(Activity activity, Handler handler, String[] strings)
    {
        mActivity = activity;
        mHandler = handler;
        mStrings = strings;
        isRecognize = false;
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   //음성인식 intent생성
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mActivity.getPackageName()); //데이터 설정
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);    //음성인식 객체
        mRecognizer.setRecognitionListener(mSTTListener);          //음성인식 리스너 등록
        mRecognizer.startListening(i);
    }

    public void stopListening()
    {
        if(mRecognizer != null)
            mRecognizer.stopListening();
    }

    private RecognitionListener mSTTListener = new RecognitionListener(){

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.i("onReadyForSpeech","onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i("onBeginningOfSpeech","onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            Log.i("onRmsChanged","onRmsChanged : "+v);
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.i("onBufferReceived","onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i("end of speech","end of speech");
            if(!isRecognize){
                mHandler.sendEmptyMessageDelayed(1, 400);
            }
        }

        @Override
        public void onError(int i) {
            Log.e("error Log"," error no : "+i);
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> strs =  bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(String s : strs){
                Log.i("String TAG" , " recognize String : "+s);
                for(String s1:mStrings){
                    if(s.contains(s1)){
                        Toast.makeText(mActivity,s1,Toast.LENGTH_SHORT).show();
                        isRecognize=true;
                        stopListening();
                        mHandler.sendEmptyMessageDelayed(2, 400);
                        break;
                    }
                }
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };
}
