package com.example.technote.Database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class Calling extends AppCompatActivity {
    TextView calling_name, calling_phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_calling);

        calling_name = (TextView)findViewById(R.id.text_caller_name);
        calling_phone_number = (TextView)findViewById(R.id.text_phone_number);
        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);
        final Intent intent = getIntent(); /*데이터 수신*/
        SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AddressBookList", null);
        final String name = intent.getExtras().getString("name");
        final String phone_number = intent.getExtras().getString("phone_number");

        calling_name.setText(name);
        calling_phone_number.setText(phone_number);

    }
}