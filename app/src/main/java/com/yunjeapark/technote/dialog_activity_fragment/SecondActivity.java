package com.yunjeapark.technote.dialog_activity_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yunjeapark.technote.R;

public class SecondActivity extends Activity {
	EditText editText1;
	Button button1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_second);
		
		    editText1=(EditText)findViewById(R.id.editText1);
	        button1=(Button)findViewById(R.id.button1);
	        
	        button1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String message=editText1.getText().toString();
			        Intent intent=new Intent();
			        intent.putExtra("MESSAGE",message);
			        
			        setResult(2,intent);
			        
			        finish();//finishing activity
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}
}
