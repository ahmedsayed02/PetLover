package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends Fragment implements View.OnClickListener {
    EditText nameOfClient, emailOfClient, phone, hisMessage;
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Contact Us!");
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        Button submit_btn = view.findViewById(R.id.contact_mail_id);
        nameOfClient = view.findViewById(R.id.client_name_id);
        emailOfClient = view.findViewById(R.id.email_of_client_id);
        phone = view.findViewById(R.id.client_phone_id);
        hisMessage = view.findViewById(R.id.body_message_id);
        submit_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String email_str1 = emailOfClient.getText().toString();
        String Phone_str1 = phone.getText().toString();

        if (email_str1.isEmpty()) {
            emailOfClient.setError("Enter your email !");
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please enter your email !", Snackbar.LENGTH_LONG);

            snackbar.show();
        } else if (Phone_str1.isEmpty()) {
            phone.setError("Enter your phone !");
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter your phone !", Snackbar.LENGTH_LONG);

            snackbar.show();
        } else {
            sendEmail();
        }
    }

    private void sendEmail() {


        Log.i("Send email", "Sending...");
        String[] TO = {"ranosha.66.rm@gmail.com"};
        String[] CC = {"alaarashad32@gmail.com","mrmr.9026@gmail.com"};

        StringBuilder body = new StringBuilder();
        body.append("Client Name: " + nameOfClient.getText().toString());
        body.append("\n\n\nMessage: " + hisMessage.getText().toString());
        body.append("\n\n\nPhone: " + phone.getText().toString());
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);


//        emailIntent.setPackage("com.microsoft.office.outlook");

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Details");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        startActivity(Intent.createChooser(emailIntent, "Send mail in..."));
        getActivity().finish();


    }
}
