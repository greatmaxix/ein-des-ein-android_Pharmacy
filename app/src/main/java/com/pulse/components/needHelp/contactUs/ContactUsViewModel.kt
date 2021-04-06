package com.pulse.components.needHelp.contactUs

import androidx.lifecycle.viewModelScope
import com.pulse.components.needHelp.contactUs.repository.ContactUsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ContactUsViewModel(private val repository: ContactUsRepository) : BaseViewModel() {

    val resultEvent = SingleShotEvent<Unit>()

    fun sendRequest(text: String) = viewModelScope.execute {
        resultEvent.offerEvent(repository.sendRequest(text))
    }
}