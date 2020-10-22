package com.pulse.core.extensions

import android.util.LongSparseArray
import android.util.SparseArray
import java.util.*

infix fun <T> Collection<T>.sameContentWith(collection: Collection<T>?) = collection?.let { size == it.size && containsAll(it) } ?: false

infix fun <T> List<T>.containsOrReturn(item: T) = if (contains(item)) {
    this[indexOf(item)]
} else {
    item
}

fun <E> MutableList<E>.setAll(list: List<E>) {
    clear()
    addAll(list)
}

fun <E> MutableList<E>.replace(item: E) {
    this[indexOf(item)] = item
}

inline fun <T> SparseArray<T>.forEach(action: (key: Int, value: T) -> Unit) {
    for (index in 0 until size()) {
        action(keyAt(index), valueAt(index))
    }
}

inline fun <E> SparseArray<E>.valuesForEach(value: (E) -> Unit) {
    val size = this.size()
    for (i in 0 until size) {
        if (size != this.size()) throw ConcurrentModificationException()
        value(this.valueAt(i))
    }
}

fun <E> LongSparseArray<E>.allValues(): List<E> {
    val size = this.size()
    val list = arrayListOf<E>()
    for (i in 0 until size) {
        if (size != this.size()) throw ConcurrentModificationException()
        list.add(this.valueAt(i))
    }
    return list
}

fun <T> MutableList<T>.removeOrAdd(item: T) = contains(item).apply {
    if (this) {
        remove(item)
    } else {
        add(item)
    }
}