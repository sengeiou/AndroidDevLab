package com.example.technote.TN_Network;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

class MyMediaController extends MediaController {
    static VideoView videoView;
    Context context;
    private MyMediaControllerView controllerView;
    TextView textView_time;
    SeekBar seekBar;
    ImageView btn_full;
    ImageView btn_play;
    ImageView btn_pause;

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
        View anchor = view;

        removeAllViews();
        //View 세팅
        controllerView = new MyMediaControllerView(context.getApplicationContext());

        BoardContent_Video2.textView_time = controllerView.textView_time;
        BoardContent_Video2.seekBar = controllerView.seekBar;
        BoardContent_Video2.btn_full = controllerView.btn_full;
        BoardContent_Video2.btn_play = controllerView.btn_play;
        BoardContent_Video2.btn_pause = controllerView.btn_pause;


        BoardContent_Video2.btn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllerView.isFullScreen) {
                    //setOriginalScreen();
                    controllerView.isFullScreen = false;
                } else {
                    //setFullScreen();
                    controllerView.isFullScreen = true;
                }
            }
        });

        BoardContent_Video2.btn_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BoardContent_Video2.videoView.isPlaying())
                    return;
                BoardContent_Video2.videoView.start();
            }
        });
        BoardContent_Video2.btn_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BoardContent_Video2.videoView.isPlaying())
                    return;

                BoardContent_Video2.videoView.pause();
            }
        });
        BoardContent_Video2.seekBar.setMax(1000);
        BoardContent_Video2.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    int where = (progress * BoardContent_Video2.duration / seekBar.getMax());

                    BoardContent_Video2.videoView.seekTo(where);
                    BoardContent_Video2.textView_time.setText(BoardContent_Video2.stringForTime(where) + "/" + BoardContent_Video2.stringForTime(BoardContent_Video2.duration));
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

                        if(BoardContent_Video2.videoView == null || BoardContent_Video2.textView_time == null || BoardContent_Video2.seekBar == null)
                            continue;

                        BoardContent_Video2.setProgress();

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
    public interface MediaPlayerControl {
        void fullscreen();
    }
}