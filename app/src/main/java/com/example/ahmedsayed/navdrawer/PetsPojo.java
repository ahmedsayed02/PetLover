package com.example.ahmedsayed.navdrawer;


import android.os.Parcel;
import android.os.Parcelable;


public class PetsPojo implements Parcelable {

    int ID;
    String NameOfpet;
    int Gender;
    String Type;
    String BirthDay;
    String Notes;
    String bath;
    String hair;
    String nails;

    protected PetsPojo(Parcel in) {
        ID = in.readInt();
        NameOfpet = in.readString();
        Gender = in.readInt();
        Type = in.readString();
        BirthDay = in.readString();
        Notes = in.readString();
        bath = in.readString();
        hair = in.readString();
        nails = in.readString();
        teeth = in.readString();
        ears = in.readString();
        internalDeworming = in.readString();
        ImageOfPet = in.readString();
        OwnerID = in.readInt();
        Color = in.readString();
    }

    public static final Creator<PetsPojo> CREATOR = new Creator<PetsPojo>() {
        @Override
        public PetsPojo createFromParcel(Parcel in) {
            return new PetsPojo(in);
        }

        @Override
        public PetsPojo[] newArray(int size) {
            return new PetsPojo[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getNameOfpet() {
        return NameOfpet;
    }

    public int getGender() {
        return Gender;
    }

    public String getType() {
        return Type;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public String getNotes() {
        return Notes;
    }

    public String getBath() {
        return bath;
    }

    public String getHair() {
        return hair;
    }

    public String getNails() {
        return nails;
    }

    public String getTeeth() {
        return teeth;
    }

    public String getEars() {
        return ears;
    }

    public String getInternalDeworming() {
        return internalDeworming;
    }

    public String getImageOfPet() {
        return ImageOfPet;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public String getColor() {
        return Color;
    }

    String teeth;
    String ears;

    public PetsPojo(int ID, String nameOfpet, int gender, String type, String birthDay, String notes, String bath, String hair, String nails, String teeth, String ears, String internalDeworming, String imageOfPet, int ownerID, String color) {
        this.ID = ID;
        NameOfpet = nameOfpet;
        Gender = gender;
        Type = type;
        BirthDay = birthDay;
        Notes = notes;
        this.bath = bath;
        this.hair = hair;
        this.nails = nails;
        this.teeth = teeth;
        this.ears = ears;
        this.internalDeworming = internalDeworming;
        ImageOfPet = imageOfPet;
        OwnerID = ownerID;
        Color = color;
    }

    String internalDeworming;

    String ImageOfPet;
    int OwnerID;
    String Color;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(NameOfpet);
        parcel.writeInt(Gender);
        parcel.writeString(Type);
        parcel.writeString(BirthDay);
        parcel.writeString(Notes);
        parcel.writeString(bath);
        parcel.writeString(hair);
        parcel.writeString(nails);
        parcel.writeString(teeth);
        parcel.writeString(ears);
        parcel.writeString(internalDeworming);
        parcel.writeString(ImageOfPet);
        parcel.writeInt(OwnerID);
        parcel.writeString(Color);
    }
}
