package com.pharmacy.myapp.produtcList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.product.model.ProductLite

object ProductListDiffUtil : DiffUtil.ItemCallback<ProductLite>() {

    override fun areItemsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem.globalProductId == newItem.globalProductId

    override fun areContentsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem == newItem

}