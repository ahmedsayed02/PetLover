package com.example.ahmedsayed.navdrawer;

/**
 * Created by Owner on 7/31/2017.
 */

public interface RequestCallback {

    void Success(String response);
    void Error(Exception ex);
}
