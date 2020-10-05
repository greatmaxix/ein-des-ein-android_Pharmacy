package com.pharmacy.myapp.data.remote.rest.request.order

import com.pharmacy.myapp.R

enum class DeliveryType(val type: String) {
    DELIVERY("delivery_address"), PICKUP("pickup");

    companion object {
        fun getDeliveryType(type: String) = values().find { it.type == type } ?: PICKUP
    }

    val isPickup
        get() = this == PICKUP

    val isDelivery
        get() = this == DELIVERY

    val textRes
        get() = if (isPickup) R.string.pickup else R.string.delivery

    val icon
        get() = if (isPickup) R.drawable.ic_shopping_bag else R.drawable.ic_delivery

    val backgroundColor
        get() = if (isPickup) R.color.deliveryPickup else R.color.deliveryAddress
}