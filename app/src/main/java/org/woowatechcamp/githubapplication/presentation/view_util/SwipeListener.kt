package org.woowatechcamp.githubapplication.presentation.view_util

import androidx.recyclerview.widget.RecyclerView

interface SwipeListener {
    fun swipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int, position : Int)
}