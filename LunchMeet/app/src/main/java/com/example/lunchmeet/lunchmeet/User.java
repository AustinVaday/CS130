package com.example.lunchmeet.lunchmeet;

/**
 * Created by NewYeah on 11/4/2017.
 */

public class User {
    private String name;
    private String gender;
    private String profile_pic;
    private double lat;
    private double lon;
    private Group group;
    private State state;
    private Free_agent free_agent;
    private Member member;
    private Creator creator;

    public User(String name,String gender,String profile_pic,float lat,float lon){
        free_agent=new Free_agent();
        member=new Member();
        creator=new Creator();
        this.name=name;
        this.gender=gender;
        this.profile_pic=profile_pic;
        this.lat=lat;
        this.lon=lon;
        this.state=free_agent;
    }
    public String getName(){
        return name;
    }

    public void setState(State s){
        state=s;
    }

    public State getState(String s){
        if(s.equals("member")){
            return member;
        }
        else if(s.equals("creator")){
            return creator;
        }
        else if(s.equals("freeagent")){
            return free_agent;
        }
        return null;
    }

    public void setCoordinates(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return lon;
    }

    public interface State{
        void join_group(Group group);
        Group create_group(User user);
        void initiate_chat(User user);
        void leave_group(Group group);
        void invite_user(User user);
        void dissolve_group(Group group);

    }

    public class Free_agent implements State{

        @Override
        public void join_group(Group group) {
            group.add_user(User.this);
            User.this.setState(User.this.getState("member"));
        }

        @Override
        public Group create_group(User user) {
            User.this.setState(User.this.getState("creator"));
            return new Group(User.this);


        }

        @Override
        public void initiate_chat(User user) {

        }

        @Override
        public void leave_group(Group group) {


        }

        @Override
        public void invite_user(User user) {

        }

        @Override
        public void dissolve_group(Group group) {


        }
    }

    public class Member implements State{

        @Override
        public void join_group(Group group) {

        }

        @Override
        public Group create_group(User user) {
            return null;
        }

        @Override
        public void initiate_chat(User user) {

        }

        @Override
        public void leave_group(Group group) {
            User.this.setState(User.this.getState("freeagent"));
            group.remove_user(User.this);
        }

        @Override
        public void invite_user(User user) {

        }

        @Override
        public void dissolve_group(Group group) {

        }
    }

    public class Creator implements State{

        @Override
        public void join_group(Group group) {

        }

        @Override
        public Group create_group(User user) {
            return group;
        }

        @Override
        public void initiate_chat(User user) {

        }

        @Override
        public void leave_group(Group group) {

        }

        @Override
        public void invite_user(User user) {

        }

        @Override
        public void dissolve_group(Group group) {
            User.this.setState(User.this.getState("freeagent"));
            group=null;
        }
    }
}
