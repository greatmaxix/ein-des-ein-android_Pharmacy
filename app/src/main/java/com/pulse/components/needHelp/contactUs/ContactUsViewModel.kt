package com.pulse.components.needHelp.contactUs

import androidx.lifecycle.LiveData
import com.pulse.components.needHelp.contactUs.repository.ContactUsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ContactUsViewModel(private val repository: ContactUsRepository) : BaseViewModel() {

    private val _resultLiveData by lazy { SingleLiveEvent<Unit>() }
    val resultLiveData: LiveData<Unit> by lazy { _resultLiveData }

    fun sendRequest(text: String) /*= requestLiveData */ { // TODO implement request sending
        repository.sendRequest(text)
        _resultLiveData.postValue(Unit)
    }
}