package com.pulse.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.onClick
import com.pulse.model.category.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(view: View, val click: (Category) -> Unit) : BaseViewHolder<Category>(view) {
    override fun bind(item: Category) {
        itemView.tvCategoryName.text = item.name
        itemView.mcvCategory.onClick { click(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Category) -> Unit) = CategoryViewHolder(parent.inflate(R.layout.item_category), click)
    }
}