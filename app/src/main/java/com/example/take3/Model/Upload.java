/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3.Model;

public class Upload {

    private String mUserId;
    private String mImageName;
    private String mImageUrl;
    private String mVideoUrl;
    private String mSaved;

    public Upload() {

    }

    public Upload(String userId, String imageName, String imageUrl, String videoUrl, String saves) {

        if (imageName.trim().equals("")) {
            imageName = "No Name";
        }

        mUserId = userId;
        mImageName = imageName;
        mImageUrl = imageUrl;
        mVideoUrl = videoUrl;
        mSaved = saves;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) { mUserId = userId; }

    public String getName(){
        return mImageName;
    }

    public void setName(String imageName){
        mImageName = imageName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getSaved(){
        return mSaved;
    }

    public void setSaved(String saves){
        mSaved = "false";
    }

}
