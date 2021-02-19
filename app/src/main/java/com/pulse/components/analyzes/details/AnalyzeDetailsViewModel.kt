package com.pulse.components.analyzes.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pulse.components.analyzes.details.repository.AnalyzeDetailsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeDetailsViewModel(private val repository: AnalyzeDetailsRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _clinicsListLiveData by lazy { MutableLiveData<String>() }
    val clinicsListLiveData by lazy {
        _clinicsListLiveData.switchMap {
            requestLiveData { repository.clinicsList(it).items }
        }
    }

    fun requestClinics(code: String) = _clinicsListLiveData.postValue(code)
}