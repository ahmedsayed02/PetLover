package com.example.ahmedsayed.navdrawer;

import android.content.SharedPreferences;

/**
 * Created by Owner on 4/10/2018.
 */

public class constant {

    public static final String PREFERENCES = "UserData";
    public static final String ID = "ID";
    public static final String Name = "Name";
    public static final String Token = "token";
    public static final String serverToken = "server_token";
    public static final String Number = "MobileNumber";
    public static final String isLoggedIn = "LoginIn";
    public static final String Image = "image";
    public static final String Email = "email";
    public static final String Password = "password";
    public static final String Address = "address";
    public static final String Type = "type";

    public static SharedPreferences sharedPreferences = null;
    public static SharedPreferences.Editor editor = null;
}
