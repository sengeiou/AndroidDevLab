package com.example.technote.Dialog_Activity_Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.technote.R;

import at.grabner.circleprogress.CircleProgressView;

public class Activity_Screen_2 extends AppCompatActivity {
    Toolbar screen_2_toolbar;
    CircleProgressView mCircleView;
    ProgressBar carbs_progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_2);

        screen_2_toolbar = (Toolbar) findViewById(R.id.screen_2_toolbar);
        carbs_progressbar = (ProgressBar)findViewById(R.id.carbs_progressbar);
        setSupportActionBar(screen_2_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 삭제
        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setUnitVisible(false);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen_2, menu);
        return true;
    }
}