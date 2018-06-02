package com.example.waterflower124.googlemap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.net.URI;


public class ListViewItem {
    public URI pictureuri ;
    public String placeName, vicinty ;
    public String place_id;
    public String latitude, longitude;
    public String rating, review, distance;
    String open_now, phonenumber;

    public ListViewItem(String placename, String vicinty, String place_id, String lat, String lng,
                        String rating, String review, String distance, String open_now, String phonenumber) {
//        this.pictureDrawable = picture;
        this.placeName = placename;
        this.vicinty = vicinty;
        this.place_id = place_id;
        this.latitude = lat;
        this.longitude = lng;
        this.rating = rating;
        this.review = review;
        this.distance = distance;
        this.open_now = open_now;
        this.phonenumber = phonenumber;
//        this.pictureuri = uri;
    }


    public String getPlace_ID() {
        return this.place_id ;
    }
    public String getPlaceNmae() {
        return this.placeName ;
    }
    public String getVicinty() {
        return this.vicinty ;
    }
}
