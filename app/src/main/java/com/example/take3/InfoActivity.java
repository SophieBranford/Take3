/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InfoActivity extends AppCompatActivity {

    //variables declared
    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RadioButton button1, button2, button3, button4, button5,
            button6, button7, button8;
    private Button mBtnSubmit;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mAuth = FirebaseAuth.getInstance();

        Button mBtnBack = findViewById(R.id.back_btn);

        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the home activity
                startActivity(new Intent(InfoActivity.this,HomeActivity.class));
            }
        });


        //RADIO BUTTONS
        button1 = (RadioButton) findViewById(R.id.button1);
        button2 = (RadioButton) findViewById(R.id.button2);
        button3 = (RadioButton) findViewById(R.id.button3);
        button4 = (RadioButton) findViewById(R.id.button4);
        button5 = (RadioButton) findViewById(R.id.button5);
        button6 = (RadioButton) findViewById(R.id.button6);
        button7 = (RadioButton) findViewById(R.id.button7);
        button8 = (RadioButton) findViewById(R.id.button8);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when choice is clicked, set button to checked
                //therefore set text colour to white and background resource to purple drawable
                button1.setChecked(true);
                button1.setTextColor(Color.WHITE);
                button1.setBackgroundResource(R.drawable.radio_button_checked);
                //set second choice to unchecked
                //set text colour to black and background resource to white drawable
                button2.setChecked(false);
                button2.setTextColor(Color.BLACK);
                button2.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });

        //SIMILAR SETTINGS FOR ALL BUTTONS BELOW
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setChecked(true);
                button2.setTextColor(Color.WHITE);
                button2.setBackgroundResource(R.drawable.radio_button_checked);
                button1.setChecked(false);
                button1.setTextColor(Color.BLACK);
                button1.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3.setChecked(true);
                button3.setTextColor(Color.WHITE);
                button3.setBackgroundResource(R.drawable.radio_button_checked);
                button4.setChecked(false);
                button4.setTextColor(Color.BLACK);
                button4.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4.setChecked(true);
                button4.setTextColor(Color.WHITE);
                button4.setBackgroundResource(R.drawable.radio_button_checked);
                button3.setChecked(false);
                button3.setTextColor(Color.BLACK);
                button3.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button5.setChecked(true);
                button5.setTextColor(Color.WHITE);
                button5.setBackgroundResource(R.drawable.radio_button_checked);
                button6.setChecked(false);
                button6.setTextColor(Color.BLACK);
                button6.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6.setChecked(true);
                button6.setTextColor(Color.WHITE);
                button6.setBackgroundResource(R.drawable.radio_button_checked);
                button5.setChecked(false);
                button5.setTextColor(Color.BLACK);
                button5.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button7.setChecked(true);
                button7.setTextColor(Color.WHITE);
                button7.setBackgroundResource(R.drawable.radio_button_checked);
                button8.setChecked(false);
                button8.setTextColor(Color.BLACK);
                button8.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button8.setChecked(true);
                button8.setTextColor(Color.WHITE);
                button8.setBackgroundResource(R.drawable.radio_button_checked);
                button7.setChecked(false);
                button7.setTextColor(Color.BLACK);
                button7.setBackgroundResource(R.drawable.radio_button_unchecked);
            }
        });

        //SUBMIT BUTTON
        Button mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF a particular combination is checked
                if (button2.isChecked() && button3.isChecked() && button5.isChecked() && button8.isChecked()) {

                    //put information about After Effects into an intent to pass into the InfoResultActivity
                    //this is the title, image drawable, information about the software, and a url for the company's website
                    Intent aeIntent = new Intent(InfoActivity.this,InfoResultActivity.class);
                    aeIntent.putExtra("title", "After Effects");
                    aeIntent.putExtra("image", R.drawable.aftereffects);
                    aeIntent.putExtra("info", getString(R.string.after_effects_info));
                    aeIntent.putExtra("url", "https://www.adobe.com/uk/products/aftereffects.html");
                    //start the activity and pass the intent through
                    startActivity(aeIntent);

                } if (button2.isChecked() && button4.isChecked() && button6.isChecked() && button7.isChecked()) {

                    //IF a different combination is checked, store Sony Vegas information in an intent
                    Intent vpIntent = new Intent(InfoActivity.this,InfoResultActivity.class);
                    vpIntent.putExtra("title", "Vegas Pro");
                    vpIntent.putExtra("image", R.drawable.vegaspro);
                    vpIntent.putExtra("info", getString(R.string.vegas_pro_info));
                    vpIntent.putExtra("url", "https://www.vegascreativesoftware.com/gb/vegas-pro/");
                    startActivity(vpIntent);

                } if (button1.isChecked() && button4.isChecked() && button5.isChecked() && button8.isChecked()) {

                    //IF a different combination is checked, store Video Star information in an intent
                    Intent vsIntent = new Intent(InfoActivity.this,InfoResultActivity.class);
                    vsIntent.putExtra("title", "Video Star");
                    vsIntent.putExtra("image", R.drawable.videostar);
                    vsIntent.putExtra("info", getString(R.string.video_star_info));
                    vsIntent.putExtra("url", "https://videostarapp.com/");
                    startActivity(vsIntent);

                } if (button1.isChecked() && button3.isChecked() && button5.isChecked() && button7.isChecked()) {

                    //IF a different combination is checked, store Alight Motion information in an intent
                    Intent amIntent = new Intent(InfoActivity.this, InfoResultActivity.class);
                    amIntent.putExtra("title", "Alight Motion");
                    amIntent.putExtra("image", R.drawable.alightmotion);
                    amIntent.putExtra("info", getString(R.string.alight_motion_info));
                    amIntent.putExtra("url", "https://alightcreative.com/");
                    startActivity(amIntent);

                } if (!(button2.isChecked() && button3.isChecked() && button5.isChecked() && button8.isChecked() ||
                        button2.isChecked() && button4.isChecked() && button6.isChecked() && button7.isChecked() ||
                        button1.isChecked() && button4.isChecked() && button5.isChecked() && button8.isChecked() ||
                        button1.isChecked() && button3.isChecked() && button5.isChecked() && button7.isChecked())) {

                    //ELSE IF none of the above combinations are checked, display error message saying that this combination
                    //does not exist yet

                    Toast.makeText(InfoActivity.this, "Oops, we don't have that option yet!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //NAVIGATION DRAWER
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
                        Intent homeIntent = new Intent(InfoActivity.this , HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(InfoActivity.this , UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(InfoActivity.this , BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(InfoActivity.this , InfoActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(InfoActivity.this , SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog=new ProgressDialog(InfoActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG,"NO USER SIGNED IN");
                        startActivity(new Intent(InfoActivity.this,LoginActivity.class));
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
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
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
