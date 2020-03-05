package com.example.technote.TN_Dialog_Activity_Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.technote.TN_Network.Fragment_Board_List;
import com.example.technote.R;

public class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment_Board_List fragment = new Fragment_Board_List();
        fragmentTransaction.add(R.id.fragment_real, fragment);
        fragmentTransaction.commit();
    }

}