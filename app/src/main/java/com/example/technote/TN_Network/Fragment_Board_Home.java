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

public class Fragment_Board_Home extends Fragment {
    View view;
    private Button imageUploadButton, videoUploadButton, videoPlayButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network_board_home, container, false);

        imageUploadButton = (Button)view.findViewById(R.id.button_network_board_write);
        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BoardUpload_Image.class));
            }
        });

        videoUploadButton = (Button)view.findViewById(R.id.button_network_board_upload_video);
        videoUploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), BoardUpload_Video.class));
            }
        });

        videoPlayButton = (Button)view.findViewById(R.id.btn_play_video);
        videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardContent_Video2.class);
                //intent.putExtra("video_url","http://yjpapp.com/upload_video/video/8.mp4");
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("onDestroyView","홈");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy","홈");
    }

}
