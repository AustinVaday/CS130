package com.example.lunchmeet.lunchmeet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageTestActivity extends AppCompatActivity {
    private final String TAG = "MessageActivity";

    private final DBManager mManager = DBManager.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_test);

        if(mUser == null) {
            return;
        }

        mGid = "TESTING";

//        final LinearLayout linearLayout = findViewById(R.id.messages);
//        mManager.getAllMessages(mGid, new DBListener<List<DBMessage>>() {
//            @Override
//            public void run(List<DBMessage> param) {
//                linearLayout.removeAllViews();
//                for(DBMessage message : param){
//                    linearLayout.addView(addMessage(message));
//                }
//            }
//        });

        mManager.attachListenerForNewMessages(mGid, new DBListener<DBMessage>() {
            @Override
            public void run(DBMessage param) {
                addMessage(param);
            }
        });
    }

    public void addMessage(DBMessage message){
        TableLayout table = findViewById(R.id.messages);
        //table.setPadding(16,2,16,2);
        TableRow row = new TableRow(this);

        final TextView name = new TextView(this);
        mManager.getUserFromUid(new DBListener<DBUser>() {
            @Override
            public void run(DBUser param) {
                name.setText(param.getName()+": ");
            }
        }, message.getUid());

        row.addView(name);

        TextView text = new TextView(this);
        text.setText(message.getMessage());
        row.addView(text);
        table.addView(row);
    }

    public void sendMessage(View view){

        EditText editText = findViewById(R.id.messageText);
        String message = editText.getText().toString();
        if(!message.equals("")) {
            String uid = mUser.getUid();
            mManager.sendMessage(mGid, new DBMessage(uid,message));

            editText.getText().clear();
        }
    }
}