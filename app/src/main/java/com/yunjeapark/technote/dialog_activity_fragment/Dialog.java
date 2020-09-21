package com.yunjeapark.technote.dialog_activity_fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yunjeapark.technote.R;

import java.util.Calendar;

public class Dialog extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

    }
    // 1. classic 다이얼로그 (기본)
    public void showDialog1(View _view) {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("앱을 종료하시겠습니까?")
                .setTitle("일반 Dialog")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "취소");
                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }
    // 2. List 다이얼로그
    public void showDialog2(View _view)
    {
        final CharSequence[] oItems = {"하나", "둘", "셋", "넷", "다섯"};

        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setTitle("숫자를 선택하세요")
                .setItems(oItems, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getApplicationContext(),
                                oItems[which], Toast.LENGTH_LONG).show();
                    }
                })
                .setCancelable(false)
                .show();
    }

    int nSelectItem = -1;
    // 3. RadioButton 다이얼로그
    public void showDialog3(View _view)
    {
        final CharSequence[] oItems = {"하나", "둘", "셋", "넷", "다섯"};


        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setTitle("숫자를 선택하세요")
                .setSingleChoiceItems(oItems, -1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        nSelectItem = which;
                    }
                })
                .setNeutralButton("선택", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which >= 0)
                            Toast.makeText(getApplicationContext(),
                                    oItems[nSelectItem], Toast.LENGTH_LONG).show();
                    }
                })
                .setCancelable(false)
                .show();
    }
    //4. CheckBox 다이얼로그
    public void showDialog4(View _view){
        /*색상이 분홍색으로 나오는 다이얼로그 코드
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(com.example.technote.Dialog_Activity_Fragment.Dialog.this);
         */
        AlertDialog.Builder checkboxdialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        final String data []   = {"하나","둘","셋","넷","다섯"};
        final boolean checked[]= {false,  false, false,  false,false};

        checkboxdialog.setTitle("숫자를 선택하세요")
                .setPositiveButton("선택완료",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = "선택된 값은 : ";
                                for (int i = 0; i < checked.length; i++) {
                                    if (checked[i]) {
                                        str = str + data[i] +", ";
                                    }
                                }
                                Toast.makeText(getApplicationContext(),
                                        str, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("취소", null)
                .setMultiChoiceItems
                        (data, // 체크박스 리스트 항목
                                checked, // 초기값(선택여부) 배열
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which, boolean isChecked) {
                                        checked[which] = isChecked;
                                    }
                                })
                .show(); // 리스너
    }
    // HTML 다이얼로그
    public void showDialog5(View _view)
    {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        String strHtml =
                "<b><font color='#ff0000'>HTML 컨텐츠 팝업</font></b> 입니다.<br/> Text";
        Spanned oHtml;

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            oHtml = Html.fromHtml(strHtml);
        }
        else
        {
            oHtml = Html.fromHtml(strHtml, Html.FROM_HTML_MODE_LEGACY);
        }

        oDialog.setTitle("HTML Dialog")
                .setMessage(oHtml)
                .setPositiveButton("ok", null)
                .setCancelable(false)
                .show();
    }

    public void showDialog6(View _view)
    {
        Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR);
        int nMon = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String strDate = String.valueOf(year) + "년 ";
                        strDate += String.valueOf(monthOfYear+1) + "월 ";
                        strDate += String.valueOf(dayOfMonth) + "일";
                        Toast.makeText(getApplicationContext(), strDate, Toast.LENGTH_SHORT).show();
                    }
                };

        DatePickerDialog oDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mDateSetListener, nYear, nMon, nDay);
        oDialog.show();
    }

    public void showDialog7(View _view)
    {
        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(getApplicationContext(),
                                hourOfDay + ":" + minute, Toast.LENGTH_SHORT)
                                .show();
                    }
                };

        TimePickerDialog oDialog = new TimePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mTimeSetListener, 0, 0, false);
        oDialog.show();
    }

    public void showDialog8(View _view)
    {
        ProgressDialog oDialog = new ProgressDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        oDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        oDialog.setMessage("잠시만 기다려 주세요.");

        oDialog.show();
        // dialog.dismiss();
    }

    public void showDialog9(View _view)
    {
        final ProgressDialog oDialog = new ProgressDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        oDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        oDialog.setMessage("로딩중..");
        oDialog.setMax(200);

        oDialog.show();

        // Progress 증가 시키기.
        oDialog.setProgress(111);
    }

    public void showDialog10(View _view)
    {
        CustomDialog oDialog = new CustomDialog(this);
        oDialog.setCancelable(false);
        oDialog.show();
    }
    // 다이얼로그 위 다이얼로그
    public void showDialog11(View _view)
    {
        // 캘린더 인스턴스 생성

        // 다이얼로그 셋팅
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        final Button datePicker = new Button(this);
        datePicker.setText("날짜 선택");
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        alBuilder.setTitle("Dialog On Dialog");
        alBuilder.setView(datePicker);
        alBuilder.setPositiveButton("닫기" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }
    private void showDialog(){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        oDialog.setMessage("앱을 종료하시겠습니까?")
                .setTitle("일반 Dialog")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "취소");
                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)   // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }
}
