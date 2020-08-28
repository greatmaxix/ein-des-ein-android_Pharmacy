package com.pharmacy.myapp.main

import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    private val destIdLiveData by lazy { MutableLiveData<Int>() }
    val directionLiveData: LiveData<Int> by lazy {
        destIdLiveData.switchMap { id ->
            liveData(viewModelScope.coroutineContext + IO) {
                val isCustomerExist = repository.isCustomerExist()
                emit(if (id == R.id.profile_graph && !isCustomerExist) R.id.profile_graph_guest else id)
            }
        }
    }

    fun navSelected(@IdRes id: Int) = destIdLiveData.postValue(id)

}