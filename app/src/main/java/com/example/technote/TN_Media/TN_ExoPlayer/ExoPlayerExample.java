package com.example.technote.TN_Media.TN_ExoPlayer;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
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
    private String videoSamplePath = "http://www-itec.uni-klu.ac.at/ftp/datasets/DASHDataset2014/BigBuckBunny/4sec/BigBuckBunny_4s_onDemand_2014_05_09.mpd";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mediaplayer_exoplayer_example);
        Uri sampleMediaUri = Uri.parse(videoSamplePath);

        exoPlayerView = findViewById(R.id.exo_video_view);

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