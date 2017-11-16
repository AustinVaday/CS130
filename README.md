# CS130 Project - Lunch Meet
### To provide an Android app for individuals to easily find lunch buddies in real-time
* Backend: Firebase
* Team members: Teodik Meserkhani, Isaac Yeo En Jie, Stephanie Lam, Austin Vaday, Mikhail Nikiforov, Brian Kwak

## Directory Structure
* We have implemented our project as an Android mobile application. 
* Library dependencies can be found in LunchMeet/app/build.gradle 
* Source code can be found in LunchMeet/app/src/main. 
  * LunchMeet/app/src/main/java contains the Java files for our project. 
    * MainActivity.java: Contains the home screen that the user will see upon opening the app. Allows the user to authenticate with Facebook. 
    * MapsActivity.java: Main screen of our app. Shows a map of the user's current location and their adjacent surroundings using Google Maps and location services. 
    * Group.java: Contains methods to create, modify, and delete a lunch group.
    * User.java: Contains methods to get and modify user information, such as their name, Facebook profile picture, and what group they are in. Also contains class definitions of the different states a user can be in (free agent, creator, and member) based on whether they are in a group or not.
    * DBActive.java, DBGroup.java, DBListener.java, DBManager.java, DBUser.java: Classes containing backend APIs to access the data in the Firebase database.
    * DBTestActivity.java: Temporary activity used to debug the backend APIs.
  * app/src/main/res contains the XML files used for the app UI. 
    * activity_dbtest.xml: Contains UI for DBTestActivity.java.
    * activity_main.xml: Contains UI for MainActivity.java.
    * activity_maps.xml: Contains UI for MapsActivity.java.
    * popup.xml: Contains UI for popup window obtained by clicking on a user's marker.
* Test code can be found in LunchMeet/app/src/main/test/java/com.example.lunchmeet.lunchmeet
    * ExampleUnitTest.java contains several unit tests used to test the functionality of our User and Group classes. 
      * userTest() tests whether a user was successfully created by checking that the User object's name is equal to the name we gave it. 
      * groupTest() tests whether a group was successfully created by checking that its size upon creation is 1. 
      * addUserToGroupTest() tests whether a user was added to a group successfully. After creating the group, we add a member and check that the new group size is equal to 2. 
      * removeUserToGroupTest() tests whether a user was removed from a group successfully. After removing a member, we check that the new group size is equal to 1.
      * freestateUserTest() tests whether a user is put into the free agent state upon creation.
      * userCreatestGroupTest() tests whether a user is put into the creator state upon creating a group. 
      * memberTest() tests whether a user is put into the member state after joining a group. 

* Detailed class/API definitions can be viewed through our JavaDoc documentation, located at LunchMeet/index.html