package com.example.technote.Database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technote.Adapter.AddressDataAdapter;
import com.example.technote.R;

import java.util.ArrayList;

public class AddressBook extends AppCompatActivity  implements AddressDataAdapter.MyRecyclerViewClickListener{
    private Button button_add_address, button_call, button_call_log;
    private AddressDataAdapter mAdapter;
    private ArrayList<AddressData> mArrayList;
    private RecyclerView mRecyclerView;
    final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AdressBookList.db", null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_address_book);

        mRecyclerView = (RecyclerView)findViewById(R.id.address_book_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();

        mAdapter = new AddressDataAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

        SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM AdressBookList", null);
        while (cursor.moveToNext()) {
            AddressData addressData = new AddressData();
            addressData.setName((cursor.getString(1)));
            addressData.setPhone_number(cursor.getString(2));
            mArrayList.add(addressData);
            mAdapter.notifyDataSetChanged();
        }

        button_add_address = (Button)findViewById(R.id.button_add_address);
        button_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddAddress.class));
            }
        });

        button_call = (Button)findViewById(R.id.button_call);
        button_call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CallEvent.class));
            }
        });

        button_call_log = (Button)findViewById(R.id.button_call_log);
        button_call_log.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CallLog_Test.class));
            }
        });

    }
    @Override
    public void onItemClicked(int position) {
        SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AdressBookList", null);
        cursor.move(position+1);
        Toast.makeText(getApplicationContext(), cursor.getString(0) +"          " + cursor.getString(2), Toast.LENGTH_LONG).show();
    }
}