package com.pulse.components.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.visible
import com.pulse.databinding.ItemCategoryBinding
import com.pulse.model.category.Category

class CategoryViewHolder(view: View) : BaseViewHolder<Category>(view) {

    private val binding by viewBinding(ItemCategoryBinding::bind)

    override fun bind(item: Category) = with(binding) {
        mtvName.text = item.name
        if (item.drawableName != -1) {
            ivIcon.setImageResource(item.drawableName)
            ivIcon.visible()
        } else {
            ivIcon.gone()
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = CategoryViewHolder(parent.inflate(R.layout.item_category))
    }
}