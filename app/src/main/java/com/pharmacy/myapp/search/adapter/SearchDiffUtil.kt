package com.pharmacy.myapp.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.model.product.Product

object SearchDiffUtil : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.globalProductId == newItem.globalProductId

    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

}