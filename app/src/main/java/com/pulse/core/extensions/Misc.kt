package com.pulse.core.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

fun valueAnimatorMedium(duration: Long = 400, tick: (Float) -> Unit): ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
    addUpdateListener { tick(it.animatedFraction) }
    this.duration = duration
    start()
}

fun valueAnimatorARGB(from: Int, to: Int, duration: Long, onUpdate: (Int) -> Unit): ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
    addUpdateListener { onUpdate(it.animatedValue as Int) }
    this.duration = duration
    start()
}

fun valueAnimatorInt(from: Int, to: Int, duration: Long, onUpdate: (Int) -> Unit): ValueAnimator = ValueAnimator.ofInt(from, to).apply {
    addUpdateListener { onUpdate(it.animatedValue as Int) }
    interpolator = FastOutSlowInInterpolator()
    this.duration = duration
    start()
}

fun <T> lazyNotSynchronized(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)