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

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailsPetFragment extends Fragment {

    TextView PetName, gender_pet, type_pet, birthday_pet, notes_pet, color_pet;
    ImageView imageView;
    Button edit,delete;
    TextView bath, hair, nails, teeth, ears, internalDeworming;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.details_pets_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Pet Details");
        setHasOptionsMenu(true);
        PetName = view.findViewById(R.id.name_pet_id);
        gender_pet = view.findViewById(R.id.gender_pet_id);
        type_pet = view.findViewById(R.id.type_pet_id);
        birthday_pet = view.findViewById(R.id.birthday_pet_id);
        notes_pet = view.findViewById(R.id.notes_pet_id);
        color_pet = view.findViewById(R.id.pet_color);
        imageView = view.findViewById(R.id.profile_pet_img_id);
        edit = view.findViewById(R.id.btn_edit_pet);
        delete = view.findViewById(R.id.btn_delete_pet);

        bath = view.findViewById(R.id.bath);
        hair = view.findViewById(R.id.hair);
        nails = view.findViewById(R.id.nails);
        teeth = view.findViewById(R.id.teeth);
        ears = view.findViewById(R.id.ears);
        internalDeworming = view.findViewById(R.id.internalDeworming);

        final PetsPojo petsObj = getArguments().getParcelable("data");

        PetName.setText(petsObj.getNameOfpet());

        if(petsObj.getGender() == 1)
            gender_pet.setText("male");
        else
            gender_pet.setText("female");

        type_pet.setText(petsObj.getType());
        birthday_pet.setText(petsObj.getBirthDay());
        notes_pet.setText(petsObj.getNotes());
        color_pet.setText(petsObj.getColor());

        bath.setText(petsObj.getBath());
        hair.setText(petsObj.getHair());
        nails.setText(petsObj.getNails());
        teeth.setText(petsObj.getTeeth());
        ears.setText(petsObj.getEars());
        internalDeworming.setText(petsObj.getInternalDeworming());

        Picasso.get()
                .load(Services.url + petsObj.getImageOfPet()).centerCrop().resize(100, 100)
                .noFade().onlyScaleDown()
                .into(imageView);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("data",petsObj);
                Fragment fragment = new add_new_pet();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_id,fragment).commit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
                Dialog.setCanceledOnTouchOutside(false);
                Dialog.show();

                Dialog.dialogTime.setVisibility(View.GONE);
                Dialog.dialogDate.setText("Are You sure you want to delete this Pet");


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
                        Services.deletePet(constant.sharedPreferences.getInt(constant.ID, -1),
                                constant.sharedPreferences.getString(constant.Token, null),
                                petsObj.getID(),
                                new RequestCallback() {
                                    @Override
                                    public void Success(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int success = jsonObject.getInt("success");
                                            String msg = jsonObject.getString("message");
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                            if(success == 1){
                                                Dialog.dismiss();
                                                Fragment fragment = new MyPetFragment();
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.fragment_id,fragment).commit();
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
        });
    }

}


