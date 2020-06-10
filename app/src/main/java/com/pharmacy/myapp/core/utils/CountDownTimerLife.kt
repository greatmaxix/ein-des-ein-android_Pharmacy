package com.pharmacy.myapp.core.utils

import android.os.CountDownTimer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class CountDownTimerLife(
    private var tick: ((Long) -> Unit)? = null,
    private var finish: (() -> Unit)? = null,
    allTime: Long = 20000,
    interval: Long = 1000
) : CountDownTimer(allTime, interval), LifecycleObserver {

    override fun onTick(millisUntilFinished: Long) {
        tick?.invoke(millisUntilFinished)
    }

    override fun onFinish() {
        finish?.invoke()
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    private fun stopTicks() {
        cancel()
        tick = null
        finish = null
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    private fun startTicks() = start()
}