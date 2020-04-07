package com.example.technote.TN_Network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.Network_Board_ImageListAdapter;
import com.example.technote.TN_Network.Data.Network_Board_ImageListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_Board_ImageList extends Fragment implements Network_Board_ImageListAdapter.MyRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "phptest";
    public static int THREAD_REQUEST = 1;
    public static int THREAD_RESPONSE = 2;
    public static int offset;

    private static ArrayList<Network_Board_ImageListData> mArrayList = new ArrayList<>();
    private static Network_Board_ImageListAdapter mAdapter;
    private static Network_Board_ImageListData imageListData;
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private String url = "http://yjpapp.com/get_image_list.php";
    private String total_recode_url = "http://www.yjpapp.com/get_recode_count.php";
    private SwipeRefreshLayout swipeRefreshLayout = null; // 위로 끌어당겨서 새로고침
    private JSONArray jsonArray;

    private int scrollRange, imageId, total_recode;

    private MyHandler myHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_network_board_image_list, container, false);
        mAdapter = new Network_Board_ImageListAdapter(getActivity(), mArrayList);
        myHandler = new MyHandler();
        AndroidNetworking.get(total_recode_url) // 처음 서버에 전체 recode 개수를 가져온 후 서버에서 ImageView 로드
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("GetRecodeResult","GetRecodeResult Type : get,result: onResponse");
                        try {
                            jsonArray = response.getJSONArray("yjpapp");
                            total_recode = Integer.parseInt(jsonArray.getJSONObject(0).get("recode").toString());
                            Log.d("total",String.valueOf(total_recode));

                            firstImageListDataUpdate(10,offset); // 처음 표현할 10개의 데이터 업데이트

                            offset+=10;
                            myHandler.sendEmptyMessage(THREAD_REQUEST); // 핸들러에 백그라운드 Thread run을 Request한다.

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("GetRecodeResult","GetRecodeResult Type : get, result : onError" + error.toString());
                        // handle error
                    }
                });
        swipeRefreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.network_board_image_list_swipeRefreshLayout);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.network_board_image_list_recyclerview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(layout.getContext(), 1));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("onScrolled","computeVerticalScrollOffset : " + String.valueOf(scrollRange));

        // offset는 0, updateCount는 onCreate에서 한번 업데이트를 함으로 1, 초기 ScrollBar OffsetRange에서 49/50한 값은 대략 1600
        scrollRange = 1600;  offset = 0;
        mAdapter.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        // swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorCyan);
        // swipeRefreshLayout Color &#xc9c0;&#xc815;&#xd558;&#xae30;

        /*RecyclerView를 아래로 내릴 때 작동
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("onScrolled","computeVerticalScrollOffset : " + String.valueOf(recyclerView.computeVerticalScrollOffset()));
                Log.d("onScrolled","computeVerticalScrollRange : " + String.valueOf(recyclerView.computeVerticalScrollRange()));
                Log.d("onScrolled","computeVerticalScrollRange : " + String.valueOf(mRecyclerView.computeVerticalScrollRange()));

                if(recyclerView.computeVerticalScrollOffset() >= scrollRange){ //restArray가 양수 일때
                    mAdapter.notifyDataSetChanged();

                    scrollRange = (49 * mRecyclerView.computeVerticalScrollRange()) / 50; // ScrollBar가 49/50 지점에서 업데이트하도록 설정.
                    Log.d("onScrolled","ifIn");
                }
            }
        });
         */
        return layout;
    }
    // SwipeRefreshLayout.OnRefreshListener
    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "onRefresh()", Toast.LENGTH_SHORT).show();
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("yjpapp");
                            if (Integer.parseInt(jsonArray.getJSONObject(0).get("id").toString())>imageId){
                                for(int i = 0;i<jsonArray.length();i++){
                                    firstImageListDataUpdate(10,0);
                                }
                                reversRecyclerView();
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false); //Refreshing 알려주는 Progress를 멈춤
                    }
                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false); //Refreshing 알려주는 Progress를 멈춤
                    }
                });
    }
    //Network_Board_ImageListAdapter.MyRecyclerViewClickListener
    @Override
    public void onItemClicked(int position) {
        Intent startImageSliderTest = new Intent(getActivity(), BoardContent_Image.class);
        startImageSliderTest.putExtra("id_send",mArrayList.get(position).getId());
        startActivity(startImageSliderTest);
    }

    public void firstImageListDataUpdate(int updateSize, int settingOffset){
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .addQueryParameter("offset",String.valueOf(settingOffset))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RequestResult","FANExample Type : get,result: onResponse");
                        try {
                            jsonArray = response.getJSONArray("yjpapp");

                            imageId = Integer.parseInt(jsonArray.getJSONObject(0).get("id").toString());
                            for (int i = 0; i < updateSize; i++) { // 쌓인 데이터 중 최신꺼를 updateSize 표현
                                imageListData = new Network_Board_ImageListData();
                                imageListData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                imageListData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                imageListData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                imageListData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                imageListData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                mArrayList.add(imageListData);
                            }
                            total_recode -= 10;
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.d(TAG, "showResult : ", e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("RequestResult","FANExample Type : get, result : onError" + error.toString());
                        // handle error
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mArrayList.clear();
        Log.d("onDestroyView","게시판");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy","게시판");
    }

    public void reversRecyclerView(){
        //RecyclerView를 역순으로 정렬하는 코드
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public class BackgroundThread extends Thread {
        public void run() {
            AndroidNetworking.get(url)
                    .setPriority(Priority.HIGH)
                    .addQueryParameter("offset",String.valueOf(offset))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RequestResult","ImageListData_BackgroundThread Type : get,result: onResponse");
                            try {
                                jsonArray = response.getJSONArray("yjpapp");
                                for (int i = 0; i < 10; i++) { // 쌓인 데이터 중 최신꺼를 updateSize 표현
                                    imageListData = new Network_Board_ImageListData();
                                    imageListData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                    imageListData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                    imageListData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                    imageListData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                    imageListData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                    mArrayList.add(imageListData);
                                }
                            } catch (JSONException e) {
                                Log.d(TAG, "showResult : ", e);
                            }

                            offset += 10; // 상위 10개 데이터를 구현했음으로 그 다음 상위 10개 데이터를 구현하기 위해 10을 더한다.
                            total_recode -= 10; // 10개의 이미지 리스트 데이터를 넣었음으로 표현할 전체 남은 개수를 10개 줄인다.
                            myHandler.sendEmptyMessage(THREAD_RESPONSE);

                        }
                        @Override
                        public void onError(ANError error) {
                            Log.d("RequestResult","ImageListData_BackgroundThread Type : get, result : onError" + error.toString());
                            // handle error
                        }
                    });
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == THREAD_REQUEST){
                BackgroundThread backgroundThread = new BackgroundThread();
                if(total_recode>0){
                    Log.d("HandlerState", "THREAD_REQUEST start thread");
                    backgroundThread.start();
                }else{
                    //End Thread
                    Log.d("HandlerState", "THREAD_REQUEST interrupt thread");

                    //backgroundThread.interrupt();
                }
            }else if (msg.what == THREAD_RESPONSE) {
                Log.d("HandlerState","THREAD_RESPONSE");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();

                    }
                });
                sendEmptyMessage(THREAD_REQUEST);
            }
        }
    }
}