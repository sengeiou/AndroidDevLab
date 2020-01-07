package com.example.technote.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class ConstraintLayout_Test extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_constraint_test);

        Button goAnalogClock = (Button)findViewById(R.id.goAnalogClockExam);
        goAnalogClock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ConstraintLayout_Analog_Test.class));
            }
        });
    }
}