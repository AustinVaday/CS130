package com.example.lunchmeet.lunchmeet;

import android.graphics.Bitmap;

/**
 * A class to keep track of user information, such as their name, gender, and profile picture as taken from Facebook.
 * Keeps track of the user's current state and allows or disallows certain actions based on the state. Provides
 * getters and setters to manipulate user information.
 */
public class User {

    private String name;
    private String uid;
    private Bitmap profile_pic;
    private String url;
    private double lat;
    private double lon;
    private String gid;

    /**
     * Default constructor to initialize user information.
     * @param name user's name
     * @param profile_pic user's profile picture URL from Facebook

     */
    public User(String name, Bitmap profile_pic, float lat, float lon, String uid,String bmp_url,String gid){

        this.uid=uid;
        this.url=bmp_url;
        this.gid=gid;
        this.name=name;
        this.profile_pic=profile_pic;
        this.lat=lat;
        this.lon=lon;

    }

    /**
     * Gets the user's name.
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's current state according to the parameter
     * @param s state to transition to
     */


    public void setgid(String s){
        this.gid=s;
    }
    public String getGid(){
        return gid;
    }

    /**
     * Gets the user's current state.
     * @param s string version of user's current state (eg if user is in member state, s = "member")
     * @return instantiation of the state class the user is in
     */

    /**
     * creates a group by calling state.create_group()
     * if the state == Free_state it will create a group
     * in any other state it will do nothing.
     */


    public String geturl(){
        return url;
    }
    /**
     * If the user is in freeagent state
     * then the user cann join to a group

    /*
     * @return the group object which the user is in.
     * */

    public String getuid(){
        return uid;
    }
    public Bitmap get_bmp(){return profile_pic; }

    public void set_bmp(Bitmap bmp){this.profile_pic=bmp; }


    /**
     * @return the current state of the user
     */


    /**
     * Sets the user's location to the latitude and longitude provided in the parameters.
     * @param latitude user's new latitude
     * @param longitude user's new longitude
     */
    public void setCoordinates(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }

    /**
     * Gets the user's current latitude
     * @return user's latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the user's current longitude
     * @return user's longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Interface defining all possible user actions in a state. A user can join a group, create a group,
     * start a chat with group members, leave a group, invite a user, or dissolve a group. What actions they
     * can take depends on what state they are in.
     */

}
