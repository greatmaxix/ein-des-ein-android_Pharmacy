package com.pulse.core.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import com.pulse.core.utils.flow.EventFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> LifecycleCoroutineScope.observeEvent(flow: EventFlow<T>, action: (value: T) -> Unit) {
    launchWhenResumed {
        flow.collect { event ->
            event.contentOrNull?.let { action(it) }
        }
    }
}

fun <T> LifecycleCoroutineScope.observe(flow: Flow<T>, action: (value: T) -> Unit) {
    launchWhenResumed {
        flow.collect { event ->
            action(event)
        }
    }
}