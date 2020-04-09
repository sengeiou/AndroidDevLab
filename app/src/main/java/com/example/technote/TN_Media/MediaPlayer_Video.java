package com.example.technote.TN_Media;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class MediaPlayer_Video extends AppCompatActivity {
    static VideoView videoView;
    static MyMediaController controller;
    static int duration, position;
    //Custom Controller
    static TextView textView_time;
    static SeekBar seekBar;
    static ImageView btn_play;
    static ImageView btn_play_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer_video_view);

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
        controller.setAnchorView((FrameLayout)findViewById(R.id.vv_frame_layout));
        videoView.setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        videoView.setMediaController(controller);

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);
         */

        videoView.start();
    }

    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public static int setProgress() {
        position = videoView.getCurrentPosition();
        if(duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            seekBar.setProgress((int) pos);
        }
        return position;
    }
}