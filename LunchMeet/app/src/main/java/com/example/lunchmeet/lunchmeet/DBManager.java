package com.example.lunchmeet.lunchmeet;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    void updateUser(DBUser u){
        database.child("users").child(u.getUid()).setValue(u.toMap());
    }

    void updateUser(String uid, double lat, double lng){
        DatabaseReference ref = database.child("users").child(uid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
    }

    void getUsers(DBUserObserver o){
        final DBUserObserver obs = o;
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener(){
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

    void removeUser(DBUser u){
        database.child("users").child(u.getUid()).removeValue();
    }
    void removeUser(String uid){
        database.child("users").child(uid).removeValue();
    }

    void createGroup(String uid){
        DatabaseReference ref = database.child("groups").push();
        String key = ref.getKey();
        ref.child(uid).setValue(true);

        database.child("users").child(uid).child("group").setValue(key);
    }

}
