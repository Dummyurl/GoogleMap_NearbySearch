package com.example.waterflower124.googlemap;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailDataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String photo = "";
        String place_id = "";
        String formatted_phone_number = "ddddd";
        String rating = "";
        int review = 0;
        String opening_status = "";
        String international_phone_number = "international";

        Log.d("DataParser","jsonobject =" + googlePlaceJson.toString());

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("place_id")) {
                place_id = googlePlaceJson.getString("place_id");
            }
            if(!googlePlaceJson.isNull("formatted_phone_number")) {
                formatted_phone_number = googlePlaceJson.getString("formatted_phone_number");
            } else formatted_phone_number = "";
            if(!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating");
            } else rating = "";
            if(!googlePlaceJson.isNull("reviews")) {
                review = googlePlaceJson.getJSONArray("reviews").length();
            } else review = 0;
            if(!googlePlaceJson.isNull("opening_hours")) {
                Boolean open_now = googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now");
                if(open_now) opening_status = "Work Time";
                else opening_status = "No Work Time";
            } else opening_status =  "N/A";

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("place_id", place_id);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("review", String.valueOf(review));
            googlePlaceMap.put("open_now", opening_status);
            googlePlaceMap.put("formatted_phone_number", formatted_phone_number);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;

    }

    public HashMap<String, String> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject, jsonDetail = null;

        Log.d("json data", jsonData);


        try {
            jsonObject = new JSONObject(jsonData);
            jsonDetail = jsonObject.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlace(jsonDetail);
    }

}
