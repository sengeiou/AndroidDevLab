package com.example.technote.Dialog_Activity_Fragment;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.technote.R;

public class Activity_Screen_2 extends AppCompatActivity {
    Toolbar screen_2_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_2);

        screen_2_toolbar = (Toolbar)findViewById(R.id.screen_2_toolbar);
        setSupportActionBar(screen_2_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 삭제
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen_2, menu);
        return true;
    }
}