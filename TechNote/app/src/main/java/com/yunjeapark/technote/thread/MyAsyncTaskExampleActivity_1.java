package com.yunjeapark.technote.thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

public class MyAsyncTaskExampleActivity_1 extends AppCompatActivity {

    private final String ASYNC_TASK_TAG = "ASYNC_TASK";

    private Button executeAsyncTaskButton;
    private Button cancelAsyncTaskButton;
    private ProgressBar asyncTaskProgressBar;
    private TextView asyncTaskLogTextView;

    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_my_asynctask_example_1);

        setTitle("AsyncTask Example");

        executeAsyncTaskButton = (Button)findViewById(R.id.executeAsyncTaskButton);
        executeAsyncTaskButton.setEnabled(true);

        cancelAsyncTaskButton = (Button)findViewById(R.id.cancelAsyncTaskButton);
        cancelAsyncTaskButton.setEnabled(false);

        asyncTaskProgressBar = (ProgressBar)findViewById(R.id.asyncTaskProgressBar);
        asyncTaskLogTextView = (TextView)findViewById(R.id.asyncTaskLogTextView);

        executeAsyncTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Need to create a new MyAsyncTask instance for each call,
                // otherwise there will through an exception.
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(Integer.parseInt("10"));

                executeAsyncTaskButton.setEnabled(false);
                cancelAsyncTaskButton.setEnabled(true);
            }
        });

        cancelAsyncTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel a running task, then MyAsyncTask's onCancelled(String result) method will be invoked.
                myAsyncTask.cancel(true);
            }
        });
    }

    // MyAsyncTask is used to demonstrate async task process.
    private class MyAsyncTask extends AsyncTask<Integer, Integer, String>{

        // Main Thread에서 실행되며, doInBackground 메소드 전에 호출 (초기화 작업을 하는데 사용)
        @Override
        protected void onPreExecute() {
            asyncTaskLogTextView.setText("Loading");
            Log.i(ASYNC_TASK_TAG, "onPreExecute() is executed.");
        }

        // doInBackground(String... strings) is used to execute background task, can not modify UI component in this method.
        // It return a String object which can be used in onPostExecute() method.
        // 백그라운드 상에 처리, 이곳에서 UI 처리하면 오류발생
        // execute 메소드를 호출 시 전달한 인자를 파라메터로 받게 된다.
        // 값을 리턴하면 onPostExecute 메소드에서 받는다.
        @Override
        protected String doInBackground(Integer... inputParams) {

            StringBuffer retBuf = new StringBuffer();
            boolean loadComplete = false;

            try
            {
                Log.i(ASYNC_TASK_TAG, "doInBackground(" + inputParams[0] + ") is invoked.");

                int paramsLength = inputParams.length;
                if(paramsLength > 0) {
                    Integer totalNumber = inputParams[0];
                    int totalNumberInt = totalNumber.intValue();

                    for(int i=0;i < totalNumberInt; i++)
                    {
                        // First calculate progress value.
                        int progressValue = (i * 100 ) / totalNumberInt;

                        //Call publishProgress method to invoke onProgressUpdate() method.
                        publishProgress(progressValue);

                        // Sleep 0.2 seconds to demo progress clearly.
                        Thread.sleep(200);
                    }
                    loadComplete = true;
                }
            }catch(Exception ex)
            {
                Log.i(ASYNC_TASK_TAG, ex.getMessage());
            }finally {
                if(loadComplete) {
                    // Load complete display message.
                    retBuf.append("Load complete.");
                }else
                {
                    // Load cancel display message.
                    retBuf.append("Load canceled.");
                }
                return retBuf.toString();
            }
        }

        // onProgressUpdate is used to update async task progress info.
        // doInBackground 메소드에서 publishProgress 메소드를 호출함으로써 Main Thread에서 실행.
        // publishProgress 메소드를 받아서 UI 처리.
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(ASYNC_TASK_TAG, "onProgressUpdate(" + values + ") is called");
            asyncTaskProgressBar.setProgress(values[0]);
            asyncTaskLogTextView.setText("loading..." + values[0] + "%");
        }

        // onPostExecute() is used to update UI component and show the result after async task execute.
        // Main Thread에서 실행되며, doInBackground 메소드 종류 후 호출.
        // doInBackground 메소드에서 리턴한 값을 받는다.
        @Override
        protected void onPostExecute(String result) {
            Log.i(ASYNC_TASK_TAG, "onPostExecute(" + result + ") is invoked.");
            // Show the result in log TextView object.
            asyncTaskLogTextView.setText(result);

            asyncTaskProgressBar.setProgress(100);

            executeAsyncTaskButton.setEnabled(true);
            cancelAsyncTaskButton.setEnabled(false);
        }

        // onCancelled() is called when the async task is cancelled.
        // doInBackground 메소드에서 수행중인 작업이 취소되면 호출.
        @Override
        protected void onCancelled(String result) {
            Log.i(ASYNC_TASK_TAG, "onCancelled(" + result + ") is invoked.");
            // Show the result in log TextView object.
            asyncTaskLogTextView.setText(result);
            asyncTaskProgressBar.setProgress(0);

            executeAsyncTaskButton.setEnabled(true);
            cancelAsyncTaskButton.setEnabled(false);
        }
    }
}