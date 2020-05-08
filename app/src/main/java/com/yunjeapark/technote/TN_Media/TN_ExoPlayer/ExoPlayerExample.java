package com.yunjeapark.technote.TN_Media.TN_ExoPlayer;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerExample extends AppCompatActivity {
    private PlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private ImageButton exoScreenMode;
    private String videoSamplePath = "http://www-itec.uni-klu.ac.at/ftp/datasets/DASHDataset2014/BigBuckBunny/4sec/BigBuckBunny_4s_onDemand_2014_05_09.mpd";
    private boolean fullscreen = false;
    private int playerHeight;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mediaplayer_exoplayer_example);
        Uri sampleMediaUri = Uri.parse(videoSamplePath);

        exoPlayerView = findViewById(R.id.exo_video_view);
        exoScreenMode = findViewById(R.id.exo_screen_mode);
        playerHeight = exoPlayerView.getLayoutParams().height;
        /** 일반 ExoPlayer 사용법
         ** 미디어 데이터가 로드 되는 DataSource 인스턴스 생성
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "TechNote"));
        // 재생 할 미디어를 나타내는 MediaSource
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mp4VideoUri);
        //플레이어 연결
        exoPlayerView.setPlayer(exoPlayer);

         exoPlayer.prepare(mediaSource);
         **/
        exoScreenMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExoPlayerExample.this, "ItemClick", Toast.LENGTH_SHORT).show();
                if(fullscreen) {
                    //exoScreenMode.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = playerHeight;
                    Log.d("Params",String.valueOf(params.height));
                    exoPlayerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    //fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_close));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    exoPlayerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
        // Dash 사용법
        // 데이터 소스 Factory를 만든다.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "TechNote"));
        // DASH manifest uri를 가리키는 DASH 미디어 소스를 만든다.
        MediaSource mediaSource = new DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(sampleMediaUri);
        // ExoPlayer의 인스턴스를 만든다.
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
    }
}