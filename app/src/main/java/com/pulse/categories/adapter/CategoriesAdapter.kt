package com.pulse.categories.adapter

import android.view.ViewGroup
import com.pulse.categories.adapter.viewHolder.CategoryTileViewHolder
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.extensions.onClick
import com.pulse.model.category.Category

class CategoriesAdapter(private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryTileViewHolder>() {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryTileViewHolder.newInstance(parent)
        .apply {
            itemView.onClick {
                click(getItem(bindingAdapterPosition))
            }
        }
}