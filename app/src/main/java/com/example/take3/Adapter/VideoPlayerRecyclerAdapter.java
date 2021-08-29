/**
 * B00324770
 * UWS 2020/21
 *
 * This Activity's code is inspired by
 * Eddydn's Video Player on GitHub
 *
 */
package com.example.take3.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.take3.Model.MediaObject;
import com.example.take3.R;
import com.example.take3.VideoPlayerViewHolder;

import java.util.ArrayList;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //variables declared
    private ArrayList<MediaObject> mediaObjects;
    private RequestManager requestManager;


    public VideoPlayerRecyclerAdapter(ArrayList<MediaObject> mediaObjects, RequestManager requestManager) {
        //set variables to parameters
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(
                //inflate the video list item view
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_video_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //define ViewHolder used in VideoPlayerViewHolder.java
        //get the position of item stored in mediaObjects array
        ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
    }


    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

}

