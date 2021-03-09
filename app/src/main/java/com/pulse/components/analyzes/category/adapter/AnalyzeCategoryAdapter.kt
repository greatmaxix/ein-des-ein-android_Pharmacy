package com.pulse.components.analyzes.category.adapter

import android.view.ViewGroup
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.extensions.setDebounceOnClickListener

class AnalyzeCategoryAdapter(private val click: (AnalyzeCategory) -> Unit) : BaseFilterRecyclerAdapter<AnalyzeCategory, AnalyzeCategoryViewHolder>() {

    override fun diffResult(origin: List<AnalyzeCategory>, new: List<AnalyzeCategory>) = AnalyzeCategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AnalyzeCategoryViewHolder.newInstance(parent)
        .apply {
            itemView.setDebounceOnClickListener {
                click(getItem(bindingAdapterPosition))
            }
        }
}