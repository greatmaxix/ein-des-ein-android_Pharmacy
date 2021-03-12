package com.pulse.components.needHelp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.components.needHelp.model.HelpAdapterModel

class HelpDiff(private val oldList: List<HelpAdapterModel>, private val newList: List<HelpAdapterModel>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].help == newList[newItemPosition].help

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}