package com.pharmacy.myapp.core.base.adapter.coroutine

import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class CoAdapter<Type>(list: List<Type> = mutableListOf()) : BaseRecyclerAdapter<Type, CoViewHolder<Type>>(list) {

    override fun onViewRecycled(holder: CoViewHolder<Type>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}