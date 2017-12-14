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
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
        LinearLayout table = findViewById(R.id.messages);
        //table.setPadding(16,2,16,2);
        LayoutInflater layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final MessageRowUI row = new MessageRowUI(this);

        final ImageView imageView = row.imageView;
        final TextView name = row.name;
        final TextView text = row.message;

        mManager.getUserFromUid(new DBListener<DBUser>() {
            @Override
            public void run(DBUser param) {
                if(mUid.equals(param.getUid())){
                    row.setFromSelf();
                } else{
                    loadImage( param.getPhotoUrl(), imageView);
                }
                name.setText(param.getName());
                text.setText(msg.getMessage());
            }
        }, message.getUid());
        table.addView(row);

    }

    public void loadImage(String photoUrl, ImageView imageView){
        // placeholder code to put in the image
        // ideally i'd like to use the circle bitmap stuff that we do in map activity but I have no
        // idea how that works at all lol
        Picasso.with(this)
                .load(photoUrl)
                .into(imageView);
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