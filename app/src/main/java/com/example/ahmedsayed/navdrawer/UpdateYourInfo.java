package com.example.ahmedsayed.navdrawer;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateYourInfo extends Fragment implements View.OnClickListener {

    private Button save_btn;
    private ImageView profile_img_edit;
    private Bitmap bitmap;
    private int GALLERY = 1, CAMERA = 2;

    private EditText name, password, mobile, address;


    public UpdateYourInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_your_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Update My Profile");
        save_btn = view.findViewById(R.id.saveInfo_id);
        profile_img_edit =view.findViewById(R.id.profile_edit_id);
        profile_img_edit.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        name = view.findViewById(R.id.etNamed_id);
        password = view.findViewById(R.id.etPassword_id);
        mobile = view.findViewById(R.id.etMobile_id);
        address = view.findViewById(R.id.etAddress_id);

//        constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE);
        password.setText(constant.sharedPreferences.getString(constant.Password,null));
        name.setText(constant.sharedPreferences.getString(constant.Name,null));
        mobile.setText(String.valueOf(constant.sharedPreferences.getInt(constant.Number,-1)));
        address.setText(constant.sharedPreferences.getString(constant.Address,null));
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.profile_edit_id) {
            showPictureDialog();
        }
        else if (v.getId()==R.id.saveInfo_id){
            boolean isChanged;
            bitmap = ((BitmapDrawable) profile_img_edit.getDrawable()).getBitmap();
            isChanged = !password.getText().toString().equals(constant.sharedPreferences.getString(constant.Password, null));
            Services.updateProfile(name.getText().toString(),
                    constant.sharedPreferences.getString(constant.Email, null),
                    password.getText().toString(), mobile.getText().toString(),
                    constant.sharedPreferences.getInt(constant.Type, -1),
                    address.getText().toString(), isChanged,
                    getStringImage(bitmap),
                    constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");
                                if(success == 1){
                                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                                    for(int i = 0; i < jsonArray.length(); ++i){
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        int id = obj.getInt("id");
                                        String name = obj.getString("name");
                                        String email = obj.getString("email");
                                        String password = obj.getString("password");
                                        int phone = obj.getInt("phone");
                                        int type = obj.getInt("type");
                                        String image = obj.getString("image");
                                        String address = obj.getString("address");
                                        String token = obj.getString("token");

                                        constant.editor = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE).edit();
                                        constant.editor.putInt(constant.ID, id);
                                        constant.editor.putString(constant.Name, name);
                                        constant.editor.putString(constant.Password, password);
                                        constant.editor.putString(constant.Email, email);
                                        constant.editor.putInt(constant.Number, phone);
                                        constant.editor.putString(constant.Token, token);
                                        constant.editor.putString(constant.Image, image);
                                        constant.editor.putString(constant.Address, address);
                                        constant.editor.putInt(constant.Type, type);
                                        constant.editor.putBoolean(constant.isLoggedIn,true);
                                        constant.editor.apply();
                                    }


                                    Fragment MyProfile = new MyProfile();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, MyProfile)
                                            .addToBackStack(null).commit();
                                }else{
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                    profile_img_edit.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            profile_img_edit.setImageBitmap(bitmap);

        }
    }
}
