package com.example.technote.TN_Network;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class BoardContent_Video2 extends AppCompatActivity {
    static VideoView videoView;
    static MyMediaControllerView controllerView;
    static MyMediaController controller;
    static int duration;

    //Custom Controller
    static TextView textView_time;
    static SeekBar seekBar;
    static ImageView btn_full;
    static ImageView btn_play;
    static ImageView btn_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_content_video_view);

        videoView = findViewById(R.id.video_view);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        duration = videoView.getDuration();
                        textView_time.setText("00:00:00/" + stringForTime(duration));
                    }
                });
            }
        });

        controller = new MyMediaController(this);
//        controller.setAnchorView((FrameLayout)findViewById(R.id.video_view_container));
        videoView.setMediaController(controller);
        videoView.setVideoPath("/storage/emulated/0/Download/8.mp4");

    }

    public void setFullScreen() {
        if(videoView == null)
            return;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout
                .LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);
    }

    public void setOriginalScreen() {
        if(videoView == null)
            return;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout
                .LayoutParams) videoView.getLayoutParams();
        params.width = 300*metrics.densityDpi; //원하는 사이즈
        params.height = 300*metrics.densityDpi; //원하는 사이즈
        params.leftMargin = 30;
        videoView.setLayoutParams(params);
    }

    public static String stringForTime(int timeMs) {

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public static int setProgress() {
        //if(videoView == null)
        //  return 0;


        int position = videoView.getCurrentPosition();

        if(duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            seekBar.setProgress((int) pos);
        }

        textView_time.setText(stringForTime(position) + "/" + stringForTime(duration));

        return position;
    }
}