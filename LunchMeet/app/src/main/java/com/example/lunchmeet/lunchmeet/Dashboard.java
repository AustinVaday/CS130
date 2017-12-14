package com.example.lunchmeet.lunchmeet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    DBUser u;
    private final DBManager mManager = DBManager.getInstance();
    private Button b;
    private HashMap<String,String> uid_profilePicURL_hm = new HashMap<String,String>();
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

        gotoMaps();

        b=(Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMaps();
            }
        });
    }

    private void gotoMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("uid_profilePicURL_hm",uid_profilePicURL_hm);
        startActivity(intent);
    }
}
