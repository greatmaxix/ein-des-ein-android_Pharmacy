package com.pulse.components.analyzes.list.adapter

import android.view.ViewGroup
import com.pulse.components.analyzes.list.model.Analyze
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.core.extensions.onClickDebounce

class AnalyzesAdapter(
    private val itemClick: (Analyze) -> Unit,
    private val makeCall: (String) -> Unit,
) : BaseRecyclerAdapter<Analyze, AnalyzesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AnalyzesViewHolder.newInstance(parent, makeCall)
        .apply { itemView.onClickDebounce { itemClick(getItem(bindingAdapterPosition)) } }

    fun notifyItems(newItems: List<Analyze>) {
        items = newItems.toMutableList()
    }
}