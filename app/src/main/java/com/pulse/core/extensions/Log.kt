package com.pulse.core.extensions

import com.pulse.BuildConfig
import timber.log.Timber

@Suppress("NOTHING_TO_INLINE")
inline fun Any?.log(prefix: String = "object:") = Timber.d("$prefix${toString()}")

inline fun debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}
