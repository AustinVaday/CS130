package com.example.lunchmeet.lunchmeet;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brian Kwak on 11/4/2017.
 */

/**
 * The sole point of communication between the rest of the app and Firebase's realtime database.
 *
 * @author Brian Kwak
 */
public class DBManager{

    private static final DBManager instance = new DBManager();
    private DatabaseReference database;
    private String TAG = "DBManager";

    private ChildEventListener requestListener;
    private ChildEventListener messageListener;

    private DBManager(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * The getter method to return the single instance of the class, as per the Singleton pattern.
     * The instance is automatically created via eager instantiation.
     * Returns the single instance of the DBManager class.
     *
     * @return The single instance of the DBManager class.
     */
    public static DBManager getInstance(){
        return instance;
    }

    /**
     * Adds a user to the database.
     *
     * @param u The user to be added to the database.
     */
    public void addUser(DBUser u){
        database.child("users").child(u.getUid()).setValue(u.toMap());
    }

    /**
     * Removes a user from the database.
     *
     * @param u The user to be removed from the database.
     */
    public void removeUser(DBUser u){
        removeUser(u.getUid());
    }

    /**
     * Removes a user from the database, using the UID only.
     *
     * @param uid The UID of the user to be removed from the database.
     */
    public void removeUser(String uid){
        database.child("users").child(uid).removeValue();
    }

    /**
     * Retrieves user information corresponding to the given UID from the database.
     * Database information is read asynchronously, so a listener is required
     * to call the proper methods after the read is sucessful.
     *
     * In this case, the user information is collected as a DBUser which is sent to the
     * provided DBListener, which can then run based off that DBUser.
     *
     * @param o The DBListener that will process the user information read from the database.
     * @param id The user ID of the user whose information should be read from the database.
     */
    public void getUserFromUid(DBListener<DBUser> o, String id){
        final DBListener obs = o;
        final String uid = id;
        database.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBUser u = dataSnapshot.getValue(DBUser.class);
                u.setUid(uid);
                obs.run(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUserFromUid:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Updates an active user's location in the database, as well as their name and photo URL.
     *
     * @param u The user whose location should be updated.
     * @param lat The latitude of the user.
     * @param lng The longitude of the user.
     */
    public void updateActiveUser(DBUser u, double lat, double lng){
        DatabaseReference ref = database.child("active").child(u.getUid());
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
        ref.child("photoUrl").setValue(u.getPhotoUrl());
        ref.child("name").setValue(u.getName());
    }

    /**
     * Updates an active user's location in the database based on the user's UID.
     *
     * @param uid The ID of the user whose location should be updated.
     * @param lat The latitude of the user.
     * @param lng The longitude of the user.
     */
    public void updateActiveUser(String uid, double lat, double lng){
        DatabaseReference ref = database.child("active").child(uid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
    }

    /**
     * Removes a user from the active users section of the database. To be used when a user goes
     * inactive (e.g. closes the app).
     *
     * @param uid The ID of the user to be removed.
     */
    public void removeActiveUser(String uid){
        database.child("active").child(uid).removeValue();
    }

    /**
     * Attaches a listener to the database to read data about active users from the database.
     * When data about active users is read, the data will be presented as a List of DBActives,
     * which can then be processed by the provided DBListener.
     *
     * This method needs to be called only once, and the DBListener will run its run() method
     * every time the list of active users updates.
     *
     * @param o The DBListener that will process the active user information from the database.
     */
    public void attachListenerForActiveUsers(DBListener<List<DBActive>> o){
        final DBListener obs = o;
        database.child("active").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DBActive> result = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    DBActive active = child.getValue(DBActive.class);
                    active.setUid(child.getKey());

                    result.add(active);
                }
                obs.run(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "attachListenerForActiveUsers:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Using the ID of a group, updates the group with the ID of the leader in the database.
     *
     * @param gid The ID of the group to be updated.
     * @param uid The ID of the leader to be updated.
     */
    public void updateGroupLeader(String gid, String uid){
        database.child("groups").child(gid).child("leader").setValue(uid);
    }

    /**
     * Updates the location of a group in the database.
     *
     * @param gid The ID of the group to be updated.
     * @param lat The latitutde of the group.
     * @param lng The longitude of the group.
     */
    public void updateGroupLocation(String gid, double lat, double lng){
        DatabaseReference ref = database.child("groups").child(gid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);

    }

    /**
     * Updates the size of a group in the database.
     *
     * @param gid The ID of the group to be updated.
     * @param size The size of the group.
     */
    public void updateGroupSize(String gid, int size){
        database.child("groups").child(gid).child("size").setValue(size);
    }

    /**
     * Removes a group from the database.
     *
     * @param gid The ID of the group to be removed.
     */
    public void removeGroup(String gid){
        database.child("groups").child(gid).removeValue();
    }

    /**
     * Attaches a listener to the database to read data about groups from the database.
     * When data about groups is read, the data will be presented as a List of DBGroups,
     * which can then be processed by the provided DBListener.
     *
     * This method needs to be called only once, and the DBListener will run its run() method
     * every time the list of active users updates.
     *
     * @param o The DBListener that will process the group information from the database.
     */
    public void attachListenerForGroups(DBListener<List<DBGroup>> o){
        final DBListener obs = o;
        database.child("groups").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DBGroup> result = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    DBGroup group = child.getValue(DBGroup.class);
                    group.setGid(child.getKey());

                    result.add(group);
                }
                obs.run(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "attachListenerForGroups:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Adds an entry in the database pertaining to a new group.
     *
     * @param uid The ID of the leader that is creating the group. Every group requires a leader.
     * @return The ID of the group that has just been created.
     */
    public String createGroup(String uid){
        DatabaseReference ref = database.child("members").push();
        String key = ref.getKey();
        ref.child(uid).setValue(true);

        updateGroupLeader(key,uid);
        updateGroupSize(key,1);

        database.child("active").child(uid).child("gid").setValue(key);

        return key;
    }

    /**
     * Adds a new member to the list of members in a group.
     * Also updates the size of the group with the given size.
     *
     * @param gid The ID of the group being joined.
     * @param uid The ID of the user that is joining the group.
     * @param size The current size of the group.
     */
    public void joinGroup(String gid, String uid, int size){
        database.child("members").child(gid).child(uid).setValue(true);
        database.child("groups").child(gid).child("size").setValue(size + 1);
        database.child("active").child(uid).child("gid").setValue(gid);
    }

    /**
     * Retrieves a list of members in the group corresponding to the given group ID.
     * When data about the members is read, the data will be presented as a List of
     * Strings (user IDs) that will then be passed to the DBListener to process.
     *
     * @param o The DBListener that will process the member information from the database.
     * @param gid The ID of the group whose members are being retrieved.
     */
    public void getMembers(DBListener<List<String>> o, String gid){
        final DBListener obs = o;
        database.child("members").child(gid).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> result = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    result.add(child.getKey());
                }
                obs.run(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getMembers:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Removes a member from the list of members of a group.
     *
     * @param gid The ID of the group that is being left.
     * @param uid The ID of the user that is leaving.
     * @param size The current size of the group.
     */
    public void leaveGroup(String gid, String uid, int size){
        database.child("members").child(gid).child(uid).removeValue();
        database.child("groups").child(gid).child("size").setValue(size - 1);

        database.child("active").child(uid).child("gid").setValue(null);
    }

    /**
     * Dissolves a group, removing all information about it from the database.
     *
     * It will also automatically add information to the database for users within the group to
     * learn that they were within a group that was just dissolved.
     *
     * @param gid The ID of the group being dissolved.
     * @param uid The ID of the user doing the dissolving.
     */
    public void dissolveGroup(String gid, String uid){
        final String groupID = gid;
        final String userID = uid;
        getMembers(new DBListener<List<String>>() {
            @Override
            public void run(List<String> param) {
                for(String id : param){
                    database.child("dissolve").child(id).setValue(true);
                    database.child("active").child(id).child("gid").setValue(null);
                }

                database.child("groups").child(groupID).removeValue();
                database.child("members").child(groupID).removeValue();
                database.child("requests").child(groupID).removeValue();
                database.child("messages").child(groupID).removeValue();

                if (requestListener != null) {
                    database.removeEventListener(requestListener);
                    requestListener = null;
                }
                if(messageListener != null){
                    database.removeEventListener(messageListener);
                    messageListener = null;
                }
            }
        }, gid);
    }

    /**
     * Invites a user to a group. Specifically, puts down a group ID under a list for
     * the given user.
     *
     * @param gid The ID of the group that the user is being invited to.
     * @param uid The ID of the user being invited.
     */
    public void inviteUser(String gid, String uid){
        database.child("invites").child(uid).child(gid).setValue(true);
    }

    /**
     * Attaches a listener to a user ID for invites to join a group. When called, it will run
     * individually on every ID in the list, then will run on every new ID as it is added (without
     * touching the older ones.)
     *
     * @param o The DBListener that processes a group ID as it is added to the invite list.
     * @param uid The ID of the current user, listening for invites.
     */
    public void waitForInvites(DBListener<String> o, String uid){
        final DBListener obs = o;
        database.child("invites").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String gid = dataSnapshot.getKey();
                obs.run(gid);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "waitForInvites:onCancelled", databaseError.toException());

            }
        });
    }

    /**
     * Removes an invite from the database, regardless of whether the invite is accepted
     * or rejected.
     *
     * @param gid The ID of the group whose invite is being handled.
     * @param uid The ID of the user with the invite.
     */
    public void handleInvite(String gid, String uid){
        database.child("invites").child(uid).child(gid).removeValue();
    }

    /**
     * Sends a request to the leader of the group for the user to join the group. Similar to
     * invites.
     *
     * @param gid The ID of the group that is being requested to join.
     * @param uid The ID of the user doing the requesting.
     */
    public void requestToJoinGroup(String gid, String uid){
        database.child("requests").child(gid).child(uid).setValue(true);
    }

    /**
     * Attaches a listener to a group ID for requests to join the group. When called, it will run
     * individually on every ID in the list, then will run on every new ID as it is added (without
     * touching the older ones.)
     *
     * @param o The DBListener that processes a user ID as it is added to the request list.
     * @param gid The ID of the group, whose leader is listening for requests.
     */
    public void waitForRequests(DBListener<String> o, String gid){
        final DBListener obs = o;

        requestListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getKey();
                obs.run(uid);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "waitForRequests:onCancelled", databaseError.toException());

            }
        };

        database.child("requests").child(gid).addChildEventListener(requestListener);
    }

    /**
     * Removes a request from the database, regardless of whether the request was accepted or
     * rejected.
     *
     * @param gid The ID of the group that handling the request.
     * @param uid The ID of the user whose request is being handled.
     */
    public void handleJoinRequest(String gid, String uid){
        database.child("requests").child(gid).child(uid).removeValue();
    }

    /**
     * Attaches a listener to a "dissolve group" database section to inform members of a group
     * when the group gets dissolved.
     *
     * When a group is dissolved, an entry containing the user's ID is created, which is caught by
     * the listener formed in this method. The behavior of the DBListener passed into the method
     * can then be called, and then the user's ID will be removed from the database section.
     *
     * This method should be called at the very beginning, probably.
     * @param o The DBListener that runs as a result of a group being dissolved.
     * @param uid The ID of the user.
     */
    public void listenForDissolveGroup(DBListener<Object> o, String uid){
        final DBListener<Object> obs = o;
        final String userID = uid;
        database.child("dissolve").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                obs.run(null);
                database.child("dissolve").child(userID).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "listenForDissolveGroup:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Adds a message to the database to the chat corresponding to the given group ID.
     *
     * @param gid The ID of the group whose chat is being messaged.
     * @param message The content of the message.
     */
    public void sendMessage(String gid, DBMessage message){
        DatabaseReference ref = database.child("messages").child(gid).push();
        ref.setValue(message.toMap());
    }

    /**
     * Attaches a listener for new messages to the chatroom. In the beginning, it reads all the
     * existing messages at once, then handles each new message separately as they arrive.
     *
     * @param gid The ID of the group whose chat is being read.
     * @param o The DBListener to handle each message that is read.
     */
    public void attachListenerForNewMessages(String gid, DBListener<DBMessage> o){
        final DBListener<DBMessage> obs = o;
        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DBMessage result = dataSnapshot.getValue(DBMessage.class);
                //Log.i(TAG,"" + dataSnapshot);
                obs.run(result);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.child("messages").child(gid).addChildEventListener(messageListener);
    }
}
