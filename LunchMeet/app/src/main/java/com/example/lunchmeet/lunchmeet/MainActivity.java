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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class handles the Facebook login authentication as well as set up the elements on the front page of the app
 */

public class MainActivity extends AppCompatActivity {

    //for debugging
    private static final String TAG = "login";
    private ProgressBar progressBar;
    private Button backtoDashboard;

    ///////////////

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private long count = 0;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private final DBManager mManager = DBManager.getInstance();
    DBUser u;
    private int loginTracker = 0;


    /**
     * Called when the main activity is starting. Defines the user interface of the front page. Sets up buttons and layout. Handles Facebook login authentication and exchanging the Facebook access token with a Firebase credential. Sets up connection to Firebase. Starts MapsActivity once user signs in
     * @param savedInstanceState A Bundle object containing the activity's previously saved state. Value is null upon when the app is first initialized
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"hello");
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.continueWithFBProgressBar);
        progressBar.setVisibility(View.GONE);
        backtoDashboard = (Button) findViewById(R.id.backToDashboard);
        backtoDashboard.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        backtoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Dashboard.class);
                startActivity(intent);
            }
        });

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
                if(currentAccessToken == null) {
                    LogOut();
                    updateUI(null);
                }
            }
        };
    }

    /**
     * Handles the Facebook login logic using an access token and the Facebook API.
     * @param token Facebook access token
     */
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
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user == null)
        {
            backtoDashboard.setVisibility(View.GONE);
        }
        else
        {
            if (getIntent().hasExtra("back")) {
                if (getIntent().getStringExtra("back").equals("dashboard")) {
                    progressBar.setVisibility(View.GONE);
                    backtoDashboard.setVisibility(View.VISIBLE);
                }
            }
            else {
                if (loginTracker == 0) {
                    loginTracker = 1;
                    progressBar.setVisibility(View.GONE);
                    gotoDashBoard();
                }
            }
        }
    }

    /**
     * Logs the user out of both Firebase and Facebook.
     */
    private void LogOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        loginTracker = 0;
        updateUI(null);
    }

    /**
     * Starts the Dashboard activity.
     */
    private void gotoDashBoard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}
