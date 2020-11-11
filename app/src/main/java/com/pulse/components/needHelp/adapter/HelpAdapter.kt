package com.pulse.components.needHelp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.pulse.R
import com.pulse.components.needHelp.model.HelpItem
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_help.view.*

class HelpAdapter : BaseFilterRecyclerAdapter<HelpItem, HelpAdapter.HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HelpViewHolder.newInstance(parent)

    override fun diffResult(origin: List<HelpItem>, new: List<HelpItem>) = HelpDiff(origin, new)

    class HelpViewHolder(view: View) : BaseViewHolder<HelpItem>(view) {

        init {
            itemView.itemHeaderNeedHelp.setDebounceOnClickListener {
                val item = itemView.tag as HelpItem
                item.isExpanded = !item.isExpanded
                itemView.tvTextNeedHelp.isVisible = item.isExpanded
                itemView.itemHeaderNeedHelp.isSelected = item.isExpanded
            }
        }

        override fun bind(item: HelpItem) = with(itemView) {
            tag = item
            itemHeaderNeedHelp.icon = item.icon
            itemHeaderNeedHelp.title = item.title
            tvTextNeedHelp.text = item.text
            tvTextNeedHelp.isVisible = item.isExpanded
            itemHeaderNeedHelp.isSelected = item.isExpanded
        }

        companion object {

            fun newInstance(parent: ViewGroup) = HelpViewHolder(parent.inflate(R.layout.item_help))
        }
    }
}