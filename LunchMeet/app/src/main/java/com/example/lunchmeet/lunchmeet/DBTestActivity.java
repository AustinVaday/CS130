package com.example.lunchmeet.lunchmeet;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBTestActivity extends AppCompatActivity {
    private final String TAG = "DBTestActivity";

    private final DBManager mManager = DBManager.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        if(mUser == null) {
            return;
        }

        DBUser u = new DBUser(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString(),0.0,0.0,null);

        mManager.updateUser(u);
        mManager.getUsers(new DBObserver<List<DBUser>>(){
            @Override
            public void run(List<DBUser> list){
                drawUsers(list);
            }
        });

        mManager.getGroups(new DBObserver<Map<String,Map<String,Boolean>>>(){
            @Override
            public void run(Map<String,Map<String,Boolean>> map){
                LinearLayout ll = findViewById(R.id.groups);
                ll.removeAllViews();
                for(String gid : map.keySet()){
                    TableLayout tl = drawGroup(gid,map.get(gid));
                    ll.addView(tl);
                }
            }
        });
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
        tr.setPadding(0, 20, 0, 29);
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

    public TableRow drawUser(DBUser u, boolean isLeader){
        TableRow tr = new TableRow(this);
        tr.setPadding(0, 20, 0, 29);
        tr.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        Picasso.with(this)
                .load(u.getPhotoUrl())
                .into(image);
        tr.addView(image);

        TextView view = new TextView(this);
        view.setPadding(10,0,0,0);

        String name = u.getName();
        if(isLeader) name += " " + getResources().getString(R.string.leader);
        view.setText(name);
        tr.addView(view);


        TextView loc = new TextView(this);
        loc.setPadding(10,0,0,0);
        loc.setText("Loc: ("+u.getLat()+","+u.getLng()+")");
        tr.addView(loc);
        return tr;
    }

    public TableLayout drawGroup(String gid, Map<String,Boolean> uids){
        final TableLayout layout = new TableLayout(this);
        final String groupId = gid;

        TableRow tr = new TableRow(this);
        tr.setPadding(0, 20, 0, 29);
        tr.setOrientation(LinearLayout.HORIZONTAL);
        TextView view = new TextView(this);
        view.setText("Group");
        tr.addView(view);

        Button button = new Button(this);
        button.setText("Join Group");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.joinGroup(groupId, mUser.getUid());
            }
        });
        tr.addView(button);
        layout.addView(tr);


        for(String uid : uids.keySet()){
            final boolean bool = uids.get(uid);
            mManager.getUserFromUid(new DBObserver<DBUser>(){
                @Override
                public void run(DBUser u){
                    TableRow entry = drawUser(u, bool);
                    layout.addView(entry);
                }
            }, uid);
        }

        return layout;

    }

    public void createGroup(View view){
        mManager.createGroup(mUser.getUid());
    }
}
