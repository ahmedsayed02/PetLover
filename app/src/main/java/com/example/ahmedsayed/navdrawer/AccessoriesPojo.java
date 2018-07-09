package com.example.ahmedsayed.navdrawer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed Sayed on 14-Apr-18.
 */

public class AccessoriesPojo implements Parcelable {
    int ID;

    protected AccessoriesPojo(Parcel in) {
        ID = in.readInt();
        Owner = in.readInt();
        type = in.readInt();
        NameOfAccessories = in.readString();
        if (in.readByte() == 0) {
            Price = null;
        } else {
            Price = in.readDouble();
        }
        ImageOfAccessories = in.readString();
        StoreId = in.readInt();
        quantity = in.readInt();
        storeName = in.readString();
        storeAddress = in.readString();
        storePhone = in.readString();
        openAt = in.readString();
        closeAt = in.readString();
    }

    public static final Creator<AccessoriesPojo> CREATOR = new Creator<AccessoriesPojo>() {
        @Override
        public AccessoriesPojo createFromParcel(Parcel in) {
            return new AccessoriesPojo(in);
        }

        @Override
        public AccessoriesPojo[] newArray(int size) {
            return new AccessoriesPojo[size];
        }
    };

    public int getType() {
        return type;
    }

    int type;

    int Owner;

    public int getOwner() {
        return Owner;
    }

    public int getID() {
        return ID;
    }

    public String getNameOfAccessories() {
        return NameOfAccessories;
    }

    public Double getPrice() {
        return Price;
    }

    public String getImageOfAccessories() {
        return ImageOfAccessories;
    }

    public int getStoreId() {
        return StoreId;
    }

    String NameOfAccessories;
    Double Price;
    String ImageOfAccessories;
    int StoreId;

    public int getQuantity() {
        return quantity;
    }

    int quantity;

    public AccessoriesPojo(int ID, int type, String nameOfAccessories, Double price, String imageOfAccessories, int storeId, String storeName, String storeAddress, String storePhone, String openAt, String closeAt, int Quantity, int Owner) {
        this.ID = ID;
        this.type = type;
        NameOfAccessories = nameOfAccessories;
        Price = price;
        ImageOfAccessories = imageOfAccessories;
        StoreId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.quantity = Quantity;
        this.Owner = Owner;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public String getOpenAt() {
        return openAt;
    }

    public String getCloseAt() {
        return closeAt;
    }

    String storeName, storeAddress, storePhone, openAt, closeAt;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeInt(Owner);
        parcel.writeInt(type);
        parcel.writeString(NameOfAccessories);
        if (Price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(Price);
        }
        parcel.writeString(ImageOfAccessories);
        parcel.writeInt(StoreId);
        parcel.writeInt(quantity);
        parcel.writeString(storeName);
        parcel.writeString(storeAddress);
        parcel.writeString(storePhone);
        parcel.writeString(openAt);
        parcel.writeString(closeAt);
    }
}
