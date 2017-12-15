package com.example.lunchmeet.lunchmeet;

/**
 * A class to keep track of formed lunch group information, such as the members, capacity, and current size
 * of a group. Includes methods to add and remove members from a group, as well as getters and setters
 * for the group information.
 */

public class Group {
    private User[] members;
    private String leaderId;
    private int capacity;
    private int curr_size;
    private double lat,lon;

    /**
     * Default constructor that creates a group of maximum 6 members.
     * @param user the creator of the group
     */
    public Group(User user){
        members = new User[6]; // default capacity of 6 group members
        members[0] = user;
        leaderId = user.getuid();
        capacity = 6;
        curr_size = 1;
        lat = user.getLat();
        lon = user.getLon();
    }

    /**
     * Parametrized constructor that creates a group with capacity set by the group creator.
     * @param user the creator of the group
     * @param c the max capacity of the group
     */
    public Group(User user, int c) {
        members = new User[c]; // capacity set by parameter c
        members[0] = user;
        leaderId = user.getuid();
        capacity = c;
        curr_size = 1;
        lat = user.getLat();
        lon = user.getLon();
    }

    /**
     * Adds a user to the group, if there is space.
     * @param user the user to add to the group
     * @return true if the user was added, false if the user was not (user in another group or group reached max capacity)
     */
    public boolean add_user(User user){
        if (curr_size >= capacity)
            return false;

        members[curr_size] = user;
        curr_size++;
        return true;
    }

    /**
     * Removes a user from the group.
     * @param user the user to be removed
     * @return true if the user was removed, false if the user was not.
     */
    public boolean remove_user(User user){
        for(int i = 0; i < curr_size; i++){
            if(members[i] == user){
                members[i] = null;
                curr_size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Disbands a group by setting all members to the free agent state.
     * @return true if the group was successfully disbanded, false otherwise.
     */


    /**
     * Gets the members of the group.
     * @return array of Users who are members of the group
     */
    public User[] getGroupMembers() {
        return members;
    }

    /**
     * Gets the group's maximum capacity.
     * @return group's maximum capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the group's max capacity to the parameter c.
     * @param c new max capacity
     */
    public void setCapacity (int c) {
        capacity = c;
    }

    /**
     * Gets the group's current size.
     * @return number of members currently in the group
     */
    public int getCurr_size() {
        return curr_size;
    }

    /**
     * Sets the current size of the group.
     * @param size the new size of the group
     */
    public void setSize(int size) {
        curr_size = size;
    }

    /**
     * Sets the group's location to the latitude and longitude specified by the parameters.
     * @param latitude new group location latitude
     * @param longitude new group location longitude
     */
    public void setGroupLoc(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }

    /**
     * Gets the group's current latitude.
     * @return group's latitude
     */
    public double getGroupLat() {
        return lat;
    }

    /**
     * Gets the group's current longitude.
     * @return group's longitude
     */
    public double getGroupLong() {
        return lon;
    }

    /**
     * Gets the group leader's user id.
     * @return group leader's user id
     */
    public String getLeader() {
        return leaderId;
    }

    /**
     * Sets the group leader's user id to the passed in parameter.
     * @param lID user id of the user to be the group leader
     */
    public void setLeader(String lID) {
        leaderId = lID;
    }

}
