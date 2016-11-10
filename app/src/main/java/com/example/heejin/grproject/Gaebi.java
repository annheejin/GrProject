package com.example.heejin.grproject;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.example.heejin.grproject.Activity.MainActivity;
import com.example.heejin.grproject.util.VoiceManager;

import java.util.ArrayList;

/**
 * Created by heejin on 2016. 11. 8..
 */
public class Gaebi extends Application {

    public static Gaebi _instance;
    private VoiceManager mVoiceManager= new VoiceManager();;
    public Gaebi(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = new Gaebi();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public VoiceManager getmVoiceManager() {
        return mVoiceManager;
    }
}
