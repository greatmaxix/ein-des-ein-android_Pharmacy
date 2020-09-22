package com.pharmacy.myapp.cart.adapter.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View.ROTATION
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.cart.adapter.CartViewHolder

class ItemExpandAnimator : DefaultItemAnimator() {

    override fun recordPreLayoutInformation(state: RecyclerView.State, vh: RecyclerView.ViewHolder, changeFlags: Int, payloads: MutableList<Any>) =
        if (vh.isHeader) ItemInfoHeader.newInstance(vh) else super.recordPreLayoutInformation(state, vh, changeFlags, payloads)

    override fun recordPostLayoutInformation(state: RecyclerView.State, vh: RecyclerView.ViewHolder) =
        if (vh.isHeader) ItemInfoHeader.newInstance(vh) else super.recordPostLayoutInformation(state, vh)

    override fun animateChange(oldVH: RecyclerView.ViewHolder, vh: RecyclerView.ViewHolder, pre: ItemHolderInfo, post: ItemHolderInfo): Boolean {
        if (pre is ItemInfoHeader && post is ItemInfoHeader && vh is CartViewHolder.CartHeaderViewHolder) {
            ObjectAnimator.ofFloat(vh.ivArrow, ROTATION, pre.arrowRotation, post.arrowRotation)
                .apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            vh.ivArrow.rotation = post.arrowRotation
                            dispatchAnimationFinished(vh)
                        }
                    })
                    start()
                }
        }
        return super.animateChange(oldVH, vh, pre, post)
    }

    override fun canReuseUpdatedViewHolder(vh: RecyclerView.ViewHolder) = true

    private val RecyclerView.ViewHolder.isHeader
        get() = this is CartViewHolder.CartHeaderViewHolder
}