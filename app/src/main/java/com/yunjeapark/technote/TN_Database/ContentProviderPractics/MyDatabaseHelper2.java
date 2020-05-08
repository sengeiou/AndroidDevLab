package com.yunjeapark.technote.TN_Database.ContentProviderPractics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressBook2.db";
    private static final int DATABASE_VERSION = 1;

    MyDatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AddressListTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AddressListTable.onUpgrade(db, oldVersion, newVersion);
    }
}