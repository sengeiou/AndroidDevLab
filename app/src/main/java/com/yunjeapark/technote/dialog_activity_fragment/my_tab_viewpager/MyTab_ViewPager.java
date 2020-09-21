package com.yunjeapark.technote.dialog_activity_fragment.my_tab_viewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yunjeapark.technote.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.tabs.TabLayout;

public class MyTab_ViewPager extends AppCompatActivity {
    ViewPager vp;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actvity_my_tap_viewpager);

        vp = (ViewPager) findViewById(R.id.vp); //뷰 페이저
        TabLayout mTab = (TabLayout) findViewById(R.id.tabs);
        //mTab.setTabGravity(TabLayout.GRAVITY_FILL); // TabItem을 꽉채우지 않고 개별 크기로 통일 시켜서 놓음
        mTab.addTab(mTab.newTab().setText("Fragment_A"));
        mTab.addTab(mTab.newTab().setText("Fragment_B"));
        mTab.addTab(mTab.newTab().setText("Fragment_C"));

        MyTabLayoutAdapter tabLayoutAdapter = new MyTabLayoutAdapter(getSupportFragmentManager(), mTab.getTabCount());
        tabLayoutAdapter.notifyDataSetChanged();
        vp.setAdapter(tabLayoutAdapter);
        // vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTab));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Test","Test");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("Test","Test");
            }
        });

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    /*getSupportActionBar().setTitle("홈");*/
                }else if(tab.getPosition()==1){
                    /* getSupportActionBar().setTitle("나의 장터");*/
                }else if(tab.getPosition()==2){
                    /*getSupportActionBar().setTitle("장터");*/
                }else if(tab.getPosition()==3){
                    /*getSupportActionBar().setTitle("마이페이지");*/
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 하단 메뉴바 버튼 클릭 이벤트
                switch (item.getItemId()) {
                    case R.id.action_one:
                        return true;
                    case R.id.action_two:
                        return true;
                    case R.id.action_three:
                        return true;
                    case R.id.action_four:
                        return true;
                }
                return false;
            }
        });
        //BottomNavigationHelper.disableShiftMode(bottomNavigationView);//하단 메뉴바 쉬프트 모드 없애기
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }
}
