package com.pulse.components.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pulse.components.language.model.LanguageAdapterModel
import com.pulse.components.language.repository.LanguageRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import com.pulse.core.locale.LocaleEnum
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LanguageViewModel(private val repository: LanguageRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _languageLiveData by lazy { MutableLiveData<List<LanguageAdapterModel>>() }
    val languageLiveData: LiveData<List<LanguageAdapterModel>> by lazy { _languageLiveData }

    init {
        val selectedLocale = repository.selectedLocale
        _languageLiveData.value = LocaleEnum.values().map {
            LanguageAdapterModel(it, selectedLocale == it)
        }
    }
}