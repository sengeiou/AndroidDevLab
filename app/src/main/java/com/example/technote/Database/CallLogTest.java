package com.example.technote.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.technote.R;

public class CallLogTest extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_call_log_test);
        final CallLogDBHelper callLogDBHelper = new CallLogDBHelper(this, "CallLogTable.db", null, 1);
        final SQLiteDatabase db = callLogDBHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM CallLogTable", null);
    }
}