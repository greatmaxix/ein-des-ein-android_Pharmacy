package com.pharmacy.myapp.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.category.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(view: View, val click: (Category) -> Unit) : BaseViewHolder<Category>(view) {
    override fun bind(item: Category) {
        itemView.tvCategoryName.text = item.rusName
        itemView.mcvCategory.onClick { click(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Category) -> Unit) = CategoryViewHolder(parent.inflate(R.layout.item_category), click)
    }
}