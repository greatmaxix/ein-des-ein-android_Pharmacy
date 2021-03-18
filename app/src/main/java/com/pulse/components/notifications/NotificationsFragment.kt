package com.pulse.components.notifications

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.databinding.FragmentNotificationsBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsFragment(private val viewModel: NotificationsViewModel) : BaseMVVMFragment(R.layout.fragment_notifications) {

    private val binding by viewBinding(FragmentNotificationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        viewModel.getNotificationState()

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changePushState(isChecked) }
        switchEmailNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeEmailState(isChecked) }
        switchProductArrivalNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeChatRequestState(isChecked) }
        switchPriceReductionNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeRatingUpdateState(isChecked) }
    }

    override fun onBindLiveData() = with(binding) {
        observe(viewModel.notificationStateLiveData) {
            switchPushNotifications.isChecked = it.isPushEnabled
            switchEmailNotifications.isChecked = it.isEmailEnabled
            switchProductArrivalNotifications.isChecked = it.isPriceArrivalEnabled
            switchPriceReductionNotifications.isChecked = it.isPriceReductionEnabled
        }
    }
}