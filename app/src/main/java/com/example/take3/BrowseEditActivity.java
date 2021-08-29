/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.take3.Adapter.VideoPlayerRecyclerAdapter;
import com.example.take3.Interface.VideoLoadListener;
import com.example.take3.Model.MediaObject;
import com.example.take3.Model.Upload;
import com.example.take3.Utils.VerticalSpaceDecorator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowseEditActivity extends AppCompatActivity implements VideoLoadListener {

    //variables declared
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //ButterKnife used to bind the shimmer frame layout and edit recycler view
    @BindView(R.id.shimmer_frame)
    ShimmerFrameLayout mShimmerFrameLayout;
    @BindView(R.id.video_player)
    VideoPlayerRecyclerView mVideoPlayerRecyclerView;

    //video load listener interface
    VideoLoadListener mListener;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_video_edit);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        Button mBtnBack = findViewById(R.id.back_btn);
        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the home activity
                startActivity(new Intent(BrowseEditActivity.this,BrowseActivity.class));
            }
        });


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
                        //redirect to appropriate activity
                        Intent homeIntent = new Intent(BrowseEditActivity.this , HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(BrowseEditActivity.this , UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(BrowseEditActivity.this , BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(BrowseEditActivity.this , InfoActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(BrowseEditActivity.this , SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog=new ProgressDialog(BrowseEditActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG,"NO USER SIGNED IN");
                        startActivity(new Intent(BrowseEditActivity.this,LoginActivity.class));
                        //let user know they have signed out
                        Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        //start bind method
        bind();
    }

    //bind with ButterKnife the views
    private void bind() {
        ButterKnife.bind(this);

        //set the listener to
        mListener = this;

        //declare linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //set the layout manager to the recycler view to use
        mVideoPlayerRecyclerView.setLayoutManager(mLayoutManager);
        //set height of gap between posts for decorator
        VerticalSpaceDecorator decorator = new VerticalSpaceDecorator(10);
        //add the decorator to the recycler view
        mVideoPlayerRecyclerView.addItemDecoration(decorator);

        //start load video method
        loadVideoFromFirebase();
    }

    private void loadVideoFromFirebase() {
        //start the shimmer animation
        //this will be the view_upload_edits.xml screen and gives the editing page a shimmery loading screen
        //just like an Instagram or Facebook post does
        mShimmerFrameLayout.startShimmerAnimation();

        TextView display_text = findViewById(R.id.video_txt);

        ArrayList<MediaObject> videoList = new ArrayList<>();
        //Firebase load slowed down so shimmer effect is seen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //get the database referece
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("upload");
                mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //IF data from the Firebase location exists
                        if (snapshot.exists()){
                            for (DataSnapshot videoSnapshot : snapshot.getChildren()) {
                                //transform data into the Upload data class
                                Upload upload = videoSnapshot.getValue(Upload.class);
                                //set the data into the Media object class
                                MediaObject mediaObject = new MediaObject(
                                        upload.getName(),
                                        upload.getVideoUrl(),
                                        upload.getImageUrl()
                                );
                                //add the object to an array list
                                videoList.add(mediaObject);
                            }
                            //set the array list to the listener
                            mListener.onVideoLoadSuccess(videoList);
                            //set text (in case) to nothing
                            display_text.setText("");
                            //ELSE
                        } else {
                            //display error message saying video is not found using listener
                            mListener.onVideoLoadFailed("Error: video not found");
                            //set text view to message "videos not found"
                            display_text.setText("Error: videos not found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }, 3000);
    }

    @Override
    public void onVideoLoadSuccess(ArrayList<MediaObject> videoList) {

        //set the edit videos retrieved from database to the recycler view
        mVideoPlayerRecyclerView.setMediaObjects(videoList);
        //set the adapter to the video player adapter storing array list and glide method to load image and video in
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(videoList, initGlide());
        mVideoPlayerRecyclerView.setAdapter(adapter);

        //when videos are retrieved stop the shimmer animation
        mShimmerFrameLayout.stopShimmerAnimation();
        //set the shimmer frame to invisible
        mShimmerFrameLayout.setVisibility(View.GONE);

    }

    private RequestManager initGlide() {
        //set drawable resource to display (placeholder) when resource is loading
        //and drawable if load fails (error)
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.blank)
                .error(R.drawable.blank);

        //begin load with glide to this activity (browse edit activity)
        //set options to the defined options above
        return Glide.with(this).setDefaultRequestOptions(options);

    }

    @Override
    public void onVideoLoadFailed(String errorMessage) {
        //also stop shimmer animation and make shimmer frame invisible
        mShimmerFrameLayout.stopShimmerAnimation();
        mShimmerFrameLayout.setVisibility(View.GONE);
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
                //open the drawer
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
