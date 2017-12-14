package com.example.lunchmeet.lunchmeet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
    private final String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_test);

        if(mUid == null) {
            return;
        }

        mGid = "TESTING";


        mManager.attachListenerForNewMessages(mGid, new DBListener<DBMessage>() {
            @Override
            public void run(DBMessage param) {
                addMessage(param);

                ScrollView scrollView = findViewById(R.id.scrollView);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void addMessage(DBMessage message){
        final DBMessage msg = message;
        TableLayout table = findViewById(R.id.messages);
        //table.setPadding(16,2,16,2);
        LayoutInflater layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        MessageRowUI row = new MessageRowUI(this);

        final TextView name = row.name;
        final TextView text = row.message;

        mManager.getUserFromUid(new DBListener<DBUser>() {
            @Override
            public void run(DBUser param) {
                name.setText(param.getName()+": ");
                text.setText(msg.getMessage());
            }
        }, message.getUid());
        table.addView(row);

    }

    public void sendMessage(View view){

        EditText editText = findViewById(R.id.messageText);
        String message = editText.getText().toString();
        if(!message.equals("")) {
            mManager.sendMessage(mGid, new DBMessage(mUid,message));

            editText.getText().clear();
        }
    }
}