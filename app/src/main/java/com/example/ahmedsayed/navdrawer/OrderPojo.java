package com.example.ahmedsayed.navdrawer;

public class OrderPojo  {
    String Name;

    public OrderPojo(String name, String mobile, String product, int orderId, String address) {
        Name = name;
        Mobile = mobile;
        Product = product;
        OrderId = orderId;
        Address = address;
    }

    String Mobile;
    String Product ;

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getProduct() {
        return Product;
    }

    public int getOrderId() {
        return OrderId;
    }


    public String getAddress() {
        return Address;
    }

    int OrderId;
    String Address;

}
