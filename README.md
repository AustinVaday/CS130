# CS130 Project - Lunch Meet
### To provide an Android app for individuals to easily find lunch buddies in real-time
* Backend: Firebase
* Team members: Teodik Meserkhani, Isaac Yeo En Jie, Stephanie Lam, Austin Vaday, Mikhail Nikiforov, Brian Kwak

## Directory Structure
* We have implemented our project as an Android mobile application. 
* Library dependencies can be found in LunchMeet/app/build.gradle. 
* Source code can be found in LunchMeet/app/src/main. 
  * LunchMeet/app/src/main/java contains the Java files for our project. 
    * MainActivity.java: Contains the home screen that the user will see upon opening the app. Allows the user to authenticate with Facebook. 
    * MapsActivity.java: Main screen of our app. Shows a map of the user's current location and their adjacent surroundings using Google Maps and location services. 
    * MessageActivity.java: Contains the UI of the chat page. Users use this page to communicate with the members of their group. A user who isn't in a group can't access this activity. 
    * Dashboard.java: Screen preceding the maps screen used to load user data from Firebase before displaying the map. 
    * Group.java: Contains methods to create, modify, and delete a lunch group.
    * User.java: Contains methods to get and modify user information, such as their name, Facebook profile picture, and what group they are in. Also contains class definitions of the different states a user can be in (free agent, creator, and member) based on whether they are in a group or not.
    * DBActive.java, DBGroup.java, DBListener.java, DBManager.java, DBMessage.java, DBUser.java: Classes containing backend APIs to access the data in the Firebase database.
    * ImageDownloaderTask.java: Used to download bitmaps that are used to display the user's Facebook profile picture in the messaging screen.
    * MessageRowUI.java: Contains the UI for one chat message for use in MessageActivity.
    * Tuple.java: Contains code to store a tuple of variables (x1, x2). 
  * LunchMeet/app/src/main/res contains the XML files used for the app UI. 
    * activity_main.xml: Contains UI for MainActivity.java.
    * activity_maps.xml: Contains UI for MapsActivity.java.
    * activity_dashboard.xml: Contains UI for Dashboard.java.
    * activity_message.xml: Contains UI for MessageActivity.java.
    * app_bar.xml: Contains UI for custom toolbar used in MapsActivity to display the chat icon in the upper right corner.
    * message_row.xml: Contains UI for one row of chat, used in MessageActivity.
    * popup.xml: Contains UI for popup window obtained by clicking on a user's marker.
    * popup2.xml: Contains UI for popup window obtained when a user invites/requests to join a group. 
* Test code can be found in LunchMeet/app/src/main/test/java/com.example.lunchmeet.lunchmeet
    * UserUnitTest.java contains several unit tests used to test the functionality of our User class. 
      * userTest() tests whether a user was successfully created by checking that the User object's name is equal to the name we gave it. 
      * freestateUserTest() tests whether a user is put into the free agent state upon creation.
      * userCreatestGroupTest() tests whether a user is put into the creator state upon creating a group. 
      * memberTest() tests whether a user is put into the member state after joining a group. 
    * GroupUnitTest.java contains several unit tests used to test the functionality of our Group class. 
      * groupTest() tests whether a group was successfully created by checking that its size upon creation is 1. 
      * addUserToGroupTest() tests whether a user was added to a group successfully. After creating the group, we add a member and check that the new group size is equal to 2. 
      * removeUserToGroupTest() tests whether a user was removed from a group successfully. After removing a member, we check that the new group size is equal to 1.
      * leaderTest() tests whether the User who created the group is set as the group's leader.
      * coordinatesTest() tests whether the group's coordinates are set to be the leader's latitude and longitude. 

* Detailed class/API definitions can be viewed through our JavaDoc documentation, located at LunchMeet/index.html


## Running the Code
* Import the project into Android Studio.
* Either download an Android emulator from Android Studio or plug in an Android phone in development mode and download the app to the phone.
* Run the app either in the emulator or the phone. 
* To run unit tests, navigate to the file in Android Studio, right click, and then press "Run" on that file. 