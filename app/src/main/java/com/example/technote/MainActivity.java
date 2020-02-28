package com.example.technote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.technote.BLE.Exam1.BleExam1_Main;
import com.example.technote.BLE.FastBle.FastBleMain;
import com.example.technote.Database.AddressBook;
import com.example.technote.Database.ContentProviderPractics.ContentProviderTest2;
import com.example.technote.Database.SQLiteTest;
import com.example.technote.Database.SQLiteTest_2;
import com.example.technote.Dialog_Activity_Fragment.Activity;
import com.example.technote.Dialog_Activity_Fragment.Activity_Screen_1;
import com.example.technote.Dialog_Activity_Fragment.Activity_Screen_2;
import com.example.technote.Dialog_Activity_Fragment.Dialog;
import com.example.technote.Dialog_Activity_Fragment.FirstActivity;
import com.example.technote.GoogleMap.GoogleMapTest;
import com.example.technote.Layout.ConstraintLayout_Test;
import com.example.technote.Layout.CoordinatorLayout_Test;
import com.example.technote.Layout.DrawerLayout_Test;
import com.example.technote.Layout.FrameLayout_Test;
import com.example.technote.Layout.LinearLayout_Test;
import com.example.technote.Layout.RelativeLayoutTest;
import com.example.technote.Layout.TableLayout_Test;
import com.example.technote.Network.AndroidAsyncHttpExample;
import com.example.technote.Network.FANExample;
import com.example.technote.Network.OKHttpExample;
import com.example.technote.Network.RetrofitExample.RetrofitExample;
import com.example.technote.Network.VolleyExample;
import com.example.technote.Thread_Handler.HandlerEx;
import com.example.technote.Thread_Handler.MyAsyncTaskExample_1;
import com.example.technote.Thread_Handler.MyAsyncTaskExample_2;
import com.example.technote.Thread_Handler.ThreadNetworkExample_1;

public class MainActivity extends AppCompatActivity {
    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button10,button11,button12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar) ;
        setSupportActionBar(tb) ;
        tb.setTitleTextColor(Color.WHITE);
        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        button3 = (Button) findViewById(R.id.Button3);
        button4 = (Button) findViewById(R.id.Button4);
        button5 = (Button) findViewById(R.id.Button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.Button7);
        button8 = (Button) findViewById(R.id.Button8);
        button9 = (Button) findViewById(R.id.Button9);
        button10 = (Button) findViewById(R.id.button10);
        button11 = (Button) findViewById(R.id.Button11);
        button12 = (Button) findViewById(R.id.Button12);

        // Layout
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        // Dialog, Activity, Fragment
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                startActivity(new Intent(getApplicationContext(), Activity.class));
                                break;
                            case R.id.menu_go_screen_1:
                                startActivity(new Intent(getApplicationContext(), Activity_Screen_1.class));
                                break;
                            case R.id.menu_go_screen_2:
                                startActivity(new Intent(getApplicationContext(), Activity_Screen_2.class));
                                break;
                            case R.id.menu_firstScreen:
                                startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();//Popup Menu 보이기
            }
        });
        //BLE 연결
        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
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
        });
        //GoogleMap
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoogleMapTest.class));
            }
        });
        //Database
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        button8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.menu_network, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_ok_http_example:
                                startActivity(new Intent(getApplicationContext(), OKHttpExample.class));
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();//Popup Menu 보이기
                */
            }
        });
        button11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
        });
        button12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.menu_network, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
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
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();//Popup Menu 보이기
            }
        });
    }
}