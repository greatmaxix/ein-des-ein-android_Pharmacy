package com.pharmacy.myapp.core.dsl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.core.network.Resource
import com.pharmacy.myapp.data.GeneralException

open class ObserveGeneral<DATA>(
    var onEmmit: (DATA.() -> Unit) = {},
    var onError: ((GeneralException) -> Unit)? = null,
    var liveData: LiveData<Resource<DATA>> = MutableLiveData(),
    var onProgress: ((Boolean) -> Unit)? = null
)
