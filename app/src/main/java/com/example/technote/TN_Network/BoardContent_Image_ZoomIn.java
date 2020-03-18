package com.example.technote.TN_Network;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.ImageViewPagerAdapter_ZoomIn;
import com.example.technote.TN_Network.Adapter.CircleIndicator;

public class BoardContent_Image_ZoomIn extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    protected void onCreate(Bundle savedInstanaceState){
        super.onCreate(savedInstanaceState);
        setContentView(R.layout.activity_network_board_content_iamge_zoom_in);

        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        viewPager = findViewById(R.id.zoomInViewPager);

        Intent intent = getIntent();
        ImageViewPagerAdapter_ZoomIn scrollAdapter = new ImageViewPagerAdapter_ZoomIn(getApplicationContext(), intent.getStringArrayListExtra("data"));
        viewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        circleIndicator.setupWithViewPager(viewPager);
        circleIndicator.setVerticalScrollbarPosition(intent.getIntExtra("position",0));
    }
}
