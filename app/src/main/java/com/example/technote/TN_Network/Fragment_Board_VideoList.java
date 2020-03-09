package com.example.technote.TN_Network;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.technote.R;
import com.example.technote.TN_Network.Adapter.Network_Board_ImageListAdapter;
import com.example.technote.TN_Network.Data.Network_Board_ImageListData;

import org.json.JSONArray;

import java.util.ArrayList;

public class Fragment_Board_VideoList extends Fragment {
    private static String TAG = "phptest";
    private ArrayList<Network_Board_ImageListData> mArrayList;
    private Network_Board_ImageListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    Network_Board_ImageListData personalData;
    private String url = "http://yjpapp.com/get_video_list.php";
    private SwipeRefreshLayout swipeRefreshLayout = null; // 위로 끌어당겨서 새로고침
    private int restArray, updateCount, firstArrayLength;
    private JSONArray jsonArray;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network_board_video, container, false);

        return view;
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
