package com.pulse.components.scanner.adapter

import android.view.ViewGroup
import com.pulse.components.product.model.ProductLite
import com.pulse.components.productList.adapter.ProductListViewHolder
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.core.extensions.setDebounceOnClickListener

class ProductListScannerAdapter(private val itemClick: (Int) -> Unit, private val wishClick: (Pair<Boolean, Int>) -> Unit, list: MutableList<ProductLite>) :
    BaseRecyclerAdapter<ProductLite, ProductListViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent, wishClick)
        .apply {
            itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition).globalProductId) }
        }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) = holder.bind(getItem(position))

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

    fun notifyWish(globalProductId: Int) = items.forEachIndexed { index, productLite ->
        if (globalProductId == productLite.globalProductId) {
            productLite.apply {
                wish = !isInWish
                notifyItemChanged(index, isInWish)
            }
        }
    }
}