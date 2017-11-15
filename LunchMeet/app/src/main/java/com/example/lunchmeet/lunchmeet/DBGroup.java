package com.example.lunchmeet.lunchmeet;

/**
 * Created by Brian on 11/5/2017.
 */

/**
 * A class designed to collect data pertaining to a group to be used by the database manager
 * and facilitate interactions with the database.
 *
 * @author Brian Kwak
 */
public class DBGroup {
    private String gid;
    private String leader;
    private int size;
    private double lat;
    private double lng;

    /**
     * Gets the group ID.
     * @return The group ID.
     */
    public String getGid(){
        return gid;
    }

    /**
     * Gets the user ID of the group's leader.
     * @return The user ID of the group's leader.
     */
    public String getLeader(){
        return leader;
    }

    /**
     * Gets the size of the group.
     * @return The size of the group.
     */
    public int getSize(){
        return size;
    }

    /**
     * Gets the latitude of the group.
     * @return The latitude of the group.
     */
    public double getLat(){
        return lat;
    }

    /**
     * Gets the longitude of the group.
     * @return The longitude of the group.
     */
    public double getLng(){
        return lng;
    }

    /**
     * Sets the group ID.
     * @param gid The ID that the group will be given.
     */
    public void setGid(String gid){
        this.gid = gid;
    }
}
