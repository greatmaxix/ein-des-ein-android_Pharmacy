package com.pharmacy.myapp.core.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.pharmacy.myapp.core.general.Event
import com.pharmacy.myapp.core.network.Resource.*
import com.pharmacy.myapp.data.GeneralErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun launchIO(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(IO, block = block)

    private val errorHandler by lazy { GeneralErrorHandler() }

    protected fun <R> requestLiveData(needLoading: Boolean = true, dispatcher: CoroutineDispatcher = IO, request: suspend () -> R) =
        liveData(viewModelScope.coroutineContext + dispatcher) {
            if (needLoading) {
                emit(Progress(true))
            }
            try {
                emit(Success(request()))
            } catch (e: Exception) {
                emit(Progress(false))
                emit(Error(errorHandler.checkThrowable(e)))
            }
        }

    protected fun <R> requestEventLiveData(needLoading: Boolean = true, dispatcher: CoroutineDispatcher = IO, request: suspend () -> R) =
        liveData(viewModelScope.coroutineContext + dispatcher) {
            if (needLoading) {
                emit(Progress(true))
            }
            try {
                emit(Success(Event(request())))
            } catch (e: Exception) {
                emit(Progress(false))
                emit(Error(errorHandler.checkThrowable(e)))
            }
        }
}