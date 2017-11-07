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
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public User user;
    private Marker marker;
    private Marker counterMarker;
    PopupWindow popupwindow;
    LayoutInflater layoutinflater;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

    public void checkLocationPermissions() {
        // check if user has granted the Location permission to app to continuously track location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("request permission");

            // request Location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            // permission already granted
            System.out.println("permission already granted");
            setUpLocationRequest();
        }
    }

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
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
                        Toast.makeText(getApplicationContext(), "sup bro, this is a test", Toast.LENGTH_SHORT).show();// display toast
                    }
                }
                return true;
            }
        });
    }

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

    public Marker createMarker(LatLng currLoc) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
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

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("location change");
        updateMap(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        // check if user has granted permission for app to access Location upon connecting to Google Play Services
        checkLocationPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
