package com.example.ahmedsayed.navdrawer;

public class AppointmentPojo {

    private String from;

    public AppointmentPojo(String from, String day, String nameOfUser, int ID, int docId, int clinicId, int userId, int confirmation) {
        this.from = from;
        this.day = day;
        this.nameOfUser = nameOfUser;
        this.ID = ID;
        this.docId = docId;
        this.clinicId = clinicId;
        this.userId = userId;
        Confirmation = confirmation;
    }

    private String day;

    public String getFrom() {
        return from;
    }

    public String getDay() {
        return day;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public int getID() {
        return ID;
    }

    public int getDocId() {
        return docId;
    }

    public int getClinicId() {
        return clinicId;
    }

    public int getUserId() {
        return userId;
    }

    public int getConfirmation() {
        return Confirmation;
    }

    private String nameOfUser;
    private int ID, docId, clinicId, userId, Confirmation;




}
