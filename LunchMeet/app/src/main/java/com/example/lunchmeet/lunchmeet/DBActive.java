package com.example.lunchmeet.lunchmeet;

/**
 * Created by Brian on 11/11/2017.
 */
/**
 * A class designed to collect data pertaining to an active user to be used by the database manager
 * and facilitate interactions with the database.
 *
 * @author Brian Kwak
 */
public class DBActive {
    private String uid;
    private double lat;
    private double lng;


    /**
     * Gets the UID of the user.
     * @return The UID of the user.
     */
    public String getUid(){
        return uid;
    }

    /**
     * Gets the latitude of the user.
     * @return The latitude of the user.
     */
    public double getLat(){
        return lat;
    }

    /**
     * Gets the longitude of the user.
     * @return The longitude of the user.
     */
    public double getLng(){
        return lng;
    }

    /**
     * Sets the UID of the user.
     * @param uid The UID that the user will be given.
     */
    public void setUid(String uid){
        this.uid = uid;
    }
}
