package com.example.ienning.ncuhome.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ienning on 16-10-21.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int space;
    public SpaceItemDecoration(int space) {
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.top = space;
        }
    }
}
