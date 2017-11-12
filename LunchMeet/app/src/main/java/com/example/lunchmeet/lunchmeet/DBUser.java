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

    public DBUser(){}
    DBUser(String uid, String name, String photoUrl){
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("photoUrl",photoUrl);
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

    void setUid(String uid){
        this.uid = uid;
    }
    void setName(String name){
        this.name = name;
    }
    void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}
