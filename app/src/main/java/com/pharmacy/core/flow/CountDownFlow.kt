package com.pharmacy.core.flow

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class CountDownFlow(private val millisInFuture: Long = 60000L, private val interval: Long = 1000L, private val delay: Long = 1000L) : Flow<Long> {

    private val tickCount = millisInFuture / interval

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Long>) {
        collector.emit(millisInFuture)
        (tickCount downTo 0).forEach {
            collector.emit(it * interval)
            delay(delay)
        }
    }
}