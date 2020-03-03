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


public class Board_List_Fragment extends Fragment implements BoardListAdapter.MyRecyclerViewClickListener {
    private static String TAG = "phptest";
    private ArrayList<BoardData> mArrayList;
    private BoardListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    BoardData personalData;
    private String url = "http://yjpapp.com/getjson.php";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_network_board_list, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mArrayList = new ArrayList<>();

        mAdapter = new BoardListAdapter(getActivity(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(1)){
                    if(mArrayList.size()==5){
                        Toast.makeText(getContext(), "End Scroll", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //RecyclerView를 역순으로 정렬하는 코드
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setNestedScrollingEnabled(false);

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("onResponse","FANExample Type : get,result: onResponse");
                        try {
                            JSONArray jsonArray = response.getJSONArray("yjpapp");
                            for(int i=0;i<jsonArray.length();i++){
                                personalData = new BoardData();
                                personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                                personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                                personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                                personalData.setSubject(jsonArray.getJSONObject(i).get("subject").toString());
                                personalData.setPrice(jsonArray.getJSONObject(i).get("price").toString());
                                mArrayList.add(personalData);
                            }
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
        return layout;

    }

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
}