package com.example.ahmedsayed.navdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class resetAdapterObjectOfOrders extends BaseAdapter {

    ArrayList<OrderPojo> OrderArray;
    private Context context;

    public resetAdapterObjectOfOrders(ArrayList<OrderPojo> OrderArray, Context context) {

        this.OrderArray = OrderArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return OrderArray.size();
    }

    @Override
    public Object getItem(int position) {
        return OrderArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_templete_order, null);
        TextView name11 = convertView.findViewById(R.id.Name_id);
        TextView product11 = convertView.findViewById(R.id.Product11_id);
        TextView address11 = convertView.findViewById(R.id.Address11_id);
        TextView mobile11 = convertView.findViewById(R.id.Mobile11_id);
        final OrderPojo currentOrder = (OrderPojo) getItem(position);
        name11.setText("Name :  " + currentOrder.getName());
        product11.setText("Product :" + currentOrder.getProduct());
        address11.setText("Address : " + currentOrder.getAddress());
        mobile11.setText("Mobile : " + currentOrder.getMobile());



        return convertView;
    }
}
