package com.example.ahmedsayed.navdrawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    ListView listOfOrders;
    ArrayList<OrderPojo> OrderArray;

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.order_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Orders");
        listOfOrders = view.findViewById(R.id.listView_Order_id);

        OrderArray = new ArrayList<>();

        if(constant.sharedPreferences.getInt(constant.Type,-1) == 1){

            Services.getOrdersForUser(new RequestCallback() {
                @Override
                public void Success(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                        if(success == 1){

                            JSONArray jsonArray = jsonObject.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); ++i){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String name = obj.getString("userName");
                                String mobile = obj.getString("userPhone");
                                String product = obj.getString("productName");
                                String address = obj.getString("userAddress");
                                int id = obj.getInt("id");
                                OrderArray.add(new OrderPojo(name,mobile,product,id,address));

                            }

                            showOrders(OrderArray);

                        }else if(msg.equals("Invalid token.")){
                            Toast.makeText(getActivity(), "sorry you change ur password", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),LoginActivity.class));
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
        }else if(constant.sharedPreferences.getInt(constant.Type,-1) == 3){

            int storeId = constant.sharedPreferences.getInt("storeID", -1);

            if(storeId != -1){
                Services.getOrdersForStore(storeId, new RequestCallback() {
                    @Override
                    public void Success(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("message");
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            if(success == 1){

                                JSONArray jsonArray = jsonObject.getJSONArray("orders");
                                for (int i = 0; i < jsonArray.length(); ++i){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String name = obj.getString("userName");
                                    String mobile = obj.getString("userPhone");
                                    String product = obj.getString("productName");
                                    String address = obj.getString("userAddress");
                                    int id = obj.getInt("id");
                                    OrderArray.add(new OrderPojo(name,mobile,product,id,address));

                                }

                                showOrders(OrderArray);

                            }else if(msg.equals("Invalid token.")){
                                Toast.makeText(getActivity(), "sorry you change ur password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
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
            }


        }




    }

    private void showOrders(final ArrayList<OrderPojo> OrdersShow) {
        final resetAdapterObjectOfOrders adapter = new resetAdapterObjectOfOrders( OrdersShow,getActivity());
        listOfOrders.setAdapter(adapter);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listOfOrders,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(final ListView listView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {

                                    final CustomDialogClassEdit Dialog = new CustomDialogClassEdit(getActivity());
                                    Dialog.setCanceledOnTouchOutside(false);
                                    Dialog.show();

                                    Dialog.dialogTime.setVisibility(View.GONE);
                                    Dialog.dialogDate.setText("Are You sure you want to delete this Order ?");


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

                                            Services.deleteOrder(OrdersShow.get(position).getOrderId(),
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

                                                                    OrdersShow.remove(position);
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
        listOfOrders.setOnTouchListener(touchListener);

    }


}
