package com.example.lunchmeet.lunchmeet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    DBUser u;
    private final DBManager mManager = DBManager.getInstance();
    private Button b;
    private HashMap<String,String> uid_profilePicURL_hm = new HashMap<String,String>();

    /**
     * Called when the Dashboard activity is opened. Sets up a listener for user data to be populated into a HashMap
     * and also adds the current user to the database.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state. Value is null upon when the app is first initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mManager.attachListenerForActiveUsers(new DBListener<List<DBActive>>(){
            @Override
            public void run(List<DBActive> list){
                for(DBActive e : list){
                    uid_profilePicURL_hm.put(e.getUid(),e.getPhotoUrl());
                }
            }
        });
        super.onCreate(savedInstanceState);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        u = new DBUser(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString());
        mManager.addUser(u);
        mManager.updateActiveUser(u, 0, 1);
        setContentView(R.layout.activity_dashboard);
        b=(Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMaps();
            }
        });
    }

    /**
     * Opens the MainActivity with an extra string noting that the activity was opened from Dashboard.
     * This is used to ensure that the "Back to Dashboard" button is only displayed on MainActivity
     * when the user went to that page from the dashboard.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("back", "dashboard");
        startActivity(intent);
    }

    /**
     * Opens the MapsActivity.
     */
    private void gotoMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("uid_profilePicURL_hm",uid_profilePicURL_hm);
        startActivity(intent);
    }
}
