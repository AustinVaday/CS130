package com.example.lunchmeet.lunchmeet;

/**
 * Created by NewYeah on 11/4/2017.
 */

public class User {
    private String name;
    private String gender;
    private String profile_pic;
    private float lat;
    private float lon;
    private Group group;
    private State state;

    public User(String name,String gender,String profile_pic,float lat,float lon){
        this.name=name;
        this.gender=gender;
        this.profile_pic=profile_pic;
        this.lat=lat;
        this.lon=lon;
        this.state=free_agent;
    }

    public interface State{
        void join_group(Group group);
        Group create_group();
        void initiate_chat(User user);
        void leave_group(Group group);
        void invite_user(User user);
        void dissolve_group(Group group);

    }
}
