package com.example.technote.Database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class AddressBook extends AppCompatActivity {
    Button button_add_address, button_call, button_call_log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_address_book);
        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AdressBookList.db", null, 1);

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
    private void showDialog(){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("항목을 입력하세요.")
                .setTitle("")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

}