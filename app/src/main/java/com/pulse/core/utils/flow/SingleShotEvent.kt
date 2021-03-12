package com.pulse.core.utils.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SingleShotEvent<T> {

    private val _events = Channel<T>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: T) = _events.send(event)

    fun offerEvent(event: T) = _events.offer(event)
}