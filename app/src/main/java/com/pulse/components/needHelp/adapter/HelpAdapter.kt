package com.pulse.components.needHelp.adapter

import android.view.ViewGroup
import com.pulse.components.needHelp.model.Help
import com.pulse.components.needHelp.model.HelpAdapterModel
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.extensions.onClickDebounce

class HelpAdapter(private val contactUsClick: () -> Unit) : BaseFilterRecyclerAdapter<HelpAdapterModel, HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HelpViewHolder.newInstance(parent).apply {
        itemView.onClickDebounce {
            val item = getItem(bindingAdapterPosition)
            if (item.help == Help.CONTACT_US) {
                contactUsClick()
            } else {
                item.isExpanded = !item.isExpanded
                changeExpandState(item)
            }
        }
    }

    override fun diffResult(origin: List<HelpAdapterModel>, new: List<HelpAdapterModel>) = HelpDiff(origin, new)
}