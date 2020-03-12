package com.example.technote.TN_Network;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.AutoScrollAdapter;
import com.example.technote.TN_Network.Adapter.CircleIndicator;
import com.example.technote.TN_Network.Data.ImageSliderData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class BoardContent_Video extends AppCompatActivity implements SurfaceHolder.Callback, android.widget.MediaController.MediaPlayerControl{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String url = "http://yjpapp.com/get_video_content.php";
    private  String id;
    private JSONArray jsonArray;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_content_video);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaController.isShowing())
                    mediaController.hide();
                else
                    mediaController.show();
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.surfaceView));
        mediaController.setEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // FullScreenMode 설정

        Intent intent = getIntent();
        id = intent.getExtras().getString("id_send");
    }
    //SurfaceHolder.Callback Override 부분
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        AndroidNetworking.upload(url)
                .addMultipartParameter("id",id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Result", "onResponse");
                        try{
                            jsonArray = response.getJSONArray("yjpapp");
                            mediaPlayer.setDataSource(jsonArray.getJSONObject(0).get("video_url").toString());
                            //mediaPlayer.setVolume(0, 0); //볼륨 제거
                            mediaPlayer.setDisplay(surfaceHolder); // 화면 호출
                            mediaPlayer.prepare(); // 비디오 load 준비
                            //mediaPlayer.setOnCompletionListener(completionListener); // 비디오 재생 완료 리스너
                            mediaPlayer.start();
                        } catch (IOException e) { // setDataSource 에러
                            e.printStackTrace();
                            Log.e("MyTag","surface view error : " + e.getMessage());
                        } catch (JSONException e) { // jsonArray 에러
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("Result","onError" + anError.toString());
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

    //Media Control Override 부분
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
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
