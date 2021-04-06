package com.pulse.components.notifications

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.observe
import com.pulse.databinding.FragmentNotificationsBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NotificationsFragment : BaseToolbarFragment<NotificationsViewModel>(R.layout.fragment_notifications, NotificationsViewModel::class) {

    private val binding by viewBinding(FragmentNotificationsBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        viewModel.getNotificationState()

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changePushState(isChecked) }
        switchEmailNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeEmailState(isChecked) }
        switchProductArrivalNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeChatRequestState(isChecked) }
        switchPriceReductionNotifications.setOnCheckedChangeListener { _, isChecked -> viewModel.changeRatingUpdateState(isChecked) }
    }

    override fun onBindStates() = with(lifecycleScope) {
        with(binding) {
            observe(viewModel.notificationStateFlow) {
                switchPushNotifications.isChecked = it.isPushEnabled
                switchEmailNotifications.isChecked = it.isEmailEnabled
                switchProductArrivalNotifications.isChecked = it.isPriceArrivalEnabled
                switchPriceReductionNotifications.isChecked = it.isPriceReductionEnabled
            }
        }
    }
}