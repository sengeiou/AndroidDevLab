package com.example.technote.TN_Network;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.technote.R;

public class MyMediaControllerView extends RelativeLayout {
    Context context;
    ImageView btn_full,btn_play,btn_pause;
    SeekBar seekBar;
    TextView textView_time;

    boolean isFullScreen=false;

    MyMediaControllerView(Context context) {
        super(context);
        this.context=context;
        init();
    }
    MyMediaControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_my_media_controller_example,this,true);

        btn_full=view.findViewById(R.id.custom_fullscreen);
        btn_play=view.findViewById(R.id.custom_play);
        btn_pause=view.findViewById(R.id.custom_pause);
        seekBar=view.findViewById(R.id.custom_seekbar);
        textView_time=view.findViewById(R.id.custom_current_time);
    }

}