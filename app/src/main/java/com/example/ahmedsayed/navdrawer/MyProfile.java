package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MyProfile extends android.support.v4.app.Fragment implements View.OnClickListener {
    Button updated_btn;
    TextView email, name, phone, address;
    ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Profile");
        updated_btn = view.findViewById(R.id.update_id);
        updated_btn.setOnClickListener(this);

        profileImage = view.findViewById(R.id.profile_id);

        email = view.findViewById(R.id.email_id1);
        name = view.findViewById(R.id.named_id1);
        phone = view.findViewById(R.id.mobile_id1);
        address = view.findViewById(R.id.address_id1);

        //constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES,Context.MODE_PRIVATE);
        email.setText(constant.sharedPreferences.getString(constant.Email,null));
        name.setText(constant.sharedPreferences.getString(constant.Name,null));
        phone.setText(String.valueOf(constant.sharedPreferences.getInt(constant.Number,-1)));
        address.setText(constant.sharedPreferences.getString(constant.Address,null));
        Picasso.get()
                .load(Services.url + constant.sharedPreferences.getString(constant.Image, "")).centerCrop().resize(100, 100)
                .noFade().onlyScaleDown()
                .into(profileImage);
    }


    @Override
    public void onClick(View v) {
        //to edit profile info....
        if (v.getId() == R.id.update_id) {

            UpdateYourInfo f3 = new UpdateYourInfo();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                     fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_id, f3)
                    .addToBackStack(null)
                    .commit();
        }
    }

}

