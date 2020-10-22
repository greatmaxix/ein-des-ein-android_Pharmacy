package com.pulse.core.dsl

import com.pulse.data.GeneralException

open class ObserveGeneral<DATA>(
    var onEmmit: (DATA.() -> Unit) = {},
    var onError: ((GeneralException) -> Unit)? = null,
    var onProgress: ((Boolean) -> Unit)? = null
)
