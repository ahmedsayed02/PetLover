package com.example.ahmedsayed.navdrawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Add_Products extends Fragment {

    ListView listViewOfProduct;
    FloatingActionButton add_product_btn;

    public Add_Products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Products");
        listViewOfProduct = view.findViewById(R.id.listView_MyProduct_id);
        add_product_btn = view.findViewById(R.id.fab2_id);
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewProductDetails f4 = new AddNewProductDetails();
                f4.setArguments(getArguments());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_id, f4)
                        .addToBackStack(null)
                        .commit();
            }
        });
        int storeID = getArguments().getInt("storeId");

        Services.getAllAccessoriesForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                constant.sharedPreferences.getString(constant.Token, null),
                storeID, new RequestCallback() {
                    @Override
                    public void Success(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("message");
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            if(success == 1){
                                ArrayList<AccessoriesPojo> products = new ArrayList<>();
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

                                    products.add(new AccessoriesPojo(id, type, name, price, image, storeOwnId, storeName,
                                            storeAddress, storePhone, openAt, closeAt, quantity, owner));
                                }
                                showProducts(products);
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

    private void showProducts(final ArrayList<AccessoriesPojo> ProShowss) {
        final resetAdapterObjectOfProducts adapter = new resetAdapterObjectOfProducts(ProShowss, getActivity());
        listViewOfProduct.setAdapter(adapter);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listViewOfProduct,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {

                                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
                                    Dialog.setCanceledOnTouchOutside(false);
                                    Dialog.show();

                                    Dialog.dialogTime.setVisibility(View.GONE);
                                    Dialog.dialogDate.setText("Are You sure you want to delete this accessories");


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

                                            Services.deleteAccessory(constant.sharedPreferences.getInt(constant.ID, -1),
                                                    constant.sharedPreferences.getString(constant.Token, null),
                                                    ProShowss.get(position).getID(),
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

                                                                    ProShowss.remove(position);
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
        listViewOfProduct.setOnTouchListener(touchListener);

    }

}
