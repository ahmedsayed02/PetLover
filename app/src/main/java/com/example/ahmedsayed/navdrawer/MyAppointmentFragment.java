package com.example.ahmedsayed.navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAppointmentFragment extends Fragment {
    ListView listOfAppointment;
    ArrayList<AppointmentPojo> ApointmentsArray;

    public MyAppointmentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.appointment_listview, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Appointment");
        listOfAppointment = view.findViewById(R.id.listView_Appointment_id);
        ApointmentsArray = new ArrayList<>();
        if(constant.sharedPreferences.getInt(constant.Type,-1) == 1){

            Services.getAllAppointmentForUser(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                if(success == 1){
                                    JSONArray jsonArray = jsonObject.getJSONArray("appoinments");
                                    for (int i = 0; i < jsonArray.length(); ++i){
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        int id = obj.getInt("id");
                                        int docId = obj.getInt("doctorId");
                                        int clinicId = obj.getInt("clinicId");
                                        int userId = obj.getInt("userId");
                                        String timeFrom = obj.getString("timeFrom");
                                        String dateFrom = obj.getString("dateFrom");
                                        int status = obj.getInt("status");
                                        String userName = obj.getString("userName");
                                        ApointmentsArray.add(new AppointmentPojo(timeFrom, dateFrom, userName,
                                                id, docId, clinicId, userId, status));
                                    }

                                    showAppointments(ApointmentsArray);

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

        }else if(constant.sharedPreferences.getInt(constant.Type,-1) == 2){

            Services.getAllAppointmentForDoctor(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                if(success == 1){
                                    JSONArray jsonArray = jsonObject.getJSONArray("appoinments");
                                    for (int i = 0; i < jsonArray.length(); ++i){
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        int id = obj.getInt("id");
                                        int docId = obj.getInt("doctorId");
                                        int clinicId = obj.getInt("clinicId");
                                        int userId = obj.getInt("userId");
                                        String timeFrom = obj.getString("timeFrom");
                                        String dateFrom = obj.getString("dateFrom");
                                        int status = obj.getInt("status");
                                        String userName = obj.getString("userName");
                                        ApointmentsArray.add(new AppointmentPojo(timeFrom, dateFrom, userName,
                                                id, docId, clinicId, userId, status));
                                    }

                                    showAppointments(ApointmentsArray);

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

    private void showAppointments(final ArrayList<AppointmentPojo> AppShow) {
        final resetAdapterOfAppointment adapter = new resetAdapterOfAppointment(getActivity(), AppShow);
        listOfAppointment.setAdapter(adapter);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listOfAppointment,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(final ListView listView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {

                                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
                                    Dialog.setCanceledOnTouchOutside(false);
                                    Dialog.show();

                                    Dialog.dialogTime.setVisibility(View.GONE);
                                    Dialog.dialogDate.setText("Are You sure you want to delete this appointment ?");


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

                                            Services.deleteAppointment(constant.sharedPreferences.getInt(constant.ID, -1),
                                                    constant.sharedPreferences.getString(constant.Token, null),
                                                    AppShow.get(position).getID(),
                                                    new RequestCallback() {
                                                        @Override
                                                        public void Success(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                int success = jsonObject.getInt("success");
                                                                String msg = jsonObject.getString("message");
                                                                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                                                                if(success == 1){
                                                                    Dialog.dismiss();

                                                                    AppShow.remove(position);
                                                                    adapter.notifyDataSetChanged();

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

                            }
                        });
        listOfAppointment.setOnTouchListener(touchListener);


    }




}
