package com.example.ahmedsayed.navdrawer;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddMyClinic extends Fragment {
    TimePicker timePicker1_from, timePicker2_to;
    EditText bussinessName, contact_clinic, address_clinic;
    TextView fromTxt,toTxt;
    Button new_clinic_btn;
    int fromHour, toHour;
    int fromMin, toMin;
    boolean fromTime = false, toTime = false;
    Spinner dropdown;
    ClinicsPojo p2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_clinic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Clinic Details...");

        timePicker1_from = view.findViewById(R.id.FromTime_id);
        timePicker2_to = view.findViewById(R.id.TOTime_id);
        bussinessName = view.findViewById(R.id.bussinessName_id);
        contact_clinic = view.findViewById(R.id.contact_doctor_id);
        address_clinic = view.findViewById(R.id.adress_clinic_id);
        fromTxt = view.findViewById(R.id.from_text);
        toTxt = view.findViewById(R.id.to_text);
        new_clinic_btn = view.findViewById(R.id.add_new_clinic_id);
        dropdown = view.findViewById(R.id.spinner11);

        //////////////////////////////

        //create a list of items for the spinner.
        String[] cities = getResources().getStringArray(R.array.cities);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        ///////////////////////////////////////

        timePicker1_from.setIs24HourView(true);
        timePicker2_to.setIs24HourView(true);

        timePicker1_from.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                fromHour = hourOfDay;
                fromMin = minute;
                fromTime = true;
            }
        });

        timePicker2_to.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                toHour = hourOfDay;
                toMin = minute;
                toTime = true;
            }
        });

        if(getArguments() == null){

            new_clinic_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bussinessName.getText().toString().isEmpty()){
                        bussinessName.setError("please enter name");
                    }else if(contact_clinic.getText().toString().isEmpty()){
                        contact_clinic.setText("please enter phone of clincs");
                    }else if(!fromTime){
                        fromTxt.setError("please enter from");
                    }else if(!toTime){
                        toTxt.setError("please enter from");
                    }else if(address_clinic.getText().toString().isEmpty()){
                        address_clinic.setText("please enter address of clincs");
                    }else if(dropdown.getSelectedItemPosition() == 0){
                        ((TextView)dropdown.getSelectedView()).setError("Error message");
                    }else {

                        Services.addClinic(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                bussinessName.getText().toString(),
                                address_clinic.getText().toString(),
                                contact_clinic.getText().toString(),
                                String.valueOf(fromHour + ":" + fromMin),
                                String.valueOf(toHour + ":" + toMin),
                                dropdown.getSelectedItemPosition(),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                            if(success == 1){

                                                Services.getAllClinicsForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                                                        constant.sharedPreferences.getString(constant.Token, null),
                                                        new RequestCallback() {
                                                            @Override
                                                            public void Success(String response) {

                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    int success = jsonObject.getInt("success");
                                                                    String msg = jsonObject.getString("message");

                                                                    if(success == 1){
                                                                        JSONArray jsonArray = jsonObject.getJSONArray("clinics");
                                                                        ClinicsPojo p2 = null;
                                                                        for (int i = 0; i < jsonArray.length(); ++i){
                                                                            JSONObject obj = jsonArray.getJSONObject(i);
                                                                            p2 = new ClinicsPojo(obj.getInt("doctorID"),
                                                                                    obj.getInt("id"),
                                                                                    obj.getString("name"),
                                                                                    obj.getString("address"),
                                                                                    obj.getInt("phone"), obj.getInt("city"),
                                                                                    obj.getString("openAt"),
                                                                                    obj.getString("closeAt"),
                                                                                    obj.getString("doctorName"));
                                                                        }

                                                                        Bundle bundle2 = new Bundle();
                                                                        bundle2.putParcelable("clinic", p2);
                                                                        UpdateClinicInfo ClinicInfo = new UpdateClinicInfo();
                                                                        ClinicInfo.setArguments(bundle2);

                                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, ClinicInfo)
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
            new_clinic_btn.setText("edit Info");

            p2 = getArguments().getParcelable("clinicData");
            bussinessName.setText(p2.getNameOfClinic());
            contact_clinic.setText(String.valueOf(p2.getPhone()));
            address_clinic.setText(p2.getAddressOfClinic());
            dropdown.setSelection(p2.getCity());
            String[] from = p2.getOpenAt().split(":");
            String[] to = p2.getCloseAt().split(":");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker1_from.setHour(Integer.valueOf(from[0]));
                timePicker1_from.setMinute(Integer.valueOf(from[1]));

                timePicker2_to.setHour(Integer.valueOf(to[0]));
                timePicker2_to.setMinute(Integer.valueOf(to[1]));
            }else {
                timePicker1_from.setCurrentHour(Integer.valueOf(from[0]));
                timePicker1_from.setCurrentMinute(Integer.valueOf(from[1]));

                timePicker2_to.setCurrentHour(Integer.valueOf(to[0]));
                timePicker2_to.setCurrentMinute(Integer.valueOf(to[1]));
            }


            new_clinic_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bussinessName.getText().toString().isEmpty()){
                        bussinessName.setError("please enter name");
                    }else if(contact_clinic.getText().toString().isEmpty()){
                        contact_clinic.setText("please enter phone of clincs");
                    }else if(!fromTime){
                        fromTxt.setError("please enter from");
                    }else if(!toTime){
                        toTxt.setError("please enter from");
                    }else if(address_clinic.getText().toString().isEmpty()){
                        address_clinic.setText("please enter address of clincs");
                    }else if(dropdown.getSelectedItemPosition() == 0){
                        ((TextView)dropdown.getSelectedView()).setError("Error message");
                    }else {

                        Services.updateClinic(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                bussinessName.getText().toString(),
                                address_clinic.getText().toString(),
                                contact_clinic.getText().toString(),
                                String.valueOf(fromHour + ":" + fromMin),
                                String.valueOf(toHour + ":" + toMin),
                                p2.getIdOfClinics(),
                                dropdown.getSelectedItemPosition(),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                            if(success == 1){

                                                Services.getAllClinicsForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                                                        constant.sharedPreferences.getString(constant.Token, null),
                                                        new RequestCallback() {
                                                            @Override
                                                            public void Success(String response) {

                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    Log.d("HASSAN", "Success: " + response);
                                                                    int success = jsonObject.getInt("success");
                                                                    String msg = jsonObject.getString("message");

                                                                    if(success == 1){
                                                                        JSONArray jsonArray = jsonObject.getJSONArray("clinics");
                                                                        ClinicsPojo p2 = null;
                                                                        for (int i = 0; i < jsonArray.length(); ++i){
                                                                            JSONObject obj = jsonArray.getJSONObject(i);
                                                                            p2 = new ClinicsPojo(obj.getInt("doctorID"),
                                                                                    obj.getInt("id"),
                                                                                    obj.getString("name"),
                                                                                    obj.getString("address"),
                                                                                    obj.getInt("phone"), obj.getInt("city"),
                                                                                    obj.getString("openAt"),
                                                                                    obj.getString("closeAt"),
                                                                                    obj.getString("doctorName"));
                                                                        }

                                                                        Bundle bundle2 = new Bundle();
                                                                        bundle2.putParcelable("clinic", p2);
                                                                        UpdateClinicInfo ClinicInfo = new UpdateClinicInfo();
                                                                        ClinicInfo.setArguments(bundle2);

                                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, ClinicInfo)
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


    }

}
