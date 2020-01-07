package com.example.technote.Layout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.technote.R;

public class DrawerLayout_Test extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_drawer_test);

        Button buttonOpen = (Button) findViewById(R.id.open) ;
        buttonOpen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer) ;
                if (!drawer.isDrawerOpen(Gravity.LEFT)) { // 만약 왼쪽에서 drwer가 열리지 않았다면
                    drawer.openDrawer(Gravity.LEFT) ; //왼쪽에서 열어라
                }
            }
        });

        Button buttonClose = (Button) findViewById(R.id.close) ;
        buttonClose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer) ;
                if (drawer.isDrawerOpen(Gravity.LEFT)) { // 만약 왼쪽에서 drwer가 열렸다면
                    drawer.closeDrawer(Gravity.LEFT) ; //왼쪽으로 drwer를 닫아라
                }
            }
        });

        CheckBox checkboxLock = (CheckBox) findViewById(R.id.lock) ;
        checkboxLock.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer) ;
                if (((CheckBox)v).isChecked()) {
                    if (drawer.isDrawerOpen(Gravity.LEFT)) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    } else {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    }
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });
    }
}