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

   // private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    /**
     * Tests whether a User object was successfully created by checking if the provided parameters match
     * the data saved in the User. We check the name, the bitmap of their profile picture, their location,
     * their user ID, their profile picture URL, and their group ID to determine this.
     * @throws Exception on unsuccessful User object creation.
     */
    @Test
    public void userTest() throws Exception{
        User temp = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        assertEquals("name", temp.getName());
        assertEquals(null, temp.get_bmp());
        assertEquals(0.45, temp.getLat(),0.001);
        assertEquals(0.55, temp.getLon(),0.001);
        assertEquals("male", temp.getuid());
        assertEquals("profile_pic", temp.geturl());
        assertEquals("0", temp.getGid());
    }
    /**
     * Tests that a DBUser is created.
     * @throws Exception on the new user not being in the free agent state.
     */
    @Test
    public void DBUserTest() throws Exception{
        DBUser u = new DBUser("123", "name", "photo");
        assertEquals("123", u.getUid());
        assertEquals("name", u.getName());
        assertEquals("photo",u.getPhotoUrl());
    }
}