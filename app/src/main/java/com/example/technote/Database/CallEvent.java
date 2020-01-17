package com.example.technote.Database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class CallEvent extends AppCompatActivity {
    Button event_1, event_2, event_3;
    String phoneNumber_A,phoneNumber_B,phoneNumber_C;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_event);
        phoneNumber_A = "01012345678"; phoneNumber_B = "01011112222"; phoneNumber_C = "01022336789";
        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        event_1 = (Button)findViewById(R.id.button_event_1);
        event_2 = (Button)findViewById(R.id.button_event_2);
        event_3 = (Button)findViewById(R.id.button_event_3);

        event_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phoneNumber_A + "'";
                Cursor cursor = db.rawQuery(query , null);
                cursor.moveToFirst();

                Intent intent = new Intent(getApplicationContext(), Calling.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name",phoneNumber_A);
                    intent.putExtra("phone_number", "");
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
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

                Intent intent = new Intent(getApplicationContext(), Calling.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name",phoneNumber_B);
                    intent.putExtra("phone_number", "");
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "B에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
        event_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phoneNumber_C  + "'";
                Cursor cursor = db.rawQuery(query , null);
                cursor.moveToFirst();

                Intent intent = new Intent(getApplicationContext(), Calling.class);
                if(cursor.getCount()==0){
                    intent.putExtra("name",phoneNumber_C);
                    intent.putExtra("phone_number", "");
                    startActivity(intent);
                }else{
                    intent.putExtra("name",cursor.getString(1));
                    intent.putExtra("phone_number", cursor.getString(2));
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "C에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
    }
}