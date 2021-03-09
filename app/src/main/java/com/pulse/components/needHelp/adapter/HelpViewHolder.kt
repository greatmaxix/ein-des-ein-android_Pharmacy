package com.pulse.components.needHelp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.needHelp.model.HelpItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.getString
import com.pulse.core.extensions.inflate
import com.pulse.databinding.ItemHelpBinding

class HelpViewHolder(view: View) : BaseViewHolder<HelpItem>(view) {

    private val binding by viewBinding(ItemHelpBinding::bind)

    override fun bind(item: HelpItem) = with(binding) {
        itemHeader.icon = item.help.iconRes
        itemHeader.title = getString(item.help.title)
        mtvText.setText(item.help.content)
        mtvText.isVisible = item.isExpanded
        itemHeader.isSelected = item.isExpanded
    }

    fun changeExpandState(item: HelpItem) = with(binding) {
        mtvText.isVisible = item.isExpanded
        itemHeader.isSelected = item.isExpanded
    }

    companion object {

        fun newInstance(parent: ViewGroup) = HelpViewHolder(parent.inflate(R.layout.item_help))
    }
}