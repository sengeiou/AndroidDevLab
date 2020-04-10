package com.example.technote.TN_Media;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.technote.R;

class MyMediaController2 extends MediaController {
    private int VIDEO_START = 1;
    private int VIDEO_PAUSE = 2;
    private int PROGRESS_CHANGE = 3;
    View controlView;
    Context context;
    ImageView btn_full,btn_play, btn_play_pause;
    SeekBar seekBar;
    TextView textView_time;

    private MyMediaController controllerView;
    MyMediaControllerHandler myMediaControllerHandler = new MyMediaControllerHandler();


    MyMediaController2(Context context, boolean useFastFoward) {
        super(context, useFastFoward);
        this.context = context;
    }

    MyMediaController2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    MyMediaController2(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        removeAllViews();
        //View 세팅
        //controllerView = new MyMediaControllerView(context.getApplicationContext());
        init();

        //MediaPlayer_Video.seekBar = seekBar;
        //MediaPlayer_Video.btn_play_pause = btn_play_pause;

        MediaPlayer_Video2.btn_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MediaPlayer_Video2.videoView.isPlaying()){
                    MediaPlayer_Video2.videoView.start();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_START);
                }else{
                    MediaPlayer_Video2.videoView.pause();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_PAUSE);
                }

            }
        });
        MediaPlayer_Video2.seekBar.setMax(1000);
        MediaPlayer_Video2.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    //int where = (progress * MediaPlayer_Video.duration / seekBar.getMax());
                    long newposition = (MediaPlayer_Video2.duration * progress) / 1000L;

                    MediaPlayer_Video2.videoView.seekTo((int)newposition);
                    MediaPlayer_Video2.textView_time.setText(MediaPlayer_Video.stringForTime((int)newposition) + "/" + MediaPlayer_Video.stringForTime(MediaPlayer_Video2.duration));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(100);

                        if(MediaPlayer_Video2.videoView == null || MediaPlayer_Video2.textView_time == null || MediaPlayer_Video2.seekBar == null)
                            continue;

                        MediaPlayer_Video2.setProgress();
                        myMediaControllerHandler.sendEmptyMessage(PROGRESS_CHANGE);
                    }
                } catch(Exception e) {
                    Log.e("TAG HI", "ERROR", e);
                }
            }
        });
        thread.start();
        //addView(controllerView);
    }

    @Override
    public void hide() {
        show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
            ((Activity) getContext()).finish();

        return super.dispatchKeyEvent(event);
    }

    public class MyMediaControllerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == VIDEO_START){
                MediaPlayer_Video2.btn_play_pause.setImageResource(R.drawable.pause);
            }else if(msg.what==VIDEO_PAUSE){
                MediaPlayer_Video2.btn_play_pause.setImageResource(R.drawable.play);
            }
            else if(msg.what==PROGRESS_CHANGE){
                MediaPlayer_Video2.textView_time.setText(MediaPlayer_Video.stringForTime(MediaPlayer_Video2.position) + "/" + MediaPlayer_Video.stringForTime(MediaPlayer_Video2.duration));
            }
        }
    }
    private void init() {
        controlView= LayoutInflater.from(context).inflate(R.layout.custom_my_media_controller_example,this,true);

        //btn_play=view.findViewById(R.id.custom_play);
        MediaPlayer_Video2.btn_play_pause =controlView.findViewById(R.id.custom_play_pause);
        MediaPlayer_Video2.seekBar=controlView.findViewById(R.id.custom_seekbar);
        MediaPlayer_Video2.textView_time=controlView.findViewById(R.id.custom_current_time);
    }
}