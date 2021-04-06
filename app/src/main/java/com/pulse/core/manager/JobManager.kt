package com.pulse.core.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JobManager constructor(private val viewModeScope: CoroutineScope) {

    private val map = HashMap<Any, Job>()
    private val mapActions = HashMap<Any, suspend CoroutineScope.() -> Unit>()

    fun launch(tag: Any, block: suspend CoroutineScope.() -> Unit) {
        manage(tag, block)
    }

    fun retry() = mapActions.entries.forEach { manage(it.key, it.value) }

    fun cancel(vararg tags: Any) {
        map.filterKeys { tags.contains(it) }.forEach {
            it.value.cancel()
        }
    }

    private fun manage(tag: Any, block: suspend CoroutineScope.() -> Unit) {
        mapActions[tag] = block
        map[tag]?.cancel()
        map[tag] = viewModeScope.launch {
            block()
        }
    }
}