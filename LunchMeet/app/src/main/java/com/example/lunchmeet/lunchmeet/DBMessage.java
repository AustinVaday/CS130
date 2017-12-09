package com.example.lunchmeet.lunchmeet;

import java.util.HashMap;
import java.util.Map;

/**
 * A class designed to collect data pertaining to a single message to be used by the database manager
 * and facilitate interactions with the database.
 *
 * @author Brian Kwak
 */
public class DBMessage {
    String uid;
    String message;

    /**
     * No-argument constructor. Initializes all values to null.
     */
    public DBMessage(){}

    /**
     * The parametrized constructor. Sets the fields uid and message.
     * @param uid The uid of the user writing the message.
     * @param message The message itself.
     */
    public DBMessage(String uid, String message){
        this.uid = uid;
        this.message = message;
    }

    /**
     * Returns the ID of the user who wrote the message.
     * @return The user ID.
     */
    public String getUid(){
        return uid;
    }

    /**
     * Returns the message that was written.
     * @return The message.
     */
    public String getMessage(){
        return message;
    }

    /**
     * Converts the class data into a map to insert into the database more easily.
     * @return The map of class data.
     */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("message",message);

        return result;
    }

}
