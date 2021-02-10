package com.pulse.components.categories.adapter

import android.view.ViewGroup
import com.pulse.components.categories.adapter.viewHolder.CategoryViewHolder
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.model.category.Category

class CategoryAdapter(private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryViewHolder>() {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder.newInstance(parent)
        .apply {
            itemView.setDebounceOnClickListener {
                click(getItem(bindingAdapterPosition))
            }
        }
}