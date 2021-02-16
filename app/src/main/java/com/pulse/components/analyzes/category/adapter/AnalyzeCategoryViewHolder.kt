package com.pulse.components.analyzes.category.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.ItemAnalyzeCategoryBinding

class AnalyzeCategoryViewHolder(view: View) : BaseViewHolder<AnalyzeCategory>(view) {

    private val binding by viewBinding(ItemAnalyzeCategoryBinding::bind)

    override fun bind(item: AnalyzeCategory) = with(binding) {
        mtvTitle.text = item.name
        mtvSubtitle.visibleOrGone(!item.code.isNullOrBlank())
        mtvSubtitle.text = item.code
    }

    companion object {

        fun newInstance(parent: ViewGroup) = AnalyzeCategoryViewHolder(parent.inflate(R.layout.item_analyze_category))
    }
}