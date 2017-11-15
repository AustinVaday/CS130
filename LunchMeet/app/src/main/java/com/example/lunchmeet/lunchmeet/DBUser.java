package com.example.lunchmeet.lunchmeet;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian Kwak on 11/4/2017.
 */
/**
 * A class designed to collect data pertaining to a user to be used by the database manager
 * and facilitate interactions with the database.
 *
 * @author Brian Kwak
 */
public class DBUser {
    private String uid;
    private String name;
    private String photoUrl;

    /**
     * Default constructor.
     */
    public DBUser(){}

    /**
     * Parameterized constructor.
     * @param uid The user ID to be assigned to this user.
     * @param name The name to be assigned to this user.
     * @param photoUrl The photo URL to be assigned to this user.
     */
    DBUser(String uid, String name, String photoUrl){
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    /**
     * Converts the user information to a map, which can be used to neatly input information
     * into the Firebase database.
     *
     * @return The map containing all the information of the user.
     */
    Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("photoUrl",photoUrl);
        return map;
    }

    /**
     * Gets the UID of the user.
     * @return The UID of the user.
     */
    String getUid(){
        return uid;
    }

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    String getName(){
        return name;
    }

    /**
     * Gets the photo URL of the user.
     * @return The photo URL of the user.
     */
    String getPhotoUrl(){
        return photoUrl;
    }

    /**
     * Sets the UID of the user.
     * @param uid The UID that the user will be given.
     */
    void setUid(String uid){
        this.uid = uid;
    }

    /**
     * Sets the name of the user.
     * @param name The name that the user will be given.
     */
    void setName(String name){
        this.name = name;
    }

    /**
     * Sets the photo URL of the user.
     * @param photoUrl The photo URL that the user will be given.
     */
    void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}
