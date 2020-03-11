package com.example.technote.TN_Network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.AutoScrollAdapter;
import com.example.technote.TN_Network.Adapter.CircleIndicator;
import com.example.technote.TN_Network.Data.ImageSliderData;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BoardImageContent extends AppCompatActivity {
    private static String IP_ADDRESS = "yjpapp.com";
    private static String TAG = "phptest";
    private String mJsonString;
    private PageIndicatorView itemIndicator;
    private Toolbar imageSliderTestToolbar;
    private TextView post_title, post_price, post_content;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board_image_content);
        imageSliderTestToolbar = (Toolbar)findViewById(R.id.imageSliderToolbars);
        post_title = (TextView)findViewById(R.id.post_title);
        post_price = (TextView)findViewById(R.id.post_price);
        post_content = (TextView)findViewById(R.id.post_content);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id_send");
        //Uploading code
        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/getPoster.php", id);
        setSupportActionBar(imageSliderTestToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바에 왼쪽버튼 추가하기
        getSupportActionBar().setDisplayShowTitleEnabled(false); //툴바 타이틀 없애기


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
    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BoardImageContent.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = "id=" + params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                // 모든 요청 헤더를 전달하고 나면 HttpURLConnection 객체의 getResponseCode() 메서드를 이용해 유효한 응답이 있는지 확인할 수 있습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    //오류가 없는 경우 이제 getInputStream() 메서드를 호출해서 연결의 입력 스트림에 대한 참조를 가져올 수 있습니다.
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString =result;
                showResult();
            }
        }
    }
    private void showResult(){

        String TAG_JSON = "yjpapp";
        String TAG_ID = "id";
        String TAG_PHOTO_URL_1 = "photo_url_1";
        String TAG_PHOTO_URL_2 = "photo_url_2";
        String TAG_PHOTO_URL_3 = "photo_url_3";
        String TAG_PHOTO_URL_4 = "photo_url_4";
        String TAG_PHOTO_URL_5 = "photo_url_5";
        String TAG_TITLE = "title";
        String TAG_PRICE = "price";
        String TAG_CONTENT = "content";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                // php 서버의 데이터를 get하여 String 변수에 삽입한다.


                post_title.setText(item.getString(TAG_TITLE)); //데이터베이스의 제목을 불러와 텍스트뷰에 설정하기.
                post_price.setText(item.getString(TAG_PRICE));
                post_content.setText(item.getString(TAG_CONTENT));

                ImageSliderData imageSliderData= new ImageSliderData();
                // php 서버의 데이터를 item.getStirng을 통하여 가져와 imageSliderData에 set해놓는다.
                imageSliderData.setId(item.getString(TAG_ID));
                imageSliderData.setPhoto_url_1(item.getString(TAG_PHOTO_URL_1));
                imageSliderData.setPhoto_url_2(item.getString(TAG_PHOTO_URL_2));
                imageSliderData.setPhoto_url_3(item.getString(TAG_PHOTO_URL_3));
                imageSliderData.setPhoto_url_4(item.getString(TAG_PHOTO_URL_4));
                imageSliderData.setPhoto_url_5(item.getString(TAG_PHOTO_URL_5));

                // MySQL서버에 저장된 이미지 개수에 따라 인디케이터가 설정이 되도록 한다.
                ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
                if(imageSliderData.getPhoto_url_2() != "null" && imageSliderData.getPhoto_url_3() == "null"){
                    data.add(imageSliderData.getPhoto_url_1());
                    data.add(imageSliderData.getPhoto_url_2());
                }else if(imageSliderData.getPhoto_url_3() != "null" && imageSliderData.getPhoto_url_4() == "null"){
                    data.add(imageSliderData.getPhoto_url_1());
                    data.add(imageSliderData.getPhoto_url_2());
                    data.add(imageSliderData.getPhoto_url_3());
                }else if(imageSliderData.getPhoto_url_4() != "null" && imageSliderData.getPhoto_url_5()=="null"){
                    data.add(imageSliderData.getPhoto_url_1());
                    data.add(imageSliderData.getPhoto_url_2());
                    data.add(imageSliderData.getPhoto_url_3());
                    data.add(imageSliderData.getPhoto_url_4());
                }else if(imageSliderData.getPhoto_url_5() !="null"){
                    data.add(imageSliderData.getPhoto_url_1());
                    data.add(imageSliderData.getPhoto_url_2());
                    data.add(imageSliderData.getPhoto_url_3());
                    data.add(imageSliderData.getPhoto_url_4());
                    data.add(imageSliderData.getPhoto_url_5());
                }else{
                    data.add(imageSliderData.getPhoto_url_1());
                }

                viewPager = (ViewPager) findViewById(R.id.autoViewPager);
                AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
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
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}