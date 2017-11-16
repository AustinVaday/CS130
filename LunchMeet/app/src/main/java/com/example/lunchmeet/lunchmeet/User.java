package com.example.lunchmeet.lunchmeet;

/**
 * A class to keep track of user information, such as their name, gender, and profile picture as taken from Facebook.
 * Keeps track of the user's current state and allows or disallows certain actions based on the state. Provides
 * getters and setters to manipulate user information.
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

    /**
     * Default constructor to initialize user information.
     * @param name user's name
     * @param gender user's gender
     * @param profile_pic user's profile picture URL from Facebook
     * @param lat user's current latitude
     * @param lon user's current longitude
     */
    public User(String name,String gender,String profile_pic,float lat,float lon){
        free_agent=new Free_agent();
        member=new Member();
        creator=new Creator();
        this.name=name;
        this.gender=gender;
        this.profile_pic=profile_pic;
        this.lat=lat;
        this.lon=lon;
        this.state= free_agent;
    }

    /**
     * Gets the user's name.
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's current state according to the parameter
     * @param s state to transition to
     */
    public void setState(State s) {
        state = s;
    }

    /**
     * Gets the user's current state.
     * @param s string version of user's current state (eg if user is in member state, s = "member")
     * @return instantiation of the state class the user is in
     */
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
    /**
     * creates a group by calling state.create_group()
     * if the state == Free_state it will create a group
     * in any other state it will do nothing.
     */
    public void createGroup(){
        group = state.create_group();
    }
    /**
     * If the user is in freeagent state
     * then the user cann join to a group
     */
    public void joinAGroup(Group g){
        state.join_group(g);
    }
    /*
     * @return the group object which the user is in.
     * */
    public Group getGroup(){
        return group;
    }

    /**
     * @return the current state of the user
     */
    public State getCurrentState(){
        return state;
    }

    /**
     * Sets the user's location to the latitude and longitude provided in the parameters.
     * @param latitude user's new latitude
     * @param longitude user's new longitude
     */
    public void setCoordinates(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }

    /**
     * Gets the user's current latitude
     * @return user's latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the user's current longitude
     * @return user's longitude
     */
    public double getLong() {
        return lon;
    }

    /**
     * Interface defining all possible user actions in a state. A user can join a group, create a group,
     * start a chat with group members, leave a group, invite a user, or dissolve a group. What actions they
     * can take depends on what state they are in.
     */
    public interface State {
        /**
         * Adds the user to the specified group
         * @param group group to add user to
         */
        void join_group(Group group);

        /**
         * Creates a new group, with the user as the creator
         * @return newly created group
         */
        Group create_group();

        /**
         * Initiates a group chat.
         * @param user
         */
        void initiate_chat(User user);

        /**
         * Removes the user from the specified group.
         * @param group group to remove the user from
         */
        void leave_group(Group group);

        /**
         * Invite the specified user to the current group.
         * @param user user to invite to group
         */
        void invite_user(User user);

        /**
         * Dissolve group created by the user.
         * @param group group to dissolve
         */
        void dissolve_group(Group group);
    }

    /**
     * Free agent state. In this state, the user can only join a group or create a group.
     */
    public class Free_agent implements State{

        /**
         * Adds a user to the group specified in the parameters.
         * @param group group to add the user to.
         */
        @Override
        public void join_group(Group group) {
            User.this.group = group;
            group.add_user(User.this);
            User.this.setState(User.this.getState("member"));
        }

        /**
         * Creates a group with the user as the creator.
         * @return newly created group
         */
        @Override
        public Group create_group() {
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

    /**
     * Member state. In this state, a user can leave a group or initiate chat.
     */
    public class Member implements State {

        @Override
        public void join_group(Group group) {

        }

        @Override
        public Group create_group() {
            return null;
        }

        @Override
        public void initiate_chat(User user) {
            // TODO?
        }

        /**
         * Removes the user from the specified group.
         * @param group group to remove the user from
         */
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

    /**
     * Creator state. In this state, the user can initiate chat, invite a user, or dissolve the group.
     */
    public class Creator implements State{

        @Override
        public void join_group(Group group) {

        }

        @Override
        public Group create_group() {
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

        /**
         * Dissolves the group the user created. Moves all members of the group into the free agent state
         * @param group group to dissolve
         */
        @Override
        public void dissolve_group(Group group) {
            //User.this.setState(User.this.getState("freeagent"));
            group.dissolveGroup();
            group = null;
        }
    }
}
