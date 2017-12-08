package com.example.lunchmeet.lunchmeet;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.CallbackManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the Facebook login authentication as well as set up the elements on the front page of the app
 */

public class MainActivity extends AppCompatActivity {

    //for debugging
    private static final String TAG = "login";
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private ProgressBar progressBar;
    private Button b;
    private Button b2;

    ///////////////

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private long count = 0;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private final DBManager mManager = DBManager.getInstance();
    DBUser u;


    /**
     * Called when the main activity is starting. Defines the user interface of the front page. Sets up buttons and layout. Handles Facebook login authentication and exchanging the Facebook access token with a Firebase credential. Sets up connection to Firebase. Starts MapsActivity once user signs in
     * @param savedInstanceState A Bundle object containing the activity's previously saved state. Value is null upon when the app is first initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"hello");
        setContentView(R.layout.activity_main);
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        b=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.dbtestbutton);
        progressBar = (ProgressBar) findViewById(R.id.continueWithFBProgressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        //LogOut();
        ///////////////////////////////////
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                updateUI(null);
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                updateUI(null);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if(currentAccessToken == null)
                    updateUI(null);
            }
        };

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               gotoMaps();

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            progressBar.setVisibility(View.GONE);
                            gotoDashBoard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
    /**
     * Called when main activity exits after transitioning to another activity.
     * @param requestCode The request code supplied to startActivityForResult if it was called
     * @param resultCode The result code returned from the activity started by MainActivity
     * @param data An Intent containing result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * On start
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void updateUI(FirebaseUser user) {
        if(user == null)
        {
            b.setEnabled(false);
            b2.setEnabled(false);
        }
        else
        {
            b.setEnabled(true);
            b2.setEnabled(true);
        }
    }

    private void LogOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }


    private void gotoMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
    }

    private void gotoDashBoard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    public void goToDBTest(View view){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(getApplicationContext(),"Please log in via Facebook", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, DBTestActivity.class);
            startActivity(intent);
        }
    }

}
