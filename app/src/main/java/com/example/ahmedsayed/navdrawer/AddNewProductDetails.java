package com.example.ahmedsayed.navdrawer;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewProductDetails extends Fragment implements View.OnClickListener {
    ImageView product_img;
    EditText product_name, ProductPrice, ProductQuantity;
    RadioButton animal, food, accessories;
    RadioGroup RG;
    Button AddProdcut_btn;
    private Bitmap bitmap;
    private int GALLERY = 1, CAMERA = 2;
    int storeID;

    public AddNewProductDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_product_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Adding New Product");
        product_img = view.findViewById(R.id.product_img_id);
        product_name = view.findViewById(R.id.product_name_id);
        ProductPrice = view.findViewById(R.id.ProductPrice_id);
        ProductQuantity = view.findViewById(R.id.ProductQuantity_id);
        animal = view.findViewById(R.id.animal_id);
        food = view.findViewById(R.id.food_id);
        accessories = view.findViewById(R.id.Access_id);
        AddProdcut_btn = view.findViewById(R.id.addProductPress_id);
        RG = view.findViewById(R.id.type_product_id);



        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });


        if(getArguments().getParcelable("productData") != null){

            final AccessoriesPojo accessory = getArguments().getParcelable("productData");

            Picasso.get()
                    .load(Services.url + accessory.getImageOfAccessories()).centerCrop().resize(100, 100)
                    .noFade().onlyScaleDown()
                    .into(product_img);
            product_name.setText(accessory.getNameOfAccessories());
            ProductPrice.setText(String.valueOf(accessory.getPrice()));
            ProductQuantity.setText(String.valueOf(accessory.getQuantity()));

            switch (accessory.getType()){
                case 1:
                    accessories.setChecked(true);
                    break;
                case 2:
                    food.setChecked(true);
                    break;
                case 3:
                    animal.setChecked(true);
                    break;
            }

            AddProdcut_btn.setText("update Product");

            AddProdcut_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(product_name.getText().toString().isEmpty()){
                        product_name.setError("please enter name of product");
                    }else if(ProductPrice.getText().toString().isEmpty()){
                        ProductPrice.setError("Please enter price of product");
                    }else if(ProductQuantity.getText().toString().isEmpty()){
                        ProductQuantity.setError("Please enter Quantity of product");
                    }else if(RG.getCheckedRadioButtonId() == -1){
                        accessories.setError("please choose once");
                        animal.setError("please choose once");
                        food.setError("please choose once");
                    }else {

                        int type = 0;
                        switch (RG.getCheckedRadioButtonId()){
                            case R.id.Access_id:
                                type = 1;
                                break;
                            case R.id.food_id:
                                type = 2;
                                break;
                            case R.id.animal_id:
                                type = 3;
                                break;
                        }
                        bitmap = ((BitmapDrawable) product_img.getDrawable()).getBitmap();

                        Services.updateAccessories(accessory.getStoreId(), constant.sharedPreferences.getString(constant.Token, null),
                                product_name.getText().toString(), ProductPrice.getText().toString(),
                                getStringImage(bitmap),accessory.getID(), type, Integer.parseInt(ProductQuantity.getText().toString()),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                            if(success == 1){
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("storeId",accessory.getStoreId());
                                                Add_Products f12 = new Add_Products();
                                                f12.setArguments(bundle);
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_id, f12)
                                                        .commit();
                                            }else if(msg.equals("Invalid token.")){
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
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

            storeID = getArguments().getInt("storeId");
            AddProdcut_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(product_name.getText().toString().isEmpty()){
                        product_name.setError("please enter name of product");
                    }else if(ProductPrice.getText().toString().isEmpty()){
                        ProductPrice.setError("Please enter price of product");
                    }else if(ProductQuantity.getText().toString().isEmpty()){
                        ProductQuantity.setError("Please enter Quantity of product");
                    }else if(RG.getCheckedRadioButtonId() == -1){
                        accessories.setError("please choose once");
                        animal.setError("please choose once");
                        food.setError("please choose once");
                    }else {

                        int type = 0;
                        switch (RG.getCheckedRadioButtonId()){
                            case R.id.Access_id:
                                type = 1;
                                break;
                            case R.id.food_id:
                                type = 2;
                                break;
                            case R.id.animal_id:
                                type = 3;
                                break;
                        }
                        bitmap = ((BitmapDrawable) product_img.getDrawable()).getBitmap();

                        Services.addAccessories(storeID, constant.sharedPreferences.getString(constant.Token, null),
                                product_name.getText().toString(), ProductPrice.getText().toString(),
                                getStringImage(bitmap), type, Integer.parseInt(ProductQuantity.getText().toString()),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                            if(success == 1){
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("storeId",storeID);
                                                Add_Products f12 = new Add_Products();
                                                f12.setArguments(bundle);
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_id, f12)
                                                        .commit();
                                            }else if(msg.equals("Invalid token.")){
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
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

    @Override
    public void onClick(View v) {





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
                    product_img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            product_img.setImageBitmap(bitmap);

        }
    }
}
