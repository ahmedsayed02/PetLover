package com.example.ahmedsayed.navdrawer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed Sayed on 28-Apr-18.
 */

public  class StorePojo implements Parcelable {

    int IdOfStore;
    int IdOfOwner;
    String NameOfStore;
    int PhoneOfStore;
    String OpenAt1;
    String CloseAt1;
    String AddressOfStore;

    public StorePojo(int idOfStore, int idOfOwner, String nameOfStore, String addressOfStore, int phoneOfStore, String openAt1, String closeAt1) {
        IdOfStore = idOfStore;
        IdOfOwner = idOfOwner;
        NameOfStore = nameOfStore;
        AddressOfStore = addressOfStore;
        PhoneOfStore = phoneOfStore;
        OpenAt1 = openAt1;
        CloseAt1 = closeAt1;
    }

    protected StorePojo(Parcel in) {
        IdOfStore = in.readInt();
        IdOfOwner = in.readInt();
        NameOfStore = in.readString();
        AddressOfStore = in.readString();
        PhoneOfStore = in.readInt();
        OpenAt1 = in.readString();
        CloseAt1 = in.readString();
    }

    public static final Creator<StorePojo> CREATOR = new Creator<StorePojo>() {
        @Override
        public StorePojo createFromParcel(Parcel in) {
            return new StorePojo(in);
        }

        @Override
        public StorePojo[] newArray(int size) {
            return new StorePojo[size];
        }
    };
    public int getIdOfStore() {
        return IdOfStore;
    }

    public int getIdOfOwner() {
        return IdOfOwner;
    }

    public String getNameOfStore() {
        return NameOfStore;
    }

    public String getAddressOfStore() {
        return AddressOfStore;
    }

    public int getPhoneOfStore() {
        return PhoneOfStore;
    }

    public String getOpenAt1() {
        return OpenAt1;
    }

    public String getCloseAt1() {
        return CloseAt1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IdOfStore);
        parcel.writeInt(IdOfOwner);
        parcel.writeString(NameOfStore);
        parcel.writeString(AddressOfStore);
        parcel.writeInt(PhoneOfStore);
        parcel.writeString(OpenAt1);
        parcel.writeString(CloseAt1);
    }


}
