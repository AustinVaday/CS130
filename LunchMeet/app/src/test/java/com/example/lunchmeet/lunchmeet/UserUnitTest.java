package com.example.lunchmeet.lunchmeet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {
    /**
     * Tests whether a User object was successfully created by checking if their name matches the name provided.
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

}