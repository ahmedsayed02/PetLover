package com.example.ahmedsayed.navdrawer;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;


public class add_new_pet extends Fragment {
    private ImageView imageView;
    private TextView birthday_btn;
    private EditText name, color, notes;
    private RadioGroup gender, type;
    private RadioButton male, female, cat, dog;
    private Button submit;

    private int mYear, mMonth, mDay;
    private int cYear, cMonth, cDay;
    private Bitmap bitmap;
    private String petType;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_pet, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        birthday_btn = view.findViewById(R.id.ageOfPet_id);
        imageView = view.findViewById(R.id.profile_img_id);
        name = view.findViewById(R.id.petName_id);
        notes = view.findViewById(R.id.petNotes_id);
        color = view.findViewById(R.id.colorPet_id);
        type = view.findViewById(R.id.RG_petType);
        cat = view.findViewById(R.id.RB_cat);
        dog = view.findViewById(R.id.RB_dog);
        gender = view.findViewById(R.id.RG_gender);
        male = view.findViewById(R.id.RB_male);
        female = view.findViewById(R.id.RB_female);
        submit = view.findViewById(R.id.addPetPress_id);

        //constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE);

        if (getArguments() != null) {

            final PetsPojo data = getArguments().getParcelable("data");

            Picasso.get()
                    .load(Services.url + data.getImageOfPet()).centerCrop().resize(100, 72)
                    .noFade().onlyScaleDown()
                    .into(imageView);

          /*  Picasso.get().load(Services.url + data.getImageOfPet()).fit()
                    .placeholder(R.drawable.error).into(imageView);*/


/*
  int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
           // Loads given image
            Picasso.get()
                    .load(Services.url + data.getImageOfPet())
                    .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerInside()
                    .error(R.drawable.error)
                    .noFade()
                    .into(imageView);*/


            birthday_btn.setText(data.getBirthDay());
            name.setText(data.getNameOfpet());
            notes.setText(data.getNotes());
            color.setText(data.getColor());

            if(data.getType().equals("cat")){
                cat.setChecked(true);
            }else
                dog.setChecked(true);

            if (data.getGender() == 1) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }

            submit.setText("edit");

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (name.getText().toString().isEmpty()) {
                        name.setError("please enter Pet name");
                    } else if (birthday_btn.getText().toString().isEmpty()) {
                        birthday_btn.setError("please enter birthday of pet");
                    } else if (gender.getCheckedRadioButtonId() == -1) {
                        male.setError("please choose at least one");
                        female.setError("please choose at least one");
                    } else if (color.getText().toString().isEmpty()) {
                        color.setError("please enter color of your pet");
                    } else if (type.getCheckedRadioButtonId() == -1) {
                        cat.setError("please enter type of pet");
                        dog.setError("please enter type of pet");
                    } else if (notes.getText().toString().isEmpty()) {
                        notes.setError("please enter note");
                    } else {
                        int sex = 0;
                        switch (gender.getCheckedRadioButtonId()) {
                            case R.id.RB_male:
                                sex = 1;
                                break;
                            case R.id.RB_female:
                                sex = 2;
                                break;
                        }

                        switch (type.getCheckedRadioButtonId()){
                            case R.id.RB_cat:
                                petType = "cat";
                                break;
                            case R.id.RB_dog:
                                petType = "dog";
                                break;
                        }

                        String bath = cYear + "-" + ((cMonth + 1) % 12) + "-" + cDay;
                        String hair = cYear + "-" + ((cMonth + 12) % 12) + "-" + cDay;
                        String nails = (cYear + 1) + "-" + cMonth + "-" + cDay;
                        String teeth = cYear + "-" + cMonth + "-" + ((cDay + 14) % 30);
                        String ears = cYear + "-" + cMonth + "-" + ((cDay + 7) % 30);
                        String internalDeworming = cYear + "-" + cMonth + "-" + ((cDay + 5) % 30);

                        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                        Services.updatePet(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                name.getText().toString(), sex, petType,
                                birthday_btn.getText().toString(), notes.getText().toString(),
                                color.getText().toString(), getStringImage(bitmap),
                                bath, hair, nails, teeth, ears, internalDeworming, data.getID(),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                            if (success == 1) {
                                                Fragment fragment = new MyPetFragment();
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.fragment_id, fragment).commit();
                                            } else if (msg.equals("Invalid token.")) {
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
                    }

                }
            });

        } else {

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (name.getText().toString().isEmpty()) {
                        name.setError("please enter Pet name");
                    } else if (birthday_btn.getText().toString().isEmpty()) {
                        birthday_btn.setError("please enter birthday of pet");
                    } else if (gender.getCheckedRadioButtonId() == -1) {
                        male.setError("please choose at least one");
                        female.setError("please choose at least one");
                    } else if (color.getText().toString().isEmpty()) {
                        color.setError("please enter color of your pet");
                    } else if (type.getCheckedRadioButtonId() == -1) {
                        cat.setError("please enter type of pet");
                        dog.setError("please enter type of pet");
                    } else if (notes.getText().toString().isEmpty()) {
                        notes.setError("please enter note");
                    } else {
                        int sex = 0;
                        switch (gender.getCheckedRadioButtonId()) {
                            case R.id.RB_male:
                                sex = 1;
                                break;
                            case R.id.RB_female:
                                sex = 2;
                                break;
                        }

                        switch (type.getCheckedRadioButtonId()){
                            case R.id.RB_cat:
                                petType = "cat";
                                break;
                            case R.id.RB_dog:
                                petType = "dog";
                                break;
                        }

                        String bath = cYear + "-" + ((cMonth + 1) % 12) + "-" + cDay;
                        String hair = cYear + "-" + ((cMonth + 12) % 12) + "-" + cDay;
                        String nails = (cYear + 1) + "-" + cMonth + "-" + cDay;
                        String teeth = cYear + "-" + cMonth + "-" + ((cDay + 1) % 30);
                        String ears = cYear + "-" + cMonth + "-" + ((cDay + 7) % 30);
                        String internalDeworming = cYear + "-" + cMonth + "-" + ((cDay + 5) % 30);

                        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        Services.addPet(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                name.getText().toString(), sex, petType,
                                birthday_btn.getText().toString(), notes.getText().toString(),
                                color.getText().toString(), getStringImage(bitmap),
                                bath, hair, nails, teeth, ears, internalDeworming,
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {
                                        Log.d("HASSAN", "Success: " + response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                            if (success == 1) {
                                                Fragment fragment = new MyPetFragment();
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.fragment_id, fragment).commit();
                                            } else if (msg.equals("Invalid token.")) {
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
                    }

                }
            });

        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        birthday_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                birthday_btn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                cDay = dayOfMonth;
                                cMonth = monthOfYear + 1;
                                cYear = year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }

    public String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, ba);
        byte[] imagebyte = ba.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

        }
    }
}
