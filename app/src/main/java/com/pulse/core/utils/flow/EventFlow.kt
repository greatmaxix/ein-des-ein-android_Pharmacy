package com.pulse.core.utils.flow

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventFlow<T> : StateFlow<Event<T>> {

    private val state = MutableStateFlow(Event<T>(null))

    override val value: Event<T>
        get() = state.value

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Event<T>>) = state.collect(collector)

    override val replayCache: List<Event<T>>
        get() = state.replayCache

    fun postEvent(event: T) {
        state.value = Event(event)
    }
}