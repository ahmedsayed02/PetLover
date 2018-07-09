package com.example.ahmedsayed.navdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button register_btn, login_btn;
    EditText email_et, password_et;
    CheckBox remeber_btn ;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        email_et = findViewById(R.id.et_email);
        password_et = findViewById(R.id.et_password);
        register_btn = findViewById(R.id.register_id);
        login_btn = findViewById(R.id.login_id);
        remeber_btn = findViewById(R.id.checkBox);
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);

        remeber_btn.setChecked(false);

        sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        String name = sharedPreferences.getString("email_name",null);
        String pass = sharedPreferences.getString("password",null);

        if( name != null && pass != null){
            remeber_btn.setChecked(true);
            email_et.setText(name);
            password_et.setText(pass);
        }

    }


    @Override
    public void onClick(View view11) {
        if (view11.getId() == R.id.register_id) {
            Intent toRegPage = new Intent(this, RegistraionActivity.class);
            startActivity(toRegPage);
            finish();

        } else if (view11.getId() == R.id.login_id) {

            if(remeber_btn.isChecked()){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email_name",email_et.getText().toString());
                editor.putString("password",password_et.getText().toString());
                editor.apply();
            }else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email_name",null);
                editor.putString("password",null);
                editor.apply();
            }
            loginMethod();
        }


    }

    private void loginMethod() {
        String email_str = email_et.getText().toString();
        String password_str = password_et.getText().toString();
        if (email_str.isEmpty()) {

            email_et.setError("Please insert an email !");
        } else if (password_str.isEmpty()) {

            password_et.setError("Please insert password !");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
            email_et.setError("Invalid email !");

        } else {

            Services.login(email_str, password_str, new RequestCallback() {
                @Override
                public void Success(String response) {
                    Log.d("HASSAN", "Success: " + response);
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


                            Intent toLoginPage = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(toLoginPage);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void Error(Exception ex) {
                    Toast.makeText(LoginActivity.this, "Error connecting", Toast.LENGTH_SHORT).show();

                }
            }, LoginActivity.this);


        }

    }
}
