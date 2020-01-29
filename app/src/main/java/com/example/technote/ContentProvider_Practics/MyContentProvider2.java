package com.example.technote.ContentProvider_Practics;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyContentProvider2 extends ContentProvider {

    private MyDatabaseHelper2 dbHelper;

    private static final int ALL_COUNTRIES = 1;
    private static final int SINGLE_COUNTRY = 2;

    // authority는 나의 프로바이더에 상징적인 이름이다.
    // 다른 프로바이더와 충돌을 피하기 위해서 반드시 정의
    // Internet domain ownership (in reverse) as the basis of your provider authority. (인터넷 도메인 소유권은 나의 provider authority의 기준으로 있다.)
    private static final String AUTHORITY = "com.example.technote.ContentProvider_Practics";

    // create content URIs from the authority by appending path to database table
    // authority에서 데이터베이스 테이블에 경로를 추가하여 컨텐츠 URI 생성
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/addressList");

    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "addressList", ALL_COUNTRIES);
        uriMatcher.addURI(AUTHORITY, "addressList/#", SINGLE_COUNTRY);
    }

    // system calls onCreate() when it starts up the provider.
    @Override
    public boolean onCreate() {
        // get access to the database helper
        dbHelper = new MyDatabaseHelper2(getContext());
        return false;
    }

    //Return the MIME type corresponding to a content URI // content URI에 해당하는 MIME type을 return한다.
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                return "vnd.android.cursor.dir/vnd.com.example.technote.ContentProvider_Practics.addressList";
            case SINGLE_COUNTRY:
                return "vnd.android.cursor.item/vnd.com.example.technote.ContentProvider_Practics.addressList";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // The insert() method adds a new row to the appropriate table, using the values
    // in the ContentValues argument. If a column name is not in the ContentValues argument,
    // you may want to provide a default value for it either in your provider code or in
    // your database schema.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(AddressListTable.SQLITE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }
    // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data storage,
    // you can simply return the Cursor returned by one of the query() methods of the
    // SQLiteDatabase class. If the query does not match any rows, you should return a
    // Cursor instance whose getCount() method returns 0. You should return null only
    // if an internal error occurred during the query process.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AddressListTable.SQLITE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                //do nothing
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(AddressListTable.KEY_ROWID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    // The delete() method deletes rows based on the seletion or if an id is
    // provided then it deleted a single row. The methods returns the numbers
    // of records delete from the database. If you choose not to delete the data
    // physically then just update a flag here.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                //do nothing
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = AddressListTable.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(AddressListTable.SQLITE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    // The update method() is same as delete() which updates multiple rows
    // based on the selection or a single row if the row id is provided. The
    // update method returns the number of updated rows.
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                //do nothing
                break;
            case SINGLE_COUNTRY:
                String id = uri.getPathSegments().get(1);
                selection = AddressListTable.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(AddressListTable.SQLITE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
