package com.pharmacy.myapp.produtcList

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.product.model.ProductLite

class ProductListAdapter(private val itemClick: (Int) -> Unit, private val onClick: (Triple<Boolean, Int, Int>) -> Unit) :
    PagingDataAdapter<ProductLite, ProductListViewHolder>(ProductListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent, onClick).apply {
        itemView.onClick { itemClick(getItem(bindingAdapterPosition)!!.globalProductId) }
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

    fun notifyWish(position: Int) {
        getItem(position)?.let { product ->
            product.wish = product.wish?.let { !it } ?: true
            notifyItemChanged(position, product.isWish)
        }
    }
}