package com.pulse.core.extensions

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter

val concatWithIsolate
    get() = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()

fun ConcatAdapter.clearAdapter() = adapters.forEach(::removeAdapter)

fun PagingDataAdapter<*, *>.isEmpty() = itemCount == 0