package com.yunjeapark.technote.tn_network;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yunjeapark.technote.R;
import com.yunjeapark.technote.tn_network.adapter.ImageViewPagerAdapter_ZoomIn;
import com.yunjeapark.technote.tn_network.adapter.CircleIndicator;

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
        viewPager.setCurrentItem(intent.getIntExtra("position",0));
    }
}
