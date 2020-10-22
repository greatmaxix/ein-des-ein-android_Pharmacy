package com.pulse.core.extensions

import androidx.recyclerview.widget.ConcatAdapter

fun ConcatAdapter.clearAdapter() = adapters.forEach(::removeAdapter)