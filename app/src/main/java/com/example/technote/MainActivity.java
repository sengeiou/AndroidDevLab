package com.example.technote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidquery.AQuery;
import com.androidquery.service.MarketService;
import com.example.technote.TN_BLE.Exam1.BleExam1_Main;
import com.example.technote.TN_BLE.FastBle.FastBleMain;
import com.example.technote.TN_Database.SQLiteTest.AddressBook;
import com.example.technote.TN_Database.ContentProviderPractics.ContentProviderTest2;
import com.example.technote.TN_Database.SQLiteTest.SQLiteTest;
import com.example.technote.TN_Database.SQLiteTest.SQLiteTest_2;
import com.example.technote.TN_Dialog_Activity_Fragment.MyActivity;
import com.example.technote.TN_Dialog_Activity_Fragment.Activity_Screen_1;
import com.example.technote.TN_Dialog_Activity_Fragment.Activity_Screen_2;
import com.example.technote.TN_Dialog_Activity_Fragment.Dialog;
import com.example.technote.TN_Dialog_Activity_Fragment.MyTab_ViewPager.MyTab_ViewPager;
import com.example.technote.TN_File.XML_ParsingExample;
import com.example.technote.TN_GoogleMap.GoogleMapTest;
import com.example.technote.TN_Layout.ConstraintLayout_Test;
import com.example.technote.TN_Layout.CoordinatorLayout_Test;
import com.example.technote.TN_Layout.DrawerLayout_Test;
import com.example.technote.TN_Layout.FrameLayout_Test;
import com.example.technote.TN_Layout.LinearLayout_Test;
import com.example.technote.TN_Layout.RelativeLayoutTest;
import com.example.technote.TN_Layout.TableLayout_Test;
import com.example.technote.TN_Media.MediaPlayer_Video;
import com.example.technote.TN_Media.TN_ExoPlayer.ExoPlayerExample;
import com.example.technote.TN_Network.BoardMain;
import com.example.technote.TN_Network.NetworkAPI_Library.AndroidAsyncHttpExample;
import com.example.technote.TN_Network.NetworkAPI_Library.FANExample;
import com.example.technote.TN_Network.NetworkAPI_Library.OKHttpExample;
import com.example.technote.TN_Network.NetworkAPI_Library.RestAPIExample;
import com.example.technote.TN_Network.NetworkAPI_Library.RetrofitExamples.RetrofitExample;
import com.example.technote.TN_Network.NetworkAPI_Library.VolleyExample;
import com.example.technote.TN_OpenLibrary.AQuery_Example;
import com.example.technote.TN_Thread.HandlerEx;
import com.example.technote.TN_Thread.MyAsyncTaskExample_1;
import com.example.technote.TN_Thread.MyAsyncTaskExample_2;
import com.example.technote.TN_Thread.ThreadNetworkExample_1;
import com.example.technote.TN_Utility.UtilityTech;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        MarketService ms = new MarketService(this);
        ms.level(MarketService.MINOR).checkVersion();
    }

    @Override
    public void onClick(View v) {
        // 1. Layout
        if(v == aQuery.id(R.id.Button1).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_layout, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_linear:
                            startActivity(new Intent(getApplicationContext(), LinearLayout_Test.class));
                            break;
                        case R.id.menu_relative:
                            startActivity(new Intent(getApplicationContext(), RelativeLayoutTest.class));
                            break;
                        case R.id.menu_frame:
                            startActivity(new Intent(getApplicationContext(), FrameLayout_Test.class));
                            break;
                        case R.id.menu_talbe:
                            startActivity(new Intent(getApplicationContext(), TableLayout_Test.class));
                            break;
                        case R.id.menu_drawer:
                            startActivity(new Intent(getApplicationContext(), DrawerLayout_Test.class));
                            break;
                        case R.id.menu_constraint:
                            startActivity(new Intent(getApplicationContext(), ConstraintLayout_Test.class));
                            break;
                        case R.id.menu_coordinator:
                            startActivity(new Intent(getApplicationContext(), CoordinatorLayout_Test.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 2. Dialog,Activity, Fragment
        else if (v == aQuery.id(R.id.Button2).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_dialog_activity_fragment, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_dialog:
                            startActivity(new Intent(getApplicationContext(), Dialog.class));
                            break;
                        case R.id.menu_activity:
                            startActivity(new Intent(getApplicationContext(), MyActivity.class));
                            break;
                        case R.id.menu_go_screen_1:
                            startActivity(new Intent(getApplicationContext(), Activity_Screen_1.class));
                            break;
                        case R.id.menu_go_screen_2:
                            startActivity(new Intent(getApplicationContext(), Activity_Screen_2.class));
                            break;
                        case R.id.menu_tap_viewpager:
                            startActivity(new Intent(getApplicationContext(), MyTab_ViewPager.class));
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기

        }

        // 3. BLE 연결
        else if (v == aQuery.id(R.id.Button3).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_ble_connect, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_ble_fast_ble:
                            startActivity(new Intent(getApplicationContext(), FastBleMain.class));
                            break;
                        case R.id.menu_ble_exam1:
                            startActivity(new Intent(getApplicationContext(), BleExam1_Main.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기

        }

        // 4. GoogleMap
        else if (v == aQuery.id(R.id.Button4).getView()){
            startActivity(new Intent(getApplicationContext(), GoogleMapTest.class));

        }

        // 5. DataBase
        else if (v == aQuery.id(R.id.Button5).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_database, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_address_book:
                            startActivity(new Intent(getApplicationContext(), AddressBook.class));
                            break;
                        case R.id.menu_sqlite:
                            startActivity(new Intent(getApplicationContext(), SQLiteTest.class));
                            break;
                        case R.id.menu_sqlite_2:
                            startActivity(new Intent(getApplicationContext(), SQLiteTest_2.class));
                            break;
                        case R.id.menu_content_provider:
                            startActivity(new Intent(getApplicationContext(), ContentProviderTest2.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 6. MediaPlay
        else if (v == aQuery.id(R.id.Button6).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_mediaplay, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_mediaplay_mediaplayer:
                            startActivity(new Intent(getApplicationContext(), MediaPlayer_Video.class));
                            break;
                        case R.id.menu_mediaplay_mediaplayer2:
                            break;
                        case R.id.menu_mediaplay_expoplyer:
                            startActivity(new Intent(getApplicationContext(), ExoPlayerExample.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 7. File
        else if (v == aQuery.id(R.id.Button7).getView()){
            startActivity(new Intent(getApplicationContext(), XML_ParsingExample.class));
        }

        // 8. Network
        else if (v == aQuery.id(R.id.Button8).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
            getMenuInflater().inflate(R.menu.menu_network, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_server_connect:
                            startActivity(new Intent(getApplicationContext(), BoardMain.class));
                            break;
                        case R.id.menu_ok_http_example:
                            startActivity(new Intent(getApplicationContext(), OKHttpExample.class));
                            break;
                        case R.id.menu_fan_example:
                            startActivity(new Intent(getApplicationContext(), FANExample.class));
                            break;
                        case R.id.menu_volley_example:
                            startActivity(new Intent(getApplicationContext(), VolleyExample.class));
                            break;
                        case R.id.menu_android_async_http_example:
                            startActivity(new Intent(getApplicationContext(), AndroidAsyncHttpExample.class));
                            break;
                        case R.id.menu_retrofit_example:
                            startActivity(new Intent(getApplicationContext(), RetrofitExample.class));
                            break;
                        case R.id.menu_rest_api_example:
                            startActivity(new Intent(getApplicationContext(), RestAPIExample.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 9. 외부 Open Library
        else if (v == aQuery.id(R.id.Button9).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_open_library, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_aquery:
                            startActivity(new Intent(getApplicationContext(), AQuery_Example.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 10. Jni
        else if (v == aQuery.id(R.id.Button10).getView()){
        }

        // 11. Thread
        else if (v == aQuery.id(R.id.Button11).getView()){
            PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

            getMenuInflater().inflate(R.menu.menu_thread_handler, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_handlerEx:
                            startActivity(new Intent(getApplicationContext(), HandlerEx.class));
                            break;
                        case R.id.menu_my_asyncTaskExample_1:
                            startActivity(new Intent(getApplicationContext(), MyAsyncTaskExample_1.class));
                            break;
                        case R.id.menu_my_asyncTaskExample_2:
                            startActivity(new Intent(getApplicationContext(), MyAsyncTaskExample_2.class));
                            break;
                        case R.id.menu_thread_network_example_1:
                            startActivity(new Intent(getApplicationContext(), ThreadNetworkExample_1.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popup.show();//Popup Menu 보이기
        }

        // 12. Utility Tech
        else if (v == aQuery.id(R.id.Button12).getView()){
            startActivity(new Intent(getApplicationContext(), UtilityTech.class));
        }
    }
    public void initView(){
        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar) ;
        setSupportActionBar(tb) ;
        tb.setTitleTextColor(Color.WHITE);

        aQuery = new AQuery(this);

        aQuery.id(R.id.Button1).visible().clicked(this);
        aQuery.id(R.id.Button2).visible().clicked(this);
        aQuery.id(R.id.Button3).visible().clicked(this);
        aQuery.id(R.id.Button4).visible().clicked(this);
        aQuery.id(R.id.Button5).visible().clicked(this);
        aQuery.id(R.id.Button6).visible().clicked(this);
        aQuery.id(R.id.Button7).visible().clicked(this);
        aQuery.id(R.id.Button8).visible().clicked(this);
        aQuery.id(R.id.Button9).visible().clicked(this);
        aQuery.id(R.id.Button10).visible().clicked(this);
        aQuery.id(R.id.Button11).visible().clicked(this);
        aQuery.id(R.id.Button12).visible().clicked(this);

    }
}