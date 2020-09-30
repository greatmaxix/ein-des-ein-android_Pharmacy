package com.pharmacy.myapp.core.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun launchIO(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(IO, block = block)
}