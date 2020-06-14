package com.yunjeapark.technote.tn_network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.yunjeapark.technote.R;
import com.yunjeapark.technote.tn_network.adapter.Network_Board_VideoListAdapter;
import com.yunjeapark.technote.tn_network.data.Network_Board_VideoListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Board_VideoList extends Fragment implements Network_Board_VideoListAdapter.MyRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener{
    private static String TAG = "phptest";
    private ArrayList<Network_Board_VideoListData> mArrayList;
    private Network_Board_VideoListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Network_Board_VideoListData videoListData;
    private String url = "http://yjpapp.com/get_video_list.php";
    private SwipeRefreshLayout swipeRefreshLayout = null; // 위로 끌어당겨서 새로고침
    private int restArray, updateCount, firstArrayLength;
    private JSONArray jsonArray;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network_board_video_list, container, false);
        mArrayList = new ArrayList<>();
        mAdapter = new Network_Board_VideoListAdapter(getActivity(), mArrayList);

        mLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.network_board_video_list_swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.network_board_video_list_recyclerview);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //RecyclerVIew Performance 향상
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mAdapter.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        //RecyclerView를 아래로 내릴 때 작동

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(1)&&restArray>=0){ //restArray가 양수 일때
                    // List update
                    ++updateCount;
                    videoListDataUpdate();
                }
            }
        });
        restArray = 0; //restArray 값 초기화
        updateCount=1; // 동영상 리스트를 클릭하면 첫 1회 업데이트를 함으로 1로 설정

        videoListDataUpdate();
        return view;
    }
    //Network_Board_VideoListAdapter.MyRecyclerViewClickListener
    @Override
    public void onItemClicked(int position) {
        Intent videoContent = new Intent(getActivity(), BoardContent_Video.class);
        videoContent.putExtra("video_url",mArrayList.get(position).getVideo_url());

        startActivity(videoContent);
    }

    // swipeRefreshLayout의 OnRefreshListener (Recycler View를 위로 당길 때 작동)
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
                            if (jsonArray.length()>firstArrayLength){
                                for(int i = firstArrayLength-1;i<jsonArray.length();i++){
                                    setVideoListData(i);
                                }
                                firstArrayLength = jsonArray.length();
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

    public void videoListDataUpdate(){
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse","FANExample Type : get,result: onResponse");
                        try {
                            jsonArray = response.getJSONArray("yjpapp");

                            if(updateCount==1){ // 첫 이미지 리스트 업데이트
                                firstArrayLength = jsonArray.length(); // 데이터 개수를 저장

                                if(jsonArray.length()<10){ // 쌓인 이미지 리스트 데이터가 10개 미만이면
                                    for(int i=0;i<jsonArray.length();i++){
                                        setVideoListData(i);
                                    }
                                    reversRecyclerView();//RecyclerView를 역순으로 정렬하는 함수

                                }else{ // 10개 이상
                                    for (int i = jsonArray.length() - 1; i >= jsonArray.length() - (10 * updateCount); i--) {
                                        setVideoListData(i);
                                    }
                                }
                            }else { // 아래로 스크롤해서 업데이트
                                //Log.d("RestArray","RestArray : "+String.valueOf(restArray));
                                if(restArray<10){ // 마지막 페이지 업데이트
                                    for(int i=restArray-1;i>=0;i--){
                                        setVideoListData(i);
                                    }

                                }else{ // 중간 페이지 업데이트
                                    for (int i = restArray-1 - 1; i >= restArray - 10; i--) {
                                        setVideoListData(i);
                                    }
                                }
                            }

                            restArray = jsonArray.length() - (10 * updateCount);
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
    // json 데이터를 가져와서 set하는 함수
    public void setVideoListData(int i) throws JSONException {
        videoListData = new Network_Board_VideoListData();
        videoListData.setId(jsonArray.getJSONObject(i).get("id").toString());
        videoListData.setVideo_url(jsonArray.getJSONObject(i).get("video_url").toString());
        videoListData.setThumbnail_url(jsonArray.getJSONObject(i).get("thumbnail_url").toString());
        videoListData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
        videoListData.setSubject(jsonArray.getJSONObject(i).get("content").toString());
        mArrayList.add(videoListData);
    }

    //RecyclerView를 역순으로 정렬하는 함수
    public void reversRecyclerView(){
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("onDestroyView","동영상");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy","동영상");
    }
}