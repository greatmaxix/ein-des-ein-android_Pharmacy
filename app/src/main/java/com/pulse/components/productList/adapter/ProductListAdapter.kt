package com.pulse.components.productList.adapter

import android.view.ViewGroup
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingDataAdapter
import com.pulse.components.product.model.ProductLite
import com.pulse.core.extensions.setDebounceOnClickListener

class ProductListAdapter(private val itemClick: (Int) -> Unit, private val wishClick: (Pair<Boolean, Int>) -> Unit) :
    PagingDataAdapter<ProductLite, ProductListViewHolder>(ProductListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent, wishClick).apply {
        itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition)!!.globalProductId) }
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val payload = payloads[0]
            if (payload is Boolean) {
                holder.notifyHeart(payload)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun notifyWish(globalProductId: Int) {
        snapshot().findItemWithPosition { it?.globalProductId == globalProductId }.let {
            val (product, position) = it
            product?.apply {
                wish = !isInWish
                notifyItemChanged(position, isInWish)
            }
        }
    }

    private fun <T> ItemSnapshotList<T>.findItemWithPosition(predicate: (T?) -> Boolean) = find(predicate).run { this to indexOf(this) }
}
