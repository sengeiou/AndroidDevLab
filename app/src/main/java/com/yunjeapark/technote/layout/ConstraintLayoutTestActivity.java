package com.yunjeapark.technote.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

public class ConstraintLayoutTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_constraint_test);

        Button goAnalogClock = (Button)findViewById(R.id.goAnalogClockExam);
        goAnalogClock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConstraintLayoutAnalogTestActivity.class));
            }
        });
    }
}