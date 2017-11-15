package com.example.lunchmeet.lunchmeet;

import java.util.List;

/**
 * Created by Brian on 11/6/2017.
 */

/**
 * DBListener is an interface made to process data returned asynchronously from the database.
 * When you call various data-retrieving methods from DBManager, you will want to implement
 * a new DBListener with a run() method that will process the data once it is read.
 *
 * @param <T> The type of the object that is to be read from the database and processed by the
 *           listener.
 * @author Brian Kwak
 */
public interface DBListener<T> {

    /**
     * This method will be automatically called by DBManager, with the data read from the database
     * being passed into it. This method should be implemented with the actions that should be
     * carried out upon the data that has just been read.
     *
     * @param param The data that has been read from the database.
     */
    public void run(T param);
}
