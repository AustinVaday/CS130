package com.example.lunchmeet.lunchmeet;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DBTestActivity extends AppCompatActivity {
    private final String TAG = "DBTestActivity";

    private final DBManager manager = DBManager.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        if(user == null) {
            return;
        }

        DBUser u = new DBUser(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString(),0.0,0.0,null);
        manager.updateUser(u);

        mHandler.postDelayed(updateTask, 1000);
    }

    public void drawUsers(List<DBUser> list){
        TableLayout layout = findViewById(R.id.users);
        layout.removeAllViews();
        for(DBUser u : list){
            TableRow entry = drawUser(u);
            layout.addView(entry);
        }
    }

    public TableRow drawUser(DBUser u){
        TableRow tr = new TableRow(this);
        tr.setPadding(0, 30, 0, 30);

        //tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        Picasso.with(this)
                .load(u.getPhotoUrl())
                .into(image);
        tr.addView(image);

        TextView view = new TextView(this);
        view.setPadding(10,0,0,0);
        view.setText(u.getName());
        tr.addView(view);


        TextView loc = new TextView(this);
        loc.setPadding(10,0,0,0);
        loc.setText("Loc: ("+u.getLat()+","+u.getLng()+")");
        tr.addView(loc);
        return tr;
    }

    private Runnable updateTask = new Runnable () {
        public void run() {
            manager.getUsers(new DBUserObserver(){
                @Override
                public void run(List<DBUser> list){
                    drawUsers(list);
                }
            });

            mHandler.postDelayed(updateTask, 1000);


        }
    };
}
