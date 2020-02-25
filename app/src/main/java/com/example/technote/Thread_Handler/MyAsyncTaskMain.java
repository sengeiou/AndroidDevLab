package com.example.technote.Thread_Handler;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class MyAsyncTaskMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler_my_async_task_main);

        MyAsyncTask asyncTask = new MyAsyncTask((TextView)findViewById(R.id.textView));
        asyncTask.execute();
    }
}