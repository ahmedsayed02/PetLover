package com.example.ahmedsayed.navdrawer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyStoreFragment extends Fragment{

    EditText StoreName, contact_store, adress_store;
    TextView from,to;
    Button add_new_prod;
    TimePicker FromTime1, TOTime1;
    int fromHour, FromMin, ToHour, ToMin;
    private boolean fromTime = false, toTime = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_store_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Store Details...");
        StoreName = view.findViewById(R.id.StoreName_id);
        contact_store = view.findViewById(R.id.contact_store_id);
        adress_store = view.findViewById(R.id.adress_store_id);
        add_new_prod = view.findViewById(R.id.add_new_prod_id);
        FromTime1 = view.findViewById(R.id.FromTime1_id);
        TOTime1 = view.findViewById(R.id.TOTime1_id);
        from = view.findViewById(R.id.from1_text);
        to = view.findViewById(R.id.to1_text);



        FromTime1.setIs24HourView(true);
        TOTime1.setIs24HourView(true);


        if(getArguments() == null){

            add_new_prod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (StoreName.getText().toString().isEmpty()) {

                        StoreName.setError("enter your store!");

                    } else if (contact_store.getText().toString().isEmpty()) {

                        contact_store.setError("enter your contact of store!");
                    }else if(!fromTime){
                        from.setError("please enter openAt time");
                    }else if(!toTime){
                        to.setError("please enter closeAt time");
                    }else if (adress_store.getText().toString().isEmpty()) {
                        adress_store.setError("enter address of store");
                    }else {
                        final ArrayList<StorePojo> p12 = new ArrayList<>();

                        Services.addStore(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                StoreName.getText().toString(), adress_store.getText().toString(),
                                contact_store.getText().toString(), fromHour + ":" + FromMin,
                                ToHour + ":" + ToMin, new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                            if(success == 1){

                                                Services.getAllStoresForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                                                        constant.sharedPreferences.getString(constant.Token, null),
                                                        new RequestCallback() {
                                                            @Override
                                                            public void Success(String response) {

                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    int success = jsonObject.getInt("success");
                                                                    String msg = jsonObject.getString("message");

                                                                    if(success == 1){
                                                                        JSONArray jsonArray = jsonObject.getJSONArray("stores");
                                                                        for (int i = 0; i < jsonArray.length(); ++i){
                                                                            JSONObject obj = jsonArray.getJSONObject(i);

                                                                            constant.editor.putInt("storeID",obj.getInt("id"));
                                                                            constant.editor.apply();

                                                                            p12.add(new StorePojo(obj.getInt("id"),
                                                                                    obj.getInt("storeOwnerId"),
                                                                                    obj.getString("name"),
                                                                                    obj.getString("address"),
                                                                                    obj.getInt("phone"),
                                                                                    obj.getString("openAt"),
                                                                                    obj.getString("closeAt")));

                                                                            //GO TO Text Viewer for Info. of Store and 3 btns edit w View products , Delete

                                                                        }

                                                                        Bundle bundle233 = new Bundle();
                                                                        bundle233.putParcelable("store", p12.get(0));
                                                                        ViewOfStoreInfo v1 = new ViewOfStoreInfo(); // fragment
                                                                        v1.setArguments(bundle233);
                                                                        getActivity().getSupportFragmentManager()
                                                                                .beginTransaction()
                                                                                .replace(R.id.fragment_id, v1)
                                                                                .commit();
                                                                    }else if(msg.equals("Invalid token.")){
                                                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                                                        getActivity().finish();

                                                                    }


                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void Error(Exception ex) {

                                                            }
                                                        },getActivity());


                                            }else if(msg.equals("Invalid token.")){
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                                getActivity().finish();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void Error(Exception ex) {

                                    }
                                },getActivity());
                    }

                }
            });
        }else {
            add_new_prod.setText("Edit store info");
            final StorePojo storePojo = getArguments().getParcelable("storeData");

            StoreName.setText(storePojo.getNameOfStore());
            adress_store.setText(storePojo.getAddressOfStore());
            contact_store.setText(String.valueOf(storePojo.getPhoneOfStore()));
            String[] fromArr = storePojo.getOpenAt1().split(":");
            String[] toArr = storePojo.getCloseAt1().split(":");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FromTime1.setHour(Integer.valueOf(fromArr[0]));
                FromTime1.setMinute(Integer.valueOf(fromArr[1]));

                TOTime1.setHour(Integer.valueOf(toArr[0]));
                TOTime1.setMinute(Integer.valueOf(toArr[1]));
            }else {
                FromTime1.setCurrentHour(Integer.valueOf(fromArr[0]));
                FromTime1.setCurrentMinute(Integer.valueOf(fromArr[1]));

                TOTime1.setCurrentHour(Integer.valueOf(toArr[0]));
                TOTime1.setCurrentMinute(Integer.valueOf(toArr[1]));
            }

            add_new_prod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (StoreName.getText().toString().isEmpty()) {

                        StoreName.setError("enter your store!");

                    } else if (contact_store.getText().toString().isEmpty()) {

                        contact_store.setError("enter your contact of store!");
                    }else if(!fromTime){
                        from.setError("please enter openAt time");
                    }else if(!toTime){
                        to.setError("please enter closeAt time");
                    }else if (adress_store.getText().toString().isEmpty()) {
                        adress_store.setError("enter address of store");
                    }else {
                        final ArrayList<StorePojo> p12 = new ArrayList<>();

                        Services.updateStore(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                StoreName.getText().toString(), adress_store.getText().toString(),
                                contact_store.getText().toString(), fromHour + ":" + FromMin,
                                ToHour + ":" + ToMin, storePojo.getIdOfStore(),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                            if(success == 1){

                                                Services.getAllStoresForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                                                        constant.sharedPreferences.getString(constant.Token, null),
                                                        new RequestCallback() {
                                                            @Override
                                                            public void Success(String response) {

                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    int success = jsonObject.getInt("success");
                                                                    String msg = jsonObject.getString("message");

                                                                    if(success == 1){
                                                                        JSONArray jsonArray = jsonObject.getJSONArray("stores");
                                                                        for (int i = 0; i < jsonArray.length(); ++i){
                                                                            JSONObject obj = jsonArray.getJSONObject(i);

                                                                            p12.add(new StorePojo(obj.getInt("id"),
                                                                                    obj.getInt("storeOwnerId"),
                                                                                    obj.getString("name"),
                                                                                    obj.getString("address"),
                                                                                    obj.getInt("phone"),
                                                                                    obj.getString("openAt"),
                                                                                    obj.getString("closeAt")));

                                                                            //GO TO Text Viewer for Info. of Store and 3 btns edit w View products , Delete

                                                                        }

                                                                        Bundle bundle233 = new Bundle();
                                                                        bundle233.putParcelable("store", p12.get(0));
                                                                        ViewOfStoreInfo v1 = new ViewOfStoreInfo(); // fragment
                                                                        v1.setArguments(bundle233);
                                                                        getActivity().getSupportFragmentManager()
                                                                                .beginTransaction()
                                                                                .replace(R.id.fragment_id, v1)
                                                                                .commit();
                                                                    }else if(msg.equals("Invalid token.")){
                                                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                                                        getActivity().finish();

                                                                    }


                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void Error(Exception ex) {

                                                            }
                                                        },getActivity());


                                            }else if(msg.equals("Invalid token.")){
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                                getActivity().finish();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void Error(Exception ex) {

                                    }
                                },getActivity());
                    }

                }
            });


        }


        FromTime1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                fromHour = hourOfDay;
                FromMin = minute;
                fromTime = true;

            }
        });

        TOTime1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                ToHour = hourOfDay;
                ToMin = minute;
                toTime = true;
            }
        });


    }

}
