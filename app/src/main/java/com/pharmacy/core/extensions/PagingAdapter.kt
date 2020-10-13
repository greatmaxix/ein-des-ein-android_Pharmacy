package com.pharmacy.core.extensions

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter

fun PagingDataAdapter<*, *>.addStateListener(func: (Boolean) -> Unit) = addLoadStateListener { func(it.refresh is LoadState.Loading || it.append is LoadState.Loading) }