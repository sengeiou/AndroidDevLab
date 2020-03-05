package com.example.technote.TN_Network;

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
import com.example.technote.TN_Network.Adapter.BoardListAdapter;
import com.example.technote.TN_Network.Data.BoardData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_Board_List extends Fragment implements BoardListAdapter.MyRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "phptest";
    private ArrayList<BoardData> mArrayList;
    private BoardListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    BoardData personalData;
    private String url = "http://yjpapp.com/getjson.php";
    private SwipeRefreshLayout swipeRefreshLayout = null; // 위로 끌어당겨서 새로고침
    private int restArray, updateCount, firstArrayLength;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_network_board_list, container, false);
        mArrayList = new ArrayList<>();
        mAdapter = new BoardListAdapter(getActivity(), mArrayList);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickListener(this);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(layout.getContext(), 1));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(1)&&restArray>=0){ //restArray가 양수 일때
                    ++updateCount;
                    listDataUpdate();
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext());
        restArray=0; updateCount=1;

        swipeRefreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.network_board_list_swipeRefreshLayout);
        //swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorCyan); swipeRefreshLayout Color 지정하기
        swipeRefreshLayout.setOnRefreshListener(this);

        listDataUpdate();

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
                            JSONArray jsonArray = response.getJSONArray("yjpapp");
                            if (jsonArray.length()>firstArrayLength){
                                for(int i = firstArrayLength-1;i<jsonArray.length();i++){
                                    personalData = new BoardData();
                                    personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                    personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                    personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                    personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                    personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                    mArrayList.add(personalData);
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
    //BoardListAdapter.MyRecyclerViewClickListener
    @Override
    public void onItemClicked(int position) {

    }
    public static class AndroidAsyncHttpClient{

        private static AsyncHttpClient client = new AsyncHttpClient();

        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        private static String getAbsoluteUrl(String relativeUrl) {
            return relativeUrl;
        }
    }

    public void listDataUpdate(){
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("onResponse","FANExample Type : get,result: onResponse");
                        try {
                            JSONArray jsonArray = response.getJSONArray("yjpapp");
                            if (updateCount>1){ // 업데이트 횟수가 처음이 아니면
                                Log.d("RestArray","RestArray : "+String.valueOf(restArray));
                                if(restArray<10){ // 마지막 페이지 업데이트
                                    for(int i=restArray-1;i>=0;i--){
                                        personalData = new BoardData();
                                        personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                        personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                        personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                        personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                        personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                        mArrayList.add(personalData);
                                    }
                                }else{ // 중간 페이지 업데이트
                                    for (int i = restArray-1 - 1; i >= restArray - 10; i--) {
                                        personalData = new BoardData();
                                        personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                        personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                        personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                        personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                        personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                        mArrayList.add(personalData);
                                    }
                                }
                            }else {
                                firstArrayLength = jsonArray.length();
                                if(jsonArray.length()<10){ // 쌓인 데이터가 10개 미만이면
                                    for(int i=0;i<jsonArray.length();i++){
                                        personalData = new BoardData();
                                        personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                        personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                        personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                        personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                        personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                        mArrayList.add(personalData);
                                    }
                                    reversRecyclerView();//RecyclerView를 역순으로 정렬하는 함수

                                }else{ // 첫 번째로 보여줄 리스트 업데이트
                                    for (int i = jsonArray.length() - 1; i >= jsonArray.length() - (10 * updateCount); i--) {
                                        personalData = new BoardData();
                                        personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                        personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                        personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                        personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                        personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                        mArrayList.add(personalData);
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
    public void reversRecyclerView(){
        //RecyclerView를 역순으로 정렬하는 코드
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}