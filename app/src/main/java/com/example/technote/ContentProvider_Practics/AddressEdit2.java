package com.example.technote.ContentProvider_Practics;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technote.R;

public class AddressEdit2 extends Activity implements OnClickListener{

    private Spinner continentList;
    private Button save, delete;
    private String mode;
    private EditText name, phone_number;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_content_provider_detail_page2);

        // get the values passed to the activity from the calling activity
        // insert 할 것인지 delete 할 것인지, update를 할 것인지 mode로 받음.
        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        // 버튼 리스너
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        phone_number = (EditText) findViewById(R.id.phonenumber);
        name = (EditText) findViewById(R.id.name);

        // 전화번호 추가 버튼을 눌렀을 때 delete 버튼을 비활성화
        if(mode.trim().equalsIgnoreCase("add")){
            delete.setEnabled(false);
        }
        // get the rowId for the specific country
        else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadCountryInfo();
        }
        phone_number.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String myPhoneNumber = phone_number.getText().toString();
                    String myName = name.getText().toString();

                    // check for blanks
                    if(myPhoneNumber.trim().equalsIgnoreCase("")){
                        Toast.makeText(getBaseContext(), "전화번호를 입력하세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    // check for blanks
                    if(myName.trim().equalsIgnoreCase("")){
                        Toast.makeText(getBaseContext(), "이름을 입력하세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    ContentValues values = new ContentValues();
                    values.put(AddressListTable.KEY_NAME, myName);
                    values.put(AddressListTable.KEY_PHONE_NUMBER, myPhoneNumber);

                    // insert a record
                    if(mode.trim().equalsIgnoreCase("add")){
                        //ContentResolver를 통해 데이터를 가져온다.
                        getContentResolver().insert(MyContentProvider2.CONTENT_URI, values);
                    }
                    // update a record
                    else {
                        Uri uri = Uri.parse(MyContentProvider2.CONTENT_URI + "/" + id);
                        getContentResolver().update(uri, values, null, null);
                    }
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClick(View v) {

        // Edit Text에 있는 String data를 get
        String myPhoneNumber = phone_number.getText().toString();
        String myName = name.getText().toString();

        // 전화번호가 입력 돼 있지 않으면.
        if(myPhoneNumber.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "전화번호를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        // 이름이 입력 돼 있지 않으면
        if(myName.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "이름을 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }


        switch (v.getId()) {

            //save 버튼을 눌렀을 때
            case R.id.save:
                ContentValues values = new ContentValues();
                //value에 각각의 데이터를 put
                values.put(AddressListTable.KEY_NAME, myName);
                values.put(AddressListTable.KEY_PHONE_NUMBER, myPhoneNumber);

                // insert a record
                if(mode.trim().equalsIgnoreCase("add")){
                    //ContentResolver를 통해 데이터를 가져온다.
                    getContentResolver().insert(MyContentProvider2.CONTENT_URI, values);
                }
                // update a record
                else {
                    Uri uri = Uri.parse(MyContentProvider2.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

            // delete 버튼을 눌렀을 때
            case R.id.delete:
                // delete a record
                Uri uri = Uri.parse(MyContentProvider2.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                finish();
                break;

            // More buttons go here (if any) ...

        }
    }

    // based on the rowId get all information from the Content Provider
    // about that country
    private void loadCountryInfo(){

        String[] projection = {
                AddressListTable.KEY_ROWID,
                AddressListTable.KEY_NAME,
                AddressListTable.KEY_PHONE_NUMBER};
        Uri uri = Uri.parse(MyContentProvider2.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(AddressListTable.KEY_NAME));
            String myPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(AddressListTable.KEY_PHONE_NUMBER));
            name.setText(myName);
            phone_number.setText(myPhoneNumber);
        }
    }

    // this sets the spinner selection based on the value
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

}