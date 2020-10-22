package com.pulse.core.extensions

import timber.log.Timber

@Suppress("NOTHING_TO_INLINE")
inline fun Any?.log(prefix: String = "object:") = Timber.d("$prefix${toString()}")