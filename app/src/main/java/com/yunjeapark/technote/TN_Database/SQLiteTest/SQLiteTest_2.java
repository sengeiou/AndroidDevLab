package com.yunjeapark.technote.TN_Database.SQLiteTest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

public class SQLiteTest_2 extends AppCompatActivity {

    ContactDBHelper dbHelper = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_sqlite_test_2);
        init_tables() ;
        load_values() ;
        Button buttonSave = (Button) findViewById(R.id.buttonSave) ;
        buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_values() ;
            }
        });
        Button buttonClear = (Button) findViewById(R.id.buttonClear) ;
        buttonClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_values() ;
            }
        });
    }
    private void init_tables() {
        dbHelper = new ContactDBHelper(this) ;
    }

    private void load_values() {

        SQLiteDatabase db = dbHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;

        if (cursor.moveToFirst()) {
            // no (INTEGER) 값 가져오기.
            int no = cursor.getInt(0) ;
            EditText editTextNo = (EditText) findViewById(R.id.editTextNo) ;
            editTextNo.setText(Integer.toString(no)) ;

            // name (TEXT) 값 가져오기
            String name = cursor.getString(1) ;
            EditText editTextName = (EditText) findViewById(R.id.editTextName) ;
            editTextName.setText(name) ;

            // phone (TEXT) 값 가져오기
            String phone = cursor.getString(2) ;
            EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone) ;
            editTextPhone.setText(phone) ;

            // over20 (INTEGER) 값 가져오기.
            int over20 = cursor.getInt(3) ;
            CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20) ;
            if (over20 == 0) {
                checkBoxOver20.setChecked(false) ;
            } else {
                checkBoxOver20.setChecked(true) ;
            }
        }
    }
    private void save_values() {
        SQLiteDatabase db = dbHelper.getWritableDatabase() ;

        db.execSQL(ContactDBCtrct.SQL_DELETE) ;

        EditText editTextNo = (EditText) findViewById(R.id.editTextNo) ;
        int no = Integer.parseInt(editTextNo.getText().toString()) ;

        EditText editTextName = (EditText) findViewById(R.id.editTextName) ;
        String name = editTextName.getText().toString() ;

        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone) ;
        String phone = editTextPhone.getText().toString() ;

        CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20) ;
        boolean isOver20 = checkBoxOver20.isChecked() ;

        String sqlInsert = ContactDBCtrct.SQL_INSERT +
                " (" +
                Integer.toString(no) + ", " +
                "'" + name + "', " +
                "'" + phone + "', " +
                ((isOver20 == true) ? "1" : "0") +
                ")" ;

        db.execSQL(sqlInsert) ;
    }
    private void delete_values() {
        SQLiteDatabase db = dbHelper.getWritableDatabase() ;

        db.execSQL(ContactDBCtrct.SQL_DELETE) ;

        EditText editTextNo = (EditText) findViewById(R.id.editTextNo) ;
        editTextNo.setText("") ;

        EditText editTextName = (EditText) findViewById(R.id.editTextName) ;
        editTextName.setText("") ;

        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone) ;
        editTextPhone.setText("") ;

        CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20) ;
        checkBoxOver20.setChecked(false) ;
    }
}