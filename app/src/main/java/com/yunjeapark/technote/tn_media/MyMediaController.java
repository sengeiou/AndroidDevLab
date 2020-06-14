package com.yunjeapark.technote.tn_media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yunjeapark.technote.R;

import java.util.Formatter;
import java.util.Locale;

class MyMediaController extends MediaController {
    private int                 VIDEO_START = 1;
    private int                 VIDEO_PAUSE = 2;
    private int                 PROGRESS_CHANGE = 3;
    private int                 position;
    private MyHandler           myMediaControllerHandler = new MyHandler();
    private Context             context;
    private View                mRoot;
    private ImageView           btn_play_pause;
    private SeekBar             seekBar;
    private TextView            textView_time;
    private MediaPlayerControl  mPlayer;
    private View                mAnchor;
    private static final int    sDefaultTimeout = 3000;
    StringBuilder               mFormatBuilder;
    Formatter                   mFormatter;

    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyMediaController(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }
    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        mAnchor = view;
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        removeAllViews();

        View v = makeControllerView();
        addView(v, frameParams);
    }
    private View makeControllerView(){
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.custom_my_media_controller_example, null);

        initControllerView(mRoot);

        return mRoot;
    }
    private void initControllerView(View v) {

        btn_play_pause =v.findViewById(R.id.custom_play_pause);
        seekBar=v.findViewById(R.id.custom_seekbar);
        textView_time =v.findViewById(R.id.custom_play_time);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        btn_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayer.isPlaying()){
                    mPlayer.pause();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_PAUSE);
                }else{
                    mPlayer.start();
                    myMediaControllerHandler.sendEmptyMessage(VIDEO_START);
                }
            }
        });

        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    //int where = (progress * MediaPlayer_Video.duration / seekBar.getMax());
                    long newposition = (mPlayer.getDuration()* progress) / 1000L;

                    mPlayer.seekTo((int)newposition);
                    textView_time.setText(stringForTime((int)newposition) + "/" + stringForTime(mPlayer.getDuration()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mPlayer = player;
        //updatePausePlay();

    }
    @Override
    public void show() {
        show(sDefaultTimeout);
    }
    @Override
    public void show(int timeout){
        super.show(timeout);
    }

    @Override
    public void hide() {
        super.hide();
    }
    @Override
    public void setEnabled(boolean enabled){
        if (btn_play_pause != null) {
            btn_play_pause.setEnabled(enabled);
        }
        if (textView_time != null) {
            textView_time.setEnabled(enabled);
        }
        if (seekBar != null) {
            seekBar.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == VIDEO_START){
                btn_play_pause.setImageResource(R.drawable.pause_white);
            }else if(msg.what==VIDEO_PAUSE){
                btn_play_pause.setImageResource(R.drawable.play_white);
            }
            else if(msg.what==PROGRESS_CHANGE){
                textView_time.setText(stringForTime(position) + "/" + stringForTime(mPlayer.getDuration()));
            }
        }
    }
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }
}