package com.pharmacy.components.cart.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.components.cart.model.CartItem

class CartAdapter(
    private val cartItem: CartItem,
    private val removeClick: (Int) -> Unit,
    private val notifyCheckout: (CartItem) -> Unit
) : RecyclerView.Adapter<CartViewHolder<*>>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_HEADER = 2
        private const val VIEW_TYPE_FOOTER = 3
    }

    private var isExpanded = false
        set(value) {
            field = value
            if (field) notifyItemRangeInserted(1, productsCount) else notifyItemRangeRemoved(1, productsCount)
            notifyItemChanged(0)
        }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> VIEW_TYPE_HEADER
        1 -> if (isExpanded) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
        productsCount + 1 -> VIEW_TYPE_FOOTER
        else -> VIEW_TYPE_ITEM
    }

    override fun getItemCount() = if (isExpanded) productsCount + 2 else 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_HEADER -> CartViewHolder.CartHeaderViewHolder.newInstance(parent)
        VIEW_TYPE_FOOTER -> CartViewHolder.CartFooterViewHolder.newInstance(parent, notifyCheckout)
        else -> CartViewHolder.CartItemViewHolder.newInstance(parent, removeClick)
    }

    override fun onBindViewHolder(holder: CartViewHolder<*>, position: Int) {
        when (holder) {
            is CartViewHolder.CartItemViewHolder -> {
                val itemPosition = position - 1
                holder.bind(cartItem.products[itemPosition] to { count -> notifyProductCountChange(count, itemPosition) })
            }
            is CartViewHolder.CartHeaderViewHolder -> holder.bind(Triple(cartItem, isExpanded, { isExpanded = !isExpanded }))
            is CartViewHolder.CartFooterViewHolder -> holder.bind(cartItem)
        }
    }

    private fun notifyProductCountChange(count: Int, position: Int) {
        cartItem.updateCount(count, position)
        notifyItemChanged(itemCount - 1)
    }

    fun notifyRemoveIfContains(productId: Int, removeAdapter: (CartAdapter) -> Unit) {
        if (cartItem.products.removeIf { it.productId == productId }) {
            if (cartItem.products.isNotEmpty()) {
                notifyDataSetChanged()
            } else {
                removeAdapter(this)
            }
        }
    }

    private val productsCount
        get() = cartItem.products.size
}