package com.example.technote.ContentProvider_Practics;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.technote.R;

public class ContentProviderTest2 extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_content_provider_test2);

        displayListView();

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // starts a new Intent to add a Country
                Intent countryEdit = new Intent(getBaseContext(), CountryEdit2.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                countryEdit.putExtras(bundle);
                startActivity(countryEdit);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //LoaderManager를 onResume()상태에서 시작 (activity가 시작되거나 재 시작될 때 동작)
        getLoaderManager().restartLoader(0, null, this);
    }

    private void displayListView() {
        // 칼럼의 이름을 저장
        String[] columns = new String[] {
                AddressListDB2.KEY_NAME,
                AddressListDB2.KEY_PHONE_NUMBER
        };

        // xml address_info의 name과 phone_number의 TextView를 정의
        int[] to = new int[] {
                R.id.name,
                R.id.phone_number,
        };

        // SimpleCursorAdapter를 연결하는 코드
        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.address_info,
                null,
                columns,
                to,
                0);

        // 리스트 뷰를 get하는 코드.
        ListView listView = (ListView) findViewById(R.id.addressList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // display the selected country
                String rowId =
                        cursor.getString(cursor.getColumnIndexOrThrow(AddressListDB2.KEY_ROWID));

                // starts a new Intent to update/delete a Country
                // pass in row Id to create the Content URI for a single row
                Intent countryEdit = new Intent(getBaseContext(), CountryEdit2.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                countryEdit.putExtras(bundle);
                startActivity(countryEdit);
            }
        });
    }
    // This is called when a new Loader needs to be created.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                AddressListDB2.KEY_ROWID,
                AddressListDB2.KEY_NAME,
                AddressListDB2.KEY_PHONE_NUMBER};
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider2.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        dataAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screen_1, menu);
        return true;
    }
}