/**
 * B00324770
 * UWS 2020/21
 *
 * This Activity's code is inspired by
 * Eddydn's Video Player on GitHub
 *
 */
package com.example.take3;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.take3.Model.MediaObject;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class VideoPlayerRecyclerView extends RecyclerView {

    private enum VolumeState {ON, OFF};

    //declare user interface
    private ImageView thumbnail, btnVolume;
    private ProgressBar progressBar;
    private View mView;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;

    //declare variables
    private ArrayList<MediaObject> mediaObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPos = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    //controlling playback state
    private VolumeState volumeState;

    private static final String TAG = "Take 3";

    public VideoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrSet) {
        super(context, attrSet);
        init(context);
    }


    private void init(Context context){
        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //point to hold two interface co-ordinates
        Point point = new Point();
        display.getSize(point);
        //set x and y for height
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        //exoplayer's bandwith meter to estimate available bandwidth
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //bandwidth meter is set to exoplayer's track selection factory
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        //track selection factory is finally set to track selector
        //this is responsible for determining which media tracks can be played by player
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //CREATE EXOPLAYER
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        //bind player to the view.
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        //set volume state to ON using setBtnVolume method
        setBtnVolume(VolumeState.ON);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //IF the recycler view is currently not scrolling
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");

                    //IF the thumbnail is not null
                    if(thumbnail != null){
                        //show the old thumbnail
                        thumbnail.setVisibility(VISIBLE);
                    }

                    //IF the user is unable to scroll any further down (i.e. the end of the
                    //list has been reached) logic is handled
                    if(!recyclerView.canScrollVertically(1)){
                        //set to true
                        playVideo(true);
                    }
                    else{
                        //ELSE set to false
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                //IF the view is not empty and it is equal to the view detatched from the recycler view
                if (mView != null && mView.equals(view)) {
                    //reset the video view
                    resetVideoView();
                }

            }
        });

        videoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //switch statement for playback state
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        //if the player is buffering
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                        //IF the progress bar is not null
                        if (progressBar != null) {
                            //the user can see the circle progressBar
                            progressBar.setVisibility(VISIBLE);
                        }

                        break;

                    case Player.STATE_ENDED:
                        //if the player has finished playing the media
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        //seek to position in milliseconds to 0, therefore start
                        videoPlayer.seekTo(0);
                        break;

                    case Player.STATE_IDLE:
                        //if the player has no media to play

                        break;

                    case Player.STATE_READY:
                        //if the player is ready to play the media
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                        //IF the progress bar is not null
                        if (progressBar != null) {
                            //the user can no longer see the circle progressBar
                            progressBar.setVisibility(GONE);
                        }

                        //IF boolean is false
                        if(!isVideoViewAdded){
                            //call add video view method
                            addVideoView();
                        }

                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    public void playVideo(boolean reachedListEnd) {

        int targetPos;

        //IF boolean is false
        if(!reachedListEnd){
            int firstPos = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            int lastPos = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            //IF there are more than 2 videos on the screen
            if (lastPos - firstPos > 1) {
                //set the difference to 1
                lastPos = firstPos + 1;
            }

            //IF there are no start or end position (i.e. it is behaving wrong), return
            if (firstPos < 0 || lastPos < 0) {
                return;
            }

            //IF there is more than 1 list-item on the screen (if the start position is not equal to the end position
            if (firstPos != lastPos) {
                int firstPosVideoHeight = getSurfaceHeight(firstPos);
                int lastPosVideoHeight = getSurfaceHeight(lastPos);

                //TERNARY OPERATOR
                //condition: check if the start position of the video height is greater than the end position ?
                //if the case is true: set the start position to the target position
                //if the case is false: set the end position to the target position
                targetPos = firstPosVideoHeight > lastPosVideoHeight ? firstPos : lastPos;
            } else {
                //ELSE the target position is set to the start position
                targetPos = firstPos;
            }
        } else {
            //ELSE boolean is true
            //set target position to the last element in the mediaObjects
            //array list (size() - 1)
            targetPos = mediaObjects.size() - 1;
        }

        Log.d(TAG, "playVideo: target position: " + targetPos);

        //IF video is already playing
        if (targetPos == playPos) {
            return;
        }

        //set the position of the video that is to be played
        playPos = targetPos;
        if (videoSurfaceView == null) {
            return;
        }

        //set surface view to invisible
        //remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        //recent position = target position - the adapter position of the first visible view
        int recentPos = targetPos - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        //returns the view at the specified recent position
        View child = getChildAt(recentPos);
        if (child == null) {
            return;
        }

        VideoPlayerViewHolder holder = (VideoPlayerViewHolder) child.getTag();
        if (holder == null) {
            playPos = -1;
            return;
        }

        //declare user interface from holder
        thumbnail = holder.thumbnail;
        progressBar = holder.progressBar;
        btnVolume = holder.btnVolume;
        mView = holder.itemView;
        requestManager = holder.requestManager;
        frameLayout = holder.itemView.findViewById(R.id.media_container);

        //set video player to surface view
        videoSurfaceView.setPlayer(videoPlayer);

        //set on click listener method for image view
        mView.setOnClickListener(videoViewClickListener);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "Take3 VideoPlayer"));
        String mediaUrl = mediaObjects.get(targetPos).getMedia_url();
        if (mediaUrl != null) {
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mediaUrl));
            videoPlayer.prepare(videoSource);
            videoPlayer.setPlayWhenReady(true);
        }
    }

    private View.OnClickListener videoViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //when the view is clicked
            //toggle volume method is called and volume will turn on or off
            toggleVolume();
        }
    };

    private int getSurfaceHeight(int playPos) {
        int at = playPos - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        //the view at the specified position
        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        //declare integer array named location, containing the two co-ordinates
        //get the location smaller than the screen
        int[] location = new int[2];
        child.getLocationInWindow(location);

        //if the first coordinate is less than zero
        if (location[1] < 0) {
            //returns visible region of video surface on the screen
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            //ELSE if some is cut off, it will return less than the default height
            //of the video surface
            return screenDefaultHeight - location[1];
        }
    }


    //remove the old player
    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            //set boolean to false and recycler view listener to null
            isVideoViewAdded = false;
            mView.setOnClickListener(null);
        }

    }

    private void addVideoView(){
        frameLayout.addView(videoSurfaceView);
        //set boolean to true
        isVideoViewAdded = true;
        //give focus to this view, set it visible
        //set thumbnail view to invisible
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        //set apha property video list item view to 1
        //alpha is value between 0 (transparent) and 1 (opaque)
        videoSurfaceView.setAlpha(1);
        thumbnail.setVisibility(GONE);
    }

    private void resetVideoView(){
        //if boolean is true
        if(isVideoViewAdded){
            //call removeVideoView method
            removeVideoView(videoSurfaceView);
            playPos = -1;
            //set video player to invisible and thumbnail visible
            videoSurfaceView.setVisibility(INVISIBLE);
            thumbnail.setVisibility(VISIBLE);
        }
    }

    //release the player, called when the player is no longer required
    public void releasePlayer() {

        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }

        mView = null;
    }

    private void toggleVolume() {
        //IF the video player contains videos
        if (videoPlayer != null) {
            //IF the volume is off
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                //set it on using method
                setBtnVolume(VolumeState.ON);

            } //ELSE IF the colume is on
            else if(volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                //set it off using method
                setBtnVolume(VolumeState.OFF);

            }
        }
    }

    private void setBtnVolume(VolumeState state){
        volumeState = state;
        //IF state is off
        if(state == VolumeState.OFF){
            //set volume to silence/nothing
            videoPlayer.setVolume(0f);
            animateBtnVolume();
        } else if(state == VolumeState.ON){
            //ELSE set the volume to noise
            videoPlayer.setVolume(1f);
            //call animate volume method
            animateBtnVolume();
        }
    }

    private void animateBtnVolume(){
        //IF the image view is not null
        if(btnVolume != null){
            //bring the image view to the front
            btnVolume.bringToFront();
            //IF volume state is off
            if(volumeState == VolumeState.OFF){
                //load the audio_off icon into the image view
                requestManager.load(R.drawable.audio_off)
                        .into(btnVolume);
            } else if(volumeState == VolumeState.ON){
                //ELSE load the audio_on icon into the image view
                requestManager.load(R.drawable.audio_on)
                        .into(btnVolume);
            }
            btnVolume.animate().cancel();

            //set alpha to 1f, sets image view to visible/opaque
            btnVolume.setAlpha(1f);

            //animate the button, with delay at start
            //alpha set to 0f, sets image view to invisible/transparent
            btnVolume.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(1000);
        }
    }

    public void setMediaObjects(ArrayList<MediaObject> mediaObjects){
        this.mediaObjects = mediaObjects;
    }
}
