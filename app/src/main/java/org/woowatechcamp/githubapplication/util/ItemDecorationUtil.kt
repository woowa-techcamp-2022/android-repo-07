package org.woowatechcamp.githubapplication.util

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

sealed class ItemDecorationUtil {
    class ItemDividerDecoration(
        private val height: Float,
        private val startPadding: Float,
        @ColorInt
        private val color: Int
    ) : RecyclerView.ItemDecoration() {
        private val paint = Paint()

        init {
            paint.color = color
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val start = parent.paddingStart + startPadding
            val end = parent.width - parent.paddingEnd

            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = (child.bottom + params.bottomMargin).toFloat()
                val bottom = top + height

                c.drawRect(start, top, end.toFloat(), bottom, paint)
            }
        }
    }
}
