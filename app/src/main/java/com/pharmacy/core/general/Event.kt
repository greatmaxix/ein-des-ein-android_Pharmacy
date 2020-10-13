package com.pharmacy.core.general

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    val contentOrNull
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    val peekContent
        get() = content
}