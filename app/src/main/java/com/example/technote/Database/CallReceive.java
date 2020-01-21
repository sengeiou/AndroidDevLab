package com.example.technote.Database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallReceive extends AppCompatActivity {
    TextView calling_name, calling_phone_number;
    ImageView receive, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_receive);

        //UI
        calling_name = (TextView)findViewById(R.id.text_caller_name);
        calling_phone_number = (TextView)findViewById(R.id.text_phone_number);
        final CallLogDBHelper callLogDBHelper = new CallLogDBHelper(this, "CallLogTable.db", null, 1);
        final SQLiteDatabase callLogDB = callLogDBHelper.getReadableDatabase();

        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        final Intent intent = getIntent(); /*데이터 수신*/

        final String name = intent.getExtras().getString("name");
        final String phone_number = intent.getExtras().getString("phone_number");
        final boolean database_in_out = intent.getExtras().getBoolean("database_in_out");

        // 주소록에 해당 전화번호가 저장 돼 있지 않으면 전화번호를 이름 TextView에 표현하고 전화번호 TextView는 공백 처리한다.
        if(name.equals("null")){
            calling_name.setText(phone_number);
            calling_phone_number.setText("");
        }else{
            calling_name.setText(name);
            calling_phone_number.setText(phone_number);
        }

        receive = (ImageView)findViewById(R.id.image_calling_receive);
        cancel = (ImageView)findViewById(R.id.image_calling_cancel);
        Log.d("boolean",String.valueOf(database_in_out));
        receive.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 날짜 설정
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                // 날짜 출력될 포맷 설정
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                if(database_in_out){
                    String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phone_number + "'";
                    Cursor cursor = db.rawQuery(query , null);
                    cursor.moveToFirst();

                    callLogDBHelper.insert(cursor.getString(1),phone_number,simpleDateFormat.format(date),"receive");
                }else {
                    callLogDBHelper.insert(name,phone_number,simpleDateFormat.format(date),"receive");
                }
                finish();
                Toast.makeText(CallReceive.this, "받음", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 날짜 설정
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                // 날짜 출력될 포맷 설정
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

                if(database_in_out){
                    String query = "SELECT * FROM AddressBookList WHERE phone_number = '" + phone_number + "'";
                    Cursor cursor = db.rawQuery(query , null);
                    cursor.moveToFirst();

                    callLogDBHelper.insert(cursor.getString(1),phone_number,simpleDateFormat.format(date),"cancel");
                }else {
                    callLogDBHelper.insert(name,phone_number,simpleDateFormat.format(date),"cancel");
                }
                finish();
                Toast.makeText(CallReceive.this, "끊음", Toast.LENGTH_SHORT).show();
            }
        });
    }
}