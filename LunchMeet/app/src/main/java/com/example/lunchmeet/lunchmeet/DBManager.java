package com.example.lunchmeet.lunchmeet;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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

public class DBManager{
    private static final DBManager instance = new DBManager();
    private DatabaseReference database;
    private String TAG = "DBManager";


    private DBManager(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    static DBManager getInstance(){
        return instance;
    }

    void addUser(DBUser u){
        database.child("users").child(u.getUid()).setValue(u.toMap());
    }
    void addUser(String uid, String name, String photoUrl){
        DatabaseReference ref = database.child("users").child(uid);
        ref.child("name").setValue(name);
        ref.child("photoUrl").setValue(photoUrl);
    }
    void removeUser(DBUser u){
        removeUser(u.getUid());
    }
    void removeUser(String uid){
        database.child("users").child(uid).removeValue();
    }
    void getUserFromUid(DBListener<DBUser> o, String id){
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

    void updateActiveUser(DBUser u, double lat, double lng){
        updateActiveUser(u.getUid(),lat,lng);
    }
    void updateActiveUser(String uid, double lat, double lng){
        DatabaseReference ref = database.child("active").child(uid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
    }
    void removeActiveUser(String uid){
        database.child("active").child(uid).removeValue();
    }
    void attachListenerForActiveUsers(DBListener<List<DBActive>> o){
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

    void updateGroupLeader(String gid, String uid){
        database.child("groups").child(gid).child("leader").setValue(uid);
    }
    void updateGroupLocation(String gid, double lat, double lng){
        DatabaseReference ref = database.child("groups").child(gid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);

    }
    void updateGroupSize(String gid, int size){
        database.child("groups").child(gid).child("size").setValue(size);
    }
    void removeGroup(String gid){
        database.child("groups").child(gid).removeValue();
    }
    void attachListenerForGroups(DBListener<List<DBGroup>> o){
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

    String createGroup(String uid){
        DatabaseReference ref = database.child("members").push();
        String key = ref.getKey();
        ref.child(uid).setValue(true);

        return key;
    }
    void joinGroup(String gid, String uid, int size){
        database.child("members").child(gid).child(uid).setValue(true);
        database.child("groups").child(gid).child("size").setValue(size + 1);
    }
    void getMembers(DBListener<List<String>> o, String gid){
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
                Log.w(TAG, "attachListenerForMembers:onCancelled", databaseError.toException());
            }
        });
    }
}
