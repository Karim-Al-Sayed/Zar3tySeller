package com.elasdka2.zar3tyseller.Helper;

import androidx.recyclerview.widget.RecyclerView;

public interface ChatsRecylcerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
