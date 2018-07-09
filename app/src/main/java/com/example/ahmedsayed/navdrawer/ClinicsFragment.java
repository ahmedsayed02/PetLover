package com.example.ahmedsayed.navdrawer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClinicsFragment extends Fragment {

    ListView listViewOfClicnics;
    private Button filter;

    public ClinicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Clinics");
        listViewOfClicnics = view.findViewById(R.id.listView_Clinics_id);
        filter = view.findViewById(R.id.btn_filterClinics);

        //get the spinner from the xml.
        final Spinner dropdown = view.findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] cities = getResources().getStringArray(R.array.cities);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);


        final ArrayList<ClinicsPojo> clinics = new ArrayList<>();
        constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE);

        Services.getAllClinics(constant.sharedPreferences.getInt(constant.ID, -1),
                constant.sharedPreferences.getString(constant.Token, null),
                new RequestCallback() {
                    @Override
                    public void Success(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("message");
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            if(success == 1){
                                JSONArray jsonArray = jsonObject.getJSONArray("clinics");
                                for (int i = 0; i < jsonArray.length(); ++i){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    String name = obj.getString("name");
                                    String address = obj.getString("address");
                                    int phone = obj.getInt("phone");
                                    String openAt = obj.getString("openAt");
                                    String closeAt = obj.getString("closeAt");
                                    int doctorID = obj.getInt("doctorID");
                                    int city = obj.getInt("city");
                                    String docName = obj.getString("doctorName");

                                    clinics.add(new ClinicsPojo(doctorID,id, name, address, phone, city, openAt, closeAt, docName));
                                }
                                showDoctors(clinics);
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
                }, getActivity());

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(dropdown.getSelectedItemPosition() == 0){
                    ((TextView)dropdown.getSelectedView()).setError("Error message");
                }else {

                    ArrayList<ClinicsPojo> filteredList = new ArrayList<>();
                    for (ClinicsPojo clinic: clinics) {
                        if(clinic.getCity() == dropdown.getSelectedItemPosition()){
                            filteredList.add(clinic);
                        }
                    }

                    showDoctors(filteredList);

                }


            }
        });



    }
    private void showDoctors(ArrayList<ClinicsPojo> DocShowss){
        resetAdapterObjectOfClinics adapter = new resetAdapterObjectOfClinics(DocShowss,getActivity());
        listViewOfClicnics.setAdapter(adapter);

    }

}
