package com.example.technote.TN_Network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaTimestamp;
import android.media.SubtitleData;
import android.media.TimedMetaData;
import android.media.TimedText;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class BoardContent_Video extends AppCompatActivity implements SurfaceHolder.Callback, CustomMediaController.MediaPlayerControl {


    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaPlayer mediaPlayer;

    //private MediaController mediaController;
    private CustomMediaController mediaController;
    //private MyMediaController mediaController;
    //private MyMediaController mediaController;
    private String video_url;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_content_video);

        surfaceView = findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        //mediaController = new com.example.technote.TN_Network.CustomMediaController(this);
        mediaController = new CustomMediaController(this);

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        mediaController.setEnabled(true);

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaController.isShowing())
                    mediaController.hide();
                else
                    mediaController.show();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // FullScreenMode 설정

        Intent intent = getIntent();
        if (intent.getExtras().getString("video_url") !=null){
            video_url = intent.getExtras().getString("video_url");
        }
    }
    protected void onPause(){
        super.onPause();
        mediaPlayer.pause();
    }
    //SurfaceHolder.Callback Override 부분
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

            mediaPlayer = new MediaPlayer();

            try {
                //simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();

                mediaPlayer.setDataSource(video_url);
                mediaPlayer.prepare(); // 비디오 load 준비
                //mediaPlayer.prepareAsync();
                mediaPlayer.start();
                mediaPlayer.setDisplay(surfaceHolder); // 화면 호출

                Log.d("getTimestamp", mediaPlayer.getTimestamp().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("onBufferingUpdate","onPrepared");
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d("onBufferingUpdate", "percent : " + String.valueOf(percent));

            }
        });

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d("onVideoSizeChanged", "onVideoSizeChanged : ");
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_UNKNOWN"  );
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_SERVER_DIED"  );
                        break;
                }
                switch (extra){
                    case MediaPlayer.MEDIA_ERROR_IO:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_IO"  );
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_MALFORMED"  );
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_UNSUPPORTED"  );
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        Log.d("onError", "onError what : " + String.valueOf(what) + " => MediaPlayer.MEDIA_ERROR_TIMED_OUT"  );
                }
                return false;
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch(what){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " => MEDIA_ERROR_UNKNOWN");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_VIDEO_RENDERING_START");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_BUFFERING_START");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        // Progress Dialog 삭제 (버퍼링 끝)
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_BUFFERING_END");
                        break;
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_NOT_SEEKABLE");
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_UNSUPPORTED_SUBTITLE");
                        break;
                    case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                        Log.d("onInfo","onInfo what : " + String.valueOf(what) + " =>  MEDIA_INFO_SUBTITLE_TIMED_OUT");
                        break;
                }
                return false;
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            mediaPlayer.setOnMediaTimeDiscontinuityListener(new MediaPlayer.OnMediaTimeDiscontinuityListener() { // API 레벨 28이상
                @Override
                public void onMediaTimeDiscontinuity(@NonNull MediaPlayer mp, @NonNull MediaTimestamp mts) {
                    Log.d("MediaTimeDiscontinue","mts : " +mts.toString());
                }
            });

            //그냥 안불러지는 리스너
            mediaPlayer.setOnSubtitleDataListener(new MediaPlayer.OnSubtitleDataListener() { // API 레벨 28이상,
                // 자막이 존재하는 비디오를 재생해본결과 나오지 않음
                // MP4 파일이 아닌 AVI 파일 형식으로 다른 비디오를 재생한 결과 :
                @Override
                public void onSubtitleData(@NonNull MediaPlayer mp, @NonNull SubtitleData data) {
                    Log.d("onSubtitleData","data : " +data.toString() + " getData() : " + data.getData());

                }
            });
        }



        // 그냥 플레이로는 안불려지는 리스너

        mediaPlayer.setOnTimedMetaDataAvailableListener(new MediaPlayer.OnTimedMetaDataAvailableListener() { // API 23이상
            @Override
            public void onTimedMetaDataAvailable(MediaPlayer mp, TimedMetaData data) {
                Log.d("TimedMetaData","data : " +data.toString() + " getMetaData() : " + data.getMetaData().toString() + "getTimestamp : " + data.getTimestamp());
            }
        });
        mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                Log.d("onTimedText","data : " +text.toString()+ " getText() : " + text.getText()+ "getTimestamp : " + text.getBounds().toString());
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    // Implement MediaPlayer.OnPreparedListener
    //Media Control Click Listener Override 부분
    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();

    }
    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {

        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void toggleFullScreen() {
        // 풀스크린
        Log.d("FullScreenClick","BoardContent_Video toggleFullScreen");
        setFullscreen(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    private void setFullscreen(boolean fullscreen)
    {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullscreen)
        {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        else
        {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(attrs);
    }
}
