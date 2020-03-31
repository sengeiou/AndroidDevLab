package com.example.technote.TN_Media;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;

import com.example.technote.R;

class MyMediaController extends MediaController {
    private int VIDEO_START = 1;
    private int VIDEO_PAUSE = 2;
    private int PROGRESS_CHANGE = 3;

    Context context;
    private MyMediaControllerView controllerView;
    MyMediaControllerHandler myMediaControllerHandler = new MyMediaControllerHandler();

    MyMediaController(Context context, boolean useFastFoward) {
        super(context, useFastFoward);
        this.context = context;
    }

    MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    MyMediaController(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        removeAllViews();
        //View 세팅
        controllerView = new MyMediaControllerView(context.getApplicationContext());

        MediaPlayer_Video.textView_time = controllerView.textView_time;
        MediaPlayer_Video.seekBar = controllerView.seekBar;
        MediaPlayer_Video.btn_play_pause = controllerView.btn_play_pause;

        MediaPlayer_Video.btn_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MediaPlayer_Video.videoView.isPlaying()){
                    MediaPlayer_Video.videoView.start();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_START);
                }else{
                    MediaPlayer_Video.videoView.pause();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_PAUSE);
                }

            }
        });
        MediaPlayer_Video.seekBar.setMax(1000);
        MediaPlayer_Video.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    //int where = (progress * MediaPlayer_Video.duration / seekBar.getMax());
                    long newposition = (MediaPlayer_Video.duration * progress) / 1000L;

                    MediaPlayer_Video.videoView.seekTo((int)newposition);
                    MediaPlayer_Video.textView_time.setText(MediaPlayer_Video.stringForTime((int)newposition) + "/" + MediaPlayer_Video.stringForTime(MediaPlayer_Video.duration));
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

                        if(MediaPlayer_Video.videoView == null || MediaPlayer_Video.textView_time == null || MediaPlayer_Video.seekBar == null)
                            continue;

                        MediaPlayer_Video.setProgress();
                        myMediaControllerHandler.sendEmptyMessage(PROGRESS_CHANGE);
                    }
                } catch(Exception e) {
                    Log.e("TAG HI", "ERROR", e);
                }
            }
        });
        thread.start();
        addView(controllerView);
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
               MediaPlayer_Video.btn_play_pause.setImageResource(R.drawable.pause);
           }else if(msg.what==VIDEO_PAUSE){
               MediaPlayer_Video.btn_play_pause.setImageResource(R.drawable.play);
           }
           else if(msg.what==PROGRESS_CHANGE){
               MediaPlayer_Video.textView_time.setText(MediaPlayer_Video.stringForTime(MediaPlayer_Video.position) + "/" + MediaPlayer_Video.stringForTime(MediaPlayer_Video.duration));
           }
        }
    }
}