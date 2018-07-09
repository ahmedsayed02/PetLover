package com.example.ahmedsayed.navdrawer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RegistraionActivity extends Activity implements View.OnClickListener {
    EditText name_et, yourEmail_et, password_et, mobile_et, yourAddress_et;
    Button submit_btn;
    ImageView profile_img;
    RadioGroup RG;
    RadioButton user,doctor,store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_page);
        name_et = findViewById(R.id.named_id);
        yourEmail_et = findViewById(R.id.email_id);
        password_et = findViewById(R.id.password_id);
        mobile_et = findViewById(R.id.mobile_id);
        yourAddress_et = findViewById(R.id.address_id);
        submit_btn = findViewById(R.id.submit_id);
        submit_btn.setOnClickListener(this);
        profile_img = findViewById(R.id.profile_img_id);
        profile_img.setOnClickListener(this);
        RG = findViewById(R.id.RG_type);
        user = findViewById(R.id.user_id);
        doctor = findViewById(R.id.doctor_id);
        store = findViewById(R.id.store_id);


    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, LoginActivity.class);
        startActivity(backIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

//        to get put these values in the DataBase
        String name_str = name_et.getText().toString();
        String email_str = yourEmail_et.getText().toString();
        String password_str = password_et.getText().toString();
        String mobile_num = String.valueOf(mobile_et.getText());
        String address_str = yourAddress_et.getText().toString();
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        if (name_str.isEmpty()) {

            name_et.setError("your name pelase !");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {

            yourEmail_et.setError("write vaild email !");
        } else if (password_str.isEmpty()) {
            password_et.setError("insert password");
        } else if (mobile_num.isEmpty()) {
            mobile_et.setError("insert your phone");
        } else if(RG.getCheckedRadioButtonId() == -1){
            user.setError("check one");
            doctor.setError("check one");
            store.setError("check one");
        } else {
            int id = RG.getCheckedRadioButtonId();
            int type = 0;
            switch (id){
                case R.id.user_id:
                    type = 1;
                    break;
                case R.id.doctor_id:
                    type = 2;
                    break;
                case R.id.store_id:
                    type = 3;
                    break;

            }

            Services.registration(name_str, email_str, password_str, mobile_num, type, address_str,
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");
                                if(success == 1){
                                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                                    for(int i = 0; i < jsonArray.length(); ++i){
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        int id = obj.getInt("id");
                                        String name = obj.getString("name");
                                        String email = obj.getString("email");
                                        String password = obj.getString("password");
                                        int phone = obj.getInt("phone");
                                        int type = obj.getInt("type");
                                        String image = obj.getString("image");
                                        String address = obj.getString("address");
                                        String token = obj.getString("token");

                                        constant.editor = getSharedPreferences(constant.PREFERENCES, MODE_PRIVATE).edit();
                                        constant.editor.putInt(constant.ID, id);
                                        constant.editor.putString(constant.Name, name);
                                        constant.editor.putString(constant.Password, password);
                                        constant.editor.putString(constant.Email, email);
                                        constant.editor.putInt(constant.Number, phone);
                                        constant.editor.putString(constant.Token, token);
                                        constant.editor.putString(constant.Image, image);
                                        constant.editor.putString(constant.Address, address);
                                        constant.editor.putInt(constant.Type, type);
                                        constant.editor.putBoolean(constant.isLoggedIn,true);
                                        constant.editor.apply();
                                    }


                                    Intent toMainActivity = new Intent(RegistraionActivity.this, MainActivity.class);
                                    startActivity(toMainActivity);
                                    finish();
                                }else{
                                    Toast.makeText(RegistraionActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void Error(Exception ex) {

                        }
                    },RegistraionActivity.this);

        }

    }


}
