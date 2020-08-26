package com.pharmacy.myapp.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.product.model.ProductLite

class SearchAdapter(private val itemClick: (Int) -> Unit) : PagingDataAdapter<ProductLite, SearchViewHolder>(SearchDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder = SearchViewHolder.newInstance(parent).apply {
        itemView.onClick { itemClick(getItem(bindingAdapterPosition)!!.globalProductId) }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}