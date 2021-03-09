package com.pulse.components.needHelp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.components.needHelp.model.HelpItem

class HelpDiff(private val oldList: List<HelpItem>, private val newList: List<HelpItem>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].help == newList[newItemPosition].help

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}