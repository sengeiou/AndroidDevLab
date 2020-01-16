package com.example.technote.Database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technote.R;

public class AddressRevise extends AppCompatActivity {
    Button button_cancel, button_add;
    EditText etName, etPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_add_address);

        final Intent intent = getIntent(); /*데이터 수신*/

        etName = (EditText)findViewById(R.id.etName);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        final int id = intent.getExtras().getInt("position") + 1;
        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AdressBookList.db", null, 1);
        SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        String sql = "SELECT * FROM AdressBookList where _id = " + Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();

        etName.setText(cursor.getString(1));
        etPhoneNumber.setText(cursor.getString(2));

        button_add = (Button)findViewById(R.id.button_add_address_add);
        button_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty()){
                    showEmptyNameDialog();
                }else if(etPhoneNumber.getText().toString().isEmpty()){
                    showEmptyPhoneNumberDialog();
                }else{
                    String name = etName.getText().toString();
                    String phone_number = etPhoneNumber.getText().toString();
                    addressBookDBHelper.update(name,phone_number,id);
                    finish();
                    startActivity(new Intent(getApplicationContext(),AddressBook.class));
                }
            }
        });
        button_cancel = (Button)findViewById(R.id.button_add_address_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showEmptyNameDialog(){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("이름을 입력하세요.")
                .setTitle("")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }
    private void showEmptyPhoneNumberDialog(){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("전화번호를 입력하세요.")
                .setTitle("")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }
}