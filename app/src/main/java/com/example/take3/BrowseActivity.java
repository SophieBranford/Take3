/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BrowseActivity extends AppCompatActivity {

    //variables declared
    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mAuth = FirebaseAuth.getInstance();

        Button btnMusic = findViewById(R.id.button_music);
        Button btnEdits = findViewById(R.id.button_edits);

        Button mBtnBack = findViewById(R.id.back_btn);
        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the home activity
                startActivity(new Intent(BrowseActivity.this,HomeActivity.class));
            }
        });

        //EDITS BUTTON
        btnEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirect to browse edit activity using explicit intent
                Intent browseEditIntent = new Intent(BrowseActivity.this , BrowseEditActivity.class);
                startActivity(browseEditIntent);
            }
        });

        //MUSIC BUTTON
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare URL to Soundcloud for latest audios for edits (#audios for edits)
                String url = "https://soundcloud.com/tags/audios%20for%20edits";

                //set parsed url string data to intent
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(url));
                //open URL in browser using implicit intent
                startActivity(websiteIntent);
            }
        });


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        //set checked item to appropriate activity
        navigationView.setCheckedItem(R.id.browse);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //when an option is clicked
                switch (item.getItemId()) {
                    //check which option has been clicked
                    case R.id.home:
                        //highlight selected item
                        item.setChecked(true);
                        //redirect to appropriate activity using explicit intent
                        Intent homeIntent = new Intent(BrowseActivity.this , HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(BrowseActivity.this , UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(BrowseActivity.this , BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(BrowseActivity.this , InfoActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(BrowseActivity.this , SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog=new ProgressDialog(BrowseActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG,"NO USER SIGNED IN");
                        startActivity(new Intent(BrowseActivity.this,LoginActivity.class));
                        //let user know they have signed out
                        Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the navigation menu so user can select hamburger menu
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //if the menu item selected is the menu option
            case R.id.menu:
                case R.id.browse:
                    drawerLayout.openDrawer(GravityCompat.END);
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
