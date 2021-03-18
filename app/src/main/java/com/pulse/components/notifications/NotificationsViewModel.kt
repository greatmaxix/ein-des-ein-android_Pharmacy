package com.pulse.components.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pulse.components.notifications.model.NotificationState
import com.pulse.components.notifications.repository.NotificationsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsViewModel(private val repository: NotificationsRepository) : BaseViewModel() {

    private var notificationState: NotificationState =
        NotificationState(isPushEnabled = true, isEmailEnabled = true, isPriceArrivalEnabled = true, isPriceReductionEnabled = false) // TODO get from repo
    private val _notificationStateLiveData by lazy { MutableLiveData<NotificationState>() }
    val notificationStateLiveData: LiveData<NotificationState> by lazy { _notificationStateLiveData }

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
        _notificationStateLiveData.value = notificationState
    }
}