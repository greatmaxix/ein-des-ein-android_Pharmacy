package com.pulse.components.notifications

import com.pulse.components.notifications.model.NotificationState
import com.pulse.components.notifications.repository.NotificationsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsViewModel(private val repository: NotificationsRepository) : BaseViewModel() {

    private var notificationState: NotificationState =
        NotificationState(isPushEnabled = true, isEmailEnabled = true, isPriceArrivalEnabled = true, isPriceReductionEnabled = false) // TODO get from repo
    val notificationStateFlow = StateEventFlow(notificationState)

    fun changePushState(checked: Boolean) {
        notificationState = notificationState.copy(isPushEnabled = checked)
    }

    fun changeEmailState(checked: Boolean) {
        notificationState = notificationState.copy(isEmailEnabled = checked)
    }

    fun changeChatRequestState(checked: Boolean) {
        notificationState = notificationState.copy(isPriceArrivalEnabled = checked)
    }

    fun changeRatingUpdateState(checked: Boolean) {
        notificationState = notificationState.copy(isPriceReductionEnabled = checked)
    }

    fun getNotificationState() {
        notificationStateFlow.postState(notificationState)
    }
}