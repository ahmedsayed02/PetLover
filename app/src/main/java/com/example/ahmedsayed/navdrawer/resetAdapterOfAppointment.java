package com.example.ahmedsayed.navdrawer;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class resetAdapterOfAppointment extends BaseAdapter {
    private Context context;
    ArrayList<AppointmentPojo> ApointmentsArray;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public resetAdapterOfAppointment(Context context, ArrayList<AppointmentPojo> apointments) {
        this.context = context;
        ApointmentsArray = apointments;
    }


    @Override
    public int getCount() {
        return ApointmentsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return ApointmentsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_templete_appoinment, null);
        ImageButton details = convertView.findViewById(R.id.appointment_details);
        TextView from = convertView.findViewById(R.id.FromAppo_id);
        TextView day = convertView.findViewById(R.id.Day_id);
        TextView NameOfUser = convertView.findViewById(R.id.NameOfUser_id);
        TextView Confirm = convertView.findViewById(R.id.Confirm_id);
        final AppointmentPojo c1 = ApointmentsArray.get(position);
        from.setText("From : " + c1.getFrom());
        day.setText("Day : " +c1.getDay());
        NameOfUser.setText("Name of user\n" + c1.getNameOfUser());

        switch (c1.getConfirmation()){
            case 1:
                Confirm.setText("status : " +"pending");
                break;
            case 2:
                Confirm.setText("status : " +"accepted");
                break;
            case 3:
                Confirm.setText("status : " +"rejected");
                break;
        }

        if(constant.sharedPreferences.getInt(constant.Type,-1) == 2){

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(context);
                    Dialog.setCanceledOnTouchOutside(true);
                    Dialog.show();

                    Dialog.dialogTime.setVisibility(View.GONE);
                    Dialog.dialogDate.setText("Is this time available ?");


                    Dialog.yes.setText("yes");
                    Dialog.no.setText("no");

                    Dialog.no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Services.sendMessage(c1.getUserId(), "appointment",
                                    "Hello " + c1.getNameOfUser() + "\n" + "Time you chose is not available, " +
                                            "please choose another time",
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                if(success == 0){
                                                    Toast.makeText(context,"user is offline",Toast.LENGTH_LONG).show();
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

                            Services.updateAppointment(constant.sharedPreferences.getString(constant.Token, null),
                                    c1.getDocId(),
                                    c1.getUserId(),
                                    c1.getClinicId(),
                                    c1.getFrom(),
                                    c1.getDay(),
                                    c1.getID(),
                                    3, //TODO: 1 -> pending, 2 -> accepted, 3 -> rejected
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                String msg = jsonObject.getString("message");
                                                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                if(success == 1){

                                                    MyAppointmentFragment f7 = new MyAppointmentFragment();
                                                    ((MainActivity)context).getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.fragment_id, f7)
                                                            .commit();

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
                    });

                    Dialog.yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Services.sendMessage(c1.getUserId(), "appointment",
                                    "Hello " + c1.getNameOfUser() + "\n" + "Time you chose is available",
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                if(success == 0){
                                                    Toast.makeText(context,"user is offline",Toast.LENGTH_LONG).show();
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

                            Services.updateAppointment(constant.sharedPreferences.getString(constant.Token, null),
                                    c1.getDocId(),
                                    c1.getUserId(),
                                    c1.getClinicId(),
                                    c1.getFrom(),
                                    c1.getDay(),
                                    c1.getID(),
                                    2, //TODO: 1 -> pending, 2 -> accepted, 3 -> rejected
                                    new RequestCallback() {
                                        @Override
                                        public void Success(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                int success = jsonObject.getInt("success");
                                                String msg = jsonObject.getString("message");
                                                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                if(success == 1){

                                                    MyAppointmentFragment f7 = new MyAppointmentFragment();
                                                    ((MainActivity)context).getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.fragment_id, f7)
                                                            .commit();

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
                    });
                }
            });


        }else if(constant.sharedPreferences.getInt(constant.Type,-1) == 1){

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(context);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.show();

                    Dialog.dialogDate.setText(c1.getDay());
                    Dialog.dialogTime.setText(c1.getFrom());

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

                                Services.sendMessage(c1.getDocId(), "appointment",
                                        "I have edited the appointment on " + Dialog.dialogDate.getText().toString() + " at " +
                                                Dialog.dialogTime.getText().toString(),
                                        new RequestCallback() {
                                            @Override
                                            public void Success(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    int success = jsonObject.getInt("success");
                                                    if(success == 0){
                                                        Toast.makeText(context,"doctor is offline",Toast.LENGTH_LONG).show();
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

                                Services.updateAppointment(constant.sharedPreferences.getString(constant.Token, null),
                                        c1.getDocId(),
                                        c1.getUserId(),
                                        c1.getClinicId(),
                                        Dialog.dialogTime.getText().toString(),
                                        Dialog.dialogDate.getText().toString(),
                                        c1.getID(),
                                        c1.getConfirmation(),
                                        new RequestCallback() {
                                            @Override
                                            public void Success(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    int success = jsonObject.getInt("success");
                                                    String msg = jsonObject.getString("message");
                                                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                    if(success == 1){

                                                        MyAppointmentFragment f7 = new MyAppointmentFragment();
                                                        ((MainActivity)context).getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.fragment_id, f7)
                                                                .commit();

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
                }
            });

        }
        return convertView;
    }


}
