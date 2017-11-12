package com.example.lunchmeet.lunchmeet;

/**
 * Created by Brian on 11/5/2017.
 */

public class DBGroup {
    private String gid;
    private String leader;
    private int size;
    private double lat;
    private double lng;

    public String getGid(){
        return gid;
    }
    public String getLeader(){
        return leader;
    }
    public int getSize(){
        return size;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public void setGid(String gid){
        this.gid = gid;
    }
}
