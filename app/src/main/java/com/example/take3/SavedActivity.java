/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.take3.Adapter.SavedGridViewAdapter;
import com.example.take3.Model.Upload;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedActivity extends AppCompatActivity {

    //variables declared
    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    private GridView mGridView;

    public SavedGridViewAdapter mAdapter;

    final String TAG = "Take3";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        mAuth = FirebaseAuth.getInstance();

        mUploads = new ArrayList<>();

        //set adapter to grid view
        mGridView = findViewById(R.id.grid_view);
        mAdapter = new SavedGridViewAdapter(SavedActivity.this, mUploads);
        mGridView.setAdapter(mAdapter);

        //text view in background
        TextView saved_text = findViewById(R.id.save_txt);

        Button mBtnBack = findViewById(R.id.back_btn);

        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the home activity
                startActivity(new Intent(SavedActivity.this,HomeActivity.class));
            }
        });

        //get the database reference location (the upload folder)
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("upload");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear any previous data collected in array list
                mUploads.clear();

                //if the data snapshot does not exist
                if (!dataSnapshot.exists()){
                    //set the text view to error message saying no saved edits exist
                    saved_text.setText("Oops! you have no saved edits.");
                }

                //FOR each child contained in reference
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //set value from class to array list
                    Upload upload = postSnapshot.getValue(Upload.class);
                    //IF the value in "saved" equals "true"
                    if (postSnapshot.child("saved").getValue().equals("true")) {
                        //add upload data
                        mUploads.add(upload);
                    }

                    //if the grid view is also empty
                    if (mGridView.getAdapter().isEmpty()) {
                        //set the text view to error message saying no saved edits exist
                        saved_text.setText("Oops! you have no saved edits.");
                    } else {
                        //ELSE set it to nothing
                        saved_text.setText("");
                    }
                }

                //reverse the array list so that the latest saved is first
                Collections.reverse(mUploads);

                //adapter lets grid view know that the data has been modified so, in order to present the new data,
                //the GridView must be redrawn
                mAdapter.notifyDataSetChanged();
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(SavedActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        //DRAWER NAVIGATION
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        //set checked item to appropriate activity
        navigationView.setCheckedItem(R.id.saved);

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
                        Intent homeIntent = new Intent(SavedActivity.this , HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(SavedActivity.this , UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(SavedActivity.this , BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(SavedActivity.this , InfoActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(SavedActivity.this , SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog=new ProgressDialog(SavedActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG,"NO USER SIGNED IN");
                        startActivity(new Intent(SavedActivity.this,LoginActivity.class));
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
                case R.id.saved:
                    drawerLayout.openDrawer(GravityCompat.END);
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
