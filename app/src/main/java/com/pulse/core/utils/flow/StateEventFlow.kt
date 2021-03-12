package com.pulse.core.utils.flow

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StateEventFlow<T>(initial: T) : StateFlow<T> {

    private val state = MutableStateFlow(initial)

    override val value: T
        get() = state.value

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) = state.collect(collector)

    override val replayCache: List<T>
        get() = state.replayCache

    fun postState(value: T) {
        state.value = value
    }
}