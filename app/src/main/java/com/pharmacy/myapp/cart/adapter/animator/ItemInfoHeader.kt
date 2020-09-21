package com.pharmacy.myapp.cart.adapter.animator

import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.cart.adapter.CartViewHolder

class ItemInfoHeader : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var arrowRotation = 0f
        private set

    override fun setFrom(holder: RecyclerView.ViewHolder): RecyclerView.ItemAnimator.ItemHolderInfo {
        if (holder is CartViewHolder.CartHeaderViewHolder)
            arrowRotation = holder.ivArrow.rotation

        return super.setFrom(holder)
    }

    companion object {

        fun newInstance(vh: RecyclerView.ViewHolder) = ItemInfoHeader().apply { setFrom(vh) }

    }
}