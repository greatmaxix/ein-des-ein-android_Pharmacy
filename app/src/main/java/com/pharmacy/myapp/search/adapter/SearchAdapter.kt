package com.pharmacy.myapp.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.product.Product

class SearchAdapter(private val itemClick: (Product?) -> Unit) : PagingDataAdapter<Product, SearchViewHolder>(SearchDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder = SearchViewHolder.newInstance(parent).apply {
        itemView.onClick { itemClick(getItem(bindingAdapterPosition)) }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}