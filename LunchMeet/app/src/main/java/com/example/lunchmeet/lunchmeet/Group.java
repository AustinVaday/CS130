package com.example.lunchmeet.lunchmeet;

import java.lang.reflect.Array;

/**
 * Created by NewYeah on 11/4/2017.
 */

public class Group {
    private User[] members;
    private int capacity;
    private int curr_size;
    private float lat,lon;

    public Group(User user){
        members=new User[6];
        members[0]=user;
        capacity=5;
        curr_size=1;
        lat=0;
        lon=0;
    }

    public void add_user(User user){
        for(int i=0;i<Array.getLength(members);i++){
            if(members[i]==null){
                members[i]=user;
                curr_size++;
                break;
            }
        }


    }

    public void remove_user(User user){
        for(int i=0;i< Array.getLength(members);i++){
            if(members[i]==user){
                    members[i]=null;
                    curr_size--;
            }

        }
    }


    public int getCurr_size() {
        return curr_size;
    }
}
