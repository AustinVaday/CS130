package com.example.lunchmeet.lunchmeet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by stephanielam on 12/14/17.
 */

public class GroupUnitTest {
    /**
     * Tests whether a Group object was successfully created by checking if the group size is equal to 1 after creation.
     * Size 1 is obtained from the creator being the only member of that group.
     * @throws Exception on unsuccessful Group object creation.
     */
    @Test
    public void groupTest() throws Exception{
        User temp = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
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
        User user1 = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        Group group = new Group(user1);
        User user2 = new User("name2",null, (float).45,(float).55,"male", "profile_pic2", "0");
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
        User user1 = new User("name",null, (float).45,(float).55,"male", "profile_pic", "0");
        Group group = new Group(user1);
        User user2 = new User("name2",null, (float).45,(float).55,"male", "profile_pic2", "0");
        group.add_user(user2);
        assertEquals(2, group.getCurr_size());
        group.remove_user(user2);
        assertEquals(1, group.getCurr_size());
    }

    @Test
    public void leaderTest() throws Exception {
        User user1 = new User("name",null, (float).45,(float).55,"123", "profile_pic", "0");
        Group group = new Group(user1);
        User user2 = new User("name2",null, (float).45,(float).55,"1234", "profile_pic2", "0");
        group.add_user(user2);
        assertEquals("123", group.getLeader());
    }

    @Test
    public void coordinatesTest() throws Exception {
        User user1 = new User("name",null, (float).45,(float).55,"123", "profile_pic", "0");
        Group group = new Group(user1);
        User user2 = new User("name2",null, (float)0,(float)1,"1234", "profile_pic2", "0");
        group.add_user(user2);
        assertEquals(.45, group.getGroupLat(), 1e-1);
        assertEquals(.55, group.getGroupLong(), 1e-1);
    }

    /**
     * Test for DBGroup creation. Also, it sets the DBGroup ID as Leader's group ID.
     * @throws Exception
     */
    @Test
    public void DBGroupTest() throws Exception{
        DBUser u = new DBUser("123", "name", "photo");
        DBGroup g = new DBGroup();
        g.setGid(u.getUid());
        assertEquals("123", g.getGid());
    }
}
