package com.example.lunchmeet.lunchmeet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void userTest() throws Exception{
        User temp = new User("name","male", "profile_pic", 0,0);
        assertEquals("name", temp.getName());
    }

    @Test
    public void groupTest() throws Exception{
        User temp = new User("name","male", "profile_pic", 0,0);
        Group group = new Group(temp);
        assertEquals(1, group.getCurr_size());
    }

    @Test
    public void addUserToGroupTest()throws Exception{
        User user1 = new User("name","male", "profile_pic", 0,0);
        Group group = new Group(user1);
        User user2 = new User("name2","male", "profile_pic2", 10,10);
        group.add_user(user2);
        assertEquals(2, group.getCurr_size());
    }

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
}