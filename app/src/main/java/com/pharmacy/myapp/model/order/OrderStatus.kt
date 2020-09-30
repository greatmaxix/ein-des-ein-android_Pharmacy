package com.pharmacy.myapp.model.order

import com.pharmacy.myapp.R

enum class OrderStatus(val status: String) {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    READY("ready"),
    TO_DELIVERY("to_delivery"),
    IN_DELIVERY("in_delivery"),
    DONE("done"),
    CANCELED("canceled");

    private val isInProgress
        get() = this == IN_PROGRESS

    val statusColor
        get() = if (isInProgress) R.color.green else R.color.primaryBlue

    val getStatusRes
        get() = when (this) {
            NEW -> R.string.statusNew
            IN_PROGRESS -> R.string.statusInProgress
            READY -> R.string.statusReady
            TO_DELIVERY -> R.string.statusToDelivery
            IN_DELIVERY -> R.string.statusInDelivery
            DONE -> R.string.statusDone
            else -> R.string.statusCanceled
        }

    companion object {
        fun getOrderStatus(status: String) = values().find { it.status == status } ?: NEW
    }

}