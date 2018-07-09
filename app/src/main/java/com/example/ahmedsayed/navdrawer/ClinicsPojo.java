package com.example.ahmedsayed.navdrawer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed Sayed on 13-Apr-18.
 */

public class ClinicsPojo implements Parcelable {

    int IdOfDoctor;
    int IdOfClinics;
    String NameOfClinic;
    String AddressOfClinic;
    int Phone;
    int city;
    String OpenAt;
    String CloseAt;
    String doctorName;

    public ClinicsPojo(int idOfDoctor, int idOfClinics, String nameOfClinic, String addressOfClinic, int phone, int city, String openAt, String closeAt, String doctorName) {
        IdOfDoctor = idOfDoctor;
        IdOfClinics = idOfClinics;
        NameOfClinic = nameOfClinic;
        AddressOfClinic = addressOfClinic;
        Phone = phone;
        this.city = city;
        OpenAt = openAt;
        CloseAt = closeAt;
        this.doctorName = doctorName;
    }

    protected ClinicsPojo(Parcel in) {
        IdOfDoctor = in.readInt();
        IdOfClinics = in.readInt();
        NameOfClinic = in.readString();
        AddressOfClinic = in.readString();
        Phone = in.readInt();
        city = in.readInt();
        OpenAt = in.readString();
        CloseAt = in.readString();
        doctorName = in.readString();
    }

    public static final Creator<ClinicsPojo> CREATOR = new Creator<ClinicsPojo>() {
        @Override
        public ClinicsPojo createFromParcel(Parcel in) {
            return new ClinicsPojo(in);
        }

        @Override
        public ClinicsPojo[] newArray(int size) {
            return new ClinicsPojo[size];
        }
    };

    public int getIdOfDoctor() {
        return IdOfDoctor;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getIdOfClinics() {
        return IdOfClinics;
    }


    public int getPhone() {
        return Phone;
    }

    public String getOpenAt() {
        return OpenAt;
    }

    public String getCloseAt() {
        return CloseAt;
    }

    public String getNameOfClinic() {
        return NameOfClinic;
    }

    public String getAddressOfClinic() {
        return AddressOfClinic;
    }

    public int getCity() {
        return city;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IdOfDoctor);
        parcel.writeInt(IdOfClinics);
        parcel.writeString(NameOfClinic);
        parcel.writeString(AddressOfClinic);
        parcel.writeInt(Phone);
        parcel.writeInt(city);
        parcel.writeString(OpenAt);
        parcel.writeString(CloseAt);
        parcel.writeString(doctorName);
    }
}
