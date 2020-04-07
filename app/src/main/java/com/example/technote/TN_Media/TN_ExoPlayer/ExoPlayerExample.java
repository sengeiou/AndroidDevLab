package com.example.technote.TN_Media.TN_ExoPlayer;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerExample extends AppCompatActivity {
    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;
    private String sample = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mediaplayer_exoplayer_example);

        exoPlayerView = findViewById(R.id.exo_video_view);
        player = new SimpleExoPlayer.Builder(this).build();

        //플레이어 연결
        exoPlayerView.setPlayer(player);

        // 미디어 데이터가 로드 되는 DataSource 인스턴스 생성
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "TechNote"));
        // 재생 할 미디어를 나타내는 MediaSource
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(sample));
        // Prepare the player with the source.
        player.prepare(videoSource);

        
    }
}