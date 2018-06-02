package com.example.waterflower124.googlemap;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterflower124.googlemap.getNearbyPlaceData.DataDownloadListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FusedLocationProviderClient client;
    double latitude, longitude;
    int PROXIMITY_RADIUS = 10000;

    ListView mListView;
    boolean loc_permission = true;

    List<HashMap<String, String>> nearbyPlaceList;

    Fragment_GoogleMap fragment_googleMap;
    CustomListAdapter customListAdapter;

    BroadcastReceiver mBroadcastReceiver;

    CustomLoader loader;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.listview);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //// display current location in google map  ////////////
        getLocation();

        //delay waiting for receive location from google service: delay is 2s
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Bundle bundle = new Bundle();
                bundle.putString("place_id", "");
                bundle.putString("lat", String.valueOf(latitude));
                bundle.putString("lng", String.valueOf(longitude));
                fragment_googleMap = new Fragment_GoogleMap();
                fragment_googleMap.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_contents, fragment_googleMap);
                fragmentTransaction.commit();

            }
        }, 1000);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String place_id = intent.getStringExtra("place_id");
                String latti = intent.getStringExtra("lat");
                String longi = intent.getStringExtra("lng");
                String placename = intent.getStringExtra("placename");
                String vicinty = intent.getStringExtra("vicinty");
                String phone_number = intent.getStringExtra("phone_number");

                Intent intent1 = new Intent(getApplicationContext(), ListActivity.class);
                intent1.putExtra("place_id", place_id);
                startActivity(intent1);


//                Bundle bundle = new Bundle();
//                bundle.putString("place_id", place_id);
//                bundle.putString("lat", latti);
//                bundle.putString("lng", longi);
//                bundle.putString("current_lat", String.valueOf(latitude));
//                bundle.putString("current_lng", String.valueOf(longitude));
//                bundle.putString("placename", placename);
//                bundle.putString("vicinty", vicinty);
//                bundle.putString("phone_number", phone_number);
//
//                fragment_googleMap = new Fragment_GoogleMap();
//                fragment_googleMap.setArguments(bundle);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.main_contents, fragment_googleMap);
//                fragmentTransaction.commit();

            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter("Detail Map"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void displayGoogleMap(String place_id, String lat, String lng) {

        Bundle bundle = new Bundle();
        bundle.putString("place_id", place_id);
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);

        fragment_googleMap = new Fragment_GoogleMap();
        fragment_googleMap.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_contents, fragment_googleMap);
        fragmentTransaction.commit();

    }
//    @Override
//    public void onBackPressed() {
//
//        new AlertDialog.Builder(this)
//                .setTitle("Warning")
//                .setIcon(R.drawable.ic_warning)
//                .setMessage("Do you want really to exit?")
//                .setNegativeButton("No", null)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                    }
//                }).create().show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //////////////////////////      when click category DOCTOR, HOSPITAL, PHARMACY              /////////////////////////
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final MenuItem menuItem = item;

        if(fragment_googleMap != null)
            getSupportFragmentManager().beginTransaction().remove(fragment_googleMap).commit();

        //Get information for current location;
        getLocation();

        //delay for 1000ms for response for current place latitude and longitude
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            // Handle navigation view item clicks here.
            int id = menuItem.getItemId();
            public void run() {
                if (id == R.id.hospital) {
                    if(loc_permission) {
                        MainActivity.this.showCategoryList("hospital");
                    }

                } else if (id == R.id.doctor) {
                    if(loc_permission) {
                        MainActivity.this.showCategoryList("doctor");
                    }
                } else if (id == R.id.pharmacy) {
                    if(loc_permission) {
                        MainActivity.this.showCategoryList("pharmacy");
                    }

                } else if (id == R.id.profile) {
                    Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                } else if(id == R.id.bloodbank) {
//                    MainActivity.this.showCategoryList("blood bank");
                }
            }
        }, 1000);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showCategoryList(String category) {

        //Progress Bar Starting
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        if(category.equals("hospital"))
            setTitle("Hospital");
        else if(category.equals("doctor"))
            setTitle("Doctor");
        else if(category.equals("pharmacy"))
            setTitle("Pharmacy");

        getNearbyPlaceData nearByPlaceData = new getNearbyPlaceData();
        nearByPlaceData.setDataDownloadListener(new DataDownloadListener()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void dataDownloadedSuccessfully(List<HashMap<String, String>> nearbyPlaceList) {
//                                MainActivity.this.print(aa);

                customListAdapter = new CustomListAdapter(getApplicationContext());
                mListView.setAdapter(customListAdapter);

                for(int i = 0; i < nearbyPlaceList.size(); i ++) {
                    HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                    String place_id = googlePlace.get("place_id");

                    GetDetailPlaceData mgetDetailPlaceData= new GetDetailPlaceData();
                    mgetDetailPlaceData.setDataDownloadListener(new GetDetailPlaceData.DataDownloadListener() {

                        @Override
                        public void dataDownloadedSuccessfully(HashMap<String, String> detailPlaceData) {
                            String placeName = detailPlaceData.get("place_name");
                            String vicinity = detailPlaceData.get("vicinity");
                            String place_id = detailPlaceData.get("place_id");
                            String target_latitude = detailPlaceData.get("lat");
                            String target_longitude = detailPlaceData.get("lng");
                            String rating = detailPlaceData.get("rating");
                            String review = detailPlaceData.get("review");
                            String open_now = detailPlaceData.get("open_now");
                            String phone_number = detailPlaceData.get("formatted_phone_number");
                            String distance;
                            float[] float_distance = new float[1];
                            Location.distanceBetween(latitude, longitude, Double.parseDouble(target_latitude), Double.parseDouble(target_longitude), float_distance);
                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            distance = String.valueOf(Double.valueOf(twoDForm.format((Double)(float_distance[0] / 1000.0))));

                            customListAdapter.list.add(new ListViewItem(placeName, vicinity, place_id, target_latitude, target_longitude, rating,
                                    review, distance, open_now, phone_number));
                            customListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void dataDownloadFailed() {

                        }
                    });
                    Object dataTransfer[] = new Object[2];
                    String urlDetail = getUrl(place_id);
                    dataTransfer[0] = "DATA";
                    dataTransfer[1] = urlDetail;

                    mgetDetailPlaceData.execute(dataTransfer);
                }
                //Progress Bar Stop
                if (loader.isShowing())
                    loader.dismiss(); //progressbar stop
            }

            @Override
            public void dataDownloadFailed() {
                // handler failure (e.g network not available etc.)
            }
        });
        if(category.equals("blood bank"))
            category = "health";
        String urlNearBy = getUrl(latitude, longitude, category);
        nearByPlaceData.execute(urlNearBy, mListView);

    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //Get Latitude and Longitude for current place
    private void getLocation() {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            loc_permission = false;

            Toast.makeText(this, "PERMISSION disable", Toast.LENGTH_LONG).show();
        } else loc_permission = true;

        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!= null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
//                    Toast.makeText(MainActivity.this, "|||" + location.toString() + "|||", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyAq8yrm7ELoaDB3GED86yWfn3Y-TdTEgPc");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private String getUrl(String place_id)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlaceUrl.append("placeid=" + place_id.trim());
        googlePlaceUrl.append("&key=" + "AIzaSyAq8yrm7ELoaDB3GED86yWfn3Y-TdTEgPc");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }


}
