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
    }

    /**
     * Tests that a newly created user is in the free agent state by default.
     * @throws Exception on the new user not being in the free agent state.
     */
    @Test
    public void freestateUserTest() throws Exception{
        User temp = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        assertEquals("Free_agent", temp.getCurrentState().getClass().getSimpleName());
    }

    /**
     * Tests that a user who creates a group is in the creator state.
     * @throws Exception on the user not being in the creator state after making a group.
     */
    @Test
    public void userCreatesGroupTest() throws Exception{
        User user1 = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        user1.createGroup();
        assertEquals("Creator", user1.getCurrentState().getClass().getSimpleName());
    }

    /**
     * Tests that a user who joins a group is in the member state.
     * @throws Exception on the user not being in the member state after joining a group.
     */
    @Test
    public void memberTest()throws Exception{
        User user1 = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        user1.createGroup();
        User user2 = new User("name2",null, (float).45,(float).55,"male", "profile_pic2", "0");
        user2.joinAGroup(user1.getGroup());
        assertEquals("Member", user2.getCurrentState().getClass().getSimpleName());
    }
}