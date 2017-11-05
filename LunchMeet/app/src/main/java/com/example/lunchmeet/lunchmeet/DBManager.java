package com.example.lunchmeet.lunchmeet;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Brian Kwak on 11/4/2017.
 */

public class DBManager{
    private static final DBManager instance = new DBManager();
    private DatabaseReference database;
    private DBUserNotifier userNotifier;



    private DBManager(){
        database = FirebaseDatabase.getInstance().getReference();
        userNotifier = new DBUserNotifier();
    }

    public static DBManager getInstance(){
        return instance;
    }

    public void updateUser(DBUser u){
        database.child("users").child(u.getUid()).setValue(u.toMap());
    }

    public void updateUser(String uid, double lat, double lng){
        DatabaseReference ref = database.child("users").child(uid);
        ref.child("lat").setValue(lat);
        ref.child("lng").setValue(lng);
    }

    public void removeUser(DBUser u){
        database.child("users").child(u.getUid()).removeValue();
    }

    public void removeUser(String uid){
        database.child("users").child(uid).removeValue();
    }

    public void addUserObserver(DBObserver o){
        userNotifier.attach(o);
    }
    public void removeUserObserver(DBObserver o){
        userNotifier.detach(o);
    }

}
