package com.example.technote.Database;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class Calling extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_calling);

        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);

    }
}