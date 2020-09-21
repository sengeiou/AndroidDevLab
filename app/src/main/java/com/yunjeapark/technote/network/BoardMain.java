package com.yunjeapark.technote.network;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yunjeapark.technote.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class BoardMain extends AppCompatActivity {
    private int BOTTOM_NAVIGATION_STATE_HOME = 1;
    private int BOTTOM_NAVIGATION_STATE_IMAGE_LIST = 2;
    private int BOTTOM_NAVIGATION_STATE_VIDEO_LIST = 3;
    private int bottomNavigationButtonState;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_main);

        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setTitle("게시판");

        bottomNavigationButtonState = BOTTOM_NAVIGATION_STATE_HOME;

        //fragment 선언
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_network_board_main, new Fragment_Board_Home()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 하단 메뉴바 버튼 클릭 이벤트
                switch (item.getItemId()) {
                    case R.id.action_one:
                        if(bottomNavigationButtonState != BOTTOM_NAVIGATION_STATE_HOME){
                            replaceFragment(new Fragment_Board_Home());
                            bottomNavigationButtonState = BOTTOM_NAVIGATION_STATE_HOME;
                        }
                        return true;
                    case R.id.action_two:
                        if(bottomNavigationButtonState != BOTTOM_NAVIGATION_STATE_IMAGE_LIST){
                            replaceFragment(new Fragment_Board_ImageList());
                            bottomNavigationButtonState = BOTTOM_NAVIGATION_STATE_IMAGE_LIST;
                        }
                        return true;
                    case R.id.action_three:
                        Log.d("item",String.valueOf(item.getItemId()));
                        if(bottomNavigationButtonState != BOTTOM_NAVIGATION_STATE_VIDEO_LIST){
                            replaceFragment(new Fragment_Board_VideoList());
                            bottomNavigationButtonState = BOTTOM_NAVIGATION_STATE_VIDEO_LIST;
                        }
                        return true;
                    case R.id.action_four:
                        Log.d("item",String.valueOf(item.getItemId()));
                        return true;
                }
                return false;
            }
        });
        //BottomNavigationHelper.disableShiftMode(bottomNavigationView);//하단 메뉴바 쉬프트 모드 없애기
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }

    @Override // 툴바 메뉴 설정하기
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_network_board_main,menu);
        return true;
    }
    //툴바 아이콘 클릭 이벤트
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_network_board_main, fragment).commit();
    }
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_network_board_main);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_network_board_main);
    }
}
