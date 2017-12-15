package com.example.lunchmeet.lunchmeet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {

    private HashMap<String,User> user_hmp = new HashMap<String,User>();
   // private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    /**
     * Tests whether a User object was successfully created by checking if their name matches the name provided.
     * @throws Exception on unsuccessful User object creation.
     */
    @Test
    public void userTest() throws Exception{
        User temp = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        assertEquals("name", temp.getName());
    }

    /**
     * Tests that a DBUser is created.
     * @throws Exception on the new user not being in the free agent state.
     */
    @Test
    public void DBUserTest() throws Exception{
        DBUser u = new DBUser("123", "name", "photo");
        assertEquals("123", u.getUid());
    }
}