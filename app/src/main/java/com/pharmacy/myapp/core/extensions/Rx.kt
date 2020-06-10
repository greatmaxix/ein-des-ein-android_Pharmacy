package com.pharmacy.myapp.core.extensions

import com.pharmacy.myapp.core.utils.EmptyConsumer
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeEmpty(consumer: (T) -> Unit = {}): Disposable = subscribe(Consumer { consumer.invoke(it) }, EmptyConsumer())

fun <T> Flowable<T>.subscribeEmpty(consumer: (T) -> Unit = {}): Disposable = subscribe(Consumer { consumer.invoke(it) }, EmptyConsumer())

fun <T> Single<T>.subscribeEmpty(consumer: (T) -> Unit = {}): Disposable = subscribe(Consumer { consumer.invoke(it) }, EmptyConsumer())

fun <T> Maybe<T>.subscribeEmpty(consumer: (T) -> Unit = {}): Disposable = subscribe(Consumer { consumer.invoke(it) }, EmptyConsumer())

inline fun runRxIO(crossinline block: () -> Unit) = Observable.fromCallable { block.invoke() }.subscribeOn(Schedulers.io()).subscribeEmpty()

inline fun <T> observableRxIO(crossinline block: () -> T): Observable<T> = Observable.fromCallable { block.invoke() }.subscribeOn(Schedulers.io())

inline fun <T> flowableRxIO(crossinline block: () -> T): Flowable<T> = Flowable.fromCallable { block.invoke() }.subscribeOn(Schedulers.io())

inline fun <T> singleRxIO(crossinline block: () -> T): Single<T> = Single.fromCallable { block.invoke() }.subscribeOn(Schedulers.io())

fun <T> T.asFlowable(): Flowable<T> = if (this == null) throw Exception("Cant be null") else Flowable.just(this)

fun <T> T.asObservable(): Observable<T>? = if (this == null) null else Observable.just(this)

fun <T> T.asSingle(): Single<T>? = if (this == null) null else Single.just(this)