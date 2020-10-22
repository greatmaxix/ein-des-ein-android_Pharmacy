package com.pulse.core.base.adapter

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext

abstract class BaseFilterRecyclerAdapter<T, VH : BaseViewHolder<T>>(list: List<T> = mutableListOf()) : BaseRecyclerAdapter<T, VH>(list, false) {

    private var originList = listOf<T>()
        set(value) {
            field = value
            items = transformList(field.toMutableList())
            notifyDataSetChanged()
        }

    abstract fun diffResult(origin: List<T>, new: List<T>): DiffUtil.Callback

    open fun notifyDataSet(list: List<T>) {
        originList = list
    }

    open fun transformList(list: MutableList<T>) = list

    // TODO have to refactor this section, for remove @fun transformList@
    suspend fun filter(predicate: (T) -> Boolean) {
        val (newList, diff) = withContext(Default) {
            originList
                .filter(predicate)
                .run { transformList(toMutableList()) }
                .run { this to DiffUtil.calculateDiff(diffResult(items, this)) }
        }
        items = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
        scrollTop()
    }

}