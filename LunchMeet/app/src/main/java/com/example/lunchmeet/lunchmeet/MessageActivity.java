package com.example.lunchmeet.lunchmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity that gets all the messages in a group and presents them beautifully.
 *
 * @author Brian Kwak
 */
public class MessageActivity extends AppCompatActivity {
    private final String TAG = "MessageActivity";

    private final DBManager mManager = DBManager.getInstance();
    private String mUid;
    private String mGid;

    /**
     * The behavior that runs when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent i = getIntent();
        mGid = i.getExtras().getString("gid");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null) {
            return;
        }
        mUid = firebaseUser.getUid();

        //mGid = "TESTING";
        System.out.println("passed gid = " + mGid);


        mManager.attachListenerForNewMessages(mGid, new DBListener<DBMessage>() {
            @Override
            public void run(DBMessage param) {
                addMessage(param);

                ScrollView scrollView = findViewById(R.id.scrollView);
                scrollView.scrollTo(0,scrollView.getBottom());
            }
        });


        EditText editText = findViewById(R.id.messageText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sendMessage(view);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Creates a MessageRowUI object and adds it to the layout in the activity.
     * @param message The DBMessage containing the ID of the author and the content of the
     *                message.
     */
    public void addMessage(DBMessage message){
        final DBMessage msg = message;
        LinearLayout table = findViewById(R.id.messages);
        //table.setPadding(16,2,16,2);
        LayoutInflater layoutinflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final boolean self = message.getUid().equals(mUid);
        final MessageRowUI row = new MessageRowUI(this, self);

        final ImageView imageView = row.imageView;
        final TextView name = row.name;
        final TextView text = row.message;

        mManager.getUserFromUid(new DBListener<DBUser>() {
            @Override
            public void run(DBUser param) {
                if(!self){
                    loadImage( param.getPhotoUrl(), imageView);
                }
                name.setText(param.getName());
                text.setText(msg.getMessage());
            }
        }, message.getUid());
        table.addView(row);

    }


    /**
     * Asynchronously loads the image from the URL and places it in the given ImageView.
     * @param photoUrl The URL of the image to be loaded.
     * @param imageView The ImageView.
     */
    public void loadImage(String photoUrl, ImageView imageView){
        new ImageDownloaderTask(imageView).execute(photoUrl);
    }

    /**
     * Sends the content of the editText at the bottom to the database.
     * @param view The view.
     */
    public void sendMessage(View view){

        EditText editText = findViewById(R.id.messageText);
        String message = editText.getText().toString();
        if(!message.equals("")) {
            mManager.sendMessage(mGid, new DBMessage(mUid,message));

            editText.getText().clear();
        }
    }
}