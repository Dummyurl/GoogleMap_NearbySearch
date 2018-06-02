package com.example.waterflower124.googlemap;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDetailPlaceData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    String req;

    HashMap<String, String> placeDetailData;

    DataDownloadListener dataDownloadListener;

    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }

    @Override
    protected String doInBackground(Object... objects){
//        mMap = (GoogleMap)objects[0];
        req = ((String)objects[0]).trim();
        if(req.equals("DATA"))
            url = (String)objects[1];
        else if(req.equals("MAP")) {
            mMap = (GoogleMap) objects[1];
            url = (String)objects[2];
        }

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        DetailDataParser parser = new DetailDataParser();
        placeDetailData = parser.parse(s);
        Log.d("nearby places data","called parse method");

        if(req.equals("DATA"))
            dataDownloadListener.dataDownloadedSuccessfully(placeDetailData);
    }


    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(HashMap<String, String> data);
        void dataDownloadFailed();
    }
}
