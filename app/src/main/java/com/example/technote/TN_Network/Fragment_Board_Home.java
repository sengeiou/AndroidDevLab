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
    private Button writeButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network_board_home, container, false);

        writeButton = (Button)view.findViewById(R.id.button_network_board_write);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BoardImageUpload.class));
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("onDestoryView","홈");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy","홈");
    }

}
