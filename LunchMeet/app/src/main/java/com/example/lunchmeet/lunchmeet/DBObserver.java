package com.example.lunchmeet.lunchmeet;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Brian Kwak on 11/4/2017.
 */

public interface DBObserver {
    void notify(DataSnapshot dataSnapshot);
}
