package com.example.waterflower124.googlemap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_GoogleMap extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    String place_id, lat, lng, placename, vicinty, phone_number;
    public static final int REQUEST_LOCATION_CODE = 99;

    private GoogleApiClient client;
    double latitude, longitude;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    private LocationRequest locationRequest;
    MarkerOptions markerOptions;

    public Fragment_GoogleMap() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_googlemap, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        place_id = bundle.getString("place_id");
        lat = bundle.getString("lat");
        lng = bundle.getString("lng");
        if (!place_id.isEmpty()) {
            placename = bundle.getString("placename");
            vicinty = bundle.getString("vicinty");
//            phone_number = bundle.getString("phone_number");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(true);

        if(place_id.isEmpty()) {
            LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            map.addMarker(new MarkerOptions().position(currentPosition).title("Current Location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {

            LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            map.addMarker(new MarkerOptions().position(currentPosition).title(placename).snippet(vicinty));
            map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));

        }

    }


    private String getUrl(String place_id)
    {
        map.clear();
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlaceUrl.append("placeid=" + place_id.trim());
        googlePlaceUrl.append("&key=" + "AIzaSyAq8yrm7ELoaDB3GED86yWfn3Y-TdTEgPc");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();

    }





}
