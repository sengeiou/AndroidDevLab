package com.yunjeapark.technote.database.sqlite_test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yunjeapark.technote.database.adapter.CallLogAdapter;
import com.yunjeapark.technote.database.data.CallLogData;
import com.yunjeapark.technote.R;
import com.yunjeapark.technote.utility.ChoSearchQuery;

import java.util.ArrayList;

public class CallLogTest extends AppCompatActivity implements CallLogAdapter.MyRecyclerViewClickListener {
    private CallLogAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SearchView callLogSearchView;

    final CallLogDBHelper callLogDBHelper = new CallLogDBHelper(this, "CallLogTable.db", null, 1);
    final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_log_test);

        mRecyclerView = (RecyclerView)findViewById(R.id.call_log_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allCallLogList();

        callLogSearchView = (SearchView)findViewById(R.id.search_call_log);
        callLogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()==0){
                    allCallLogList();
                }else{
                    searchCallLog(s);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClicked(final int position) {
    }

    public void allCallLogList(){
        final SQLiteDatabase callLogListDB = callLogDBHelper.getReadableDatabase();
        final Cursor callLogListDB_cursor = callLogListDB.rawQuery("SELECT * FROM CallLogTable", null);

        final SQLiteDatabase address_book_list_db = addressBookDBHelper.getReadableDatabase();

        ArrayList<CallLogData> callLogList;
        callLogList = new ArrayList<>();
        callLogListDB_cursor.moveToFirst();

        while(callLogListDB_cursor.moveToNext()){
            CallLogData callLogData = new CallLogData();
            String sql = "SELECT * FROM AddressBookList WHERE phone_number = '" + callLogListDB_cursor.getString(2) + "'" ;
            final Cursor address_book_db_cursor = address_book_list_db.rawQuery(sql, null);
            address_book_db_cursor.moveToFirst();
            if(address_book_db_cursor.getCount()==0){
                callLogData.setName(callLogListDB_cursor.getString(2));
            }else{
                callLogData.setName(address_book_db_cursor.getString(1));
                callLogDBHelper.update(address_book_db_cursor.getString(1),callLogListDB_cursor.getString(2));
            }
            callLogData.setPhoneNumber(callLogListDB_cursor.getString(2));
            callLogData.setDate(callLogListDB_cursor.getString(3));
            callLogData.setType(callLogListDB_cursor.getString(4));
            callLogList.add(callLogData);
        }
        mAdapter = new CallLogAdapter(this, callLogList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickListener(this);
    }
    public void searchCallLog(String s){
        final SQLiteDatabase callLogListDB = callLogDBHelper.getReadableDatabase();
        String search_call_log_query = "SELECT * FROM CallLogTable WHERE name LIKE '%" + s + "%' OR" + ChoSearchQuery.makeQuery(s) + ";";
        final Cursor callLogListDB_cursor = callLogListDB.rawQuery(search_call_log_query, null);

        final SQLiteDatabase address_book_list_db = addressBookDBHelper.getReadableDatabase();

        ArrayList<CallLogData> callLogList;
        callLogList = new ArrayList<>();
        callLogListDB_cursor.moveToFirst();
        mAdapter = new CallLogAdapter(this, callLogList);

        while(callLogListDB_cursor.moveToNext()){
            CallLogData callLogData = new CallLogData();
            String sql = "SELECT * FROM AddressBookList WHERE phone_number = '" + callLogListDB_cursor.getString(2) + "'" ;
            final Cursor address_book_db_cursor = address_book_list_db.rawQuery(sql, null);
            address_book_db_cursor.moveToFirst();
            if(address_book_db_cursor.getCount()==0){
                callLogData.setName(callLogListDB_cursor.getString(2));
            }else{
                callLogData.setName(address_book_db_cursor.getString(1));
            }
            callLogData.setPhoneNumber(callLogListDB_cursor.getString(2));
            callLogData.setDate(callLogListDB_cursor.getString(3));
            callLogData.setType(callLogListDB_cursor.getString(4));
            callLogList.add(callLogData);
        }

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickListener(this);
    }
}