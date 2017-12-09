package com.example.lunchmeet.lunchmeet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Target;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.internal.Utility.logd;

/**
 * The Default MapActivity class.
 * Displays a map of the user's location and the surrounding area using the Google Maps API.
 * The map is shown upon the user successfully logging into the app via Facebook.
 * From here, the user can choose to invite others to their group, join a group, or create a group.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG1 = "MapsActivity";
    private Random r = new Random();
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    /**
     * User object that keeps track of the current user's state. Different actions are available to the
     * user based on what state they are in (eg a user in creator state can dissolve a group, but a user in
     * the free agent state cannot).
     */
    private User user;
    /**
     * Marker used to show the user's image and location on the map.
     */
    private HashMap<String,Marker> markerHashMap = new HashMap<String,Marker>();
    /**
     * CounterMarker used to show how many other people are in the user's group.
     */
    private HashMap<String,Marker> counterMarkerHashMap = new HashMap<String,Marker>();
    private HashMap<String,User> user_hmp = new HashMap<String,User>();
    private HashMap<String,Tuple<Double,Double>> uid_loc_hm = new HashMap<String,Tuple<Double,Double>>();
    //private HashMap<String,String> uid_profilePicURL_hm = new HashMap<String,String>();
    private HashMap<String,Thread> uid_threads = new HashMap<String,Thread>();

    /**
     * HashMap to store (User Id, Group Id) pairs of active groups.
     */
    private HashMap<String, String> leaders = new HashMap<String, String>();

    private HashMap<String, Integer> groupSize = new HashMap<String, Integer>();
    private int idx_1=0;
    /**
     * Bitmap Hashmap used to draw the user and counter markers on the map.
     */
    private HashMap<String,Bitmap> uid_bitmaps = new HashMap<String,Bitmap>();

    /**
     * Popup window used to display the user's name, picture, and action buttons (ex invite to group)
     * when clicked on.
     */
    PopupWindow popupwindow;
    /**
     * The Layoutinflater.
     */
    LayoutInflater layoutinflater;
    private final DBManager mManager = DBManager.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String mGid;
    /**
     * DBUser instance that keeps track of user info obtained from Facebook + Firebase
     */
    DBUser u;

    public Marker m;

//    Thread thread;

    private long count = 0;

    /**
     * int identifier for the ACCESS_FINE_LOCATION permission. This permission allows the app to keep
     * track of the user's location using their phone GPS.
     */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    /**
     * Sets up the map and user/database instances so they can be used. Called upon starting the
     * maps activity. Also takes care of location permissions: if the user has not permitted the app
     * to access their location, they will be prompted to do so before being able to access any of the
     * map features.
     *
     * @param savedInstanceState saved activity state, null upon first starting the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG1, "test");
        setContentView(R.layout.activity_maps);
        if(mUser == null) {
            return;
        }

        mGid = null;

        u = new DBUser(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString());

        user_hmp.put(u.getUid(),new User(u.getName(),null,0,0,u.getUid(),u.getPhotoUrl(),"default"));
        mManager.addUser(u);
        mManager.updateActiveUser(u, 0, 0);

        ImageView image = new ImageView(this);

        final Button createGroupButton = (Button)findViewById(R.id.createGroupButton);
        final Button dissolveGroupButton = (Button)findViewById(R.id.dissolveGroupButton);

        /*
        System.out.println("leaders size" + leaders.size());

        // check if the user is a creator. if they are, display the dissolve group button instead of create group
        if (leaders.containsKey(u.getUid())) {
            System.out.println("leader");
            dissolveGroupButton.setVisibility(View.VISIBLE);
            createGroupButton.setVisibility(View.GONE);
        }
        else { // still need to check if the user is a member of a group
            createGroupButton.setVisibility(View.VISIBLE);
            dissolveGroupButton.setVisibility(View.GONE);
        }
        */

        //uid_profilePicURL_hm = (HashMap<String, String>) getIntent().getSerializableExtra("uid_profilePicURL_hm");
        mManager.attachListenerForActiveUsers(new DBListener<List<DBActive>>(){
            @Override
            public void run(List<DBActive> list){
                for(DBActive e : list){
                    if(!user_hmp.containsKey(e.getUid())){
                        user_hmp.put(e.getUid(),new User(e.getName(),null,(float)e.getLat(),(float)e.getLng(),e.getUid(),e.getPhotoUrl(),e.getGid()));

                    }
                    Log.d("USERS","PhotoURL: " + user_hmp.get(e.getUid()).geturl());

                    user_hmp.get(e.getUid()).setCoordinates(e.getLat(),e.getLng());
                    user_hmp.get(e.getUid()).setgid(e.getGid());


                    final int idx = 1;
                    final String uid=e.getUid();
                    if(uid_threads.get(uid)==null) {
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    try {
                                        URL url = new URL(user_hmp.get(uid).geturl());
                                        Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                        //uid_bitmaps.put(uid,bm);
                                        user_hmp.get(uid).set_bmp(bm);



                                        System.out.println("thread " + idx + " created");
                                    } catch(IOException e) {
                                        System.out.println(e);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                            uid_threads.put(uid, thread);
                            thread.start();
                            idx_1++;
                    }
//                    uids.add(e.getUid());
//                    double lat = e.getLat();
//                    double lng = e.getLng();
//                    Tuple <Double,Double> coord = new Tuple <Double, Double> (lat,lng);
//                    uid_loc_hm.put(e.getUid(),coord);
                    System.out.println(e.getLat());
                    System.out.println(e.getPhotoUrl());
                }
            }
        });

        mManager.attachListenerForGroups(new DBListener<List<DBGroup>>(){
            @Override
            public void run(List<DBGroup> list){
                for(DBGroup g : list) {
                    leaders.put(g.getLeader(), g.getGid());
                    System.out.println("added to leaders: " + g.getLeader() + " " + g.getGid());
                    groupSize.put(g.getGid(), g.getSize());

                    if (g.getLeader().equals(u.getUid())) {
                        createGroupButton.setVisibility(View.GONE);
                        dissolveGroupButton.setVisibility(View.VISIBLE);
                    }
                    else {
                        createGroupButton.setVisibility(View.VISIBLE);
                        dissolveGroupButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gID =  mManager.createGroup(u.getUid());
                Toast.makeText(getApplicationContext(),"A Group is created", Toast.LENGTH_SHORT).show();
                Marker m = markerHashMap.get(u.getUid());
                m.remove();
                markerHashMap.remove(u.getUid());
                //m.setVisible(false);
                double lat = user_hmp.get(u.getUid()).getLat();
                System.out.println(lat);
                double lng = user_hmp.get(u.getUid()).getLon();
                System.out.println(lng);
                LatLng pos = new LatLng(lat, lng);
                /*
                uid_loc_hm.remove(u.getUid());
                markerHashMap.remove(u.getUid());
                counterMarkerHashMap.remove(u.getUid());
                */
                createMarker(u.getUid(), pos, 1); // create marker with group leader as picture

                leaders.put(u.getUid(), gID);

                createGroupButton.setVisibility(View.GONE);
                dissolveGroupButton.setVisibility(View.VISIBLE);
            }
        });

        dissolveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marker m = markerHashMap.get(u.getUid());
                Marker counter = counterMarkerHashMap.get(u.getUid());
                m.remove();
                counter.remove();
                markerHashMap.remove(u.getUid());
                counterMarkerHashMap.remove(u.getUid());
                //m.setVisible(false);
                //counter.setVisible(false);

                /*
                // repopulate markers for all members of the group
                mManager.getMembers(new DBListener<List<String>>() {
                    @Override
                    public void run(List<String> param) {
                        for (String id : param) {

                            double lat = user_hmp.get(id).getLat();
                            System.out.println(lat);
                            double lng = user_hmp.get(id).getLon();
                            System.out.println(lng);
                            LatLng pos = new LatLng(lat, lng);

                            createMarker(id, pos, 0);
                        }
                    }
                }, leaders.get(u.getUid()));
                */

                mManager.dissolveGroup(leaders.get(u.getUid()), u.getUid());
                Toast.makeText(getApplicationContext(),"A Group is deleted", Toast.LENGTH_SHORT).show();
                leaders.remove(u.getUid());

                createGroupButton.setVisibility(View.VISIBLE);
                dissolveGroupButton.setVisibility(View.GONE);
                updateMap();
            }
        });

//        int idx_1 = 1;
//        for(Map.Entry<String,String> entry : uid_profilePicURL_hm.entrySet()) {
//            final int idx = idx_1;
//            final String uid = entry.getKey();
//            final String profilePicURL = entry.getValue();
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try  {
//                        try {
//                            URL url = new URL(profilePicURL);
//                            Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                            //uid_bitmaps.put(uid,bm);
//                            user_hmp.get(uid).set_bmp(bm);
//
//
//
//                            System.out.println("thread " + idx + " created");
//                        } catch(IOException e) {
//                            System.out.println(e);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            uid_threads.put(uid,thread);
//            thread.start();
//            idx_1++;
//        }

        //bm=((BitmapDrawable)image.getDrawable()).getBitmap();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    /**
     * Checks if the user has granted location permission to the app. If they have not, the user will be
     * prompted to grant this permission so that the app can use GPS to track their location.
     */
    public void checkLocationPermissions() {
        // check if user has granted the Location permission to app to continuously track location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("request permission");

            // request Location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // permission already granted
            System.out.println("permission already granted");
            setUpLocationRequest();
        }
    }

    /**
     * Sets up location request using Google Play Services in order to track the user's location continuously
     * through GPS. Upon obtaining an initial location for the user, it sets up a marker to display
     * their location + info on the map.
     */
    public void setUpLocationRequest() {
        // set up location request for Google Play Services to get location continuously
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // get user's current location + add marker on map at that loc
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null) {
            LatLng currLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
            if (markerHashMap.get(u.getUid()) == null) {
                if (leaders.containsKey(u.getUid()))
                    createMarker(u.getUid(), currLoc, groupSize.get(leaders.get(u.getUid())));
                else
                    createMarker(u.getUid(), currLoc, 0);
            }
            else{
                updateMarker(u.getUid(),currLoc);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 17));
        }
    }

    /**
     * Takes the appropriate actions based on whether the user granted location permissions to the app or not.
     * If the permission was granted, set up the Google Play Services location request to use GPS tracking.
     * Otherwise, don't allow the user to see the map actions.
     *
     * @param requestCode the permission the user granted/denied
     * @param permissions list of permissions needed
     * @param grantResults which permissions were granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("requesting permission");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the task you need to do.
                    System.out.println("permission granted");
                    setUpLocationRequest();

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }


    /**
     * Sets up the map once available. The map is obtained through Google Maps API.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Sets a listener to display a user's photo + info when their marker is clicked.
     *
     * @param googleMap the google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    m = marker;
//                    if (marker.getTitle().equals("Current Location")) { // if marker source is clicked
                        // Toast.makeText(getApplicationContext(), "sup bro, this is a test", Toast.LENGTH_SHORT).show();// display toast
                        final String marker_uid = marker.getTitle();
                       // if(marker_uid.equals(u.getUid())){
                        layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup container = (ViewGroup) layoutinflater.inflate(R.layout.popup, null);
                        popupwindow = new PopupWindow(container, 700, 600, true);
                    Point p = mMap.getProjection().toScreenLocation(marker.getPosition());
                        popupwindow.showAtLocation(findViewById(R.id.map),Gravity.NO_GRAVITY,p.x-350,p.y-300);

                        LinearLayout ib = (LinearLayout) container.findViewById(R.id.linear);

                        ImageView imb=new ImageView(getApplicationContext());
                    ImageButton imb2=new ImageButton(getApplicationContext());




                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.file);

                        if(user_hmp.get(marker_uid).get_bmp()!=null) {
                           // Bitmap resized = Bitmap.createScaledBitmap(uid_bitmaps.get(marker_uid), 200, 200, true);
                            Bitmap resized = Bitmap.createScaledBitmap(user_hmp.get(marker_uid).get_bmp(), 200, 200, true);
                            //ib.setImageBitmap(getCircleBitmap(resized, 0, "0"));
                            imb.setImageBitmap(getCircleBitmap(resized, 0, "0"));


                            ib.addView(imb);




                        }
                        TextView text = (TextView) container.findViewById(R.id.textView);
                        System.out.println("name = " + u.getName());

                        text.setText(user_hmp.get(marker_uid).getName());

                        Button bt = (Button)container.findViewById(R.id.button2);
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),user_hmp.get(marker_uid).getName(), Toast.LENGTH_SHORT).show();
                                /*
                                String gID =  mManager.createGroup(u.getUid());
                                Toast.makeText(getApplicationContext(),"A Group is created", Toast.LENGTH_SHORT).show();
                                m.setVisible(false);
                                double lat = user_hmp.get(u.getUid()).getLat();
                                System.out.println(lat);
                                double lng = user_hmp.get(u.getUid()).getLon();
                                System.out.println(lng);
                                LatLng pos = new LatLng(lat, lng);
                                uid_loc_hm.remove(u.getUid());
                                markerHashMap.remove(u.getUid());
                                counterMarkerHashMap.remove(u.getUid());
                                createMarker(u.getUid(), pos, 1); // create marker with group leader as picture
                                */
                            }
                        });
                        popupwindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER, 0, 150);

                        container.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                popupwindow.dismiss();
                                return false;
                            }
                        });
//                    }
                    }
                //}
                return true;
            }
        });
    }


    /**
     * Update the map based on the user's new location.
     *
     //* @param lat user's new latitude
     //* @param lon user's new longitude
     */
    public void updateMap() {
        // set marker at the user's new position specified by lat and lon
        Iterator it=user_hmp.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key=(String)entry.getKey();
            double lat = user_hmp.get(key).getLat();
            double lng = user_hmp.get(key).getLon();
            String uid = user_hmp.get(key).getuid();
            LatLng pos = new LatLng(lat, lng);
            if (markerHashMap.get(uid) == null) {
                if (leaders.containsKey(uid))
                    createMarker(uid, pos, groupSize.get(leaders.get(uid)));
                else if(user_hmp.get(key).getGid()==null)
                    createMarker(uid, pos, 0);
            }
            else {
                if( leaders.get(key)!=null && user_hmp.get(key).getGid() == null){
                    leaders.remove(uid);
                    markerHashMap.get(uid).remove();
                    counterMarkerHashMap.get(uid).remove();
                    createMarker(uid, pos, 0);

                }


                updateMarker(uid, pos);
            }
        }
        LatLng currPos = new LatLng(user_hmp.get(u.getUid()).getLat(),user_hmp.get(u.getUid()).getLon());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currPos));
    }

    /**
     * Create markers for the user and count of how many people are in their group (if any).
     * Markers are created using bitmaps.
     *
     //* @param currLoc the current location of the user
     * @return the marker of the user's current location
     */
    public void createMarker(String uid, LatLng loc,int counter) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized;
        Bitmap black = BitmapFactory.decodeResource(getResources(),
                R.drawable.black);

        Integer iconExists; // value to check whether we loaded the icon or not
        if(user_hmp.get(uid) != null && user_hmp.get(uid).get_bmp()!=null) {
            resized = Bitmap.createScaledBitmap(user_hmp.get(uid).get_bmp(), 200, 200, true);
            iconExists = 1;
        }
        else{
            resized = Bitmap.createScaledBitmap(black, 200, 200, true);
            iconExists = 0;
        }

        Bitmap r_black = Bitmap.createScaledBitmap(black, 75, 75, true);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(loc)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(resized, 0, "5")))
                .draggable(false)
                .title(uid));
        // we put whether the icon was loaded or not as a tag, so that we can check it later
        marker.setTag(iconExists);
        boolean visible=true;

        if(counter==0){
            visible=false;

        }
        else{
            visible=true;
        }
        Marker counterMarker = mMap.addMarker(new MarkerOptions()
                .position(loc)
                .anchor((float)-.75,(float).75)
                .visible(visible)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(r_black,1,Integer.toString(counter))))
                .draggable(false)
                .title(uid));

        double lat = loc.latitude;
        double lng = loc.longitude;
        Tuple <Double,Double> coord = new Tuple <Double, Double> (lat,lng);
        uid_loc_hm.put(uid,coord);
        markerHashMap.put(uid,marker);
        counterMarkerHashMap.put(uid,counterMarker);
    }

    /**
     * Updates the location of the marker, as well as the marker's icon if it hadn't already been
     * loaded.
     *
     * @param uid The ID of the user that the marker corresponds to.
     * @param loc The new location of the marker.
     */
    public void updateMarker(String uid, LatLng loc){
        Marker currentMarker = markerHashMap.get(uid);
        Marker counter= counterMarkerHashMap.get(uid);
        currentMarker.setPosition(loc);
        counterMarkerHashMap.get(uid).setPosition(loc);

        // checking the tag and if the icon has been loaded. if the icon is loaded but the tag is 0,
        // we can reset the icon and update the tag.
        Integer iconExists = (Integer)currentMarker.getTag();
        Bitmap icon = user_hmp.get(uid).get_bmp();
        if(iconExists == 0 && icon != null){
            Log.d("Verification","inside of marker update");
            Bitmap resized = Bitmap.createScaledBitmap(icon, 200, 200, true);
            currentMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(getCircleBitmap(resized, 0, "5")));
            currentMarker.setTag(1);

        }
        if(leaders.get(uid)!=null){
            Bitmap black = BitmapFactory.decodeResource(getResources(),
                    R.drawable.black);
            Bitmap r_black = Bitmap.createScaledBitmap(black, 75, 75, true);

            counter.setIcon(BitmapDescriptorFactory
                    .fromBitmap(getCircleBitmap(r_black, 1, Integer.toString(groupSize.get(uid)))));
            counter.setVisible(true);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap,int subcircle,String num) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        paint.setTextSize(50);
        //paint.setColor(Color.BLACK);
        if(subcircle==1)
        canvas.drawText(num, (float)25, (float)55, paint );

        bitmap.recycle();

        return output;
    }

    /**
     * Called when the user's location changes based on the location tracked from the phone GPS.
     * Calls the updateMap() method to show the user's new location on the map.
     *
     * @param location user's new location
     */
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("location change");
        System.out.println(counterMarkerHashMap.size());
        System.out.println(markerHashMap.size());
        System.out.println(uid_loc_hm.size());
//        updateMap(m_marker, m_counterMarker, location.getLatitude(), location.getLongitude());
        mManager.updateActiveUser(u.getUid(), location.getLatitude(), location.getLongitude());
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Tuple <Double,Double> coord = new Tuple <Double, Double> (lat,lng);
        //uid_loc_hm.put(u.getUid(),coord);

        User tempUser = user_hmp.get(u.getUid());
        if(tempUser != null) {
            user_hmp.get(u.getUid()).setCoordinates(lat, lng);
        }

        //for testing only
        //create test coordinates and update all the other users
//        for(String uid : uids) {
//            if(uid != u.getUid()) {
//                double lat_lowerbound = location.getLatitude()-0.0005;
//                double lat_upperbound = location.getLatitude()+0.0005;
//                double lng_lowerbound = location.getLongitude()-0.0005;
//                double lng_upperbound = location.getLongitude()+0.0005;
//                double randomlat = lat_lowerbound + (lat_upperbound - lat_lowerbound) * r.nextDouble();
//                double randomlng = lng_lowerbound + (lng_upperbound - lng_lowerbound) * r.nextDouble();
//                mManager.updateActiveUser(uid, randomlat, randomlng, uid_profilePicURL_hm.get(uid));
//                Tuple <Double,Double> randomCoord = new Tuple <Double, Double> (randomlat,randomlng);
//                uid_loc_hm.put(uid,randomCoord);
//            }
//        }

        updateMap();
    }

    /**
     * Called upon the app connecting to Google Play Services. Once this happens, checks that the user
     * has granted location permissions to the app.
     *
     * @param bundle the application state
     */
    @Override
    public void onConnected(Bundle bundle) {
        // check if user has granted permission for app to access Location upon connecting to Google Play Services
        checkLocationPermissions();
    }

    /**
     * On connection suspended.
     *
     * @param i the suspended code
     */
    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection Suspended");
        mGoogleApiClient.connect();
    }

    /**
     * On connection failed.
     *
     * @param connectionResult the connection result
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Connection failed. Error: " + connectionResult.getErrorCode());
    }

    /**
     * On start.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * On stop.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
