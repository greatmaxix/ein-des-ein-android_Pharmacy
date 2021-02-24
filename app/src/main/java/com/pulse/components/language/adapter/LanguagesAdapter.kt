package com.pulse.components.language.adapter

import android.view.ViewGroup
import com.pulse.components.language.model.LanguageAdapterModel
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.core.extensions.onClickDebounce
import com.pulse.core.locale.LocaleEnum

class LanguagesAdapter(private val itemClick: (LocaleEnum) -> Unit) : BaseRecyclerAdapter<LanguageAdapterModel, LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageViewHolder.newInstance(parent).apply {
        itemView.onClickDebounce { itemClick(getItem(bindingAdapterPosition).language) }
    }

    fun notifyItems(list: List<LanguageAdapterModel>) {
        items = list.toMutableList()
    }
}