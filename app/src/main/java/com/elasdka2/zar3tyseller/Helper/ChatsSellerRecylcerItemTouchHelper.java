package com.elasdka2.zar3tyseller.Helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Adapters.DisplayChatsAdapter;

public class ChatsSellerRecylcerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    public ChatsSellerRecylcerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null){
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreground = ((DisplayChatsAdapter.ViewHolder)viewHolder).view_foreground;
        getDefaultUIUtil().clearView(foreground);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground = ((DisplayChatsAdapter.ViewHolder)viewHolder).view_foreground;
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground = ((DisplayChatsAdapter.ViewHolder)viewHolder).view_foreground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null){
            View foreground = ((DisplayChatsAdapter.ViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().onSelected(foreground);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
