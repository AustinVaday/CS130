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

public abstract class DBNotifier {
    protected DatabaseReference database;
    protected List<DBObserver> observers;

    public DBNotifier(){
        observers = new ArrayList<DBObserver>();
        database = FirebaseDatabase.getInstance().getReference();
        setListener();
    }

    public void attach(DBObserver o){
        observers.add(o);
    }

    public void detach(DBObserver o){
        observers.remove(o);
    }

    public abstract void setListener();


}