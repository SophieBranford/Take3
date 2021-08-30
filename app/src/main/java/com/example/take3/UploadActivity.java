/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.take3.Model.Upload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {

    //variables declared
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String currentUserID;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;

    private EditText mEditTextDesc;
    private VideoView mVideoView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mVideoUri;
    private Uri mImageUri;
    private Uri imageDownloadUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseImageRef;
    private DatabaseReference mDatabaseVideoRef;

    StorageTask mUploadTask;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    final String TAG = "Take3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mAuth = FirebaseAuth.getInstance();

        //get storage and database reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseImageRef = FirebaseDatabase.getInstance().getReference("upload");
        mDatabaseVideoRef = FirebaseDatabase.getInstance().getReference("upload");

        mVideoView = findViewById(R.id.videoView);
        Button mBtnChooseVideo = findViewById(R.id.uploadVidBtn);
        mEditTextDesc = findViewById(R.id.editDesc);
        mImageView = findViewById(R.id.uploadImage);
        Button mBtnChooseImage = findViewById(R.id.uploadImgBtn);
        mProgressBar = findViewById(R.id.progress_bar);
        Button mButtonUpload = findViewById(R.id.post_btn);
        Button mBtnBack = findViewById(R.id.back_btn);

        //BACK BUTTON
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if back button is clicked go back to the home activity
                startActivity(new Intent(UploadActivity.this,HomeActivity.class));
            }
        });


        //UPLOAD VIDEO
        //handle click on choosing video
        mBtnChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the "upload video" is clicked
                //begin the onFileChooser method to pick video from gallery
                openVideoFileChooser();
            }
        });


        //UPLOAD IMAGE
        String filename = mEditTextDesc.getText().toString().trim();
        //if the description box is empty
        if (TextUtils.isEmpty(filename)){
            //display error message to say that this field is required
            mEditTextDesc.setError("*Description required");
        }

        //handle click on choosing image
        mBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the "upload thumbnail" is clicked
                //begin the onFileChooser method to pick photo
                openImageFileChooser();
            }
        });

        //POST EVERYTHING
        //handle click for making post
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {

                } else {
                    //message to say files are being uploaded
                    ProgressDialog progressDialog = new ProgressDialog(UploadActivity.this);
                    progressDialog.setMessage("Posting...");
                    //show message
                    progressDialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 3000);
                    //get start uploadFiles method
                    uploadFiles();
                }
            }
        });


        //NAVIGATION DRAWER LAYOUT
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        //set checked item to appropriate activity in navigation drawer
        //this allows users to see what activity they are on
        navigationView.setCheckedItem(R.id.upload);

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
                        Intent homeIntent = new Intent(UploadActivity.this , HomeActivity.class);
                        startActivity(homeIntent);
                        //then close the drawer layout
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.upload:
                        item.setChecked(true);
                        Intent uploadIntent = new Intent(UploadActivity.this , UploadActivity.class);
                        startActivity(uploadIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.browse:
                        item.setChecked(true);
                        Intent browseIntent = new Intent(UploadActivity.this , BrowseActivity.class);
                        startActivity(browseIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.info:
                        item.setChecked(true);
                        Intent infoIntent = new Intent(UploadActivity.this , InfoActivity.class);
                        startActivity(infoIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.saved:
                        item.setChecked(true);
                        Intent savedIntent = new Intent(UploadActivity.this , SavedActivity.class);
                        startActivity(savedIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.logout:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        //set progress dialog to show user is logging out
                        ProgressDialog dialog=new ProgressDialog(UploadActivity.this);
                        dialog.setMessage("Logging out...");
                        dialog.show();
                        //sign out of Firebase Auth
                        mAuth.signOut();
                        //user is taken to log in page
                        Log.e(TAG,"NO USER SIGNED IN");
                        startActivity(new Intent(UploadActivity.this,LoginActivity.class));
                        //let user know they have signed out
                        Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

    }
    //INFLATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //OPEN DRAWER METHOD
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //if the menu item selected is the menu option
            case R.id.menu:
                case R.id.upload:
                    drawerLayout.openDrawer(GravityCompat.END);
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //VIDEO FILE CHOOSE METHOD
    private void openVideoFileChooser(){
        //set intent type to image so user can only choose image
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    //set chosen video to video view
    private void setVideoToVideoView(){
        //set media controller to video view
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mediaController.setPadding(150, 0, 150, 900);
        mVideoView.setMediaController(mediaController);

        //set the url
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
    }

    //IMAGE FILE CHOOSE METHOD
    private void openImageFileChooser() {
        //set intent type to image so user can only choose image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user is picking a video
        //IF all is well, and data is not null
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            //get the data and set the video to
            mVideoUri = data.getData();
            setVideoToVideoView();
        }

        //if the user is picking an image
        //do the same
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFiles() {
        //IF both the image and video uri are not null
        if (mImageUri != null && mVideoUri != null) {
            //set up storage folders for the files to go to, concatenating the milliseconds since the system was booted
            //with the file extension of either .jpg or .mp4
            //image is stored first as it is assumed videos may take longer to load in
            final StorageReference imageFileReference = mStorageRef.child("uploadImages/" + (SystemClock.uptimeMillis() + "." + getFileExtension(mImageUri)));
            final StorageReference videoFileReference = mStorageRef.child("uploadVideos/" + (SystemClock.uptimeMillis() + "." + getFileExtension(mVideoUri)));
            imageFileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> taskimage) throws Exception {
                    //IF the task is not successful
                    if (!taskimage.isSuccessful()) {
                        //throw an exception error
                        throw taskimage.getException();
                    }
                    //get the Url from "uploadImages" folder in storage
                    return imageFileReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            //IF upload of image is successful
                            if (task.isSuccessful()) {
                                //get the image Uri and store it to variable to use once
                                //video data is retrieved in putFile() method later on
                                imageDownloadUri = task.getResult();
                            }
                        }
                    });
            videoFileReference.putFile(mVideoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    //get the Url from "uploadVideos" folder in storage
                    return videoFileReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            //IF upload is successful
                            if (task.isSuccessful()) {
                                //get the current user ID from Firebase to store as value
                                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                currentUserID = mFirebaseUser.getUid();

                                //get the Uri
                                Uri videoDownloadUri = task.getResult();

                                //append values to Upload model
                                //including imageDownloadUri from imageFileReference
                                //saved is automatically set to false
                                Upload upload = new Upload(currentUserID,
                                        mEditTextDesc.getText().toString().trim(),
                                        imageDownloadUri.toString().trim(),
                                        videoDownloadUri.toString().trim(),
                                        "false");

                                //push data to and create new entry in database
                                //containing meta data of upload
                                mDatabaseImageRef.push().setValue(upload);

                                //display message to let user know edit has been posted
                                Toast.makeText(UploadActivity.this, "Posted edit!", Toast.LENGTH_LONG).show();
                            } else {
                                //ELSE error
                                Toast.makeText(UploadActivity.this, "Error adding to database!", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            mUploadTask = imageFileReference.putFile(mImageUri);
            mUploadTask = videoFileReference.putFile(mVideoUri);
            mUploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    //set the progress bar colour to red when item is uploaded
                    mProgressBar.getProgressDrawable().setColorFilter(
                            Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);

                    //extract progress from taskSnapshot using double variable
                    //the bytes transferred will be divided by the total number of bytes
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //increments value of progress bar as image uploads
                    mProgressBar.incrementProgressBy((int) progress);
                }
            });
        } else {
            //ELSE display error that video or image file has not been selected
            Toast.makeText(this, "Video or Image file not selected!", Toast.LENGTH_LONG).show();
        }
    }
}
