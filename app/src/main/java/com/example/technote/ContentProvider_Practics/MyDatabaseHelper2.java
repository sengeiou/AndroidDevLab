package com.example.technote.ContentProvider_Practics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.technote.ContentProviderEx.CountriesDb;

public class MyDatabaseHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressBook2.db";
    private static final int DATABASE_VERSION = 1;

    MyDatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CountriesDb.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CountriesDb.onUpgrade(db, oldVersion, newVersion);
    }
}