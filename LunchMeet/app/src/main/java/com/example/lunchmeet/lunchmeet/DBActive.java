package com.example.lunchmeet.lunchmeet;

/**
 * Created by Brian on 11/11/2017.
 */

public class DBActive {
    private String uid;
    private double lat;
    private double lng;

    public String getUid(){
        return uid;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
}
