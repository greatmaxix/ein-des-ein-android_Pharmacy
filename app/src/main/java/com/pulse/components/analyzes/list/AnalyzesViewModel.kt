package com.pulse.components.analyzes.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pulse.components.analyzes.details.repository.AnalyzeDetailsRepository
import com.pulse.components.analyzes.list.model.Analyze
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import com.pulse.data.remote.DummyData
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzesViewModel(private val repository: AnalyzeDetailsRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _analyzesListLiveData by lazy { MutableLiveData<List<Analyze>>() }
    val analyzesListLiveData: LiveData<List<Analyze>> by lazy { _analyzesListLiveData }

    init {
        _analyzesListLiveData.postValue(DummyData.analyzesList)
    }
}