package com.yunjeapark.technote.database.content_provider_practice;

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

import com.yunjeapark.technote.R;

public class ContentProviderTest2Activity extends Activity implements
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
                Intent countryEdit = new Intent(getBaseContext(), AddressEdit2Activity.class);
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
                AddressListTable.KEY_NAME,
                AddressListTable.KEY_PHONE_NUMBER
        };

        // xml address_info의 name과 phone_number의 TextView를 정의
        int[] to = new int[] {
                R.id.name,
                R.id.phone_number,
        };

        // SimpleCursorAdapter를 연결하는 코드
        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_address_info,
                null,
                columns,
                to,
                0);

        // 리스트 뷰를 get.
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

                // rowId를 cursor를 통해 get
                String rowId =
                        cursor.getString(cursor.getColumnIndexOrThrow(AddressListTable.KEY_ROWID));

                // 리스트뷰의 item을 클릭하면 Edit 화면을 띄우고, 기존에 저장해 있던 데이터를 bundle을 통해 put하여 적재 시킨다.
                Intent countryEdit = new Intent(getBaseContext(), AddressEdit2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                countryEdit.putExtras(bundle);
                startActivity(countryEdit);
            }
        });
    }
    // CursorLoader를 이용하여 데이터를 로드.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                AddressListTable.KEY_ROWID,
                AddressListTable.KEY_NAME,
                AddressListTable.KEY_PHONE_NUMBER};
        //String selection = AddressListTable.KEY_NAME + " = 'A'";
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