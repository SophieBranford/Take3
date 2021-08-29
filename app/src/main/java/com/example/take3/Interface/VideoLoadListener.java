/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3.Interface;

import com.example.take3.Model.MediaObject;

import java.util.ArrayList;

public interface VideoLoadListener {
    //declare methods used in BrowseEditActivity
    void onVideoLoadSuccess(ArrayList<MediaObject> videoList);
    void onVideoLoadFailed(String errorMessage);
}
