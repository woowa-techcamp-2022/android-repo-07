package org.woowatechcamp.githubapplication.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.R

class SwipeCallback(
    private val dragDirs : Int,
    private val swipeDirs : Int,
    private val context : Context
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private var listener : SwipeListener? = null

    fun setListener(listener: SwipeListener) {
        this.listener = listener
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            listener?.let { it.swipeItem(viewHolder, direction, position) }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val direction = if (dX > 0) MOVE_TO_RIGHT else MOVE_TO_LEFT
            if (direction == MOVE_TO_LEFT) {
                viewHolder.itemView.let {
                    val bg = ColorDrawable()
                    bg.apply {
                        color = ContextCompat.getColor(context, R.color.primary)
                        setBounds(it.right + dX.toInt(), it.top, it.right, it.bottom)
                        draw(c)
                    }
                    val marginEnd = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 38f,
                        context.resources.displayMetrics)
                    val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check_24)
                    icon?.let { ic ->
                        if (dX < - marginEnd - ic.intrinsicWidth) {
                            val halfIcon = ic.intrinsicHeight / 2
                            val top = it.top + ((it.bottom - it.top) / 2 - halfIcon)
                            val left = it.right - halfIcon * 2
                            ic.setBounds(left - marginEnd.toInt(), top, it.right - marginEnd.toInt(), top + ic.intrinsicHeight)
                            ic.draw(c)
                        }
                    }
                }
            }
        }
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    companion object {
        private const val MOVE_TO_RIGHT = 1
        private const val MOVE_TO_LEFT = 0
    }
}