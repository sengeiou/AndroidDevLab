package com.example.technote.TN_Media;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technote.R;

class MyMediaController extends MediaController {
    private Context             context;
    private View                mRoot;
    private ImageView           btn_play_pause;
    private SeekBar             seekBar;
    private TextView            textView_time;
    private MediaPlayerControl  mPlayer;
    private View                mAnchor;
    private static final int    sDefaultTimeout = 3000;

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
        textView_time=v.findViewById(R.id.custom_current_time);

        btn_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
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
}