package com.example.heejin.grproject.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.heejin.grproject.Gaebi;
import com.example.heejin.grproject.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer mRecognizer;
    //언어모델과 인식 결과의 최대값을 위한 default values
    private final static int DEFAULT_NUMBER_RESULTS=10;
    private final static String DEFAULT_LANG_MODEL=RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;

    private int numberRecoResult=DEFAULT_NUMBER_RESULTS;
    private String languageMode=DEFAULT_LANG_MODEL;

    private static final String LOGTAG="ASRBEGIN";
    private static int ASR_CODE=123;
    private Gaebi mApplication;
    private boolean isRecognize = false;
    //액티비티 초기화 를 셋업한다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplication= Gaebi._instance;
//        MakeSpeechRecognizer();
        //언어모델과 인식결과의 최대값의 디폴트값을 GUI로 보여준다.
        showDefaultValues();
        setSpeakButton();
        startListening();

    }//onCreateEnd

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
    }

    private void startListening()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   //음성인식 intent생성
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName()); //데이터 설정
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);    //음성인식 객체
        mRecognizer.setRecognitionListener(mSTTListener);          //음성인식 리스너 등록
        mRecognizer.startListening(i);
    }

    private void stopListening()
    {
        if(mRecognizer != null)
        {
            mRecognizer.stopListening();
        }
    }
    //음성인식 초기화하고 사용자 입력값을 듣기 위한 시작부분
    private void listen(){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        //음성인식 준비시작
//        startActivityForResult(intent,ASR_CODE);
    }//listenEnd

    private void showDefaultValues(){
        //결과기본값을 보여준다.

        ((EditText)findViewById(R.id.numResult_editText)).setText(""+DEFAULT_NUMBER_RESULTS);

        //언어모델보여주기
        if(DEFAULT_LANG_MODEL.equals(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM))
            ((RadioButton)findViewById(R.id.langModelWeb_radio)).setChecked(true);
        else
            ((RadioButton)findViewById(R.id.langModelWeb_radio)).setChecked(true);

    }//showDefualtValuesEnd

    //언어모델 값을 읽고 인식값 최대치를 읽는다. GUI로
    private void setRecognitionParams(){
        String numResults=((EditText)findViewById(R.id.numResult_editText)).getText().toString();

        //String을 int 로 바꾸기, 만약 이게 가능하다면, 이것은 디폴트값으로 사용된다.
        try{
            numberRecoResult=Integer.parseInt(numResults);
        }catch (Exception e){
            numberRecoResult=DEFAULT_NUMBER_RESULTS;
        }
        //만약 number<=0이면, 이게 디폴트 값으로 사용된다.
        if(numberRecoResult<=0)
            numberRecoResult=DEFAULT_NUMBER_RESULTS;

        RadioGroup radioG=(RadioGroup)findViewById(R.id.lngModel_radioGroup);
        switch(radioG.getCheckedRadioButtonId()){
            case R.id.langModelFree_radio :
                languageMode=RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
                break;

            case R.id.langModelWeb_radio :
                languageMode=RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH;
                break;
            default:
                languageMode=DEFAULT_LANG_MODEL;
                break;
        }

    }//setRecognitionParamsEnd

    //리스너 버튼세팅하기: 말하기 위해서 스타트 버튼을 반드시 클릭해야한다.

    private void setSpeakButton(){
        Button speak=(Button)findViewById(R.id.speech_btn);

        //클릭할때 리스너
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("generic".equals(Build.BRAND.toLowerCase())){
                    Toast.makeText(getApplicationContext(),"가상단말은 지원하지 않습니다",Toast.LENGTH_SHORT).show();
                    Log.d(LOGTAG,"ASR attempt on virtual deviece");
                }
                else{
                    setRecognitionParams();
//                    listen();
                }

            }
        });

    }//setSpeakButtonEnd

    //onActivityResult 추가해야함
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == ASR_CODE)  {
            if (resultCode == RESULT_OK)  {
                if(data!=null) {
                    //Retrieves the N-best list and the confidences from the ASR result
                    ArrayList<String> nBestList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    float[] nBestConfidences = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)  //Checks the API level because the confidence scores are supported only from API level 14
                        nBestConfidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

                    String[] paths  = getResources().getStringArray(R.array.path);//배열불러오기
                        for(int i=0;i<paths.length;i++){//배열 for 문 돌리기
                            for(int j = 0; j<nBestList.size();j++){
                                if(nBestList.get(j).contains(paths[i])){//String 비교문
//                                    AsyncTask Backgound Thread
                                    Toast.makeText(MainActivity.this,paths[i],Toast.LENGTH_SHORT).show();
                                    break;
                                    //출력하라
                                }
                            }
                        }
                }
            }
            else {
                //Reports error in recognition error in log
                Log.e(LOGTAG, "Recognition was not successful");
            }
        }
    }//onActivityResultEnd

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    stopListening();
                    startListening();
                    break;
            }
        }
    };
    private void setListView(ArrayList<String>nBestView){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,nBestView);
        ListView listView = (ListView)findViewById(R.id.nbest_listview);
        listView.setAdapter(adapter);
    }//setListView End
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
                if(s.contains("개비")){
                    Toast.makeText(MainActivity.this,"개비",Toast.LENGTH_SHORT).show();
                    isRecognize=true;
                    stopListening();
                    break;
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
}//MainEnd
