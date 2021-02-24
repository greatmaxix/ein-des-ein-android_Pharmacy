package com.pulse.core.utils

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A) = instance ?: synchronized(this) {
        instance ?: run {
            creator!!(arg)
                .apply {
                    instance = this
                    creator = null
                }
        }
    }
}