package com.example.waterflower124.googlemap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class getNearbyPlaceData extends AsyncTask<Object, String, String> {

    String url;
    private String googlePlacesData;

    private  List<HashMap<String, String>> nearbyPlaceList;

    private ListView mListView;

    private Context context;

    DataDownloadListener dataDownloadListener;

//    public getNearbyPlaceData(Context context) {
//        this.context = context;
//    }

    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        url = (String)objects[0];
        mListView = (ListView)objects[1];
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

        NearbyDataParser parser = new NearbyDataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearby places data","called parse method");

        dataDownloadListener.dataDownloadedSuccessfully(nearbyPlaceList);


//        showNearbyPlaces(nearbyPlaceList);
    }

//    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
//        Toast.makeText(context, String.valueOf(nearbyPlaceList.size()), Toast.LENGTH_LONG).show();
//        CustomListAdapter customListAdapter = new CustomListAdapter(context);
//        mListView.setAdapter(customListAdapter);
//        for(int i = 0; i < nearbyPlaceList.size(); i ++) {
//            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
//            String placeName = googlePlace.get("place_name");
//            String vicinity = googlePlace.get("vicinity");
//            String place_id = googlePlace.get("place_id");
//            String latitude = googlePlace.get("lat");
//            String longitude = googlePlace.get("lng");
////            String photo = googlePlace.get("photo");
//            customListAdapter.list.add(new ListViewItem(placeName, vicinity, place_id, latitude, longitude));
//            customListAdapter.notifyDataSetChanged();
//        }
//
//    }

    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(List<HashMap<String, String>> data);
        void dataDownloadFailed();
    }
}
