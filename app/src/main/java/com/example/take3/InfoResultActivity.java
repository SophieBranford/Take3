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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InfoResultActivity extends AppCompatActivity {

    //variables declared
    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_result);

        mAuth = FirebaseAuth.getInstance();

        Button mBtnBack = findViewById(R.id.back_btn);
        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the info activity
                startActivity(new Intent(InfoResultActivity.this, InfoActivity.class));
            }
        });

        //declare user interface
        TextView mSoftwareTxt = findViewById(R.id.software_title_txt);
        TextView mParaTxt = findViewById(R.id.software_paragraph_txt);
        ImageView mSoftwareImg = findViewById(R.id.software_img);
        Button mBtnMoreInfo = findViewById(R.id.btn_more_info);

        //get intent extras from InfoActivity
        Bundle bundle = getIntent().getExtras();
        //if the bundle is not empty
        if (bundle != null) {
            //get the information
            String title = bundle.getString("title");
            String info = bundle.getString("info");
            String url = bundle.getString("url");
            int img = bundle.getInt("image");

            //set the title, info and img to the textviews and image view respectively
            mSoftwareTxt.setText(title);
            mParaTxt.setText(info);
            mSoftwareImg.setImageResource(img);

            mBtnMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create web intent
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                    //parse url obtained from InfoActivity intent bundle
                    websiteIntent.setData(Uri.parse(url));
                    startActivity(websiteIntent);
                }
            });


        }

        //NAVIGATION VIEW
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        //set checked item to appropriate activity
        navigationView.setCheckedItem(R.id.info);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //when an option is clicked
                switch (item.getItemId()) {
                    //check which option has been clicked
                    case R.id.home:
                        //highlight selected item
                        item.setChecked(true);
                        //redirect to appropriate activity
                        Intent homeIntent = new Intent(InfoResultActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(InfoResultActivity.this, UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(InfoResultActivity.this, BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(InfoResultActivity.this, InfoResultActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(InfoResultActivity.this, SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog = new ProgressDialog(InfoResultActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG, "NO USER SIGNED IN");
                        startActivity(new Intent(InfoResultActivity.this, LoginActivity.class));
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
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //if the menu item selected is the menu option
            case R.id.menu:
            case R.id.info:
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}