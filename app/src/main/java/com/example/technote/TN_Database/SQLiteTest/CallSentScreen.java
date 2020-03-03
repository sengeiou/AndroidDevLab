package com.example.technote.TN_Database.SQLiteTest;

import android.content.ContentResolver;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class CallSentScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_sent);

        ContentResolver resolver = getContentResolver();
    }
}