package com.pulse.components.language.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.language.model.LanguageAdapterModel
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.resources
import com.pulse.databinding.ItemLanguageBinding

class LanguageViewHolder(itemView: View) : BaseViewHolder<LanguageAdapterModel>(itemView) {

    private val binding by viewBinding(ItemLanguageBinding::bind)

    private val fontNormal = resources.getFont(R.font.open_sans_regular)
    private val fontSemibold = resources.getFont(R.font.open_sans_semi_bold)

    override fun bind(item: LanguageAdapterModel) {
        binding.root.setText(item.language.titleResId)
        binding.root.typeface = if (item.isSelected) fontSemibold else fontNormal
    }

    companion object {

        fun newInstance(parent: ViewGroup) = LanguageViewHolder(parent.inflate(R.layout.item_language))
    }
}