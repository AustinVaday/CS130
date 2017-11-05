package com.example.lunchmeet.lunchmeet;

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

public class DBUserNotifier extends DBNotifier {

    public DBUserNotifier(){
        super();
    }

    public void setListener(){
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DBObserver o : observers){
                    o.notify(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }


        });
    }
}
