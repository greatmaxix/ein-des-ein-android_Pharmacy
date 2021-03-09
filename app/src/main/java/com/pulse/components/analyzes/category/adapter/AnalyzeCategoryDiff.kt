package com.pulse.components.analyzes.category.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.components.analyzes.category.model.AnalyzeCategory

class AnalyzeCategoryDiff(private val oldList: List<AnalyzeCategory>, private val newList: List<AnalyzeCategory>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}