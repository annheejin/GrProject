package com.example.heejin.grproject.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.heejin.grproject.Gaebi;
import com.example.heejin.grproject.R;
import com.example.heejin.grproject.util.VoiceManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by heejin on 2016. 11. 10..
 */
public class VideoActivity extends Activity {
    private Gaebi mApplication;
    private String[] mStrings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mApplication = Gaebi._instance;
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        Toast.makeText(VideoActivity.this, "video ",Toast.LENGTH_SHORT).show();
        // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
        MediaController mediaController = new MediaController(this);
        mStrings = getResources().getStringArray(R.array.path);

        // 비디오뷰에 연결
        mediaController.setAnchorView(videoView);

        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test1);
        // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.
//        InputStream stream = getResources().openRawResource(R.raw.test1);
//        try {
//            byte[] file = new byte[stream.available()];
//            stream.read(file);
//            OutputStream ouput = new FileOutputStream(getCacheDir().getPath()+"/test1.mp4");
//            ouput.write(file);
//            ouput.close();
//            stream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
        videoView.setMediaController(mediaController);

        //비디오뷰에 재생할 동영상주소를 연결
        videoView.setVideoURI(video);
//        videoView.setVideoPath(getCacheDir().getPath()+"/test1.mp4");

        //비디오뷰를 포커스하도록 지정
        videoView.requestFocus();

        //동영상 재생
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mApplication.getmVoiceManager().startListening(VideoActivity.this, mHandler, mStrings);
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VoiceManager.FAILED_RECOGNIZE:
                    mApplication.getmVoiceManager().stopListening();
                    mApplication.getmVoiceManager().startListening(VideoActivity.this, mHandler, mStrings);
                    break;
                case VoiceManager.SUCCEED_RECOGNIZE:
                    //동영상 출력
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.getmVoiceManager().stopListening();
    }
}
