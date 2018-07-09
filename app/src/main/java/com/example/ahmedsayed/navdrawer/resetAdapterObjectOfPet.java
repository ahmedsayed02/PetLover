package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ahmed Sayed on 13-Apr-18.
 */

public class resetAdapterObjectOfPet extends BaseAdapter  {
    ArrayList<PetsPojo> PetsArray;
    private Context context;
    public resetAdapterObjectOfPet(ArrayList<PetsPojo> PetsArray, Context context) {
        this.PetsArray = PetsArray;
        this.context = context;
    }


    @Override
    public int getCount() {
        return PetsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return PetsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View oldView, final ViewGroup adapterView) {


        oldView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_template_pet, null);

        final TextView NameOfPEt = oldView.findViewById(R.id.nameOfPet_id);
        TextView TypeOfPet = oldView.findViewById(R.id.typePet_id);
        ImageView ImageOfPet = oldView.findViewById(R.id.ImageOfPet_id);

        final PetsPojo currentPet = (PetsPojo) getItem(position);
        NameOfPEt.setText(currentPet.getNameOfpet());
        TypeOfPet.setText(String.valueOf(currentPet.getType()));
        Picasso.get()
                .load(Services.url +currentPet.getImageOfPet()).centerCrop().resize(100, 100)
                .noFade().onlyScaleDown()
                .into(ImageOfPet);

        ImageView detailsImage = oldView.findViewById(R.id.detalis_id);

        detailsImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Bundle bundle = new Bundle();
              bundle.putParcelable("data",PetsArray.get(position));
              DetailsPetFragment f4 =   new DetailsPetFragment();
              f4.setArguments(bundle);
              ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                      .replace(R.id.fragment_id, f4)
                      .addToBackStack(null)
                      .commit();
          }
      });

        return oldView;
    }
}





