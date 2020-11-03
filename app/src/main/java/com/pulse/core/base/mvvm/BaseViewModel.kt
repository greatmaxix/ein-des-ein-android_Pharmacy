package com.pulse.core.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.pulse.core.network.Resource.*
import com.pulse.data.GeneralErrorHandler
import com.pulse.data.GeneralException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun launchIO(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(IO, block = block)

    protected val errorHandler by lazy { GeneralErrorHandler() }

    protected fun <R> requestLiveData(needLoading: Boolean = true, dispatcher: CoroutineDispatcher = IO, request: suspend () -> R) =
        liveData(viewModelScope.coroutineContext + dispatcher) {
            if (needLoading) {
                emit(Progress(true))
            }
            try {
                emit(Success(request()))
            } catch (e: GeneralException) {
                emit(Error(errorHandler.checkThrowable(e)))
            } catch (e: Exception) {
                emit(Error(errorHandler.checkThrowable(e)))
            }
        }
}