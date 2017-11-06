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
    PopupWindow popupwindow;
    LayoutInflater layoutinflater;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("request permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            System.out.println("permission already granted");
            setUpLocationRequest();
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
                    System.out.println("boo");
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
        //updateMap(-34, 151);
    }

    public void updateMap(Marker m, double lat, double lon) {
        CameraUpdateFactory cameraUpdateFactory;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        /*
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true); */

//        BitmapDescriptorFactory
//                .fromBitmap(getCircleBitmap(bitmap));
        mMap.setMinZoomPreference(15.0f);
        //mMap.setMinZoomPreference(2.0f);
        mMap.setMaxZoomPreference(20.0f);
        LatLng currPos = new LatLng(lat, lon);
        m.setPosition(currPos);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 10));

        /*
        Marker perth = mMap.addMarker(new MarkerOptions()

                .position(sydney)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(resized)))

                .draggable(false));
                */



        // MarkerOptions markerOptionsObj = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.file));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 17));
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("location change");
        //user.setCoordinates(location.getLatitude(), location.getLongitude());
        //updateMap(user.getLat(), user.getLong());
        updateMap(marker, location.getLatitude(), location.getLongitude());
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
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

        bitmap.recycle();

        return output;
    }

    public void setUpLocationRequest() {
        System.out.println("set up location request");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng currLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        marker = mMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(resized)))
                .draggable(false)
                .title("Current Location"));
        updateMap(marker, loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
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
