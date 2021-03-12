package com.pulse.components.privileges

import androidx.lifecycle.LiveData
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class PrivilegeViewModel : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }
}