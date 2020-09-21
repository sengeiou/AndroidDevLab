package com.yunjeapark.technote.database.sqlite_test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

public class CallEvent extends AppCompatActivity {
    Button event_1, event_2, event_3;
    String phoneNumber_A,phoneNumber_B,phoneNumber_C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_event);
        //DB접근 영역
        final CallLogDBHelper callLogDBHelper = new CallLogDBHelper(this, "CallLogTable.db", null, 1);
        final SQLiteDatabase callLogDB = callLogDBHelper.getReadableDatabase();

        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();

        //UI
        phoneNumber_A = "01012345678"; phoneNumber_B = "01011112222"; phoneNumber_C = "01022336789";

        event_1 = (Button)findViewById(R.id.button_event_1);
        event_2 = (Button)findViewById(R.id.button_event_2);
        event_3 = (Button)findViewById(R.id.button_event_3);


        event_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phoneNumber_A + "'";
                Cursor cursor = db.rawQuery(query , null);
                cursor.moveToFirst();

                Intent intent = new Intent(getApplicationContext(), CallReceiveScreen.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name","null");
                    intent.putExtra("phone_number", phoneNumber_A);
                    intent.putExtra("database_in_out",false);
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
                    intent.putExtra("database_in_out",true);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "A에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
        event_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phoneNumber_B  + "'";
                Cursor cursor = db.rawQuery(query , null);
                cursor.moveToFirst();

                Intent intent = new Intent(getApplicationContext(), CallReceiveScreen.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name","null");
                    intent.putExtra("phone_number", phoneNumber_B);
                    intent.putExtra("database_in_out",false);
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
                    intent.putExtra("database_in_out",true);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "B에게 전화거는 이벤트", Toast.LENGTH_LONG).show();
            }
        });
        event_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phoneNumber_C  + "'";
                Cursor cursor = db.rawQuery(query , null);
                cursor.moveToFirst();

                Intent intent = new Intent(getApplicationContext(), CallReceiveScreen.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name","null");
                    intent.putExtra("phone_number", phoneNumber_C);
                    intent.putExtra("database_in_out",false);
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
                    intent.putExtra("database_in_out",true);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "모르는 전화번호 전화오는 이벤트", Toast.LENGTH_LONG).show();
            }
        });

    }
}