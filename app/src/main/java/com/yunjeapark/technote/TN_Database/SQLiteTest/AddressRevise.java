package com.yunjeapark.technote.TN_Database.SQLiteTest;

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

import com.yunjeapark.technote.R;

public class AddressRevise extends AppCompatActivity {
    Button button_cancel, button_add, button_delete;
    EditText etName, etPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_address_revise);

        etName = (EditText)findViewById(R.id.revise_etName);
        etPhoneNumber = (EditText)findViewById(R.id.revise_etPhoneNumber);

        final Intent intent = getIntent(); /*데이터 수신*/
        final int id = intent.getExtras().getInt("id") ;

        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);
        final SQLiteDatabase db = addressBookDBHelper.getReadableDatabase();
        final String sql = "SELECT * FROM AddressBookList where _id = " + Integer.toString(id);
        final Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();

        final CallLogDBHelper callLogDBHelper = new CallLogDBHelper(this, "CallLogTable.db", null, 1);
        final SQLiteDatabase callLogListDB = callLogDBHelper.getReadableDatabase();
        String query = "SELECT * FROM CallLogTable WHERE phone_number = '" + cursor.getString(2) + "'";
        final Cursor callLogListDB_cursor = callLogListDB.rawQuery(query, null);


        etName.setText(cursor.getString(1));
        etPhoneNumber.setText(cursor.getString(2));

        button_add = (Button)findViewById(R.id.revise_button_add_address_add);
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
                    callLogListDB_cursor.moveToFirst();
                    callLogDBHelper.update(name,callLogListDB_cursor.getString(2));
                    finish();
                }
            }
        });

        button_cancel = (Button)findViewById(R.id.revise_button_add_address_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_delete = (Button)findViewById(R.id.revise_button_delete);
        button_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(AddressRevise.this);
                ad.setMessage("전화번호를 삭제하시겠습니까?");   // 내용 설정

                // 취소 버튼 설정
                ad.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 확인 버튼 설정
                ad.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addressBookDBHelper.delete(cursor.getString(1));
                        callLogListDB_cursor.moveToFirst();
                        callLogDBHelper.update("null",callLogListDB_cursor.getString(2));
                        finish();
                    }
                });
                // 창 띄우기
                ad.show();
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