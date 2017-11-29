package com.example.lunchmeet.lunchmeet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * DBTestActivity is basically just an activity made as a practical application of the functions
 * implemented in DBManager, testing to make sure they are all functioning correctly. Most, if not
 * all, of this is placeholder code that is unlikely to appear in the final product.
 *
 * @author Brian Kwak
 */
public class DBTestActivity extends AppCompatActivity {
    private final String TAG = "DBTestActivity";

    private final DBManager mManager = DBManager.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        if(mUser == null) {
            return;
        }

        mGid = null;

        DBUser u = new DBUser(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString());

        mManager.addUser(u);
        mManager.updateActiveUser(u, 0, 0, u.getPhotoUrl());
        mManager.attachListenerForActiveUsers(new DBListener<List<DBActive>>(){
            @Override
            public void run(List<DBActive> list){
                drawUsers(list);
            }
        });

        mManager.attachListenerForGroups(new DBListener<List<DBGroup>>() {
            @Override
            public void run(List<DBGroup> param) {
                drawGroups(param);
            }
        });
    }

    public void drawUsers(List<DBActive> list){
        final TableLayout layout = findViewById(R.id.users);
        layout.removeAllViews();
        for(DBActive u : list){
            final DBActive user = u;
            mManager.getUserFromUid(new DBListener<DBUser>(){
                @Override
                public void run(DBUser u){
                    TableRow entry = drawUser(u,user.getLat(),user.getLng());
                    layout.addView(entry);
                }
            }, u.getUid());
        }
    }


    public TableRow drawUser(DBUser u, double lat, double lng){
        TableRow tr = new TableRow(this);
        tr.setPadding(0, 20, 0, 20);
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
        loc.setText("Loc: ("+lat+","+lng+")");
        tr.addView(loc);
        return tr;
    }

    public TableRow drawUser(DBUser u){
        TableRow tr = new TableRow(this);
        tr.setPadding(0, 20, 0, 20);
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

        return tr;
    }

    public void drawGroups(List<DBGroup> list){
        final LinearLayout layout = findViewById(R.id.groups);
        layout.removeAllViews();

        for(DBGroup g : list){
            LinearLayout table = drawGroup(g);
            layout.addView(table);
        }
    }

    public LinearLayout drawGroup(DBGroup g){
        final DBGroup group = g;
        final LinearLayout layout =  new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TableLayout table = new TableLayout(this);
        final TableLayout members = new TableLayout(this);
        members.setVisibility(View.GONE);
        layout.addView(table);
        layout.addView(members);

        final Button button = new Button(this);
        button.setText("Show Group");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vis = (members.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                members.setVisibility(vis);
                String text = (button.getText().equals("Show Group") ? "Hide Group" : "Show Group");
                button.setText(text);
            }
        });

        final TextView size = new TextView(this);
        size.setText("Size: " + group.getSize());

        mManager.getUserFromUid(new DBListener<DBUser>(){
            @Override
            public void run(DBUser param){
                TableRow entry = drawUser(param,group.getLat(),group.getLng());
                entry.addView(size);
                entry.addView(button);
                table.addView(entry);
            }
        }, g.getLeader());

        TableRow joinRow = new TableRow(this);
        Button joinButton = new Button(this);
        joinButton.setText("Join Group");
        members.addView(joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup(group);
            }
        });
        members.addView(joinRow);

        mManager.getMembers(new DBListener<List<String>>(){
            @Override
            public void run(List<String> param){
                for(String uid : param){
                    mManager.getUserFromUid(new DBListener<DBUser>(){
                        @Override
                        public void run(DBUser u){
                            TableRow entry = drawUser(u);
                            members.addView(entry);
                        }
                    }, uid);
                }
            }
        }, group.getGid());

        return layout;
    }

    public void createGroup(View view){
        if(mGid == null) {
            mGid = mManager.createGroup(mUser.getUid());
            mManager.updateGroupLeader(mGid, mUser.getUid());
            mManager.updateGroupSize(mGid, 1);
            mManager.updateGroupLocation(mGid,0,0);
        } else{
            Toast.makeText(getApplicationContext(),"Already in a group!", Toast.LENGTH_LONG).show();
        }
    }

    public void joinGroup(DBGroup group){
        if(mGid == null) {
            mManager.joinGroup(group.getGid(), mUser.getUid(), group.getSize());
            mGid = group.getGid();
        } else{
            Toast.makeText(getApplicationContext(),"Already in a group!", Toast.LENGTH_LONG).show();
        }
    }
}
