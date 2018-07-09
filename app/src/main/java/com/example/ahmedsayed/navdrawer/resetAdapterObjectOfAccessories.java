package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class resetAdapterObjectOfAccessories extends BaseAdapter {
    ArrayList<AccessoriesPojo> AccessoriesArray;
    private Context context;
    private Bitmap bitmap;
    CoordinatorLayout coordinatorLayout;


    public resetAdapterObjectOfAccessories(ArrayList<AccessoriesPojo> accessoriesArray,Context context) {
        this.AccessoriesArray = accessoriesArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return AccessoriesArray.size();
    }

    @Override
    public Object getItem(int position) {
        return AccessoriesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,final ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.list_templete_accessories, null);

        final TextView nameOfAccessories = convertView.findViewById(R.id.nameOfAccessories_id);
//        View block = convertView.findViewById(R.id.block_product);
//        block.setVisibility(View.GONE);
        TextView price = convertView.findViewById(R.id.price_id);
        TextView storeName = convertView.findViewById(R.id.nameOfStore);
        TextView storeAddress = convertView.findViewById(R.id.addressOfStore);
        TextView storePhone = convertView.findViewById(R.id.phoneOfStore);
        TextView openAt = convertView.findViewById(R.id.openAtOfStore);
        TextView closeAt = convertView.findViewById(R.id.closeAtOfStore);
        coordinatorLayout = convertView.findViewById(R.id.coordinatorLayout);


        final ImageView ImageOfAccessories = convertView.findViewById(R.id.ImageOfAccessories_id);

        final AccessoriesPojo currentAccessories = (AccessoriesPojo) getItem(position);
        nameOfAccessories.setText(currentAccessories.getNameOfAccessories());
        price.setText(String.valueOf(currentAccessories.getPrice()));

        storeName.setText(currentAccessories.getStoreName());
        storeAddress.setText(currentAccessories.getStoreAddress());
        storePhone.setText(currentAccessories.getStorePhone());
        openAt.setText(currentAccessories.getOpenAt());
        closeAt.setText(currentAccessories.getCloseAt());

        Picasso.get()
                .load(Services.url + currentAccessories.getImageOfAccessories()).centerCrop().resize(100, 100)
                .noFade().onlyScaleDown()
                .into(ImageOfAccessories);

        if(currentAccessories.getQuantity() != 0){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(context);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.show();

                    Dialog.dialogTime.setVisibility(View.GONE);
                    Dialog.dialogDate.setText("Are you want to order this product ?");

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

                            if(ImageOfAccessories.getDrawable() != null){

                                Services.sendMessage(currentAccessories.getOwner(), "order",
                                        "Hello I want to order this product -> " + currentAccessories.getNameOfAccessories(),
                                        new RequestCallback() {
                                            @Override
                                            public void Success(String response) {
                                                Log.d("HASSAN", "Success: " + response);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    int success = jsonObject.getInt("success");
//                                                    Toast.makeText(context,"request sent successfully",Toast.LENGTH_LONG).show();
                                                    Toast.makeText(context,"Successfully,Support Team will contact you soon",Toast.LENGTH_LONG).show();
                                                    if(success == 0){
                                                    }else {
                                                        Snackbar snackbar = Snackbar
                                                                .make(coordinatorLayout, "Support Team will call you soon", Snackbar.LENGTH_LONG);
//                                                        Toast.makeText(context,"request sent successfully",Toast.LENGTH_LONG).show();
//                                                        Toast.makeText(context,"Support Team will contact you soon",Toast.LENGTH_LONG).show();

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void Error(Exception ex) {

                                            }
                                        }, context);

                                Services.addOrder(currentAccessories.getID(),currentAccessories.getStoreId(),
                                        new RequestCallback() {
                                            @Override
                                            public void Success(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    int success = jsonObject.getInt("success");
                                                    String msg = jsonObject.getString("message");
                                                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                    if(success == 1){

                                                        bitmap = ((BitmapDrawable) ImageOfAccessories.getDrawable()).getBitmap();
                                                        int quantity = currentAccessories.getQuantity();
                                                        --quantity;
                                                        Services.updateAccessories(currentAccessories.getStoreId(),
                                                                constant.sharedPreferences.getString(constant.Token, null),
                                                                currentAccessories.getNameOfAccessories(),
                                                                String.valueOf(currentAccessories.getPrice()), getStringImage(bitmap),
                                                                currentAccessories.getID(), currentAccessories.getType(),
                                                                quantity,
                                                                new RequestCallback() {
                                                                    @Override
                                                                    public void Success(String response) {

                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            int success = jsonObject.getInt("success");
                                                                            String msg = jsonObject.getString("message");
                                                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                                                            if(success == 1){
                                                                                Dialog.dismiss();
                                                                            }else if(msg.equals("Invalid token.")){
                                                                                context.startActivity(new Intent(context, LoginActivity.class));
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void Error(Exception ex) {

                                                                    }
                                                                },context);
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
                            }else {
                                Toast.makeText(context,"please wait till Image load",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            });
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.fade_black));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "this product not available",Toast.LENGTH_SHORT).show();
                }
            });
        }


        return convertView;
    }

    public String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, ba);
        byte[] imagebyte = ba.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }
}
