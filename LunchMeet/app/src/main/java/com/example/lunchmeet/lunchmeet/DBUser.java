package com.example.lunchmeet.lunchmeet;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian Kwak on 11/4/2017.
 */

public class DBUser {
    private String uid;
    private String name;
    private String photoUrl;
    private double lat;
    private double lng;

    public DBUser(){}
    public DBUser(String uid, String name, String photoUrl, double lat, double lng){
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
        this.lat = lat;
        this.lng = lng;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("photoUrl",photoUrl);
        map.put("lat",lat);
        map.put("lng",lng);
        return map;
    }

    public String getUid(){
        return uid;
    }

    public String getName(){
        return name;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }
}
