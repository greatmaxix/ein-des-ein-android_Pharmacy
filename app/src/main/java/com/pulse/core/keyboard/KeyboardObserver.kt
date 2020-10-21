package com.pulse.core.keyboard

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pulse.R
import com.pulse.core.extensions.isKeyboardOpen
import com.pulse.core.extensions.setHeight

class KeyboardObserver(private var view: View?) : LifecycleObserver, OnPreDrawListener {

    private val observers = mutableListOf<(Boolean) -> Unit>()
    private val openObservers = mutableListOf<() -> Unit>()
    private val closeObservers = mutableListOf<() -> Unit>()
    private val originViewHeight = view?.height ?: 0
    private val animationDuration = view?.resources?.getInteger(R.integer.animation_time)?.toLong() ?: 0

    private var keyboardState = view?.isKeyboardOpen ?: false
        set(value) {
            field = value
            notifyObservers(value)
        }
    private var keyboardHeight = 0
        set(value) {
            if (value != 0 && field != value) {
                field = value
            }
        }
    private var heightAnimator = ValueAnimator()
    private var isNeedAnimation = false
    private var currentViewHeight = -1

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        observers.clear()
        openObservers.clear()
        closeObservers.clear()

        heightAnimator.cancel()
        heightAnimator.removeAllUpdateListeners()

        view = null
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    private fun startListening() = view?.viewTreeObserver?.addOnPreDrawListener(this)

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    private fun stopListening() = view?.viewTreeObserver?.removeOnPreDrawListener(this)

    private fun notifyObservers(isOpen: Boolean) {
        observers.forEach { it.invoke(isOpen) }
        if (isOpen) {
            openObservers.forEach { it.invoke() }
        } else {
            closeObservers.forEach { it.invoke() }
        }
    }

    fun addObserver(action: (Boolean) -> Unit): KeyboardObserver {
        observers.add(action)
        return this
    }

    fun addOnOpenObserver(action: () -> Unit): KeyboardObserver {
        openObservers.add(action)
        return this
    }

    fun addOnCloseObserver(action: () -> Unit): KeyboardObserver {
        closeObservers.add(action)
        return this
    }

    fun setAnimation(isAnimate: Boolean): KeyboardObserver {
        isNeedAnimation = isAnimate
        return this
    }

    override fun onPreDraw() = isNeedDraw()

    private fun isNeedDraw(): Boolean {
        val contentHeight = (view?.parent as ViewGroup).height

        if (contentHeight == currentViewHeight) {
            return true
        }

        notifyKeyboardState(contentHeight)
        animateIfNeed(contentHeight)

        currentViewHeight = contentHeight

        return false
    }

    private fun notifyKeyboardState(contentHeight: Int) {
        val isOpen = view?.isKeyboardOpen ?: false
        if (isOpen != keyboardState) {
            keyboardState = isOpen
            keyboardHeight = originViewHeight - contentHeight
        }
    }

    private fun animateIfNeed(contentHeight: Int) {
        if (currentViewHeight != -1 && isNeedAnimation) {
            heightAnimator.cancel()
            heightAnimator = ValueAnimator.ofInt(currentViewHeight, contentHeight).apply {
                duration = animationDuration
                interpolator = FastOutSlowInInterpolator()
            }
            heightAnimator.addUpdateListener { view?.setHeight(it.animatedValue as Int) }
            heightAnimator.start()
        }
    }
}