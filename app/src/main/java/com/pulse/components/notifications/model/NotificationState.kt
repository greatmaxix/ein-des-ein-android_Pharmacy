package com.pulse.components.notifications.model

data class NotificationState(
    val isPushEnabled: Boolean,
    val isEmailEnabled: Boolean,
    val isPriceArrivalEnabled: Boolean,
    val isPriceReductionEnabled: Boolean
)