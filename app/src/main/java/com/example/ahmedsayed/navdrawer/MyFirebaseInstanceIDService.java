package com.example.ahmedsayed.navdrawer;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Owner on 4/21/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("HASSAN", "Refreshed token: " + refreshedToken);
        constant.editor = getSharedPreferences(constant.PREFERENCES, MODE_PRIVATE).edit();
        constant.editor.putString(constant.serverToken,refreshedToken);
        constant.editor.apply();
    }
}
