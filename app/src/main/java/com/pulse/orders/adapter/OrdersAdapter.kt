package com.pulse.orders.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.model.order.Order
import kotlinx.android.synthetic.main.layout_my_order_item.view.*

class OrdersAdapter(private val click: (Int) -> Unit) : PagingDataAdapter<Order, OrdersAdapter.OrdersViewHolder>(OrderDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrdersViewHolder.newInstance(parent, click)

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    class OrdersViewHolder(view: View, private val click: (Int) -> Unit) : BaseViewHolder<Order>(view) {

        override fun bind(item: Order) = with(itemView) {
            setDebounceOnClickListener { click(item.id) }
            val isPickup = item.deliveryInfo.deliveryType?.isPickup.falseIfNull()

            tvOrderState.backgroundTintList = ColorStateList.valueOf(colorCompat(item.orderStatus.statusColor))
            tvOrderState.text = stringRes(item.orderStatus.getStatusRes)
            item.deliveryInfo.deliveryType?.backgroundColor?.let {
                deliveryTypeContainer.setCardBackgroundColor(colorCompat(it))
            }
            item.deliveryInfo.deliveryType?.textRes?.let { deliveryTypeOrders.text = stringRes(it) }
            item.deliveryInfo.deliveryType?.icon?.let(ivDeliveryTypeOrders::setImageResource)
            idOrders.text = stringRes(R.string.orderNumber, item.id)

            if (isPickup) {
                addressOrders.text = item.pharmacy.location.address
            } else {
                val cityAndStreet = item.deliveryInfo.addressOrderData?.streetAndCity
                val house = item.deliveryInfo.addressOrderData?.houseAndApartment
                addressOrders.text = stringRes(R.string.orderAddressDelivery, cityAndStreet, house)
            }

            tvProductCount.text = stringRes(item.productCountString, item.productCount)
            priceOrders.text = stringRes(R.string.orderCost, item.pharmacyProductsTotalPrice.formatPrice())
        }

        companion object {

            fun newInstance(parent: ViewGroup, click: (Int) -> Unit) = OrdersViewHolder(parent.inflate(R.layout.layout_my_order_item), click)
        }
    }
}