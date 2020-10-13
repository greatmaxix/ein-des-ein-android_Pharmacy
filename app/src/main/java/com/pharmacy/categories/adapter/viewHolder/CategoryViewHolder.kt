package com.pharmacy.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.onClick
import com.pharmacy.model.category.Category
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