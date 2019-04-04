package com.unacceptable.unacceptablelibrary.Tools;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HoriztonalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int horizontalSpace;

    public HoriztonalSpaceItemDecoration(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpace;
        outRect.right = horizontalSpace;
    }
}
