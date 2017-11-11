package com.example.lunchmeet.lunchmeet;

import android.util.Log;

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

    void updateUserLocation(String uid, double lat, double lng){
        DatabaseReference ref = database.child("active").child(uid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
    }

    void updateUserLocation(DBUser u, double lat, double lng){
        updateUserLocation(u.getUid(),lat,lng);
    }

    void getActiveUsers(DBObserver<List<DBUser>> o){
        final DBObserver obs = o;
        database.child("active").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DBUser> result = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String uid = child.getKey();
                    DBUser user = child.getValue(DBUser.class);
                    user.setUid(uid);

                    result.add(user);
                }
                obs.run(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUsers:onCancelled", databaseError.toException());
            }
        });
    }

    void getUserFromUid(DBObserver<DBUser> o, String uid){
        final DBObserver obs = o;
        database.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getKey();
                DBUser user = dataSnapshot.getValue(DBUser.class);
                user.setUid(uid);

                obs.run(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUserFromUid:onCancelled", databaseError.toException());
            }
        });
    }

    void removeUser(DBUser u){
        database.child("users").child(u.getUid()).removeValue();
    }
    void removeUser(String uid){
        database.child("users").child(uid).removeValue();
    }

    String createGroup(String uid){
        DatabaseReference ref = database.child("groups").push();
        String key = ref.getKey();
        ref.child(uid).setValue(true);

        database.child("users").child(uid).child("group").setValue(key);
        return key;
    }

    void getGroups(DBObserver<Map<String,Map<String,Boolean>>> o){
        final DBObserver obs = o;
        database.child("groups").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String,Boolean>> result = new HashMap<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String gid = child.getKey();
                    Map<String,Boolean> userList = new HashMap<>();
                    for(DataSnapshot user : child.getChildren()){
                        userList.put(user.getKey(),(Boolean)user.getValue());
                    }

                    result.put(gid, userList);
                }
                obs.run(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUsersInGroup:onCancelled", databaseError.toException());
            }
        });
    }

    public void joinGroup(String gid, String uid) {
        database.child("users").child(uid).child("group").setValue(gid);
        database.child("groups").child(gid).child(uid).setValue(false);
    }
}
