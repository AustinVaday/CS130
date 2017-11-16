package com.example.lunchmeet.lunchmeet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import java.net.URL;
import java.util.List;

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
     * Bitmap object used to draw the user and counter markers on the map.
     */
    Bitmap bm;
    /**
     * Marker used to show the user's image and location on the map.
     */
    private Marker marker;
    /**
     * Marker used to show how many other people are in the user's group.
     */
    private Marker counterMarker;
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

        setContentView(R.layout.activity_maps);
        if(mUser == null) {
            return;
        }

        mGid = null;

        u = new DBUser(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString());

        mManager.addUser(u);
        mManager.updateActiveUser(u, 0, 0);

        ImageView image = new ImageView(this);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        URL url = new URL(u.getPhotoUrl());
                        bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();



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
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // get user's current location + add marker on map at that loc
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null) {
            LatLng currLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
            createMarker(currLoc);
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
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    if (marker.getTitle().equals("Current Location")) { // if marker source is clicked
                        // Toast.makeText(getApplicationContext(), "sup bro, this is a test", Toast.LENGTH_SHORT).show();// display toast
                        layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup container = (ViewGroup) layoutinflater.inflate(R.layout.popup, null);
                        popupwindow = new PopupWindow(container, 700, 600, true);
                        ImageButton ib = (ImageButton)container.findViewById(R.id.imageButton);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.file);
                        Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
                        ib.setImageBitmap(getCircleBitmap(resized, 0, "0"));
                        TextView text = (TextView)container.findViewById(R.id.textView);
                        System.out.println("name = " + u.getName());
                        text.setText(u.getName());
                        Button bt = (Button)container.findViewById(R.id.button2);
                        popupwindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER, 0, 150);

                        container.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                popupwindow.dismiss();
                                return false;
                            }
                        });
                    }
                }
                return true;
            }
        });
    }


    /**
     * Update the map based on the user's new location.
     *
     * @param lat user's new latitude
     * @param lon user's new longitude
     */
    public void updateMap(double lat, double lon) {
        // set marker at the user's new position specified by lat and lon
        LatLng currPos = new LatLng(lat, lon);
        if (marker == null) {
            marker = createMarker(currPos);
        }
        marker.setPosition(currPos);
        counterMarker.setPosition(currPos);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currPos));
    }

    /**
     * Create markers for the user and count of how many people are in their group (if any).
     * Markers are created using bitmaps.
     *
     * @param currLoc the current location of the user
     * @return the marker of the user's current location
     */
    public Marker createMarker(LatLng currLoc) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized;
        if(bm==null){
             resized= Bitmap.createScaledBitmap(bitmap, 200, 200, true);

        }
        else{
            Log.e("event","i went in the else statement");
             resized= Bitmap.createScaledBitmap(bm, 200, 200, true);

        }


        Bitmap black = BitmapFactory.decodeResource(getResources(),
                R.drawable.black);
        Bitmap r_black = Bitmap.createScaledBitmap(black, 75, 75, true);
        marker = mMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(resized, 0, "5")))
                .draggable(false)
                .title("Current Location"));

        counterMarker = mMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .anchor((float)-.75,(float).75)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(r_black,1,"5")))
                .draggable(false)
                .title("Counter"));
        return marker;
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
        updateMap(location.getLatitude(), location.getLongitude());
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
