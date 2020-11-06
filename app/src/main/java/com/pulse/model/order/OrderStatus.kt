package com.pulse.model.order

import com.pulse.R

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

    val isCanceled
        get() = this == CANCELED

    val isNew
        get() = this == NEW

    val statusColor
        get() = when (this) {
            NEW -> R.color.blueStatus
            IN_PROGRESS -> R.color.orangeStatus
            READY -> R.color.yellowStatus
            TO_DELIVERY -> R.color.purpleStatus
            IN_DELIVERY -> R.color.violetStatus
            DONE -> R.color.greenStatus
            else -> R.color.redStatus
        }

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

        fun getStatusesList() = listOf(NEW, IN_PROGRESS, DONE)
    }

}