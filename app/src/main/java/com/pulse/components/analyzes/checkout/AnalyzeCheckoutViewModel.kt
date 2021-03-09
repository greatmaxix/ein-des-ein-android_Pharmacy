package com.pulse.components.analyzes.checkout

import androidx.lifecycle.LiveData
import com.pulse.components.analyzes.checkout.repository.AnalyzeCheckoutRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension
import java.time.LocalDateTime

@KoinApiExtension
class AnalyzeCheckoutViewModel(private val repository: AnalyzeCheckoutRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    val customerInfoLiveData = repository.getCustomerInfo()

    private var selectedDateTime = LocalDateTime.now()
    private val _selectedDateTimeLiveData by lazy { SingleLiveEvent<LocalDateTime>() }
    val selectedDateTimeLiveData: LiveData<LocalDateTime> by lazy { _selectedDateTimeLiveData }

    private val _pickDateTimeLiveData by lazy { SingleLiveEvent<LocalDateTime>() }
    val pickDateTimeLiveData: LiveData<LocalDateTime> by lazy { _pickDateTimeLiveData }

    fun handlePromoCodeResult(code: String) {
        // TODO
    }

    fun pickDateTime() {
        _pickDateTimeLiveData.postValue(selectedDateTime)
    }

    fun setDateTime(localDateTime: LocalDateTime) {
        selectedDateTime = localDateTime
        _selectedDateTimeLiveData.postValue(localDateTime)
    }
}