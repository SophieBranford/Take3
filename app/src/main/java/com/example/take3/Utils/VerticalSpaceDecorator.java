/**
 * B00324770
 * UWS 2020/21
 *
 * This Activity's code is inspired by
 * Eddydn's Video Player on GitHub
 *
 */
package com.example.take3.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceDecorator(int verticalSpaceHeight) {
        //set the vertical height to the received integer
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect rectangle, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        //Rect holds four integer coordinates for a rectangle
        //set the top of the rectlangle to the height
        rectangle.top = verticalSpaceHeight;
    }
}
