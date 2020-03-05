package com.example.technote.TN_Network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.technote.R;

public class Fragment_Board_Video extends Fragment {
    View view;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network_board_video, container, false);

        button = view.findViewById(R.id.button_video_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BoardVideoPlay_1.class));
            }
        });
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
