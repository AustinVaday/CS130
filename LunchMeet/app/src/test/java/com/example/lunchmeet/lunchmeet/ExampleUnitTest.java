package com.example.lunchmeet.lunchmeet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /**
     * Tests whether a User object was successfully created by checking if their name matches the name provided.
     * @throws Exception on unsuccessful User object creation.
     */
    @Test
    public void userTest() throws Exception{
        User temp = new User("name","male", "profile_pic", 0,0);
        assertEquals("name", temp.getName());
    }

    /**
     * Tests whether a Group object was successfully created by checking if the group size is equal to 1 after creation.
     * Size 1 is obtained from the creator being the only member of that group.
     * @throws Exception on unsuccessful Group object creation.
     */
    @Test
    public void groupTest() throws Exception{
        User temp = new User("name","male", "profile_pic", 0,0);
        Group group = new Group(temp);
        assertEquals(1, group.getCurr_size());
    }

    /**
     * Tests whether adding a user to a group was successful by checking if the group size is equal to 2 after adding
     * a group member. Size 2 is obtained from the creator and the added user being the only members of that group.
     * @throws Exception on unsuccessful user add to group.
     */
    @Test
    public void addUserToGroupTest()throws Exception{
        User user1 = new User("name","male", "profile_pic", 0,0);
        Group group = new Group(user1);
        User user2 = new User("name2","male", "profile_pic2", 10,10);
        group.add_user(user2);
        assertEquals(2, group.getCurr_size());
    }

    /**
     * Tests whether removing a user from a group was successful by checking if the group size is equal to 1 after removing
     * a group member. The group size is 2 before user removal, and so should be 1 after removing the user.
     * @throws Exception on unsuccessful user remove from group.
     */
    @Test
    public void removeUserToGroupTest()throws Exception{
        User user1 = new User("name","male", "profile_pic", 0,0);
        Group group = new Group(user1);
        User user2 = new User("name2","male", "profile_pic2", 10,10);
        group.add_user(user2);
        assertEquals(2, group.getCurr_size());
        group.remove_user(user2);
        assertEquals(1, group.getCurr_size());
    }

    /**
     * Tests that a newly created user is in the free agent state by default.
     * @throws Exception on the new user not being in the free agent state.
     */
    @Test
    public void freestateUserTest() throws Exception{
        User temp = new User("name","male", "profile_pic", 0,0);
        assertEquals("Free_agent", temp.getCurrentState().getClass().getSimpleName());
    }

    /**
     * Tests that a user who creates a group is in the creator state.
     * @throws Exception on the user not being in the creator state after making a group.
     */
    @Test
    public void userCreatesGroupTest() throws Exception{
        User user1 = new User("name","male", "profile_pic", 0,0);
        user1.createGroup();
        assertEquals("Creator", user1.getCurrentState().getClass().getSimpleName());
    }

    /**
     * Tests that a user who joins a group is in the member state.
     * @throws Exception on the user not being in the member state after joining a group.
     */
    @Test
    public void memberTest()throws Exception{
        User user1 = new User("name","male", "profile_pic", 0,0);
        user1.createGroup();
        User user2 = new User("name2","male", "profile_pic2", 10,10);
        user2.joinAGroup(user1.getGroup());
        assertEquals("Member", user2.getCurrentState().getClass().getSimpleName());
    }
}