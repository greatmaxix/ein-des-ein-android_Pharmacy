package com.pharmacy.categories.adapter

import android.view.ViewGroup
import com.pharmacy.categories.adapter.viewHolder.CategoryViewHolder
import com.pharmacy.core.base.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.model.category.Category

class NestedCategoriesAdapter(private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryViewHolder>() {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder.newInstance(parent, click)

}