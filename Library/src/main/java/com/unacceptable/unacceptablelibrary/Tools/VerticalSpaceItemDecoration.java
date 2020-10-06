package com.unacceptable.unacceptablelibrary.Tools;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final boolean bAddSpaceToLastItem;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, boolean bAddSpaceToLastItem) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.bAddSpaceToLastItem = bAddSpaceToLastItem;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (bAddSpaceToLastItem || parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.top = verticalSpaceHeight;
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
