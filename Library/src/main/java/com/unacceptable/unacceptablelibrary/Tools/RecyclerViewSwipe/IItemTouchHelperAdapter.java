package com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe;

public interface IItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
