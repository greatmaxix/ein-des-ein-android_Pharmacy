package com.pharmacy.myapp.core.utils

import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pharmacy.myapp.core.extensions.isKeyboardOpen

class KeyboardObserver(private var view: View?, private var lifecycle: Lifecycle?) : LifecycleObserver, OnGlobalLayoutListener {

    init {
        lifecycle?.addObserver(this)
    }

    private val observers = mutableListOf<(Boolean) -> Unit>()
    private val openObservers = mutableListOf<() -> Unit>()
    private val closeObservers = mutableListOf<() -> Unit>()

    var keyboardState = view?.isKeyboardOpen ?: false
        private set(value) {
            field = value
            notifyObservers(value)
        }

    override fun onGlobalLayout() {
        val isOpen = view?.isKeyboardOpen ?: false
        if (isOpen != keyboardState) {
            keyboardState = isOpen
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        view = null
        observers.clear()
        openObservers.clear()
        closeObservers.clear()
        lifecycle?.removeObserver(this)
        lifecycle = null
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    private fun startListening() = view?.viewTreeObserver?.addOnGlobalLayoutListener(this)

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    private fun stopListening() = view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

    private fun notifyObservers(isOpen: Boolean) {
        observers.forEach { it.invoke(isOpen) }
        if (isOpen) {
            openObservers.forEach { it.invoke() }
        } else {
            closeObservers.forEach { it.invoke() }
        }
    }

    fun addObserver(action: (Boolean) -> Unit) {
        observers.add(action)
    }

    fun addOnOpenObserver(action: () -> Unit) {
        openObservers.add(action)
    }

    fun addOnCloseObserver(action: () -> Unit) {
        closeObservers.add(action)
    }
}