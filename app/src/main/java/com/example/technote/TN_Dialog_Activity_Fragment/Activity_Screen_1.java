package com.example.technote.TN_Dialog_Activity_Fragment;

import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.technote.R;

public class Activity_Screen_1 extends AppCompatActivity {
    Toolbar screen_1_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen_1);

        screen_1_toolbar = (Toolbar)findViewById(R.id.screen_1_toolbar);
        setSupportActionBar(screen_1_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.another); // 툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 삭제
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen_1, menu);
        return true;
    }
}