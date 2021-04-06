package com.pulse.core.base.mvvm

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pulse.core.manager.JobManager
import com.pulse.core.utils.flow.EventFlow
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.GeneralErrorHandler
import com.pulse.data.GeneralException
import com.pulse.data.remote.APIErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val theme = EventFlow<Resources.Theme>()
    val loadingState by lazy { StateEventFlow(false) }
    val messageEvent by lazy { EventFlow<String>() }
    val fieldErrorEvent by lazy { EventFlow<List<APIErrors>>() }
    val navEvent by lazy { EventFlow<NavDirections>() }
    private val jobManager = JobManager(viewModelScope)
    protected val errorHandler by lazy { GeneralErrorHandler() }

    protected fun CoroutineScope.execute(function: suspend () -> Unit) {
        launch(IO) {
            loadingState.postState(true)
            try {
                function()
            } catch (e: Throwable) {
                checkFieldErrors(errorHandler.checkThrowable(e))
                loadingState.postState(false)
            } finally {
                loadingState.postState(false)
            }
        }
    }

    protected suspend fun <T> execute(function: suspend () -> T): T? {
        loadingState.postState(true)
        return try {
            function()
        } catch (e: Throwable) {
            checkFieldErrors(errorHandler.checkThrowable(e))
            loadingState.postState(false)
            null
        } finally {
            loadingState.postState(false)
        }
    }

    private fun checkFieldErrors(exception: GeneralException) = viewModelScope.launch {
        // TODO implement base error handling
        messageEvent.postEvent(getString(exception.resId))
        //val errors = exception.data?.messages?.map { APIErrors.getByKey(it.message) } ?: arrayListOf()
//        when {
            //errors.isEmpty() -> messageEvent.postEvent(getString(R.string.something_goes_wrong)) // TODO change to proper text
            //errors.any { it.isFieldError } -> fieldErrorEvent.postEvent(errors)
            //else -> messageEvent.postEvent(getString(errors.first().errorResId))
//        }
    }

    protected fun CoroutineScope.observe(tag: Any, function: suspend () -> Unit) = jobManager.launch(tag) {
        this@observe.launch { function() }
    }

    /**
     * Retry all subscriptions if somewhere catch error
     * */
    fun retry() = jobManager.retry()

    protected fun getString(@StringRes strId: Int) =
        theme.value.peekContent?.resources?.getString(strId).orEmpty()
}