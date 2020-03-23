package com.example.technote.TN_Network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.IOException;

public class BoardContent_Video extends AppCompatActivity implements SurfaceHolder.Callback, com.example.technote.TN_Network.CustomMediaController.MediaPlayerControl {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaPlayer mediaPlayer;
    private com.example.technote.TN_Network.CustomMediaController mediaController;
    private String video_url;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_content_video);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaController.isShowing())
                    mediaController.hide();
                else
                    mediaController.show();
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        mediaController = new com.example.technote.TN_Network.CustomMediaController(this);
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        mediaController.setEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // FullScreenMode 설정

        Intent intent = getIntent();
        if (intent.getExtras().getString("video_url") !=null){
            video_url = intent.getExtras().getString("video_url");
        }
        Log.d("VideoUrl",video_url);
    }
    protected void onPause(){
        super.onPause();
        video_url = video_url;
    }
    //SurfaceHolder.Callback Override 부분
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        } else {
            mediaPlayer.reset();
        }

        mediaPlayer.setDisplay(surfaceHolder); // 화면 호출

        try {
            mediaPlayer.setDataSource(video_url);
            mediaPlayer.prepare(); // 비디오 load 준비
            //mediaPlayer.prepareAsync();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    // Implement MediaPlayer.OnPreparedListener
    //Media Control Click Listener Override 부분
    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();

    }
    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {

        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }


    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
