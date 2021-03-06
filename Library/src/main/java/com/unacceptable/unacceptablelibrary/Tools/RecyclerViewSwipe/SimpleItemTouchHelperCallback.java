package com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;

/**
 * https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final NewAdapter m_Adapter;
    private boolean m_bAllowSwipe = true;

    private int m_iSwipeFlags;

    private boolean m_bSwipeBack;

    public SimpleItemTouchHelperCallback(NewAdapter adapter) {
        m_Adapter = adapter;
        m_iSwipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;

        if (m_bAllowSwipe) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = m_iSwipeFlags;// ItemTouchHelper.START | ItemTouchHelper.END;
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

    public void setSwipeFlags(int iSwipeFlags) {
        m_iSwipeFlags = iSwipeFlags;
    }

}
