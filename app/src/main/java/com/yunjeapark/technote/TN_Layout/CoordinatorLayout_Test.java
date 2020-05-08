package com.yunjeapark.technote.TN_Layout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yunjeapark.technote.R;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorLayout_Test extends AppCompatActivity {
    Toolbar coordinator_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_coordinator_test);
        coordinator_toolbar = (Toolbar)findViewById(R.id.coordinator_toolbar);
        setSupportActionBar(coordinator_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 제거
        final List<String> testStrings = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            testStrings.add(i + " 번째 item");
        }
        RecyclerView recyclerView = findViewById(R.id.main_scrollview_recyclerview);
        recyclerView.setAdapter(new RecyclerView.Adapter<TestViewHolder>() {
            @Override
            public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = View.inflate(getApplicationContext(), android.R.layout.simple_list_item_1, null);
                return new TestViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TestViewHolder holder, int position) {
                holder.textView.setText(testStrings.get(position));
            }
            public int getItemCount() {
                return testStrings.size();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TestViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coordinator_test, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_coordinator_settings:
                Toast.makeText(this, "SettingButton", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}