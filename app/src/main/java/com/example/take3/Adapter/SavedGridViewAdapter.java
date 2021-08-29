/**
 * B00324770
 * UWS 2020/21
 */
package com.example.take3.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.take3.Model.Upload;
import com.example.take3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Upload> mUploads;

    public SavedGridViewAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    public int getCount() {
       return mUploads.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View view, ViewGroup parent) {

        ImageView imageView;

        //IF the view is null
        if (view == null) {
            imageView = new ImageView(mContext);
            //set the width and height of the image view
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            //set scale type to center crop
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //set padding to top and bottom of image
            imageView.setPadding(0, 16, 0, 16);
        } else {
            //ELSE set the image view to the view
            imageView = (ImageView) view;
        }

        //get position of item in Upload array list
        Upload uploadCurrent = mUploads.get(position);

        //get the image URL and load it with Picasso into image view
        //set the app logo as the placeholder while the app is loading the image
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.applogo)
                .fit()
                .centerInside()
                .into(imageView);

        return imageView;
    }
}
