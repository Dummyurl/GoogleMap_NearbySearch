package com.example.waterflower124.googlemap;

import java.util.HashMap;
import java.util.List;

public class Globals {

    public List<HashMap<String, String>> nearbyPlaceList;

    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }


    private Globals() {

    }


    public List<HashMap<String, String>> getNearbyValue() {
        return nearbyPlaceList;
    }


    public void setNearbyValue(List<HashMap<String, String>> nearbyPlaceList) {
        this.nearbyPlaceList = nearbyPlaceList;
    }


}
