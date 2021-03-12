package com.pulse.components.language.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.language.model.LanguageAdapterModel
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.animateVisible
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.visibleOrGone
import com.pulse.core.locale.LocaleEnum
import com.pulse.databinding.ItemCheckableBinding

class LanguageViewHolder(itemView: View, private val itemClick: (LocaleEnum) -> Unit) : BaseViewHolder<LanguageAdapterModel>(itemView) {

    private val binding by viewBinding(ItemCheckableBinding::bind)

    override fun bind(item: LanguageAdapterModel) = with(binding) {
        mtvName.setText(item.language.titleResId)
        ivCheck.visibleOrGone(item.isSelected)
        root.setDebounceOnClickListener {
            ivCheck.animateVisible()
            itemClick(item.language)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup, itemClick: (LocaleEnum) -> Unit) = LanguageViewHolder(parent.inflate(R.layout.item_checkable), itemClick)
    }
}