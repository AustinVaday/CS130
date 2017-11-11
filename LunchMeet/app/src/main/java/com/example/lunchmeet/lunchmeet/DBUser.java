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
    private String group;

    public DBUser(){}
    DBUser(String uid, String name, String photoUrl, double lat, double lng, String group){
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
        this.lat = lat;
        this.lng = lng;
        this.group = null;
    }

    Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("photoUrl",photoUrl);
        map.put("lat",lat);
        map.put("lng",lng);
        map.put("group",group);
        return map;
    }

    String getUid(){
        return uid;
    }

    String getName(){
        return name;
    }

    String getPhotoUrl(){
        return photoUrl;
    }

    double getLat(){
        return lat;
    }

    double getLng(){
        return lng;
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    void setLocation(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
}
