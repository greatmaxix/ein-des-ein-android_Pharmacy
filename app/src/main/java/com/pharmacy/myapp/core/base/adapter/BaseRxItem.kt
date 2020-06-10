package com.pharmacy.myapp.core.base.adapter

import io.reactivex.Flowable

open class BaseRxItem<F> (val flowable: Flowable<F>?)