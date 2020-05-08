package com.yunjeapark.technote.TN_Media;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

import java.io.IOException;

public class MediaPlayer_Video extends AppCompatActivity implements SurfaceHolder.Callback, MediaController.MediaPlayerControl{
    private MyMediaController controller;
    private String videoUri = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer_video_view);

        surfaceView = findViewById(R.id.surface_view2);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        controller = new MyMediaController(this);
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout)findViewById(R.id.vv_frame_layout));
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.isShowing())
                    controller.hide();
                else
                    controller.show();
            }
        });

        //mediaController = new MediaController(this);
        controller.setEnabled(true);
    }

    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
    }
    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();

        try {
            //simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();

            mediaPlayer.setDataSource(videoUri);
            mediaPlayer.prepare(); // 비디오 load 준비

            //mediaPlayer.prepareAsync();
            mediaPlayer.start();
            mediaPlayer.setDisplay(surfaceHolder); // 화면 호출

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

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
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer.isPlaying()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}