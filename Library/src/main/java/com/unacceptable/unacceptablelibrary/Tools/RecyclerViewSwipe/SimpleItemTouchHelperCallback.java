package com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;

/**
 * https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final NewAdapter m_Adapter;
    private boolean m_bAllowSwipe = true;

    public SimpleItemTouchHelperCallback(NewAdapter adapter) {
        m_Adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;

        if (m_bAllowSwipe) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        m_Adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        m_Adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public void setAllowSwipe(boolean bAllowSwipe) {
        m_bAllowSwipe = bAllowSwipe;
    }
}
