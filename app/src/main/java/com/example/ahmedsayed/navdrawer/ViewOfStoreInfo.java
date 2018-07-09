package com.example.ahmedsayed.navdrawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ViewOfStoreInfo extends Fragment implements View.OnClickListener {
    ImageView storeImage;
    TextView get_StoreName, get_store_contact, get_openat, get_closeat, get_adress_store;
    Button updated_Store_btn, delete_store_btn, view_product_btn;
    StorePojo obj;

    public ViewOfStoreInfo() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_view_of_store_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Store Details...");

        storeImage = view.findViewById(R.id.store_image_id);
        get_StoreName = view.findViewById(R.id.get_StoreName_id);
        get_store_contact = view.findViewById(R.id.get_store_contact_id);
        get_openat = view.findViewById(R.id.get_openat_id);
        get_closeat = view.findViewById(R.id.get_closeat_id);
        get_adress_store = view.findViewById(R.id.get_adress_store_id);
        updated_Store_btn = view.findViewById(R.id.updated_Store_info_id);
        delete_store_btn = view.findViewById(R.id.delete_store_id);//delete from db
        view_product_btn = view.findViewById(R.id.view_product_id);


        obj = getArguments().getParcelable("store");
        get_StoreName.setText("Store Name : " + obj.getNameOfStore());
        get_store_contact.setText(String.valueOf(obj.getPhoneOfStore()));
        get_openat.setText("Open@:" + obj.getOpenAt1());
        get_closeat.setText("Close@" + obj.getCloseAt1());
        get_adress_store.setText("Store Address : " + obj.getAddressOfStore());


        view_product_btn.setOnClickListener(this);
        updated_Store_btn.setOnClickListener(this);
        delete_store_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_product_id) {
            Bundle bundle = new Bundle();
            bundle.putInt("storeId",obj.getIdOfStore());
            Add_Products f6 = new Add_Products();
            f6.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f6)
                    .addToBackStack(null)
                    .commit();
        } else if (v.getId() == R.id.updated_Store_info_id) {

            Bundle bundle = new Bundle();
            bundle.putParcelable("storeData", obj);
            MyStoreFragment p012 = new MyStoreFragment();
            p012.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, p012)
                    .addToBackStack(null)
                    .commit();

        }else if(v.getId() == R.id.delete_store_id){

            final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();

            Dialog.dialogTime.setVisibility(View.GONE);
            Dialog.dialogDate.setText("Are You sure you want to delete this store ?");


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

                    Services.deleteStore(constant.sharedPreferences.getInt(constant.ID, -1),
                            constant.sharedPreferences.getString(constant.Token, null),
                            obj.getIdOfStore(),
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
                                            MyStoreFragment f5 = new MyStoreFragment();
                                            getActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_id,f5)
                                                    .commit();

                                        }else if(msg.equals("Invalid token.")){
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                            getActivity().finish();
                                        }else if(msg.equals("This store has product(s), please delete it first")){
                                            Dialog.dismiss();
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("storeId",obj.getIdOfStore());
                                            Add_Products f6 = new Add_Products();
                                            f6.setArguments(bundle);
                                            getActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_id, f6)
                                                    .addToBackStack(null)
                                                    .commit();
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
}
