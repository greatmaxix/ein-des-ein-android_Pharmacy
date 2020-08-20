package com.pharmacy.myapp.quickAccess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.quickAccess.repository.QuickAccessRepository

class QuickAccessViewModel(private val repository: QuickAccessRepository) : BaseViewModel() {

    private val _oftenLiveData by lazy { MutableLiveData<List<String>>() }
    val oftenLiveData: LiveData<List<String>> by lazy { _oftenLiveData }

    init {
        _oftenLiveData.postValue(listOf("Спазмы", "Головная боль", "Болит живот", "ОРВИ", "Дротаверин", "Но Шпа", "Терафлю")) // todo
    }

    val quickAccessLiveData by lazy { repository.quickAccessLiveData }

    fun clearQuickAccess() {
        launchIO { repository.clearQuickAccess() }
    }

    fun addQuickAccess(text: String): String {
        if (text.trim().isNotEmpty()) {
            launchIO { repository.addQuickAccess(text) }
        }
        return text
    }
}