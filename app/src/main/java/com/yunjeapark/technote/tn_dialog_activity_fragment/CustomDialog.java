package com.yunjeapark.technote.tn_dialog_activity_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yunjeapark.technote.R;

public class CustomDialog extends Dialog {
    private TextView oView;
    private Button oBtn;
    CustomDialog m_oDialog;
    public CustomDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        m_oDialog = this;
    }

    public void onClickBtn(View _oView) {
        this.dismiss();
    }

    private void initView(){
        oView = (TextView) this.findViewById(R.id.textView);
        oView.setText("Custom Dialog\n테스트입니다.");

        oBtn = (Button)this.findViewById(R.id.btnOK);
        oBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtn(v);
            }
        });
    }
}