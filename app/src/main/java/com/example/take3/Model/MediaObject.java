/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3.Model;

public class MediaObject {
    private String title;
    private String video_url;
    private String thumbnail_url;

    public MediaObject(String title, String video_url, String thumbnail_url) {
        this.title = title;
        this.video_url = video_url;
        this.thumbnail_url = thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia_url() {
        return video_url;
    }

    public void setMedia_url(String media_url) {
        this.video_url = media_url;
    }

    public String getThumbnail() {
        return thumbnail_url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail_url = thumbnail;
    }
}
