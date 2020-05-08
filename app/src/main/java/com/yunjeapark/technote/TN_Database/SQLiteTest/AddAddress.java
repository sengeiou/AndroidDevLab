package com.yunjeapark.technote.TN_Database.SQLiteTest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunjeapark.technote.R;

public class AddAddress extends AppCompatActivity {
    Button button_cancel, button_add;
    EditText etName, etPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_add_address);
        etName = (EditText)findViewById(R.id.etName);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        final AddressBookDBHelper addressBookDBHelper = new AddressBookDBHelper(this, "AddressBookList.db", null, 1);

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
                    addressBookDBHelper.insert(name, phone_number);
                    finish();
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

        etPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if(etName.getText().toString().isEmpty()){
                        showEmptyNameDialog();
                    }else if(etPhoneNumber.getText().toString().isEmpty()){
                        showEmptyPhoneNumberDialog();
                    }else{
                        String name = etName.getText().toString();
                        String phone_number = etPhoneNumber.getText().toString();
                        addressBookDBHelper.insert(name, phone_number);
                        finish();
                    }
                    return true;
                }
                return false;
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