package com.example.ahmedsayed.navdrawer;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////


        constant.sharedPreferences = getSharedPreferences(constant.PREFERENCES, MODE_PRIVATE);

        if (constant.sharedPreferences.getInt(constant.Type, -1) == 1) {
            // Pet owner
            navigationView.inflateMenu(R.menu.activity_main_drawer);

            MyPetFragment f1 = new MyPetFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f1)
                    .commit();

        } else if (constant.sharedPreferences.getInt(constant.Type, -1) == 2) {
            // doctor
            navigationView.inflateMenu(R.menu.doctor);

            Services.getAllClinicsForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");

                                if (success == 1) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("clinics");
                                    ClinicsPojo p2 = null;
                                    for (int i = 0; i < jsonArray.length(); ++i) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        p2 = new ClinicsPojo(obj.getInt("doctorID"),
                                                obj.getInt("id"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                obj.getInt("phone"), obj.getInt("city"),
                                                obj.getString("openAt"),
                                                obj.getString("closeAt"),
                                                obj.getString("doctorName"));
                                    }

                                    Bundle bundle2 = new Bundle();
                                    bundle2.putParcelable("clinic", p2);
                                    UpdateClinicInfo ClinicInfo = new UpdateClinicInfo();
                                    ClinicInfo.setArguments(bundle2);

                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, ClinicInfo)
                                            .addToBackStack(null).commit();

                                } else if (msg.equals("Invalid token.")) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();

                                } else {
                                    AddMyClinic f5 = new AddMyClinic();
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, f5)
                                            .commit();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void Error(Exception ex) {

                        }
                    }, MainActivity.this);

        } else {
            // store owner
            navigationView.inflateMenu(R.menu.store_owner);

            Services.getAllStoresForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");

                                if (success == 1) {
                                    ArrayList<StorePojo> p12 = new ArrayList<>();
                                    JSONArray jsonArray = jsonObject.getJSONArray("stores");
                                    for (int i = 0; i < jsonArray.length(); ++i) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        p12.add(new StorePojo(obj.getInt("id"),
                                                obj.getInt("storeOwnerId"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                obj.getInt("phone"),
                                                obj.getString("openAt"),
                                                obj.getString("closeAt")));

                                        //GO TO Text Viewer for Info. of Store and 3 btns edit w View products , Delete

                                    }

                                    Bundle bundle233 = new Bundle();
                                    bundle233.putParcelable("store", p12.get(0));
                                    ViewOfStoreInfo v1 = new ViewOfStoreInfo(); // fragment
                                    v1.setArguments(bundle233);
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, v1)
                                            .commit();
                                } else if (msg.equals("Invalid token.")) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();

                                } else {

                                    MyStoreFragment f7 = new MyStoreFragment();
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, f7)
                                            .commit();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void Error(Exception ex) {

                        }
                    }, MainActivity.this);

        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String token = FirebaseInstanceId.getInstance().getToken();
                while (token == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
//                Log.d("HASSAN", "doInBackground: " + token);


                return token;
            }

            @Override
            protected void onPostExecute(String token) {

                Services.setServerToken(constant.sharedPreferences.getInt(constant.ID, -1),
                        constant.sharedPreferences.getString(constant.Token, null),
                        token, new RequestCallback() {
                            @Override
                            public void Success(String response) {
//                                Log.d("HASSAN", "Success: " + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    String msg = jsonObject.getString("message");

//                                    Log.d("HASSAN", "doInBackground: token Added");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void Error(Exception ex) {

                            }
                        }, MainActivity.this);

            }
        }.execute();

    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else

        super.onBackPressed();
        return;



    }







        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.profile_id) {

            MyProfile f0 = new MyProfile();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f0)
                    .commit();

        } else if (id == R.id.mypet_id) {

            MyPetFragment f1 = new MyPetFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f1)
                    .commit();


        } else if (id == R.id.contact_id) {

            ContactUs f2 = new ContactUs();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f2)
                    .commit();

        } else if (id == R.id.logout_id) {

            logoutMethod();

        } else if (id == R.id.clinic_id) {
            ClinicsFragment f3 = new ClinicsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f3)
                    .commit();

        } else if (id == R.id.accessories_id) {
            Accessories f4 = new Accessories();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f4)
                    .commit();

        } else if (id == R.id.add_clinics_id) {


            Services.getAllClinicsForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");

                                if (success == 1) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("clinics");
                                    ClinicsPojo p2 = null;
                                    for (int i = 0; i < jsonArray.length(); ++i) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        p2 = new ClinicsPojo(obj.getInt("doctorID"),
                                                obj.getInt("id"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                obj.getInt("phone"), obj.getInt("city"),
                                                obj.getString("openAt"),
                                                obj.getString("closeAt"),
                                                obj.getString("doctorName"));
                                    }

                                    Bundle bundle2 = new Bundle();
                                    bundle2.putParcelable("clinic", p2);
                                    UpdateClinicInfo ClinicInfo = new UpdateClinicInfo();
                                    ClinicInfo.setArguments(bundle2);

                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_id, ClinicInfo)
                                            .addToBackStack(null).commit();

                                } else if (msg.equals("Invalid token.")) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();

                                } else {
                                    AddMyClinic f5 = new AddMyClinic();
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, f5)
                                            .commit();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void Error(Exception ex) {

                        }
                    }, MainActivity.this);

        } else if (id == R.id.my_store_id) {

            Services.getAllStoresForSpecificOwner(constant.sharedPreferences.getInt(constant.ID, -1),
                    constant.sharedPreferences.getString(constant.Token, null),
                    new RequestCallback() {
                        @Override
                        public void Success(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String msg = jsonObject.getString("message");

                                if (success == 1) {
                                    ArrayList<StorePojo> p12 = new ArrayList<>();
                                    JSONArray jsonArray = jsonObject.getJSONArray("stores");
                                    for (int i = 0; i < jsonArray.length(); ++i) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        constant.editor.putInt("storeID", obj.getInt("id"));
                                        constant.editor.apply();
                                        p12.add(new StorePojo(obj.getInt("id"),
                                                obj.getInt("storeOwnerId"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                obj.getInt("phone"),
                                                obj.getString("openAt"),
                                                obj.getString("closeAt")));

                                        //GO TO Text Viewer for Info. of Store and 3 btns edit w View products , Delete

                                    }

                                    Bundle bundle233 = new Bundle();
                                    bundle233.putParcelable("store", p12.get(0));
                                    ViewOfStoreInfo v1 = new ViewOfStoreInfo(); // fragment
                                    v1.setArguments(bundle233);
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, v1)
                                            .commit();
                                } else if (msg.equals("Invalid token.")) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();

                                } else {

                                    MyStoreFragment f7 = new MyStoreFragment();
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_id, f7)
                                            .commit();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void Error(Exception ex) {

                        }
                    }, MainActivity.this);

        } else if (id == R.id.appointment_id) {

            MyAppointmentFragment f7 = new MyAppointmentFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f7)
                    .commit();
        } else if (id == R.id.order_id) {

            OrderFragment f8 = new OrderFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_id, f8)
                    .commit();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutMethod() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure ? ")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        constant.editor.putBoolean(constant.isLoggedIn, false);
                        constant.editor.apply();
                        Log.d("HASSAN", "onClick: " + constant.sharedPreferences.getBoolean(constant.isLoggedIn, true));
                        constant.editor.apply();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                {
                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                        Log.d("HASSAN", "doInBackground: logOut");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                //call your activity where you want to land after log out
                                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                            }
                        }.execute();

                    }
                }).setNegativeButton("Cancel", null)
                .show();
    }

}
