package com.example.lunchmeet.lunchmeet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DBTestActivity extends AppCompatActivity implements DBObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }

        DBManager manager = DBManager.getInstance();

        manager.addUserObserver(this);

        DBUser u = new DBUser(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString(),0.0,0.0);
        manager.updateUser(u);

    }

    public void notify(DataSnapshot dataSnapshot){
        List<DBUser> userList = new ArrayList<>();
        for(DataSnapshot child : dataSnapshot.getChildren()){
            DBUser u = child.getValue(DBUser.class);
            userList.add(u);
        }

        LinearLayout layout = findViewById(R.id.layout);
        layout.removeAllViews();
        for(DBUser u : userList){
            LinearLayout entry = new LinearLayout(this);
            entry.setOrientation(LinearLayout.HORIZONTAL);

            ImageView image = new ImageView(this);
            Picasso.with(this)
                    .load(u.getPhotoUrl())
                    .into(image);
            entry.addView(image);

            TextView view = new TextView(this);
            view.setText(u.getName());
            entry.addView(view);

            layout.addView(entry);
        }

    }
}
