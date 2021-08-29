/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramotion.circlemenu.CircleMenuView;

public class HomeActivity extends AppCompatActivity {
    //variables declared
    private FirebaseAuth mAuth;
    private String userEmail;
    private TextView welcomeMessage;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeMessage = findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();

        //get current user and their email address from Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        //check the user exists, IF user isn't null (ie. if someone is there) THEN
        //valid user logged on, ELSE no valid user logged on
        if (currentUser != null) {
            //log to show which user has logged in
            Log.e(TAG,"AUTH STATE UPDATE : VALID USER LOGGED IN [" + userEmail + "]");
            //display toast message to welcome the user has signed on
            welcomeMessage.setText(String.format(userEmail));
        } else {
            //ELSE set display name to "invalid user"
            Log.e(TAG, "AUTH STATE UPDATE : NO VALID USER LOGGED IN");
            welcomeMessage.setText(String.format("ERROR, INVALID USER"));
        };


        //CIRCLE MENU
        final CircleMenuView menu = (CircleMenuView) findViewById(R.id.circlemenu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                //menu starts opening
                Log.e(TAG, "Menu opened");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                //menu finishes opening
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                //menu starts closing
                Log.e(TAG, "Menu closed");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                //menu finishes closing
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                //button is clicked
                Log.e(TAG, "Button index: " + index + " clicked");
                super.onButtonClickAnimationStart(view, index);
                    //when a button is clicked, check position
                    switch (index){
                        //redirect to appropriate activity
                        case 0:
                            Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            overridePendingTransition(2000, 2000);
                            break;
                        case 1:
                            Intent uploadIntent = new Intent(HomeActivity.this, UploadActivity.class);
                            startActivity(uploadIntent);
                            overridePendingTransition(2000, 2000);
                            break;
                        case 2:
                            Intent browseIntent = new Intent(HomeActivity.this, BrowseActivity.class);
                            startActivity(browseIntent);
                            overridePendingTransition(2000, 2000);
                            break;
                        case 3:
                            Intent infoIntent = new Intent(HomeActivity.this , InfoActivity.class);
                            startActivity(infoIntent);
                            overridePendingTransition(2000, 2000);
                            break;
                        case 4:
                            Intent savedIntent = new Intent(HomeActivity.this , SavedActivity.class);
                            startActivity(savedIntent);
                            overridePendingTransition(2000, 2000);
                            break;
                    }
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                //button clicking ends
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the navigation menu so user can select logout button
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //if the menu item selected is the logout option
            case R.id.log_out:
                //set progress dialog to show user is logging out
                ProgressDialog dialog=new ProgressDialog(this);
                dialog.setMessage("Logging out...");
                dialog.show();
                //sign out of Firebase Auth
                mAuth.signOut();
                //user is taken to log in page
                Log.e(TAG,"NO USER SIGNED IN");
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                //let user know they have signed out
                Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}