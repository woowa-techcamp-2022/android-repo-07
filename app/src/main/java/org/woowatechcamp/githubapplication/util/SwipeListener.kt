package org.woowatechcamp.githubapplication.util

import androidx.recyclerview.widget.RecyclerView

interface SwipeListener {
    fun swipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
}