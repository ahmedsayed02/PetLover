package com.example.ahmedsayed.navdrawer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class resetAdapterObjectOfClinics extends BaseAdapter {

    private ArrayList<ClinicsPojo> DoctorArray;
    private Context context;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public resetAdapterObjectOfClinics(ArrayList<ClinicsPojo> DoctorArray, Context context) {
        this.DoctorArray = DoctorArray;
        this.context = context;
    }


    @Override
    public int getCount() {
        return DoctorArray.size();
    }

    @Override
    public Object getItem(int position) {
        return DoctorArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View oldView, final ViewGroup adapterView) {


        oldView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_templete_clinics, null);

        TextView NameOfDoctor = oldView.findViewById(R.id.NameOfDocotr_id);
        TextView OpenAt = oldView.findViewById(R.id.OpenAt_id);
        TextView ClosedAt = oldView.findViewById(R.id.ClosedAt_id);
        TextView address = oldView.findViewById(R.id.clinicAddress_id);
        TextView phone = oldView.findViewById(R.id.clinicPhone_id);
        TextView docotr = oldView.findViewById(R.id.clinicDoctor_id);

        final ClinicsPojo currentDoctor = (ClinicsPojo) getItem(position);

        NameOfDoctor.setText(currentDoctor.getNameOfClinic());
        OpenAt.setText(currentDoctor.getOpenAt());
        ClosedAt.setText(currentDoctor.getCloseAt());
        address.setText(currentDoctor.getAddressOfClinic());
        phone.setText(String.valueOf(currentDoctor.getPhone()));
        docotr.setText(currentDoctor.getDoctorName());

        oldView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(context);
                Dialog.setCanceledOnTouchOutside(false);
                Dialog.show();

                Dialog.dialogDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        Dialog.dialogDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();

                    }
                });

                Dialog.dialogTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        Dialog.dialogTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });

                Dialog.yes.setText("Submit");
                Dialog.no.setText("Cancel");

                Dialog.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog.dismiss();
                    }
                });

                Dialog.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: send request to server & check time & date fields
                        if(Dialog.dialogDate.getText().toString().isEmpty()){
                            Dialog.dialogDate.setError("please enter Date!");
                        }else if(Dialog.dialogTime.getText().toString().isEmpty()){
                            Dialog.dialogTime.setError("please enter time!");
                        }else {

                            Services.sendMessage(currentDoctor.getIdOfDoctor(), "appointment",
                                    "Hello " + currentDoctor.getDoctorName() + "\n" +
                                    "I want to reserve an appointment on " + Dialog.dialogDate.getText().toString() + " at " +
                                    Dialog.dialogTime.getText().toString(),
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            Log.d("HASSAN", "Success: " + response);
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                if(success == 0){
                                                    Toast.makeText(context,"store is offline",Toast.LENGTH_LONG).show();
                                                }else {
                                                    Toast.makeText(context,"request sent successfully",Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void Error(Exception ex) {

                                        }
                                    }, context);

                            Services.addAppointment(constant.sharedPreferences.getString(constant.Token, null),
                                    currentDoctor.getIdOfDoctor(),
                                    constant.sharedPreferences.getInt(constant.ID, -1),
                                    currentDoctor.getIdOfClinics(),
                                    Dialog.dialogTime.getText().toString(),
                                    Dialog.dialogDate.getText().toString(),
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                String msg = jsonObject.getString("message");
                                                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                if(success == 1){

                                                }else if(msg.equals("Invalid token.")){
                                                    context.startActivity(new Intent(context, LoginActivity.class));
                                                    ((MainActivity)context).finish();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        @Override
                                        public void Error(Exception ex) {

                                        }
                                    },context);

                            Dialog.dismiss();

                        }

                    }
                });


                return false;
            }
        });
        return oldView;
    }
}
