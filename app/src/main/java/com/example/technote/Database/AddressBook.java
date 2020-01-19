package com.example.technote.Database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technote.Adapter.AddressDataAdapter;
import com.example.technote.R;
import com.example.technote.Utility.ChoSearchQuery;

import java.util.ArrayList;

public class AddressBook extends AppCompatActivity  implements AddressDataAdapter.MyRecyclerViewClickListener{
    private Button button_add_address, button_call, button_call_log;
    private AddressDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SearchView address_book_searchView;
    final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_address_book);

        //RecyclerView 영역
        mRecyclerView = (RecyclerView)findViewById(R.id.address_book_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //SQLite DB접근 영역
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        all_address_list();

        //서치뷰 영역
        address_book_searchView = findViewById(R.id.address_book_search_view);
        address_book_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               if(s.length()==0){
                   all_address_list();
               }else {
                   search_result(s);
               }
                return false;
            }
        });
        //Button Click Event 영역
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
                startActivity(new Intent(getApplicationContext(), RealCallLog_Test.class));
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        all_address_list();
    }
    @Override
    public void onItemClicked(final int position) {
        SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM AddressBookList", null);
        cursor.move(position+1);
        //Toast.makeText(getApplicationContext(), cursor.getString(0) +"          " + cursor.getString(2), Toast.LENGTH_LONG).show();

        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("연락처를 수정하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent addressbookInetent = new Intent(getApplicationContext(),AddressRevise.class);
                        addressbookInetent.putExtra("id",cursor.getInt(0));
                        startActivity(addressbookInetent);
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    void search_result(String s){
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();

        String addrss_book_search_query = "SELECT * FROM AddressBookList WHERE name LIKE '%"  + s + "%' OR" + ChoSearchQuery.makeQuery(s)+" ;";
        Cursor address_book_search_cursor = db.rawQuery(addrss_book_search_query,null);

        ArrayList<AddressData> searchResultList;
        searchResultList = new ArrayList<>();
        mAdapter = new AddressDataAdapter(this, searchResultList);

        while (address_book_search_cursor.moveToNext()){
            AddressData addressData2 = new AddressData();
            addressData2.setName((address_book_search_cursor.getString(1)));
            addressData2.setPhone_number(address_book_search_cursor.getString(2));
            searchResultList.add(addressData2);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);
    }
    void all_address_list(){
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AddressBookList", null);
        ArrayList<AddressData> mArrayList;
        mArrayList = new ArrayList<>();
        mAdapter = new AddressDataAdapter(this, mArrayList);
        while (cursor.moveToNext()) {
            AddressData addressData = new AddressData();
            addressData.setName((cursor.getString(1)));
            addressData.setPhone_number(cursor.getString(2));
            mArrayList.add(addressData);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);
    }
}