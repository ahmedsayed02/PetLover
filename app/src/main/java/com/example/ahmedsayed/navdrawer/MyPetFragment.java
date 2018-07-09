package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyPetFragment extends Fragment implements View.OnClickListener {

FloatingActionButton add_pet_btn;
ListView listViewOfPets ;
    public MyPetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pet, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Pets");
        add_pet_btn = view.findViewById(R.id.fab_id);
        add_pet_btn.setOnClickListener(this);
        listViewOfPets = view.findViewById(R.id.listView_MyPets_id);

//        constant.sharedPreferences = getActivity().getSharedPreferences(constant.PREFERENCES, Context.MODE_PRIVATE);

        final ArrayList<PetsPojo> pets = new ArrayList<>();
        Services.getAllPetsForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                constant.sharedPreferences.getString(constant.Token, null),
                new RequestCallback() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("message");
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            if(success == 1){
                                JSONArray jsonArray = jsonObject.getJSONArray("pets");
                                for (int i = 0; i < jsonArray.length(); ++i){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    String name = obj.getString("petName");
                                    int gender = obj.getInt("gender");
                                    String type = obj.getString("type");
                                    String birthday = obj.getString("birthday");
                                    String notes = obj.getString("notes");
                                    String image = obj.getString("image");
                                    String color = obj.getString("color");
                                    String bath = obj.getString("bath");
                                    String hair = obj.getString("hair");
                                    String nails = obj.getString("nails");
                                    String teeth = obj.getString("teeth");
                                    String ears = obj.getString("ears");
                                    String internalDeworming = obj.getString("internalDeworming");
                                    int ownerId = obj.getInt("petOwnerId");
                                    pets.add(new PetsPojo(id,name,gender,type,birthday,notes,bath, hair,
                                            nails, teeth, ears, internalDeworming, image,
                                            ownerId,color));
                                }

                                showPets(pets);

                                // to hide add_pet_btn in scrolling
                                listViewOfPets.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    private int mLastFirstVisibleItem;

                                    @Override
                                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                                    }

                                    @Override
                                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                                        if (mLastFirstVisibleItem < firstVisibleItem) {
                                            Log.i("SCROLLING DOWN", "TRUE");
                                            add_pet_btn.hide();

                                        }
                                        if (mLastFirstVisibleItem > firstVisibleItem) {
                                            Log.i("SCROLLING UP", "TRUE");
                                            add_pet_btn.show();

                                        }
                                        mLastFirstVisibleItem = firstVisibleItem;

                                    }
                                });

                            }else if(msg.equals("Invalid token.")){
                                Toast.makeText(getActivity(), "sorry you change ur password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }else {
                                add_new_pet f4 = new add_new_pet();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_id, f4)
                                        .commit();
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
    private void showPets(ArrayList<PetsPojo> PetsShowss){
        resetAdapterObjectOfPet adapter = new resetAdapterObjectOfPet(PetsShowss,getActivity());
        listViewOfPets.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        //////hawdeh fragment to add pet

        add_new_pet f4 = new add_new_pet();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_id, f4)
                .addToBackStack(null)
                .commit();


    }
}
