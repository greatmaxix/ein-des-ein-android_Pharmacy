package com.pulse.components.needHelp.adapter

import android.view.ViewGroup
import com.pulse.components.needHelp.model.HelpItem
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.extensions.onClickDebounce

class HelpAdapter : BaseFilterRecyclerAdapter<HelpItem, HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HelpViewHolder.newInstance(parent).apply {
        itemView.onClickDebounce {
            val item = getItem(bindingAdapterPosition)
            item.isExpanded = !item.isExpanded
            changeExpandState(item)
        }
    }

    override fun diffResult(origin: List<HelpItem>, new: List<HelpItem>) = HelpDiff(origin, new)
}