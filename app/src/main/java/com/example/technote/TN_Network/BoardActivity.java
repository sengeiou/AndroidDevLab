package com.example.technote.TN_Network;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.Network_Board_ImageListAdapter;
import com.example.technote.TN_Network.Data.Network_Board_ImageListData;
import com.example.technote.TN_Network.NetworkAPI_Library.AndroidAsyncHttpExample;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BoardActivity extends AppCompatActivity implements Network_Board_ImageListAdapter.MyRecyclerViewClickListener{
    private static String TAG = "phptest";
    private ArrayList<Network_Board_ImageListData> mArrayList;
    private Network_Board_ImageListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    Network_Board_ImageListData personalData;
    private String url = "http://yjpapp.com/getjson.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_board);

        mRecyclerView = (RecyclerView)findViewById(R.id.network_board_image_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mArrayList = new ArrayList<>();

        mAdapter = new Network_Board_ImageListAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(1)){
                    if(mArrayList.size()==5){
                        Toast.makeText(getApplicationContext(), "End Scroll", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //RecyclerView를 역순으로 정렬하는 코드
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setItemPrefetchEnabled(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setNestedScrollingEnabled(false);

        AndroidAsyncHttpExample.AndroidAsyncHttpClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("yjpapp");
                    for(int i=0;i<jsonArray.length();i++){
                        personalData = new Network_Board_ImageListData();
                        personalData.setId(jsonArray.getJSONObject(i).get("id").toString());
                        personalData.setPhoto_url_1(jsonArray.getJSONObject(i).get("photo_url_1").toString());
                        personalData.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                        personalData.setPrice(jsonArray.getJSONObject(i).get("subject").toString());
                        mArrayList.add(personalData);
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }
                Log.d("RequestResult","AndroidAsyncHttpExample Type : post, Result : onSuccess, State Code : " + String.valueOf(statusCode));
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });


    }
    @Override
    public void onItemClicked(int position) {

    }
}
