package com.pharmacy.myapp.scanner

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.scanner.repository.ScannerRepository
import kotlinx.coroutines.delay
import timber.log.Timber

class ScannerViewModel(private val repository: ScannerRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _descriptionVisibility by lazy { SingleLiveEvent<Boolean>() }
    val descriptionVisibility: LiveData<Boolean> by lazy { _descriptionVisibility }

    // TODO set proper type
    private val _searchResultItem by lazy { SingleLiveEvent<Any>() }
    val searchResultItem: LiveData<Any> by lazy { _searchResultItem }

    init {
        _descriptionVisibility.value = !repository.isQrCodeDescriptionShown()
    }

    fun descriptionViewed() {
        _descriptionVisibility.value = false
        repository.setDescriptionShown()
    }

    fun searchQrCode(code: String) {
        launchIO {
            // TODO implement code search
            // repository.searchQrCode(code)

            // --- STUB start ---
            _progressLiveData.postValue(true)
            delay(2000)
            _progressLiveData.postValue(false)

            _searchResultItem.postValue(Any())
            Timber.i(code)
            // --- STUB end ---
        }
    }
}