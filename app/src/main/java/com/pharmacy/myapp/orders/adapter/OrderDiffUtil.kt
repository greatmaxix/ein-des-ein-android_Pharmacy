package com.pharmacy.myapp.orders.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.model.order.Order

object OrderDiffUtil : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Order, newItem: Order) = true
}