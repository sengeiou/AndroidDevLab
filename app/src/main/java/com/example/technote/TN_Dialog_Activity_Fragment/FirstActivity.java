package com.example.technote.TN_Dialog_Activity_Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.technote.R;

public class FirstActivity extends Activity {
	TextView textView1;
	Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_first_activity);
        
        textView1=(TextView)findViewById(R.id.textView1);
        button1=(Button)findViewById(R.id.button1);
        
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(FirstActivity.this,SecondActivity.class);
	            startActivityForResult(intent, 2);// MyActivity is started with requestCode 2
			}
		});
    }

 // Call Back method  to get the Message form other MyActivity
    @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data)
       {
                 super.onActivityResult(requestCode, resultCode, data);
                   
                  // check if the request code is same as what is passed  here it is 2
                   if(requestCode==2)
                         {
                            String message=data.getStringExtra("MESSAGE"); 
                            textView1.setText(message);
               
                         }
   
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }
    
}
