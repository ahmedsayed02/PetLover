package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Sayed on 27-Apr-18.
 */

public class resetAdapterObjectOfProducts extends BaseAdapter {

    private Context context;
    ArrayList<AccessoriesPojo> ProductArray;

    public resetAdapterObjectOfProducts( ArrayList<AccessoriesPojo> productArray,Context context) {
        this.context = context;
        ProductArray = productArray;
    }


    @Override
    public int getCount() {
        return ProductArray.size();
    }

    @Override
    public Object getItem(int position) {
        return ProductArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_templete_my_products, null);

        final TextView NameOfProduct = convertView.findViewById(R.id.product_name);
        ImageButton details = convertView.findViewById(R.id.product_details);
        TextView TypeOfProduct = convertView.findViewById(R.id.typeProduct_id);
        ImageView ImageOfProduct = convertView.findViewById(R.id.productImage_id);

        TextView PriceOfProduct = convertView.findViewById(R.id.product_price_id);

        final AccessoriesPojo currentProduct = (AccessoriesPojo) getItem(position);
        NameOfProduct.setText(currentProduct.getNameOfAccessories());
        if(currentProduct.getType() == 1)
            TypeOfProduct.setText("Accessories");
        else if(currentProduct.getType() == 2)
            TypeOfProduct.setText("Food");
        else
            TypeOfProduct.setText("Animal");
        PriceOfProduct.setText(String.valueOf(currentProduct.getPrice()));
        Picasso.get()
                .load(Services.url + currentProduct.getImageOfAccessories()).centerCrop().resize(100, 100)
                .noFade().onlyScaleDown()
                .into(ImageOfProduct);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewProductDetails f1 = new AddNewProductDetails();
                Bundle bundle = new Bundle();
                bundle.putParcelable("productData", currentProduct);
                f1.setArguments(bundle);
                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.fragment_id,f1).commit();
            }
        });
        return convertView;
    }
}
