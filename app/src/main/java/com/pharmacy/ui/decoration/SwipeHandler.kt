package com.pharmacy.ui.decoration

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pharmacy.R
import kotlin.math.abs

class SwipeHandler(private val swiped: (Int) -> Unit) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder) =
        makeMovementFlags(0, ItemTouchHelper.START)

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        swiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, rv: RecyclerView, vh: ViewHolder, dX: Float, dY: Float, state: Int, isActive: Boolean) {
        if (state == ItemTouchHelper.ACTION_STATE_SWIPE) {
            vh.findForeground?.apply {
                alpha = 1.0f - abs(dX) / width.toFloat()
                translationX = dX
            }
        } else {
            super.onChildDraw(c, rv, vh, dX, dY, state, isActive)
        }
    }

    override fun clearView(rv: RecyclerView, vh: ViewHolder) {
        vh.findForeground?.apply {
            alpha = 1f
            translationX = 0f
        }
    }

    override fun onSelectedChanged(vh: ViewHolder?, state: Int) {
        vh?.findForeground?.let { getDefaultUIUtil().onSelected(it) }
    }

    override fun isLongPressDragEnabled() = false

    private val ViewHolder.findForeground: View?
        get() = itemView.findViewById(R.id.foregroundCart)

}