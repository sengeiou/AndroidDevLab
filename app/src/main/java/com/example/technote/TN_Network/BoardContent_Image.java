package com.example.technote.TN_Network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.AutoScrollAdapter;
import com.example.technote.TN_Network.Adapter.CircleIndicator;
import com.example.technote.TN_Network.Data.ImageSliderData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardContent_Image extends AppCompatActivity {

    private String url = "http://yjpapp.com/get_image_content.php";
    private Toolbar imageSliderTestToolbar;
    private TextView post_title, post_price, post_content;
    private ViewPager viewPager;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_content_image);
        imageSliderTestToolbar = (Toolbar)findViewById(R.id.imageSliderToolbars);
        post_title = (TextView)findViewById(R.id.post_title);
        post_price = (TextView)findViewById(R.id.post_price);
        post_content = (TextView)findViewById(R.id.post_content);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id_send");
        setSupportActionBar(imageSliderTestToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setDisplayShowTitleEnabled(false); //툴바 타이틀 없애기

        //Uploading code
        AndroidNetworking.upload(url)
                .addMultipartParameter("id",id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Result","onResponse");
                        try {
                            jsonArray = response.getJSONArray("yjpapp");
                            ImageSliderData imageSliderData= new ImageSliderData();
                            ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist

                            int image_count = Integer.parseInt(jsonArray.getJSONObject(0).get("image_count").toString());

                            if(image_count==1){
                                data.add(jsonArray.getJSONObject(0).get("photo_url_1").toString());
                            }else if (image_count==2){
                                data.add(jsonArray.getJSONObject(0).get("photo_url_1").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_2").toString());
                            }else if (image_count==3){
                                data.add(jsonArray.getJSONObject(0).get("photo_url_1").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_2").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_3").toString());
                            }else if (image_count==4){
                                data.add(jsonArray.getJSONObject(0).get("photo_url_1").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_2").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_3").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_4").toString());
                            }else if (image_count==5){
                                data.add(jsonArray.getJSONObject(0).get("photo_url_1").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_2").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_3").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_4").toString());
                                data.add(jsonArray.getJSONObject(0).get("photo_url_5").toString());
                            }
                            post_title.setText(jsonArray.getJSONObject(0).get("title").toString());
                            post_price.setText(jsonArray.getJSONObject(0).get("price").toString());
                            post_content.setText(jsonArray.getJSONObject(0).get("content").toString());

                            viewPager = (ViewPager) findViewById(R.id.autoViewPager);
                            AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getApplicationContext(), data);
                            viewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                            //viewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
                            //viewPager.startAutoScroll(); //Auto Scroll 시작
                            CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
                            circleIndicator.setupWithViewPager(viewPager);
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
                            {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    Log.d("OnPageChangeListener","onPageScrolled");

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    Log.d("OnPageChangeListener","onPageSelected");

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    Log.d("OnPageChangeListener","onPageScrollStateChanged");
                                }
                            });
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("Result","onError" + anError.toString());

                    }
                });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}