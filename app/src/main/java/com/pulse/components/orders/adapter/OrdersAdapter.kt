package com.pulse.components.orders.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.model.order.Order

class OrdersAdapter(private val click: (Int) -> Unit) : PagingDataAdapter<Order, OrdersViewHolder>(OrderDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrdersViewHolder.newInstance(parent, click)

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}