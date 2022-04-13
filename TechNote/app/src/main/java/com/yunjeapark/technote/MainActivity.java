package com.yunjeapark.technote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.androidquery.AQuery;
import com.androidquery.service.MarketService;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.yunjeapark.technote.ble.Exam1.BleExam1_MainActivity;
import com.yunjeapark.technote.ble.FastBle.FastBleMainActivity;
import com.yunjeapark.technote.database.sqlite_test.AddressBook;
import com.yunjeapark.technote.database.content_provider_practice.ContentProviderTest2Activity;
import com.yunjeapark.technote.database.sqlite_test.SQLiteTest;
import com.yunjeapark.technote.database.sqlite_test.SQLiteTest_2;
import com.yunjeapark.technote.dialog_activity_fragment.MyActivity;
import com.yunjeapark.technote.dialog_activity_fragment.Activity_Screen_1;
import com.yunjeapark.technote.dialog_activity_fragment.Activity_Screen_2;
import com.yunjeapark.technote.dialog_activity_fragment.Dialog;
import com.yunjeapark.technote.dialog_activity_fragment.my_tab_viewpager.MyTabViewPagerActivity;
import com.yunjeapark.technote.file.XMLParsingExampleActivity;
import com.yunjeapark.technote.google_map.GoogleMapTestActivity;
import com.yunjeapark.technote.layout.ConstraintLayoutTestActivity;
import com.yunjeapark.technote.layout.CoordinatorLayoutTestActivity;
import com.yunjeapark.technote.layout.DrawerLayoutTestActivity;
import com.yunjeapark.technote.layout.FrameLayoutTestActivity;
import com.yunjeapark.technote.layout.LinearLayoutTestActivity;
import com.yunjeapark.technote.layout.RelativeLayoutTestActivity;
import com.yunjeapark.technote.layout.TableLayoutTestActivity;
import com.yunjeapark.technote.media.MediaPlayer_Video;
import com.yunjeapark.technote.media.tn_exo_player.ExoPlayerExample;
import com.yunjeapark.technote.network.BoardMain;
import com.yunjeapark.technote.network.network_api_library.AndroidAsyncHttpExample;
import com.yunjeapark.technote.network.network_api_library.FANExample;
import com.yunjeapark.technote.network.network_api_library.OKHttpExample;
import com.yunjeapark.technote.network.network_api_library.RestAPIExample;
import com.yunjeapark.technote.network.network_api_library.retrofit_examples.RetrofitExample;
import com.yunjeapark.technote.network.network_api_library.VolleyExample;
import com.yunjeapark.technote.open_library.AQueryExampleActivity;
import com.yunjeapark.technote.thread.HandlerExampleActivity;
import com.yunjeapark.technote.thread.MyAsyncTaskExampleActivity_1;
import com.yunjeapark.technote.thread.MyAsyncTaskExampleActivity_2;
import com.yunjeapark.technote.thread.ThreadNetworkExampleActivity;
import com.yunjeapark.technote.util_tech.UtilityTechActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        getDate();
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
                            startActivity(new Intent(getApplicationContext(), LinearLayoutTestActivity.class));
                            break;
                        case R.id.menu_relative:
                            startActivity(new Intent(getApplicationContext(), RelativeLayoutTestActivity.class));
                            break;
                        case R.id.menu_frame:
                            startActivity(new Intent(getApplicationContext(), FrameLayoutTestActivity.class));
                            break;
                        case R.id.menu_talbe:
                            startActivity(new Intent(getApplicationContext(), TableLayoutTestActivity.class));
                            break;
                        case R.id.menu_drawer:
                            startActivity(new Intent(getApplicationContext(), DrawerLayoutTestActivity.class));
                            break;
                        case R.id.menu_constraint:
                            startActivity(new Intent(getApplicationContext(), ConstraintLayoutTestActivity.class));
                            break;
                        case R.id.menu_coordinator:
                            startActivity(new Intent(getApplicationContext(), CoordinatorLayoutTestActivity.class));
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
                            startActivity(new Intent(getApplicationContext(), MyTabViewPagerActivity.class));
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
                            startActivity(new Intent(getApplicationContext(), FastBleMainActivity.class));
                            break;
                        case R.id.menu_ble_exam1:
                            startActivity(new Intent(getApplicationContext(), BleExam1_MainActivity.class));
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
            startActivity(new Intent(getApplicationContext(), GoogleMapTestActivity.class));

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
                            startActivity(new Intent(getApplicationContext(), ContentProviderTest2Activity.class));
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
            startActivity(new Intent(getApplicationContext(), XMLParsingExampleActivity.class));
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
                            startActivity(new Intent(getApplicationContext(), AQueryExampleActivity.class));
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
                            startActivity(new Intent(getApplicationContext(), HandlerExampleActivity.class));
                            break;
                        case R.id.menu_my_asyncTaskExample_1:
                            startActivity(new Intent(getApplicationContext(), MyAsyncTaskExampleActivity_1.class));
                            break;
                        case R.id.menu_my_asyncTaskExample_2:
                            startActivity(new Intent(getApplicationContext(), MyAsyncTaskExampleActivity_2.class));
                            break;
                        case R.id.menu_thread_network_example_1:
                            startActivity(new Intent(getApplicationContext(), ThreadNetworkExampleActivity.class));
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
            startActivity(new Intent(getApplicationContext(), UtilityTechActivity.class));

        }

        // 13. UI
        else if (v == aQuery.id(R.id.Button13).getView()){
            //startActivity(new Intent(getApplicationContext(), ScrollingGalleryControls.class));
            PowerMenu powerMenu = new PowerMenu.Builder(this)
                    .addItem(new PowerMenuItem("2019 SONATA",false))
                    .addItem(new PowerMenuItem("2019 SANTAPE",false))
                    .addItem(new PowerMenuItem("2019 AVANTE",false))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black))
                    .setTextGravity(Gravity.CENTER)
                    .setSelectedTextColor(Color.BLACK)
                    .setMenuColor(Color.BLACK)
                    .setSelectedMenuColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .build();
            powerMenu.showAsDropDown(aQuery.id(R.id.Button13).getView());
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
        aQuery.id(R.id.Button13).visible().clicked(this);
    }

    private void getDate(){
        String strSDFormatDay;
        long CurrentTime = System.currentTimeMillis(); // 현재 시간을 msec 단위로 얻음
        Date TodayDate = new Date(CurrentTime); // 현재 시간 Date 변수에 저장
        SimpleDateFormat SDFormat = new SimpleDateFormat("yyyyMMdd");
        strSDFormatDay = SDFormat.format(TodayDate); // 'dd' 형태로 포맷 변경
        int saveDate = Integer.parseInt(strSDFormatDay) - 1;
        Log.d("YJP", strSDFormatDay);
        Log.d("YJP", "saveDate : " + saveDate);
    }
}