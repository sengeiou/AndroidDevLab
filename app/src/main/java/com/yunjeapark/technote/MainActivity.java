package com.yunjeapark.technote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.yunjeapark.technote.tn_ble.Exam1.BleExam1_MainActivity;
import com.yunjeapark.technote.tn_ble.FastBle.FastBleMainActivity;
import com.yunjeapark.technote.tn_database.sqlite_test.AddressBook;
import com.yunjeapark.technote.tn_database.content_provider_practice.ContentProviderTest2Activity;
import com.yunjeapark.technote.tn_database.sqlite_test.SQLiteTest;
import com.yunjeapark.technote.tn_database.sqlite_test.SQLiteTest_2;
import com.yunjeapark.technote.tn_dialog_activity_fragment.MyActivity;
import com.yunjeapark.technote.tn_dialog_activity_fragment.Activity_Screen_1;
import com.yunjeapark.technote.tn_dialog_activity_fragment.Activity_Screen_2;
import com.yunjeapark.technote.tn_dialog_activity_fragment.Dialog;
import com.yunjeapark.technote.tn_dialog_activity_fragment.my_tab_viewpager.MyTab_ViewPager;
import com.yunjeapark.technote.tn_file.XML_ParsingExampleActivity;
import com.yunjeapark.technote.tn_google_map.GoogleMapTest;
import com.yunjeapark.technote.tn_layout.ConstraintLayout_Test;
import com.yunjeapark.technote.tn_layout.CoordinatorLayout_Test;
import com.yunjeapark.technote.tn_layout.DrawerLayout_Test;
import com.yunjeapark.technote.tn_layout.FrameLayout_Test;
import com.yunjeapark.technote.tn_layout.LinearLayout_Test;
import com.yunjeapark.technote.tn_layout.RelativeLayoutTestActivity;
import com.yunjeapark.technote.tn_layout.TableLayoutTestActivity;
import com.yunjeapark.technote.tn_media.MediaPlayer_Video;
import com.yunjeapark.technote.tn_media.tn_exo_player.ExoPlayerExample;
import com.yunjeapark.technote.tn_network.BoardMain;
import com.yunjeapark.technote.tn_network.network_api_library.AndroidAsyncHttpExample;
import com.yunjeapark.technote.tn_network.network_api_library.FANExample;
import com.yunjeapark.technote.tn_network.network_api_library.OKHttpExample;
import com.yunjeapark.technote.tn_network.network_api_library.RestAPIExample;
import com.yunjeapark.technote.tn_network.network_api_library.retrofit_examples.RetrofitExample;
import com.yunjeapark.technote.tn_network.network_api_library.VolleyExample;
import com.yunjeapark.technote.tn_open_library.AQueryExampleActivity;
import com.yunjeapark.technote.tn_thread.HandlerExampleActivity;
import com.yunjeapark.technote.tn_thread.MyAsyncTaskExampleActivity_1;
import com.yunjeapark.technote.tn_thread.MyAsyncTaskExampleActivity_2;
import com.yunjeapark.technote.tn_thread.ThreadNetworkExampleActivity;
import com.yunjeapark.technote.tn_util_tech.UtilityTechActivity;

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
                            startActivity(new Intent(getApplicationContext(), RelativeLayoutTestActivity.class));
                            break;
                        case R.id.menu_frame:
                            startActivity(new Intent(getApplicationContext(), FrameLayout_Test.class));
                            break;
                        case R.id.menu_talbe:
                            startActivity(new Intent(getApplicationContext(), TableLayoutTestActivity.class));
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
            startActivity(new Intent(getApplicationContext(), XML_ParsingExampleActivity.class));
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
}