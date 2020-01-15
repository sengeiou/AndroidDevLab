package com.example.technote.Database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class CallEvent extends AppCompatActivity {
    Button event_1, event_2, event_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_event);

        event_1 = (Button)findViewById(R.id.button_event_1);
        event_2 = (Button)findViewById(R.id.button_event_2);
        event_3 = (Button)findViewById(R.id.button_event_3);

        event_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "영희에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
        event_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "철수에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
        event_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "민수에게 전화오는 이벤트 발생", Toast.LENGTH_LONG).show();
            }
        });
    }
}