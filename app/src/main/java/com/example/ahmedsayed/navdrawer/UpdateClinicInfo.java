package com.example.ahmedsayed.navdrawer;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UpdateClinicInfo extends Fragment {

TextView get_bussinessName,get_contact_doctor,openat_get,closeat_get,get_adress_doctor;
Button updated_clinic_info,delete_clinic_id;
    public UpdateClinicInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_clinic_info, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Update Clinic Info");
        get_bussinessName=view.findViewById(R.id.get_bussinessName);
        get_contact_doctor=view.findViewById(R.id.get_contact_doctor);
        openat_get=view.findViewById(R.id.openat_get_id);
        closeat_get=view.findViewById(R.id.closeat_get_id);
        get_adress_doctor=view.findViewById(R.id.get_adress_doctor);
        //////////////////////////////////////////////////
        updated_clinic_info=view.findViewById(R.id.updated_clinic_info);
        delete_clinic_id=view.findViewById(R.id.delete_clinic_id);

       final ClinicsPojo clinicObj = getArguments().getParcelable("clinic");
        get_bussinessName.setText(clinicObj.getNameOfClinic());
        get_contact_doctor.setText(String.valueOf(clinicObj.getPhone()));
        openat_get.setText(clinicObj.getOpenAt());
        closeat_get.setText(clinicObj.getCloseAt());
        get_adress_doctor.setText(clinicObj.getAddressOfClinic());

        updated_clinic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("clinicData", clinicObj);
                AddMyClinic ClinicInfo = new AddMyClinic();
                ClinicInfo.setArguments(bundle2);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, ClinicInfo)
                        .addToBackStack(null).commit();
            }
        });

        delete_clinic_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
                Dialog.setCanceledOnTouchOutside(false);
                Dialog.show();

                Dialog.dialogTime.setVisibility(View.GONE);
                Dialog.dialogDate.setText("Are You sure you want to delete this clinic");


                Dialog.yes.setText("yes");
                Dialog.no.setText("no");

                Dialog.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog.dismiss();
                    }
                });

                Dialog.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Services.deleteClinic(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                clinicObj.getIdOfClinics(), new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                            if(success == 1){
                                                Dialog.dismiss();
                                                AddMyClinic f5 = new AddMyClinic();
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_id,f5)
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

                    }
                });


            }
        });

    }
}
