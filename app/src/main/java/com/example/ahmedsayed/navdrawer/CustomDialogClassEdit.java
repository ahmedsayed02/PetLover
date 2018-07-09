package com.example.ahmedsayed.navdrawer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class CustomDialogClassEdit extends Dialog {

    public Button yes, no;
    public TextView dialogDate, dialogTime;

    public CustomDialogClassEdit(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_edit_2btn);

        dialogDate = findViewById(R.id.txt_date_appointment);
        dialogTime = findViewById(R.id.txt_time_appointment);
        yes = findViewById(R.id.btn_yes_edit);
        no = findViewById(R.id.btn_no_edit);
    }
}
