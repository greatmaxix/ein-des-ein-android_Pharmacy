package com.pharmacy.myapp.core.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRxAdapter<I : BaseRxItem<F>, F> : RecyclerView.Adapter<BaseRxViewHolder<I, F>>() {

    override fun onViewRecycled(holder: BaseRxViewHolder<I, F>) {
        super.onViewRecycled(holder)
        holder.clear()
    }

}