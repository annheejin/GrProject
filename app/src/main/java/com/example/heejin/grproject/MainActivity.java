package com.example.heejin.grproject;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //전역변수선언
    Intent i;
    SpeechRecognizer mRecognizer;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //구글음성인식 실행하기
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   //음성인식intent
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());    //호출한패키지
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);

        textView=(TextView)findViewById(R.id.textView);
        imageView=(ImageView)findViewById(R.id.imageView);

    }//onCreateEnd

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Toast.makeText(getApplicationContext(), "음성인식 준비.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onBeginningOfSpeech() {
            Toast.makeText(getApplicationContext(), "음성인식 시작.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {


        }

        @Override
        public void onResults(Bundle bundle) {
            String key="";
            key=SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult=bundle.getStringArrayList(key);
            String[] rs=new String[mResult.size()];
            mResult.toArray(rs);
            textView.setText("음성인식결과 : "+rs[0]);
            imageView.setImageResource(R.drawable.target);
            mRecognizer.startListening(i);

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };//RecognizerListener End

}//MainEnd
