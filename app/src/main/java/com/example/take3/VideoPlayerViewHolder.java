/**
 * B00324770
 * UWS 2020/21
 *
 * This Activity's code is inspired by
 * Eddydn's Video Player on GitHub
 *
 */
package com.example.take3;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.take3.Model.MediaObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    //variables declared
    private FirebaseUser mFirebaseUser;

    private String postKeys;
    private String currentUserID;

    //declare user interface
    FrameLayout media_container;
    TextView title;
    ImageView thumbnail, btnVolume;
    ImageButton btnSave;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;

    final String TAG = "Take3";

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        btnSave = itemView.findViewById(R.id.save_edit_btn);
        progressBar = itemView.findViewById(R.id.progressBar);
        btnVolume = itemView.findViewById(R.id.volume_control);

        //get the current user ID
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = mFirebaseUser.getUid();

        //set on click listener for button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF the save image button's tag is "off" (i.e. if the edit has not been saved
                //and the icon is the empty bookmark icon, bookmark_off)
                if (btnSave.getTag().equals("off")) {

                    //get the database reference location (the upload folder)
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("upload");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot datas : snapshot.getChildren()) {
                                //get the key
                                postKeys = datas.getKey();
                            }

                            //IF the user ID stored in the database value is not the same as the current user ID logged in
                            if (!snapshot.child(postKeys).child("userId").getValue().equals(currentUserID)) {
                                //set the value of saved to string "true"
                                databaseReference.child(postKeys).child("saved").setValue("true");
                                //set the button tag to "on" and set the image resource to highlighted bookmark icon (bookmark_on)
                                btnSave.setTag("on");
                                btnSave.setImageResource(R.drawable.bookmark_on);
                                Log.e(TAG, "SAVED");
                                //let user know the edit has been saved
                                Toast.makeText(itemView.getContext(), "Saved edit!", Toast.LENGTH_SHORT).show();
                            } else {
                                //ELSE the saved value is still "false", set tag to off, keep image as bookmark_off
                                databaseReference.child(postKeys).child("saved").setValue("false");
                                btnSave.setTag("off");
                                btnSave.setImageResource(R.drawable.bookmark_off);
                                //display appropriate error message letting user know they cannot save their own edit
                                Toast.makeText(itemView.getContext(), "Error: You can't save your own edit!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }
                    });

                    //ELSE
                } else {
                    //
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("upload");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot datas : snapshot.getChildren()) {
                                //get the key
                                postKeys = datas.getKey();
                            }

                            //IF the user ID stored in the database value is not the same as the current user ID logged in
                            if (!snapshot.child(postKeys).child("userId").getValue().equals(currentUserID)) {
                                //set saved to false, set button tag to off, set image to bookmark_off
                                databaseReference.child(postKeys).child("saved").setValue("false");
                                btnSave.setTag("off");
                                btnSave.setImageResource(R.drawable.bookmark_off);
                                Log.e(TAG, "NOT SAVED");
                            } else {
                                //do nothing
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    public void onBind(MediaObject mediaObject, RequestManager requestManager) {
        //get the title from mediaObject class and set it to the title text view
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(mediaObject.getTitle());
        //load the thumbnail from mediaObject class into the thumbnail image view through Glide
        this.requestManager
                .load(mediaObject.getThumbnail())
                .into(thumbnail);
    }

}









