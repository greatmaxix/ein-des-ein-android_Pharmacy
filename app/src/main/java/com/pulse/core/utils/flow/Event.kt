package com.pulse.core.utils.flow

import java.util.concurrent.atomic.AtomicBoolean

open class Event<out T>(private val content: T?) {

    var hasBeenHandled = AtomicBoolean(false)
        private set

    val contentOrNull
        get() = if (hasBeenHandled.get()) {
            null
        } else {
            hasBeenHandled.set(true)
            content
        }

    val peekContent
        get() = content
}