package com.example.ahmedsayed.navdrawer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Accessories extends Fragment {

    ListView listViewOfAcc;
    private Button filter;
    private RadioGroup RG_filter;
    private RadioButton RB_animal, RB_food, RB_accessories;
    private resetAdapterObjectOfAccessories adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accessories, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Accessories");

        filter = view.findViewById(R.id.btn_filterProduct);
        RG_filter = view.findViewById(R.id.RG_productFilter);
        RB_food = view.findViewById(R.id.RB_food);
        RB_animal = view.findViewById(R.id.RB_animal);
        RB_accessories = view.findViewById(R.id.RB_accessories);
        listViewOfAcc =view.findViewById(R.id.listView_accessories_id);


        final ArrayList<AccessoriesPojo> Accessories = new ArrayList<>();

        //constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE);
        Services.getAllAccessories(constant.sharedPreferences.getInt(constant.ID, -1),
                constant.sharedPreferences.getString(constant.Token, null),
                -1, new RequestCallback() {
                    @Override
                    public void Success(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("message");
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            if(success == 1){
                                JSONArray jsonArray = jsonObject.getJSONArray("accessories");
                                for (int i = 0; i < jsonArray.length(); ++i){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    int owner = obj.getInt("owner");
                                    String name = obj.getString("name");
                                    Double price = obj.getDouble("price");
                                    String image = obj.getString("image");
                                    int type = obj.getInt("type");
                                    int quantity = obj.getInt("quantity");
                                    int storeOwnId = obj.getInt("storeOwnId");
                                    String storeName = obj.getString("storeName");
                                    String storeAddress = obj.getString("storeAddress");
                                    String storePhone = obj.getString("storePhone");
                                    String openAt = obj.getString("openAt");
                                    String closeAt = obj.getString("closeAt");

                                    Accessories.add(new AccessoriesPojo(id, type, name, price, image, storeOwnId, storeName,
                                            storeAddress, storePhone, openAt, closeAt, quantity, owner));
                                }
                                showAccessories(Accessories);
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

                switch (RG_filter.getCheckedRadioButtonId()){
                    case R.id.RB_food:
                        refreshList(Accessories,2);
                        break;
                    case R.id.RB_animal:
                        refreshList(Accessories,3);
                        break;
                    case R.id.RB_accessories:
                        refreshList(Accessories,1);
                        break;
                    default:
                        RB_food.setError("please check once !");
                        RB_accessories.setError("please check once !");
                        RB_animal.setError("please check once !");
                }

            }
        });



    }

    private void refreshList(ArrayList<AccessoriesPojo> accessories, int i) {
        ArrayList<AccessoriesPojo> filteredList = new ArrayList<>();

        for (AccessoriesPojo acc : accessories) {
            if(acc.getType() == i){
                filteredList.add(acc);
            }
        }

        showAccessories(filteredList);
    }

    private void showAccessories(ArrayList<AccessoriesPojo> AccessoriesShowss){
        resetAdapterObjectOfAccessories adapter = new resetAdapterObjectOfAccessories(AccessoriesShowss,getActivity());
        listViewOfAcc.setAdapter(adapter);

    }
}
