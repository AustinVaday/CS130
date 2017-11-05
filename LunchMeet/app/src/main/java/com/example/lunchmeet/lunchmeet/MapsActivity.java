package com.example.lunchmeet.lunchmeet;

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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public User user;
    PopupWindow popupwindow;
    LayoutInflater layoutinflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        CameraUpdateFactory cameraUpdateFactory;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-31, 150);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.file);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        Bitmap black = BitmapFactory.decodeResource(getResources(),
                R.drawable.black);
        Bitmap r_black = Bitmap.createScaledBitmap(black, 75, 75, true);

//        BitmapDescriptorFactory
//                .fromBitmap(getCircleBitmap(bitmap));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));

        Marker perth = mMap.addMarker(new MarkerOptions()
                .title("sydney")
                .position(sydney)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(resized,0,"5")))

                .draggable(false));



        Marker counter = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .anchor((float)-.75,(float).75)

                .icon(BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(r_black,1,"5")))

                .draggable(false));



       // MarkerOptions markerOptionsObj = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.file));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker != null && marker.getTitle().equals("sydney")); // if marker  source is clicked
                Toast.makeText(getApplicationContext(), "sup bro, this is a test", Toast.LENGTH_SHORT).show();// display toast
                return true;
            }
        });
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






}
