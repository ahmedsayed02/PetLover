package com.example.ahmedsayed.navdrawer;


import android.content.Context;

import java.util.HashMap;

import static com.example.ahmedsayed.navdrawer.constant.serverToken;

public class Services {

    public static final String url = "https://hassanhamdyriad.000webhostapp.com/php_files/";
//public static final String url = "http://192.168.1.8:8080/petLover/";

    public static void login(String email, String password, RequestCallback requestCallback,
                             Context context) {

        HashMap<String,String> values = new HashMap<>();
        values.put("email",email);
        values.put("password",password);

        BaseRequest.DoPost(values,requestCallback, url +  "login.php",context);
    }

    public static void registration(String name, String email, String password, String number,
                                    int type, String address, RequestCallback requestCallback,
                                    Context context) {

        HashMap<String,String> values = new HashMap<>();
        values.put("name",name);
        values.put("email",email);
        values.put("password",password);
        values.put("number",number);
        values.put("type", String.valueOf(type));
        values.put("address",address);
        values.put("isUpdate", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "registration.php",context);
    }

    public static void updateProfile(String name, String email, String password, String number,
                                    int type, String address, Boolean bool, String encoded_string,
                                     int id, String token, RequestCallback requestCallback,
                                    Context context) {

        HashMap<String,String> values = new HashMap<>();
        values.put("name",name);
        values.put("email",email);
        values.put("password",password);
        values.put("number",number);
        values.put("type", String.valueOf(type));
        values.put("address",address);
        values.put("isUpdate", String.valueOf(2));
        values.put("boolean", String.valueOf(bool));
        values.put("encoded_string",encoded_string);
        values.put("id", String.valueOf(id));
        values.put("token",token);


        BaseRequest.DoPost(values,requestCallback, url +  "registration.php",context);
    }

    public static void addPet(int id, String token, String name, int gender, String type,
                              String birthday, String notes, String color, String encoded_string,
                              String bath, String hair, String nails, String teeth, String ears,
                              String internalDeworming, RequestCallback requestCallback,
                              Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("gender", String.valueOf(gender));
        values.put("type", type);
        values.put("birthday", birthday);
        values.put("notes", notes);
        values.put("color", color);
        values.put("bath", bath);
        values.put("hair", hair);
        values.put("nails", nails);
        values.put("teeth", teeth);
        values.put("ears", ears);
        values.put("internalDeworming", internalDeworming);
        values.put("encoded_string", encoded_string);
        values.put("isUpdate", String.valueOf(1));
        BaseRequest.DoPost(values,requestCallback, url +  "addPet.php",context);
    }

    public static void updatePet(int id, String token, String name, int gender, String type,
                              String birthday, String notes, String color, String encoded_string,
                                 String bath, String hair, String nails, String teeth, String ears,
                                 String internalDeworming, int petId,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("gender", String.valueOf(gender));
        values.put("type", type);
        values.put("birthday", birthday);
        values.put("notes", notes);
        values.put("color", color);
        values.put("bath", bath);
        values.put("hair", hair);
        values.put("nails", nails);
        values.put("teeth", teeth);
        values.put("ears", ears);
        values.put("internalDeworming", internalDeworming);
        values.put("encoded_string", encoded_string);
        values.put("isUpdate", String.valueOf(2));
        values.put("petId", String.valueOf(petId));
        BaseRequest.DoPost(values,requestCallback, url +  "addPet.php",context);
    }

    public static void getAllPets(int id, String token,
                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(3));

        BaseRequest.DoPost(values,requestCallback, url +  "getPet.php",context);

    }

    public static void getAllPetsForSpecificOwner(int id, String token,
                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "getPet.php",context);

    }

    public static void getPet(int id, String token, int petId,
                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(2));
        values.put("petId",String.valueOf(petId));

        BaseRequest.DoPost(values,requestCallback, url +  "getPet.php",context);

    }

    public static void deletePet(int id, String token, int petId,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("petId",String.valueOf(petId));

        BaseRequest.DoPost(values,requestCallback, url +  "deletePet.php",context);

    }

    public static void addStore(int id, String token, String name, String address, String phone,
                                 String openAt, String closeAt,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("openAt",openAt);
        values.put("closeAt",closeAt);
        values.put("isUpdate", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "addStore.php",context);

    }

    public static void updateStore(int id, String token, String name, String address, String phone,
                                 String openAt, String closeAt, int storeId,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("openAt",openAt);
        values.put("closeAt",closeAt);
        values.put("isUpdate", String.valueOf(2));
        values.put("storeId", String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "addStore.php",context);

    }

    public static void getAllStores(int id, String token,
                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(3));

        BaseRequest.DoPost(values,requestCallback, url +  "getStore.php",context);

    }

    public static void getAllStoresForSpecificOwner(int id, String token,
                                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "getStore.php",context);

    }

    public static void getStore(int id, String token, int storeId,
                              RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(2));
        values.put("storeId",String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "getStore.php",context);

    }

    public static void deleteStore(int id, String token, int storeId,
                                 RequestCallback requestCallback, Context context){
        //TODO: check for error print msg say "this store have accessories please delete acc first

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("storeId",String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "deleteStore.php",context);

    }


    public static void addClinic(int id, String token, String name, String address, String phone,
                                 String openAt, String closeAt, int city,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("openAt",openAt);
        values.put("closeAt",closeAt);
        values.put("city", String.valueOf(city));
        values.put("isUpdate", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "addClinics.php",context);

    }

    public static void updateClinic(int id, String token, String name, String address, String phone,
                                    String openAt, String closeAt, int clinicId, int city,
                                    RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("openAt",openAt);
        values.put("closeAt",closeAt);
        values.put("isUpdate", String.valueOf(2));
        values.put("clinicId", String.valueOf(clinicId));
        values.put("city", String.valueOf(city));

        BaseRequest.DoPost(values,requestCallback, url +  "addClinics.php",context);

    }

    public static void getAllClinics(int id, String token,
                                     RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(3));

        BaseRequest.DoPost(values,requestCallback, url +  "getClinic.php",context);

    }

    public static void getAllClinicsForSpecificOwner(int id, String token,
                                                     RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "getClinic.php",context);

    }

    public static void getClinic(int id, String token, int clinicId,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(2));
        values.put("clinicId",String.valueOf(clinicId));

        BaseRequest.DoPost(values,requestCallback, url +  "getClinic.php",context);

    }

    public static void deleteClinic(int id, String token, int clinicId,
                                    RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("clinicId",String.valueOf(clinicId));

        BaseRequest.DoPost(values,requestCallback, url +  "deleteClinic.php",context);

    }

    public static void addAccessories(int storeId, String token, String name, String price,
                                      String encoded_String, int type, int quantity,
                                      RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("storeId", String.valueOf(storeId));
        values.put("token",token);
        values.put("name",name);
        values.put("price",price);
        values.put("encoded_string",encoded_String);
        values.put("isUpdate", String.valueOf(1));
        values.put("type", String.valueOf(type));
        values.put("quantity", String.valueOf(quantity));

        BaseRequest.DoPost(values,requestCallback, url +  "addAccessories.php",context);

    }

    public static void updateAccessories(int storeId, String token, String name, String price,
                                      String encoded_String, int accessoriesId, int type, int quantity,
                                      RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("storeId", String.valueOf(storeId));
        values.put("token",token);
        values.put("name",name);
        values.put("price",price);
        values.put("encoded_string",encoded_String);
        values.put("isUpdate", String.valueOf(2));
        values.put("accessoriesId", String.valueOf(accessoriesId));
        values.put("type", String.valueOf(type));
        values.put("quantity", String.valueOf(quantity));

        BaseRequest.DoPost(values,requestCallback, url +  "addAccessories.php",context);

    }

    public static void getAllAccessories(int id, String token, int storeId,
                                     RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(3));
        values.put("store", String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "getAccessories.php",context);

    }

    public static void getAllAccessoriesForSpecificOwner(int id, String token, int storeId,
                                                     RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(1));
        values.put("store", String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "getAccessories.php",context);

    }

    public static void getAccessory(int id, String token, int accessoryId, int storeId,
                                 RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(2));
        values.put("accessoriesId",String.valueOf(accessoryId));
        values.put("store", String.valueOf(storeId));

        BaseRequest.DoPost(values,requestCallback, url +  "getAccessories.php",context);

    }

    public static void deleteAccessory(int id, String token, int accessoryId,
                                    RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("accessoriesId",String.valueOf(accessoryId));

        BaseRequest.DoPost(values,requestCallback, url +  "deleteAccessories.php",context);

    }

    public static void sendMessage(int id, String title, String message,
                                   RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("title",title);
        values.put("message", message);

        BaseRequest.DoPost(values,requestCallback, url +  "sendSinglePush.php",context);
    }

    public static void setServerToken(int id, String token, String serverToken,
                                      RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("server_token", serverToken);

        BaseRequest.DoPost(values,requestCallback, url +  "saveServerToken.php",context);

    }

    public static void addAppointment(String token, int doctorId, int userId, int clinicId,
                                      String time, String date, RequestCallback requestCallback,
                                      Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("token",token);
        values.put("doctorId", String.valueOf(doctorId));
        values.put("userId", String.valueOf(userId));
        values.put("clinicId", String.valueOf(clinicId));
        values.put("fromTime", time);
        values.put("dateTime", date);
        values.put("isUpdate", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "addAppointment.php",context);
    }

    public static void updateAppointment(String token, int doctorId, int userId, int clinicId,
                                      String time, String date, int appointmentId, int status,
                                      RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("token",token);
        values.put("doctorId", String.valueOf(doctorId));
        values.put("userId", String.valueOf(userId));
        values.put("clinicId", String.valueOf(clinicId));
        values.put("fromTime", time);
        values.put("dateTime", date);
        values.put("isUpdate", String.valueOf(2));
        values.put("appointmentId", String.valueOf(appointmentId));
        values.put("status", String.valueOf(status));

        BaseRequest.DoPost(values,requestCallback, url +  "addAppointment.php",context);
    }

    public static void deleteAppointment(int id, String token, int appointmentId,
                                       RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(id));
        values.put("token",token);
        values.put("appointmentId",String.valueOf(appointmentId));

        BaseRequest.DoPost(values,requestCallback, url +  "deleteAppointment.php",context);

    }

    public static void getAllAppointmentForDoctor(int id, String token,
                                                         RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("doctorId", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "getAppointment.php",context);

    }

    public static void getAllAppointmentForUser(int id, String token,
                                                  RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("userId", String.valueOf(id));
        values.put("token",token);
        values.put("isGetAll", String.valueOf(2));

        BaseRequest.DoPost(values,requestCallback, url +  "getAppointment.php",context);

    }


    public static void addOrder(int product, int store,
                                RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("userId", String.valueOf(constant.sharedPreferences.getInt(constant.ID, -1)));
        values.put("token",constant.sharedPreferences.getString(constant.Token,null));
        values.put("productId", String.valueOf(product));
        values.put("storeId", String.valueOf(store));

        BaseRequest.DoPost(values,requestCallback, url +  "addOrder.php",context);

    }

    public static void deleteOrder(int order, RequestCallback requestCallback, Context context){

        HashMap<String,String> values = new HashMap<>();
        values.put("id", String.valueOf(constant.sharedPreferences.getInt(constant.ID, -1)));
        values.put("token",constant.sharedPreferences.getString(constant.Token,null));
        values.put("orderId", String.valueOf(order));

        BaseRequest.DoPost(values,requestCallback, url +  "deleteOrder.php",context);

    }

    public static void getOrdersForUser(RequestCallback requestCallback, Context context){
        HashMap<String,String> values = new HashMap<>();
        values.put("userId", String.valueOf(constant.sharedPreferences.getInt(constant.ID, -1)));
        values.put("token",constant.sharedPreferences.getString(constant.Token,null));
        values.put("isGetAll", String.valueOf(2));

        BaseRequest.DoPost(values,requestCallback, url +  "getOrder.php",context);
    }

    public static void getOrdersForStore(int store, RequestCallback requestCallback, Context context){
        HashMap<String,String> values = new HashMap<>();
        values.put("storeId", String.valueOf(store));
        values.put("token",constant.sharedPreferences.getString(constant.Token,null));
        values.put("isGetAll", String.valueOf(1));

        BaseRequest.DoPost(values,requestCallback, url +  "getOrder.php",context);
    }

}
