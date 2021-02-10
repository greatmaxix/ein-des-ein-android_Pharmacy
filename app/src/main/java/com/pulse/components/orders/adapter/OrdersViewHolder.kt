package com.pulse.components.orders.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.LayoutMyOrderItemBinding
import com.pulse.model.order.Order

class OrdersViewHolder(view: View, private val click: (Int) -> Unit) : BaseViewHolder<Order>(view) {

    private val binding by viewBinding(LayoutMyOrderItemBinding::bind)

    override fun bind(item: Order) = with(binding) {
        root.setDebounceOnClickListener { click(item.id) }
        val isPickup = item.deliveryInfo.deliveryType?.isPickup.falseIfNull()

        mtvOrderState.backgroundTintList = ColorStateList.valueOf(getColor(item.orderStatus.statusColor))
        mtvOrderState.text = getString(item.orderStatus.getStatusRes)
        item.deliveryInfo.deliveryType?.backgroundColor?.let {
            mcvDeliveryTypeContainer.setCardBackgroundColor(getColor(it))
        }
        item.deliveryInfo.deliveryType?.textRes?.let { mtvDeliveryType.text = getString(it) }
        item.deliveryInfo.deliveryType?.icon?.let(ivDeliveryType::setImageResource)
        mtvOrderNumber.text = getString(R.string.orderNumber, item.id)

        if (isPickup) {
            mtvAddress.text = item.pharmacy.location.address
        } else {
            val cityAndStreet = item.deliveryInfo.addressOrderData?.streetAndCity
            val house = item.deliveryInfo.addressOrderData?.houseAndApartment
            mtvAddress.text = getString(R.string.orderAddressDelivery, cityAndStreet, house)
        }

        mtvProductCount.text = getString(item.productCountString, item.productCount)
        mtvPrice.text = getString(R.string.orderCost, item.pharmacyProductsTotalPrice.formatPrice())
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Int) -> Unit) = OrdersViewHolder(parent.inflate(R.layout.layout_my_order_item), click)
    }
}