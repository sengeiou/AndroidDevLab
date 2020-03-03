package com.example.technote.TN_Dialog_Activity_Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.technote.TN_Network.Board_List_Fragment;
import com.example.technote.R;

public class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Board_List_Fragment fragment = new Board_List_Fragment();
        fragmentTransaction.add(R.id.fragment_real, fragment);
        fragmentTransaction.commit();
    }

}