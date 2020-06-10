package com.pharmacy.myapp.core.base.adapter.rx

import io.reactivex.Flowable

open class BaseRxItem<F> (val flowable: Flowable<F>?)