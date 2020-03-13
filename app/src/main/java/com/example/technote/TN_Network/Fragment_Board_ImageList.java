package com.example.technote.TN_Network;

import android.content.Intent;
import android.os.Bundle;
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
    private ArrayList<Network_Board_ImageListData> mArrayList;
    private Network_Board_ImageListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Network_Board_ImageListData imageListData;
    private String url = "http://yjpapp.com/get_image_list.php";
    private SwipeRefreshLayout swipeRefreshLayout = null; // 위로 끌어당겨서 새로고침
    private int restArray, updateCount, firstArrayLength;
    private JSONArray jsonArray;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_network_board_image_list, container, false);
        mArrayList = new ArrayList<>();
        mAdapter = new Network_Board_ImageListAdapter(getActivity(), mArrayList);
        mAdapter.notifyDataSetChanged();

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

        //RecyclerView를 아래로 내릴 때 작동
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(1)&&restArray>=0){ //restArray가 양수 일때
                    ++updateCount;
                    imageListDataUpdate();
                }
            }
        });

        mAdapter.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        restArray=0; updateCount=1;
        //swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorCyan); swipeRefreshLayout Color 지정하기

        imageListDataUpdate();

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
                            if (jsonArray.length()>firstArrayLength){
                                for(int i = firstArrayLength-1;i<jsonArray.length();i++){
                                    setImageListData(i);
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
    //Network_Board_ImageListAdapter.MyRecyclerViewClickListener
    @Override
    public void onItemClicked(int position) {
        Intent startImageSliderTest = new Intent(getActivity(), BoardContent_Image.class);
        startImageSliderTest.putExtra("id_send",mArrayList.get(position).getId());
        startActivity(startImageSliderTest);
    }

    public void imageListDataUpdate(){
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
                                        setImageListData(i);
                                    }
                                    reversRecyclerView();//RecyclerView를 역순으로 정렬하는 함수
                                }else{ // 10개 이상
                                    for (int i = jsonArray.length() - 1; i >= jsonArray.length() - (10 * updateCount); i--) {
                                        setImageListData(i);
                                    }
                                }
                            }else { // 아래로 스크롤해서 업데이트
                                //Log.d("RestArray","RestArray : "+String.valueOf(restArray));
                                if(restArray<10){ // 마지막 페이지 업데이트
                                    for(int i=restArray-1;i>=0;i--){
                                        setImageListData(i);
                                    }
                                }else{ // 중간 페이지 업데이트
                                    for (int i = restArray-1 - 1; i >= restArray - 10; i--) {
                                        setImageListData(i);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("onDestroyView","게시판");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy","게시판");
    }
    public void setImageListData(int i) throws JSONException {
        imageListData = new Network_Board_ImageListData();
        imageListData.setId(jsonArray.getJSONObject(i).get("id").toString());
        imageListData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
        imageListData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
        imageListData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
        imageListData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
        mArrayList.add(imageListData);
    }
    public void reversRecyclerView(){
        //RecyclerView를 역순으로 정렬하는 코드
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}